package codes.meruhz.multilang.versions.component;

import codes.meruhz.multilang.api.providers.AbstractMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class BaseComponentMessage extends AbstractMessage<BaseComponent[]> {

    public BaseComponentMessage(@NotNull BaseComponentMessageStorage messageStorage, @NotNull String id) {
        super(messageStorage, id);
    }

    public @NotNull String getLegacyText(@NotNull Locale locale, Object @NotNull [] replaces) {
        return ((BaseComponentUtils) super.getStorage().getUtils()).getLegacyText(super.getText(locale, replaces));
    }

    public @NotNull String getLegacyText(Object @NotNull [] replaces) {
        return ((BaseComponentUtils) super.getStorage().getUtils()).getLegacyText(super.getText(super.getStorage().getDefaultLocale(), replaces));
    }

    public @NotNull List<@NotNull String> getLegacyArray(@NotNull Locale locale, Object @NotNull [] replaces) {
        return ((BaseComponentUtils) super.getStorage().getUtils()).getLegacyArray(super.getArrayText(locale, replaces));
    }

    public @NotNull List<@NotNull String> getLegacyArray(Object @NotNull [] replaces) {
        return ((BaseComponentUtils) super.getStorage().getUtils()).getLegacyArray(super.getArrayText(super.getStorage().getDefaultLocale(), replaces));
    }
}
