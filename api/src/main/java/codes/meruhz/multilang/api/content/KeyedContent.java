package codes.meruhz.multilang.api.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class KeyedContent<K, V> implements Map<K, V> {

    private final @NotNull Map<@NotNull K, @UnknownNullability V> map;

    protected KeyedContent(@NotNull Map<@NotNull K, @UnknownNullability V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(@NotNull Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public @UnknownNullability V get(@NotNull Object key) {
        return this.map.get(key);
    }

    @Override
    public synchronized V put(@NotNull K key, @UnknownNullability V value) {
        return this.map.put(key, value);
    }

    @Override
    public V remove(@NotNull Object key) {
        return this.map.remove(key);
    }

    @Override
    public synchronized void putAll(@NotNull Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public @NotNull Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return this.map.values();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }
}
