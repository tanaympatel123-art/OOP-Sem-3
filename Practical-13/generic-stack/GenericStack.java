import java.util.EmptyStackException;

public class GenericStack<T> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public GenericStack() {
        this(DEFAULT_CAPACITY);
    }

    public GenericStack(int initialCapacity) {
        elements = new Object[Math.max(1, initialCapacity)];
    }

    public void push(T item) {
        ensureCapacity();
        elements[size++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = (T) elements[--size];
        elements[size] = null; // prevent memory leak
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) elements[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newArr = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArr, 0, elements.length);
            elements = newArr;
        }
    }

    public static void main(String[] args) {
        System.out.println("========== Practical 13 - Problem 3: Custom Generic Stack ==========");

        // String stack
        GenericStack<String> stringStack = new GenericStack<>(2);
        stringStack.push("Java");
        stringStack.push("Python");
        stringStack.push("C++");
        System.out.println("Stack size after 3 pushes: " + stringStack.size());
        System.out.println("Top element (peek): " + stringStack.peek());
        System.out.println("Popped: " + stringStack.pop());
        System.out.println("Popped: " + stringStack.pop());
        System.out.println("Remaining element: " + stringStack.peek());

        // Integer stack
        GenericStack<Integer> intStack = new GenericStack<>();
        for (int i = 10; i <= 50; i += 10) {
            intStack.push(i);
        }
        System.out.print("\nPopping from integer stack: ");
        while (!intStack.isEmpty()) {
            System.out.print(intStack.pop() + " ");
        }
        System.out.println();
    }
}
