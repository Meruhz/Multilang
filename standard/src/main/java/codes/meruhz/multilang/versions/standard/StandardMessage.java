package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.providers.AbstractMessage;
import org.jetbrains.annotations.NotNull;

public class StandardMessage extends AbstractMessage<String> {

    public StandardMessage(@NotNull StandardMessageStorage standardStorage, @NotNull String id) {
        super(standardStorage, id);
    }
}
