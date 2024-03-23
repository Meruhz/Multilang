package codes.meruhz.multilang.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public abstract class Content<T> implements Iterable<T> {

    private final @NotNull Collection<@UnknownNullability T> collection;

    public Content(@NotNull Collection<@UnknownNullability T> collection) {
        this.collection = collection;
    }

    public boolean add(@NotNull T object) {
        return this.collection.add(object);
    }

    public boolean addAll(@NotNull Collection<@UnknownNullability T> c) {
        return this.collection.addAll(c);
    }

    public boolean remove(@NotNull T object) {
        return this.collection.remove(object);
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
