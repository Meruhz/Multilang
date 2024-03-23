package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.MessageStorage;
import codes.meruhz.multilang.api.providers.AbstractMessage;
import org.jetbrains.annotations.NotNull;

public class StandardMessage extends AbstractMessage<String> {

    public StandardMessage(@NotNull MessageStorage<@NotNull String> messageStorage, @NotNull String id) {
        super(messageStorage, id);
    }
}
