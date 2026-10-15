import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    static class BoundedBuffer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int capacity;

        BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void produce(int item) throws InterruptedException {
            while (queue.size() == capacity) {
                System.out.println("Buffer full (size=" + capacity + "). Producer waiting...");
                wait();
            }
            queue.add(item);
            System.out.println("Produced item: " + item + " (Buffer size=" + queue.size() + ")");
            notifyAll();
        }

        public synchronized int consume() throws InterruptedException {
            while (queue.isEmpty()) {
                System.out.println("Buffer empty. Consumer waiting...");
                wait();
            }
            int item = queue.poll();
            System.out.println("Consumed item: " + item + " (Buffer size=" + queue.size() + ")");
            notifyAll();
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Practical 10 - Problem 2: Producer-Consumer with wait()/notify() ==========");

        final int BUFFER_CAPACITY = 3;
        final int TOTAL_ITEMS = 10;
        BoundedBuffer buffer = new BoundedBuffer(BUFFER_CAPACITY);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= TOTAL_ITEMS; i++) {
                    buffer.produce(i);
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ProducerThread");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= TOTAL_ITEMS; i++) {
                    buffer.consume();
                    Thread.sleep(120);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConsumerThread");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Producer-Consumer coordination completed without item loss.");
    }
}
