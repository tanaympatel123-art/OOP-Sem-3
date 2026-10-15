import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateFiller {
    public static String fill(String template, Map<String, String> values) {
        Matcher matcher = Pattern.compile("\\{(\\w+)\\}").matcher(template);
        StringBuilder result = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            result.append(template, last, matcher.start());
            result.append(values.getOrDefault(matcher.group(1), "[?]"));
            last = matcher.end();
        }
        return result.append(template.substring(last)).toString();
    }

    public static void main(String[] args) {
        System.out.println(fill("Dear {name}, order {id} ships {date}.", Map.of("name", "Riya", "id", "A07")));
    }
}
