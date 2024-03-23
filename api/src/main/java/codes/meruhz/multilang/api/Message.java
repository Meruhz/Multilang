package codes.meruhz.multilang.api;

import codes.meruhz.multilang.api.content.KeyedContent;
import codes.meruhz.multilang.api.locale.Locale;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Message<T> {

    @NotNull MessageStorage<@NotNull T> getStorage();

    @NotNull String getId();

    @NotNull Locales<@NotNull T> getLocales();

    @NotNull ArrayLocales<@NotNull T> getArrayLocales();

    @NotNull T getText(@NotNull Locale locale);

    default @NotNull T getText(@NotNull Locale locale, Object @NotNull [] replaces) {
        return this.getStorage().getUtils().replaceText(this.getText(locale), replaces);
    }

    @NotNull List<@NotNull T> getArrayText(@NotNull Locale locale);

    default @NotNull List<@NotNull T> getArrayText(@NotNull Locale locale, Object @NotNull [] replaces) {
        return this.getStorage().getUtils().replaceArrayText(this.getArrayText(locale), replaces);
    }

    final class Locales<T> extends KeyedContent<Locale, T> {

        public Locales(@NotNull Map<@NotNull Locale, @NotNull T> map) {
            super(map);
        }
    }

    final class ArrayLocales<T> extends KeyedContent<Locale, List<T>> {

        public ArrayLocales(@NotNull Map<@NotNull Locale, @NotNull List<T>> map) {
            super(map);
        }
    }
}
