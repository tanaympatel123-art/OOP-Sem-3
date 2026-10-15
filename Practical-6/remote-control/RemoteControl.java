interface Switchable {
    void on();
    void off();
    default void toggle() { on(); off(); }
}

@FunctionalInterface
interface SwitchPolicy { boolean maySwitchOn(Switchable device, int hour); }

class Fan implements Switchable {
    public void on() { System.out.println("Fan on"); }
    public void off() { System.out.println("Fan off"); }
}
class Light implements Switchable {
    public void on() { System.out.println("Light on"); }
    public void off() { System.out.println("Light off"); }
}

public class RemoteControl {
    public static void main(String[] args) {
        Switchable[] devices = {new Fan(), new Light()};
        for (Switchable device : devices) device.toggle();
        SwitchPolicy anonymous = new SwitchPolicy() {
            public boolean maySwitchOn(Switchable device, int hour) { return hour >= 6 && hour < 23; }
        };
        SwitchPolicy lambda = (device, hour) -> hour >= 6 && hour < 23;
        System.out.println("Anonymous policy: " + anonymous.maySwitchOn(devices[0], 9));
        System.out.println("Lambda policy: " + lambda.maySwitchOn(devices[1], 23));
    }
}
