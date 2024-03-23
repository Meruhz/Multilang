package codes.meruhz.multilang.api.providers;

import codes.meruhz.multilang.api.Message;
import codes.meruhz.multilang.api.MessageStorage;
import codes.meruhz.multilang.api.MessageUtils;
import codes.meruhz.multilang.api.main.Multilang;
import codes.meruhz.multilang.api.model.JsonStructureModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractMessageStorage<T> implements MessageStorage<T> {

    private final @NotNull Messages<@NotNull T> messages = new Messages<>(new LinkedHashSet<>());
    private final @NotNull ArrayMessages<@NotNull T> arrayMessages = new ArrayMessages<>(new LinkedHashMap<>());

    private final @NotNull String name;
    private final @NotNull Locale defaultLocale;
    private final @NotNull JsonStructureModel structureModel;
    private final @NotNull MessageUtils<@NotNull T> messageUtils;

    public AbstractMessageStorage(@NotNull String name, @NotNull Locale defaultLocale, @NotNull MessageUtils<@NotNull T> messageUtils) {
        this.name = name;
        this.defaultLocale = defaultLocale;
        this.messageUtils = messageUtils;
        this.structureModel = new JsonStructureModel(Multilang.getStoragesDirectory(), name);
    }

    @Override
    public final @NotNull String getName() {
        return this.name;
    }

    @Override
    public final @NotNull Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Override
    public final @NotNull MessageUtils<@NotNull T> getUtils() {
        return this.messageUtils;
    }

    @Override
    public final @NotNull JsonStructureModel getStructureModel() {
        return this.structureModel;
    }

    @Override
    public final @NotNull Messages<T> getMessages() {
        return this.messages;
    }

    @Override
    public final @NotNull ArrayMessages<T> getArrayMessages() {
        return this.arrayMessages;
    }

    @Override
    public @NotNull Optional<Message<T>> getMessage(@NotNull String id) {
        return this.getMessages().stream().filter(message -> message.getId().equals(id)).findFirst();
    }

    @Override
    public @NotNull T getText(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces) {
        @NotNull Optional<Message<T>> messageOptional = this.getMessage(id);

        if(!messageOptional.isPresent()) {
            throw new NullPointerException("message '" + id + "' not found on '" + this.getName() + "'");
        }

        @NotNull Message<@NotNull T> message = messageOptional.get();

        if(!message.getLocales().containsKey(locale)) {
            try {
                return message.getLocales().get(this.getDefaultLocale());

            } catch (NullPointerException e) {
                throw new NullPointerException("locale '" + locale + "' and '" + this.getDefaultLocale() + "' was not found for message '" + message.getId() + "' on '" + this.getName() + "'");
            }

        } else {
            return message.getLocales().get(locale);
        }
    }

    @Override
    public @NotNull List<@NotNull T> getArrayText(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces) {
        @NotNull Optional<Message<T>> messageOptional = this.getMessage(id);

        if(!messageOptional.isPresent()) {
            throw new NullPointerException("message '" + id + "' not found on '" + this.getName() + "'");
        }

        @NotNull Message<@NotNull T> message = messageOptional.get();

        if(!message.getArrayLocales().containsKey(locale)) {
            try {
                return message.getArrayLocales().get(this.getDefaultLocale());

            } catch (NullPointerException e) {
                throw new NullPointerException("locale '" + locale + "' and '" + this.getDefaultLocale() + "' was not found for array message '" + message.getId() + "' on '" + this.getName() + "'");
            }

        } else {
            return message.getArrayLocales().get(locale);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        AbstractMessageStorage<?> that = (AbstractMessageStorage<?>) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
