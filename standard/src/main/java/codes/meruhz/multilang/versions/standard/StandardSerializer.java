package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.Message;
import codes.meruhz.multilang.api.StorageSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StandardSerializer implements StorageSerializer<StandardMessageStorage> {

    @Override
    public @NotNull JsonElement serialize(@NotNull StandardMessageStorage storage) {
        @NotNull StandardUtils standardUtils = (StandardUtils) storage.getUtils();
        @NotNull JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", storage.getName());
        jsonObject.addProperty("default locale", storage.getDefaultLocale().toString());

        @NotNull JsonObject messagesJson = new JsonObject();
        for(Message<String> message : storage.getMessages()) {
            @NotNull JsonObject messageJson = new JsonObject();
            @NotNull JsonObject contentJson = new JsonObject();

            for(Map.Entry<Locale, String> content : message.getLocales().entrySet()) {
                contentJson.addProperty(standardUtils.localeToString(content.getKey()), content.getValue());
            }

            for(Map.Entry<Locale, List<String>> arrayContent : message.getArrayLocales().entrySet()) {
                @NotNull JsonArray contentArray = new JsonArray();

                arrayContent.getValue().forEach(contentArray::add);
                contentJson.add(standardUtils.localeToString(arrayContent.getKey()), contentArray);
            }

            messageJson.add("content", contentJson);
            messagesJson.add(message.getId(), messageJson);
        }

        jsonObject.add("messages", messagesJson);
        return jsonObject;
    }

    @Override
    public @NotNull StandardMessageStorage deserialize(@NotNull JsonElement element) {
        try {
            @NotNull StandardUtils standardUtils = new StandardUtils();
            @NotNull JsonObject jsonObject = element.getAsJsonObject();

            @NotNull String name = jsonObject.get("name").getAsString();
            @NotNull Locale defaultLocale = standardUtils.stringToLocale(jsonObject.get("default locale").getAsString());

            @NotNull JsonObject messagesJson = jsonObject.getAsJsonObject("messages");
            @NotNull StandardMessageStorage storage = new StandardMessageStorage(name, defaultLocale, standardUtils);

            messagesJson.asMap().forEach((id, messageElement) -> {
                @NotNull JsonObject messageJson = messageElement.getAsJsonObject();
                @NotNull JsonObject contentJson = messageJson.getAsJsonObject("content");

                @NotNull StandardMessage message = new StandardMessage(storage, id);

                contentJson.asMap().forEach((stringLocale, content) -> {
                    @NotNull Locale locale = standardUtils.stringToLocale(stringLocale);

                    if(content.isJsonArray()) {
                        @NotNull List<@NotNull String> arrayText = new LinkedList<>();

                        for(JsonElement jsonElement : content.getAsJsonArray()) {
                            arrayText.add(jsonElement.getAsString());
                        }

                        message.getArrayLocales().put(locale, arrayText);

                    } else {
                        message.getLocales().put(locale, content.getAsString());
                    }
                });

                storage.getMessages().add(message).join();
            });

            return storage;

        } catch (Throwable throwable) {
            throw new RuntimeException("failed to deserialize: " + element);
        }
    }
}
