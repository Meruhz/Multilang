package codes.meruhz.multilang.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MessageUtils<T> {

    @NotNull T replaceText(@NotNull T text, Object @NotNull [] replaces);

    @NotNull List<@NotNull T> replaceArrayText(@NotNull List<@NotNull T> arrayText, Object @NotNull [] replaces);
}