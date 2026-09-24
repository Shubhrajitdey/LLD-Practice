/*Scenario: E-Commerce Notification & Alert Dispatcher
You are designing the notification module for an online checkout system. When a customer places an order, 
the system must trigger alerts to keep the customer and internal teams informed.

Functional Requirements
    Dynamic Alert Channels:

        A notification payload contains an order ID, recipient info, and message content.
        Alerts can be dispatched via Email, SMS, or In-App Push.
        A single event might need to send via one channel, multiple channels, 
        or even chain them dynamically (for example: Send In-App Push first; 
        if it's high priority, also wrap/augment it with an SMS or Email alert).

    Decoupled Event Flow:

        The core order checkout service should simply publish/emit an OrderPlaced event without directly instantiating 
        or coupling to specific alert mechanisms.
        Different services/subscribers (e.g., Customer Notification Service, Logistics Service, Analytics Service) 
        should be able to listen to this event and act independently.

    Vendor Swapping / Abstraction:
        The concrete SMS service (e.g., Twilio) and Email service (e.g., SendGrid) use third-party client SDKs with their own idiosyncratic interfaces. 
        Your core notification logic must interact with a clean, uniform interface, hiding vendor-specific payload conversion and method calls. 
*/

package AppNotificationDesign;

import java.util.ArrayList;
import java.util.List;

record Order(String orderId, String customerEmail, String customerPhone, double amount) {}

record NotificationPayload(String orderId, String recipientEmail, String recipientPhone, String message) {}

// ============================================================================
// 2. THIRD-PARTY SDK MOCKS
// ============================================================================
class SendGridSdk {
    public void dispatch(String to, String subject, String body) {
        System.out.println("[SendGrid SDK] Email sent to: " + to + " | Subject: " + subject + " | Body: " + body);
    }
}

class TwilioSdk {
    public void sendSmsPayload(String phone, String text) {
        System.out.println("[Twilio SDK] SMS sent to: " + phone + " | Message: " + text);
    }
}

// ============================================================================
// 3. ADAPTER PATTERN (Vendor Abstraction)
// ============================================================================
interface EmailGateway {
    void sendEmail(String toEmail, String subject, String body);
}

interface SmsGateway {
    void sendSms(String phoneNumber, String message);
}

class SendGridEmailAdapter implements EmailGateway {
    private final SendGridSdk sdk = new SendGridSdk();

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        sdk.dispatch(toEmail, subject, body);
    }
}

class TwilioSmsAdapter implements SmsGateway {
    private final TwilioSdk sdk = new TwilioSdk();

    @Override
    public void sendSms(String phoneNumber, String message) {
        sdk.sendSmsPayload(phoneNumber, message);
    }
}

// ============================================================================
// 4. DECORATOR PATTERN (Dynamic Channel Composition)
// ============================================================================
interface Notifier {
    void send(NotificationPayload payload);
}

// Core component: In-App Push
class PushNotifier implements Notifier {
    @Override
    public void send(NotificationPayload payload) {
        System.out.println("[In-App Push] Order " + payload.orderId() + ": " + payload.message());
    }
}

// Base Decorator
abstract class NotifierDecorator implements Notifier {
    protected final Notifier wrapped;

    public NotifierDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }
    @Override
    public void send(NotificationPayload payload) {
        wrapped.send(payload);
    }
}

class EmailDecorator extends NotifierDecorator {
    private final EmailGateway emailGateway;

    public EmailDecorator(Notifier wrapped, EmailGateway emailGateway) {
        super(wrapped);
        this.emailGateway = emailGateway;
    }

    @Override
    public void send(NotificationPayload payload) {
        super.send(payload);
        emailGateway.sendEmail(payload.recipientEmail(), "Order Confirmed!", payload.message());
    }
}

class SmsDecorator extends NotifierDecorator {
    private final SmsGateway smsGateway;

    public SmsDecorator(Notifier wrapped, SmsGateway smsGateway) {
        super(wrapped);
        this.smsGateway = smsGateway;
    }

    @Override
    public void send(NotificationPayload payload) {
        super.send(payload);
        smsGateway.sendSms(payload.recipientPhone(), payload.message());
    }
}

// ============================================================================
// 5. OBSERVER PATTERN (Decoupled Event Dispatching)
// ============================================================================
interface OrderEventListener {
    void onOrderPlaced(Order order);
}

class CustomerNotificationListener implements OrderEventListener {
    private final Notifier notifier;

    public CustomerNotificationListener(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void onOrderPlaced(Order order) {
        NotificationPayload payload = new NotificationPayload(
            order.orderId(),
            order.customerEmail(),
            order.customerPhone(),
            "Your order of $" + order.amount() + " was placed successfully!"
        );
        notifier.send(payload);
    }
}

class AnalyticsListener implements OrderEventListener {
    @Override
    public void onOrderPlaced(Order order) {
        System.out.println("[Analytics] Event captured for Order ID: " + order.orderId());
    }
}

// Subject
class OrderService {
    private final List<OrderEventListener> listeners = new ArrayList<>();

    public void registerListener(OrderEventListener listener) {
        listeners.add(listener);
    }

    public void checkout(Order order) {
        System.out.println("Processing order: " + order.orderId());
        for (OrderEventListener listener : listeners) {
            listener.onOrderPlaced(order);
        }
    }
}

// ============================================================================
// 6. DRIVER / VERIFICATION
// ============================================================================
public class Main { // Rename to Solution if on CoderPad/HackerRank
    public static void main(String[] args) {
        // 1. Initialize Adapters
        EmailGateway emailAdapter = new SendGridEmailAdapter();
        SmsGateway smsAdapter = new TwilioSmsAdapter();

        // 2. Build Decorator Pipeline: Base Push -> Email -> SMS
        Notifier notificationPipeline = new SmsDecorator(
            new EmailDecorator(
                new PushNotifier(),
                emailAdapter
            ),
            smsAdapter
        );

        // 3. Register Observers
        OrderService orderService = new OrderService();
        orderService.registerListener(new CustomerNotificationListener(notificationPipeline));
        orderService.registerListener(new AnalyticsListener());

        // 4. Trigger Execution
        Order order = new Order("ORD-9842", "alex@example.com", "+1-555-0199", 149.99);
        orderService.checkout(order);
    }
}
