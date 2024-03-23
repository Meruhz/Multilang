package codes.meruhz.multilang.api.providers;

import codes.meruhz.multilang.api.Message;
import codes.meruhz.multilang.api.MessageStorage;
import codes.meruhz.multilang.api.locale.Locale;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMessage<T> implements Message<T> {

    private final @NotNull Locales<@NotNull T> locales = new Locales<>(new LinkedHashMap<>());
    private final @NotNull ArrayLocales<@NotNull T> arrayLocales = new ArrayLocales<>(new LinkedHashMap<>());

    private final @NotNull MessageStorage<@NotNull T> messageStorage;
    private final @NotNull String id;

    public AbstractMessage(@NotNull MessageStorage<@NotNull T> messageStorage, @NotNull String id) {
        this.messageStorage = messageStorage;
        this.id = id;
        this.getStorage().getMessages().add(this).join();
    }

    @Override
    public final @NotNull MessageStorage<@NotNull T> getStorage() {
        return this.messageStorage;
    }

    @Override
    public final @NotNull String getId() {
        return this.id;
    }

    @Override
    public final @NotNull Locales<@NotNull T> getLocales() {
        return this.locales;
    }

    @Override
    public final @NotNull ArrayLocales<@NotNull T> getArrayLocales() {
        return this.arrayLocales;
    }

    @Override
    public @NotNull T getText(@NotNull Locale locale) {
        if(!this.getLocales().containsKey(locale)) {
            throw new NullPointerException("no locale '" + locale + "' for message '" + this.getId() + "'");
        }

        return this.getLocales().get(locale);
    }

    @Override
    public @NotNull List<@NotNull T> getArrayText(@NotNull Locale locale) {
        if(!this.getArrayLocales().containsKey(locale)) {
            throw new NullPointerException("no locale '" + locale + "' for array message '" + this.getId() + "'");
        }

        return this.getArrayLocales().get(locale);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        AbstractMessage<?> that = (AbstractMessage<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
