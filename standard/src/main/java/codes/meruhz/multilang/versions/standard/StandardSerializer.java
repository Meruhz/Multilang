package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.Message;
import codes.meruhz.multilang.api.StorageSerializer;
import codes.meruhz.multilang.api.locale.Locale;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StandardSerializer implements StorageSerializer<StandardMessageStorage> {

    @Override
    public @NotNull JsonElement serialize(@NotNull StandardMessageStorage storage) {
        @NotNull JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", storage.getName());
        jsonObject.addProperty("default locale", storage.getDefaultLocale().toString());

        @NotNull JsonObject messagesJson = new JsonObject();
        for(Message<String> message : storage.getMessages()) {
            @NotNull JsonObject messageJson = new JsonObject();
            @NotNull JsonObject contentJson = new JsonObject();

            for(Map.Entry<Locale, String> content : message.getLocales().entrySet()) {
                contentJson.addProperty(content.getKey().toString(), content.getValue());
            }

            for(Map.Entry<Locale, List<String>> arrayContent : message.getArrayLocales().entrySet()) {
                @NotNull JsonArray contentArray = new JsonArray();

                arrayContent.getValue().forEach(contentArray::add);
                contentJson.add(arrayContent.getKey().toString(), contentArray);
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
            @NotNull JsonObject jsonObject = element.getAsJsonObject();

            @NotNull String name = jsonObject.get("name").getAsString();
            @NotNull Locale defaultLocale = Locale.valueOf(jsonObject.get("default locale").getAsString());

            @NotNull JsonObject messagesJson = jsonObject.getAsJsonObject("messages");
            @NotNull StandardMessageStorage storage = new StandardMessageStorage(name, defaultLocale, new StandardUtils());

            for(Map.Entry<String, JsonElement> messages : messagesJson.entrySet()) {
                @NotNull JsonObject messageJson = messages.getValue().getAsJsonObject();
                @NotNull JsonObject contentJson = messageJson.getAsJsonObject("content");

                @NotNull StandardMessage message = new StandardMessage(storage, messages.getKey());

                for(Map.Entry<String, JsonElement> contents : contentJson.entrySet()) {
                    @NotNull Locale locale = Locale.valueOf(contents.getKey());
                    @NotNull JsonElement content = contents.getValue();

                    if(content.isJsonArray()) {
                        @NotNull List<@NotNull String> arrayText = new LinkedList<>();

                        for(JsonElement jsonElement : content.getAsJsonArray()) {
                            arrayText.add(jsonElement.getAsString());
                        }

                        message.getArrayLocales().put(locale, arrayText);

                    } else {
                        message.getLocales().put(locale, content.getAsString());
                    }
                }

                storage.getMessages().add(message).join();
            }

            return storage;

        } catch (Throwable throwable) {
            throw new RuntimeException("invalid syntax: " + element, throwable);
        }
    }
}
