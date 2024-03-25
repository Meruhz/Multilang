package codes.meruhz.multilang.versions.component;

import codes.meruhz.multilang.api.Message;
import codes.meruhz.multilang.api.StorageSerializer;
import codes.meruhz.multilang.api.locale.Locale;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseComponentSerializer implements StorageSerializer<BaseComponentMessageStorage> {

    @Override
    public @NotNull JsonElement serialize(@NotNull BaseComponentMessageStorage storage) {
        @NotNull BaseComponentUtils baseComponentUtils = (BaseComponentUtils) storage.getUtils();
        @NotNull JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", storage.getName());
        jsonObject.addProperty("default locale", storage.getDefaultLocale().toString());

        @NotNull JsonObject messagesJson = new JsonObject();
        for(Message<BaseComponent[]> message : storage.getMessages()) {
            @NotNull JsonObject messageJson = new JsonObject();
            @NotNull JsonObject contentJson = new JsonObject();

            for(Map.Entry<Locale, BaseComponent[]> content : message.getLocales().entrySet()) {
                contentJson.addProperty(content.getKey().toString(), baseComponentUtils.getLegacyText(content.getValue()));
            }

            for(Map.Entry<Locale, List<BaseComponent[]>> arrayContent : message.getArrayLocales().entrySet()) {
                @NotNull JsonArray contentArray = new JsonArray();

                baseComponentUtils.getLegacyArray(arrayContent.getValue()).forEach(contentArray::add);
                contentJson.add(arrayContent.getKey().toString(), contentArray);
            }

            messageJson.add("content", contentJson);
            messagesJson.add(message.getId(), messageJson);
        }

        jsonObject.add("messages", messagesJson);
        return jsonObject;
    }

    @Override
    public @NotNull BaseComponentMessageStorage deserialize(@NotNull JsonElement element) {
        try {
            @NotNull BaseComponentUtils baseComponentUtils = new BaseComponentUtils();
            @NotNull JsonObject jsonObject = element.getAsJsonObject();

            @NotNull String name = jsonObject.get("name").getAsString();
            @NotNull Locale defaultLocale = Locale.valueOf(jsonObject.get("default locale").getAsString());

            @NotNull JsonObject messagesJson = jsonObject.getAsJsonObject("messages");
            @NotNull BaseComponentMessageStorage storage = new BaseComponentMessageStorage(name, defaultLocale, baseComponentUtils);

            for(Map.Entry<String, JsonElement> messages : messagesJson.entrySet()) {
                @NotNull JsonObject messageJson = messages.getValue().getAsJsonObject();
                @NotNull JsonObject contentJson = messageJson.getAsJsonObject("content");

                @NotNull BaseComponentMessage message = new BaseComponentMessage(storage, messages.getKey());

                for(Map.Entry<String, JsonElement> contents : contentJson.entrySet()) {
                    @NotNull Locale locale = Locale.valueOf(contents.getKey());
                    @NotNull JsonElement content = contents.getValue();

                    if(content.isJsonArray()) {
                        @NotNull List<BaseComponent @NotNull []> arrayText = new LinkedList<>();

                        for(JsonElement jsonElement : content.getAsJsonArray()) {
                            arrayText.add(new BaseComponent[] { new TextComponent(baseComponentUtils.getLegacyText(new TextComponent(jsonElement.getAsString()))) });
                        }

                        message.getArrayLocales().put(locale, arrayText);

                    } else {
                        message.getLocales().put(locale, new BaseComponent[]{new TextComponent(baseComponentUtils.getLegacyText(new TextComponent(content.getAsString())))});
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
