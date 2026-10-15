import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class AnnotationValidator {
    private AnnotationValidator() {
    }

    static String[] validate(Object object) {
        List<String> errors = new ArrayList<>();

        for (Field field : Account.class.getDeclaredFields()) {
            validateField(object, field, errors);
        }

        return errors.toArray(String[]::new);
    }

    private static void validateField(
            Object object,
            Field field,
            List<String> errors
    ) {
        try {
            field.setAccessible(true);
            Object value = field.get(object);

            if (field.isAnnotationPresent(Positive.class)
                    && value instanceof Number number
                    && number.longValue() <= 0) {
                errors.add(field.getAnnotation(Positive.class).message());
            }

            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (maxLength != null
                    && value instanceof String text
                    && text.length() > maxLength.value()) {
                errors.add(field.getName() + " exceeds maximum length.");
            }
        } catch (IllegalAccessException exception) {
            errors.add("Cannot access " + field.getName());
        }
    }
}
