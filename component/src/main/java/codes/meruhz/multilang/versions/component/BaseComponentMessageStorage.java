package codes.meruhz.multilang.versions.component;

import codes.meruhz.multilang.api.MessageUtils;
import codes.meruhz.multilang.api.main.Multilang;
import codes.meruhz.multilang.api.providers.AbstractMessageStorage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BaseComponentMessageStorage extends AbstractMessageStorage<BaseComponent[]> {

    public BaseComponentMessageStorage(@NotNull String name, @NotNull Locale defaultLocale, @NotNull MessageUtils<BaseComponent @NotNull []> messageUtils) {
        super(name, defaultLocale, messageUtils);
    }

    public @NotNull String getLegacyText(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces) {
        return ((BaseComponentUtils) super.getUtils()).getLegacyText(super.getText(locale, id, replaces));
    }

    public @NotNull String getLegacyText(@NotNull String id, @NotNull Object... replaces) {
        return ((BaseComponentUtils) super.getUtils()).getLegacyText(super.getText(id, replaces));
    }

    public @NotNull List<@NotNull String> getLegacyArray(@NotNull Locale locale, @NotNull String id, @NotNull Object... replaces) {
        return ((BaseComponentUtils) super.getUtils()).getLegacyArray(super.getArrayText(locale, id, replaces));
    }

    public @NotNull List<@NotNull String> getLegacyArray(@NotNull String id, @NotNull Object... replaces) {
        return ((BaseComponentUtils) super.getUtils()).getLegacyArray(super.getArrayText(id, replaces));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> save() {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                if(!Multilang.getStoragesDirectory().isDirectory() && !Multilang.getStoragesDirectory().mkdirs()) {
                    throw new RuntimeException("cannot create storages main directory: " + Multilang.getStoragesDirectory().getAbsolutePath());
                }

                super.getStructureModel().setStructure(new BaseComponentSerializer().serialize(this), true);
                completableFuture.complete(true);

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}