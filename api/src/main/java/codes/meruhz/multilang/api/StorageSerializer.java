package codes.meruhz.multilang.api;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public interface StorageSerializer<T extends MessageStorage<?>> {

    @NotNull JsonElement serialize(@NotNull T storage);

    @NotNull T deserialize(@NotNull JsonElement element);
}
