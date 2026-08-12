/*The Template Pattern is a behavioral design pattern that provides a blueprint 
for executing an algorithm. It allows subclasses to override specific steps of the algorithm, 
but the overall structure remains the same. This ensures that the invariant parts of the algorithm 
are not changed, while enabling customization in the variable parts. */
package behaviouralpattern;

abstract class NotificationSender {
    public final void sendNotification(String to,String message) {
        // Common Logic
        rateLimitCheck(to);
        validateRecipient(to);
        String formatted = formatMessage(message);
        preSendAuditLog(to, formatted);
        
        // Specific Logic: defined by subclassese
        String composedMessage = composeMessage(formatted);
        sendMessage(to, composedMessage);
        
        // Optional Hook
        postSendAnalytics(to);
    }

    // Common step 1: Check rate limits
    private void rateLimitCheck(String to) {
        System.out.println("Checking rate limits for: " + to);
    }

    // Common step 2: Validate recipient
    private void validateRecipient(String to) {
        System.out.println("Validating recipient: " + to);
    }

    // Common step 3: Format the message (can be customized)
    private String formatMessage(String message) {
        return message.trim(); // could include HTML escaping, emoji processing, etc.
    }

    // Common step 4: Pre-send audit log
    private void preSendAuditLog(String to, String formatted) {
        System.out.println("Logging before send: " + formatted + " to " + to);
    }

    // Hook for subclasses to implement custom message composition
    protected abstract String composeMessage(String formattedMessage);

    // Hook for subclasses to implement custom message sending
    protected abstract void sendMessage(String to, String message);

    // Optional hook for analytics (can be overridden)
    protected void postSendAnalytics(String to) {
        System.out.println("Analytics updated for: " + to);
    }
}

class EmailNotificationSender extends NotificationSender {
    @Override
    protected String composeMessage(String formattedMessage) {
        return "Email: " + formattedMessage;
    }

    @Override
    protected void sendMessage(String to, String message) {
        System.out.println("Sending email to " + to + ": " + message);
    }
}

class SMSNotificationSender extends NotificationSender {
    @Override
    protected String composeMessage(String formattedMessage) {
        return "SMS: " + formattedMessage;
    }

    @Override
    protected void sendMessage(String to, String message) {
        System.out.println("Sending SMS to " + to + ": " + message);
    }

    @Override
    protected void postSendAnalytics(String to) {
        System.out.println("SMS analytics updated for: " + to);
    }
}

public class TemplatePattern{
    public static void main(String[] args) {
        NotificationSender emailSender = new EmailNotificationSender();
        emailSender.sendNotification("me","Hello via Email!");
        
        NotificationSender smsSender = new SMSNotificationSender();
        smsSender.sendNotification("you","Hello via SMS!");
    }
}