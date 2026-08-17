/*The Mediator Pattern is a behavioral design pattern that centralizes complex communication 
between objects into a single mediation object. It promotes loose coupling and organizes the interaction between components.
Instead of objects communicating directly with each other, they interact through the mediator, which helps simplify and manage
their communication.
 */
package behaviouralpattern;

import java.util.*;

enum Role {
    VIEWER,
    EDITOR,
    ADMIN;

    public boolean canEdit() {
        return this == EDITOR || this == ADMIN;
    }
}

interface DocumentSessionMediator{
    void broadcastChange(String change,User Sender);
    void join(User user, Role role);
}

class CollaborativeDocument implements DocumentSessionMediator{
    private final Map<User, Role> userRoles = new HashMap<>();

    @Override
    public void join(User user, Role role){
       userRoles.put(user,role);
    }

    @Override
    public void broadcastChange(String change,User sender){
        Role senderRole = userRoles.get(sender);

        // Verify sender role from mediator's registry
        if (senderRole == null || !senderRole.canEdit()) {
            System.out.println("[ACCESS DENIED] " + sender.getName() 
                               + " (" + senderRole + ") does not have edit permissions.");
            return;
        }

        System.out.println("[BROADCAST] " + sender.getName() + " made edit: \"" + change + "\"");
        for (User user : userRoles.keySet()) {
            if (user != sender) {
                user.receiveChange(change, sender);
            }
        }
    }
}

class User {
    protected String name;
    protected DocumentSessionMediator mediator;

    public User(String name, DocumentSessionMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }
    public String getName() {
        return name;
    }
    // Method for users to make a change
    public void makeChange(String change) {
        System.out.println(name + " edited the document: " + change);
        mediator.broadcastChange(change, this);
    }

    // Method to receive a change from another user
    public void receiveChange(String change, User sender) {
        System.out.println(name + " saw change from " + sender.name + ": \"" + change + "\"");
    }
}


public class MediatorPattern {
    public static void main(String[] args) {
         CollaborativeDocument doc = new CollaborativeDocument();

        // Creating users
        User alice = new User("Alice", doc);
        User bob = new User("Bob", doc);
        User charlie = new User("Charlie", doc);

        // Joining the collaborative document
        doc.join(alice,Role.ADMIN);
        doc.join(bob,Role.VIEWER);
        doc.join(charlie,Role.EDITOR);

        // Users making changes
        alice.makeChange("Added project title");
        bob.makeChange("Corrected grammar in paragraph 2");
    }
}
