package codes.meruhz.multilang.api.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class Content<T> implements Iterable<T> {

    private final @NotNull Collection<@UnknownNullability T> collection;

    protected Content(@NotNull Collection<@UnknownNullability T> collection) {
        this.collection = collection;
    }

    public @NotNull CompletableFuture<Boolean> add(@NotNull T object) {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {

                synchronized (this) {
                    completableFuture.complete(this.collection.add(object));
                }

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }

        });

        return completableFuture;
    }

    public @NotNull CompletableFuture<Boolean> addAll(@NotNull Collection<@UnknownNullability T> c) {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {

                synchronized (this) {
                    completableFuture.complete(this.collection.addAll(c));
                }

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }

        });

        return completableFuture;
    }

    public @NotNull CompletableFuture<Boolean> remove(@NotNull T object) {
        @NotNull CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {

                synchronized (this) {
                    completableFuture.complete(this.collection.remove(object));
                }

            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }

        });

        return completableFuture;
    }

    public boolean contains(@NotNull T object) {
        return this.collection.contains(object);
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int size() {
        return this.collection.size();
    }

    public void clear() {
        this.collection.clear();
    }

    public @NotNull Stream<T> stream() {
        return this.collection.stream();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.collection.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Content<?> content = (Content<?>) o;
        return Objects.equals(collection, content.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection);
    }
}
