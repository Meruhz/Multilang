package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.main.Multilang;
import codes.meruhz.multilang.api.providers.AbstractMessageStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class StandardMessageStorage extends AbstractMessageStorage<String> {

    public StandardMessageStorage(@NotNull String name, @NotNull Locale defaultLocale, @NotNull StandardUtils standardUtils) {
        super(name, defaultLocale, standardUtils);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> save() {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                if(!Multilang.getStoragesDirectory().isDirectory() && !Multilang.getStoragesDirectory().mkdirs()) {
                    throw new RuntimeException("cannot create storages main directory: " + Multilang.getStoragesDirectory().getAbsolutePath());
                }

                super.getStructureModel().setStructure(new StandardSerializer().serialize(this), true);
                completableFuture.complete(true);

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}