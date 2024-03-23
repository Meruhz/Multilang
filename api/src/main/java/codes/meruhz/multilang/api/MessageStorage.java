package codes.meruhz.multilang.api;

import codes.meruhz.multilang.api.content.Content;
import codes.meruhz.multilang.api.content.KeyedContent;
import codes.meruhz.multilang.api.model.JsonStructureModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface MessageStorage<T> {

    @NotNull String getName();

    @NotNull Locale getDefaultLocale();

    @NotNull MessageUtils<@NotNull T> getUtils();

    @NotNull JsonStructureModel getStructureModel();

    @NotNull Messages<T> getMessages();

    @NotNull ArrayMessages<T> getArrayMessages();

    @NotNull Optional<Message<T>> getMessage(@NotNull String id);

    @NotNull T getText(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces);

    default @NotNull T getText(@NotNull String id, @NotNull Object... replaces) {
        return this.getText(this.getDefaultLocale(), id, replaces);
    }

    @NotNull List<@NotNull T> getArrayText(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces);

    default @NotNull List<@NotNull T> getArrayText(@NotNull String id, @NotNull Object... replaces) {
        return this.getArrayText(this.getDefaultLocale(), id, replaces);
    }

    @NotNull CompletableFuture<Boolean> save();

    final class Messages<T> extends Content<Message<T>> {

        public Messages(@NotNull Collection<@NotNull Message<T>> collection) {
            super(collection);
        }
    }

    final class ArrayMessages<T> extends KeyedContent<Locale, List<T>> {

        public ArrayMessages(@NotNull Map<@NotNull Locale, @NotNull List<T>> map) {
            super(map);
        }
    }
}
