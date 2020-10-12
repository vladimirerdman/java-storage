package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private String key;
    private String value;

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(String key) { this.key = key; }

    public void setValue(String value) { this.value = value; }

    public String getKey() { return key; }

    public String getValue() { return value; }

    public static List<Config> parseConfigLine(String line) {
        List<Config> html = new ArrayList<>();
        // Config line: Title : 'Value';
        String regex = "^([^$]*)\\:[\\s]*[']([^$]*)['];$";
        Pattern r = Pattern.compile(regex);
        if (!line.startsWith("//")) {
            Matcher m = r.matcher(line);
            if (m.find()) {
                for (int i = 1; i < 3; i++) {
                    html.add(new Config(m.group(1), m.group(2)));// m.group(1) - key, m.group(2) - value
                    break;
                }
            }
        } else {
            return null;
        }
        return html;
    }
}
