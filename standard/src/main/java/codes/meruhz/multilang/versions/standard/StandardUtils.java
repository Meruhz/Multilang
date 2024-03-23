package codes.meruhz.multilang.versions.standard;

import codes.meruhz.multilang.api.MessageUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class StandardUtils implements MessageUtils<String> {

    @Override
    public @NotNull String replaceText(@NotNull String text, Object @NotNull [] replaces) {
        for(Object replace : replaces) {
            text = text.replaceFirst("%s", replace.toString());
        }
        return text;
    }

    @Override
    public @NotNull List<@NotNull String> replaceArrayText(@NotNull List<@NotNull String> arrayText, Object @NotNull [] replaces) {
        return arrayText.stream().map(text -> this.replaceText(text, replaces)).collect(Collectors.toList());
    }
}
