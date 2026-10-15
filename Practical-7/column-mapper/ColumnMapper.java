import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {

    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }
}

public class ColumnMapper {

    public static void populate(Object obj, String[] headers, String[] data) {

        Map<String, String> row = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            row.put(headers[i], data[i]);
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Column.class)) {

                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                if (!row.containsKey(columnName)) {
                    System.out.println("Missing column: " + columnName);
                    continue;
                }

                String value = row.get(columnName);

                try {
                    field.setAccessible(true);

                    if (field.getType() == int.class) {
                        field.setInt(obj, Integer.parseInt(value));
                    } else if (field.getType() == String.class) {
                        field.set(obj, value);
                    }

                } catch (Exception e) {
                    System.out.println("Error setting field: " + field.getName());
                }
            }
        }
    }

    public static void main(String[] args) {

        String[] headers = {
                "id",
                "name",
                "email"
        };

        String[] data = {
                "101",
                "Tanay",
                "tanay@example.com"
        };

        Student student = new Student();

        populate(student, headers, data);

        student.display();
    }
}