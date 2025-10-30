package net.nukebob.augment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/augmentation.json");

    public static Map<String, ArrayList<Ench>> loadConfig() {
        Map<String, ArrayList<Ench>> itemsList = new HashMap<>();
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            JsonObject items = root.getAsJsonObject("items");
            JsonObject templates = root.getAsJsonObject("templates");

            for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                ArrayList<Ench> enchList = new ArrayList<>();
                String item = entry.getKey();
                JsonElement value = entry.getValue();

                if (value.isJsonObject()) {
                    // Direct item configuration
                    for (Map.Entry<String, JsonElement> entry1 : value.getAsJsonObject().entrySet()) {
                        try {
                            enchList.add(new Ench(entry1.getKey(), entry1.getValue().getAsInt()));
                        } catch (Exception ignored) {}
                    }
                } else {
                    // Reference to a template
                    String templateName = value.getAsString().replace("templates/", "");
                    JsonObject resolvedTemplate = resolveTemplate(templateName, templates);

                    for (Map.Entry<String, JsonElement> entry1 : resolvedTemplate.entrySet()) {
                        try {
                            enchList.add(new Ench(entry1.getKey(), entry1.getValue().getAsInt()));
                        } catch (Exception ignored) {}
                    }
                }
                itemsList.put(item, enchList);
            }
        } catch (Error | FileNotFoundException ignored) {
            try (InputStream inputStream = Settings.class.getClassLoader().getResourceAsStream("assets/augment/default_enchants.json")) {
                try (OutputStream outputStream = new FileOutputStream(CONFIG_FILE)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                } catch (Exception ignored2) {}
            } catch (Exception ignored1) {}
        }
        return itemsList;
    }

    private static JsonObject resolveTemplate(String templateName, JsonObject templates) {
        JsonObject template = templates.getAsJsonObject(templateName);
        if (template.has("parent")) {
            String parentName = template.get("parent").getAsString().replace("templates/", "");
            JsonObject parentTemplate = resolveTemplate(parentName, templates);

            // Merge parent properties into the current template
            for (Map.Entry<String, JsonElement> entry : parentTemplate.entrySet()) {
                template.add(entry.getKey(), entry.getValue());
            }
            // Remove the parent field to prevent circular references
            template.remove("parent");
        }
        return template;
    }

}
