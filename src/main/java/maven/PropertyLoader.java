package maven;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyLoader {

    public static Map<String, Object> parseYamlFile(String filePath) {
        try (InputStream inputStream = PropertyLoader.class.getResourceAsStream(filePath)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void flattenProperties(Map<String, Object> properties, String prefix, Map<String, String> result) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedProperties = (Map<String, Object>) value;
                String nestedPrefix = prefix.isEmpty() ? key : prefix + "." + key;
                flattenProperties(nestedProperties, nestedPrefix, result);
            } else {
                String flattenedKey = prefix.isEmpty() ? key : prefix + "." + key;
                result.put(flattenedKey, value.toString());
            }
        }
    }

    public static void printPropertiesFormat(Map<String, Object> yamlData) {
        Map<String, String> flattenedProperties = new LinkedHashMap<>();
        flattenProperties(yamlData, "", flattenedProperties);

        Properties properties = new Properties();
        properties.putAll(flattenedProperties);
        properties.list(System.out);
    }

    public static void main(String[] args) {
        String filePath = "/example.yaml"; // Replace with your YAML file path
        Map<String, Object> yamlData = parseYamlFile(filePath);
        if (yamlData != null) {
            printPropertiesFormat(yamlData);
        }
    }
}