import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {

    @Run
    public void testAddition() {
        System.out.println("Running testAddition");
    }

    @Run
    public void testSubtraction() {
        System.out.println("Running testSubtraction");
    }

    public void normalMethod() {
        System.out.println("This method should not run");
    }

    @Run
    public void testMessage() {
        System.out.println("Running testMessage");
    }
}

public class MiniTestRunner {

    public static void main(String[] args) {

        MyTests testObject = new MyTests();

        int count = 0;

        Method[] methods = MyTests.class.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                try {
                    method.invoke(testObject);
                    count++;
                } catch (Exception e) {
                    System.out.println("Error running " + method.getName());
                }
            }
        }

        System.out.println("Total @Run methods executed: " + count);
    }
}