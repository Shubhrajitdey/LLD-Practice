package behaviouralpattern;

import java.util.HashMap;
import java.util.Map;

interface Device {
    void On();
    void Off();
}

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

interface Command {
    void execute();
}

abstract class DeviceCommand implements Command {
    protected Device device;
    public DeviceCommand(Device device) {
        this.device = device;
    }
}

class OnCommand extends DeviceCommand {
    public OnCommand(Device device) {
        super(device);
    }
    @Override
    public void execute() {
        device.On();
    }
}

class OffCommand extends DeviceCommand {
    public OffCommand(Device device) {
        super(device);
    }
    @Override
    public void execute() {
        device.Off();
    }
}

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

class RemoteControl {
    private Command command;

    public void setCommand(int buttonNumber) {
        this.command = FactoryCommand.getCommand(buttonNumber);
    }

    public void pressButton(int buttonNumber) {
        setCommand(buttonNumber);
        command.execute();
    }
}

public class CommandPattern {
    public static void main(String[] args) {
        RemoteControl remote = new RemoteControl();
        remote.pressButton(1); // AC is On
        remote.pressButton(2); // AC is Off
        remote.pressButton(3); // TV is On
        remote.pressButton(4); // TV is Off
    }
}