/*The Command Pattern is a behavioral design pattern that encapsulates a request as an object, 
allowing for parameterization of clients with different requests, queuing of requests,
 and logging of the requests. It lets you add features like undo, redo, logging, and dynamic command
execution without changing the core business logic. */

package behaviouralpattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// Receiver interface
interface Device {
    void On();
    void Off();
}
// Concrete implementation of Device for AC
class ACDevice implements Device {
    @Override
    public void On() {
        System.out.println("AC is On");
    }

    @Override
    public void Off() {
        System.out.println("AC is Off");
    }
}
// Concrete implementation of Device for TV
class TVDevice implements Device {
    @Override
    public void On() {
        System.out.println("TV is On");
    }

    @Override
    public void Off() {
        System.out.println("TV is Off");
    }
}
// Command interface
interface Command {
    void execute();
    void undo();
}
// Abstract Command class to hold a reference to the device
abstract class DeviceCommand implements Command {
    protected Device device;
    public DeviceCommand(Device device) {
        this.device = device;
    }
}
// Concrete Command for turning on the device
class OnCommand extends DeviceCommand {
    public OnCommand(Device device) {
        super(device);
    }
    @Override
    public void execute() {
        device.On();
    }
    @Override
    public void undo() {
        device.Off();
    }
}
// Concrete Command for turning off the device
class OffCommand extends DeviceCommand {
    public OffCommand(Device device) {
        super(device);
    }
    @Override
    public void execute() {
        device.Off();
    }
    @Override
    public void undo() {
        device.On();
    }
}

// Factory class to manage command creation and mapping
class FactoryCommand {
    // Registry mapping button slot numbers to Command Suppliers
    private static final Map<Integer, Command> commandRegistry = new HashMap<>();

    static {
        // Pre-create shared device instances (or instantiate them on-demand if needed)
        Device ac = new ACDevice();
        Device tv = new TVDevice();

        // Register button mappings without switch-case statements
        commandRegistry.put(1,  new OnCommand(ac));
        commandRegistry.put(2,  new OffCommand(ac));
        commandRegistry.put(3,  new OnCommand(tv));
        commandRegistry.put(4,  new OffCommand(tv));
    }

    public static Command getCommand(int buttonNumber) {
        Command command = commandRegistry.get(buttonNumber);
        if (command == null) {
            throw new IllegalArgumentException("No command assigned to button slot: " + buttonNumber);
        }
        return command;
    }
}

// Invoker class
class RemoteControl {
    private Command command;
    private Stack<Command> commandHistory = new Stack<>();
    public void setCommand(int buttonNumber) {
        this.command = FactoryCommand.getCommand(buttonNumber);
    }

    public void pressButton(int buttonNumber) {
        setCommand(buttonNumber);
        command.execute();
        commandHistory.push(command);
    }
    public void pressUndo() {
        if (!commandHistory.isEmpty()) {
            commandHistory.pop().undo();
        } else {
            System.out.println("No commands to undo.");
        }
    }
}

//client code
public class CommandPattern {
    public static void main(String[] args) {
        RemoteControl remote = new RemoteControl();
        remote.pressButton(1); // AC is On
        remote.pressButton(3); // TV is On
        remote.pressUndo(); // TV is Off
        remote.pressButton(2); // AC is Off
        remote.pressButton(4); // TV is Off
        remote.pressUndo(); // TV is On
        remote.pressUndo(); // AC is On    
    }
}