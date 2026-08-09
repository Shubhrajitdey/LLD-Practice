/*The Prototype Pattern is a creational design pattern used to clone existing objects 
instead of constructing them from scratch. It enables efficient object creation, 
especially when the initialization process is complex or costly. */
package creationalpattern;
import java.util.HashMap;
import java.util.Map;

// FIX 1: Extend Cloneable so super.clone() works at runtime
interface EmailTemplate extends Cloneable {
    EmailTemplate clone();
    String send();
}

class WelcomeEmailTemplate implements EmailTemplate {
    private String subject;
    private String body;

    public WelcomeEmailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public WelcomeEmailTemplate clone() {
        try{
            return (WelcomeEmailTemplate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }

    @Override
    public String send() {
        return "Sending Welcome Email: Subject: " + subject + ", Body: " + body;
    }
}

class EmailTemplateRegistry {
    private static final Map<String, EmailTemplate> templates = new HashMap<>();

    static {
        templates.put("welcome", new WelcomeEmailTemplate("Welcome!", "Thank you for joining us!"));
    }

    public static EmailTemplate getTemplate(String type) {
        return templates.get(type).clone();
    }
}

public class ProtoTypePattern {
    public static void main(String[] args) {
        // Create an original object
        EmailTemplate original = new WelcomeEmailTemplate("Welcome!", "Thank you for joining us!");
        
        // Clone the original object
        EmailTemplate clone = original.clone();
        
        // Display the original and cloned objects
        System.out.println("Original: " + original.send());
        System.out.println("Clone: " + clone.send());

        EmailTemplate welcomeTemplate = EmailTemplateRegistry.getTemplate("welcome");
        System.out.println(welcomeTemplate.send()); 

        // FIX 2 & 3: Fix the (Em) typo and declare as WelcomeEmailTemplate so we can use setSubject()
        WelcomeEmailTemplate anotherWelcomeTemplate = (WelcomeEmailTemplate) EmailTemplateRegistry.getTemplate("welcome");
        anotherWelcomeTemplate.setSubject("Hello and Welcome!");
        System.out.println(anotherWelcomeTemplate.send());
    }
}