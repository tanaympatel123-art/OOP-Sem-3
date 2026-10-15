@FunctionalInterface
interface Notifier {
    void send(String message);
}

interface Urgent {
}

class UrgentEmailNotifier implements Notifier, Urgent {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class Notifications {
    public static void main(String[] args) {
        Notifier[] notifiers = {
            new UrgentEmailNotifier(),
            message -> System.out.println("SMS: " + message)
        };

        for (Notifier notifier : notifiers) {
            notifier.send("MiniBank maintenance tonight");
            if (notifier instanceof Urgent) {
                notifier.send("URGENT: MiniBank maintenance tonight");
            }
        }
    }
}
