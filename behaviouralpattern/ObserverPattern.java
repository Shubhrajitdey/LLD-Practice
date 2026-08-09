/*The Observer Pattern is a behavioral design pattern that defines a one-to-many dependency 
between objects so that when one object (the subject) changes its state, 
all its dependents (called observers) are notified and updated automatically. */
package behaviouralpattern;
import java.util.ArrayList;
import java.util.List;
interface Subscriber {
    void update(String message);
}

class EmailSubscriber implements Subscriber {
    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("Email sent to " + email + ": " + message);
    }
}

class SMSSubscriber implements Subscriber {
    private String phoneNumber;

    public SMSSubscriber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(String message) {
        System.out.println("SMS sent to " + phoneNumber + ": " + message);
    }
}

interface Channel {
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notifySubscribers(String message);
    void uploadVideo(String videoTitle);
}

class YoutubeChannel implements Channel {
    private List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(message);
        }
    }

    @Override
    public void uploadVideo(String videoTitle) {
        System.out.println("New video uploaded: " + videoTitle);
        notifySubscribers(videoTitle);
    }
}


public class ObserverPattern {
    public static void main(String[] args) {
        Channel youtubeChannel = new YoutubeChannel();
        youtubeChannel.subscribe(new EmailSubscriber("bit2"));
        youtubeChannel.subscribe(new SMSSubscriber("1234567890"));

        youtubeChannel.uploadVideo("Observer Pattern in Java");
    }
}
