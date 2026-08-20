/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Map on top of array.
 *
 * <p>This class is truly immutable. This means that it never changes
 * its encapsulated values and is annotated with {@code @Immutable}
 * annotation.
 *
 * <p>Limitation:
 * Encapsulated objects with exposed mutators can mutate their state.
 *
 * @param <K> Map key type
 * @param <V> Value key type
 * @since 0.1
 */
// @checkstyle ConstructorsCodeFreeCheck (400 lines)
// @checkstyle ConstructorsOrderCheck (400 lines)
@Immutable
@Loggable(Loggable.DEBUG)
@SuppressWarnings({
    "rawtypes", "unchecked",
    "PMD.ConstructorOnlyInitializesOrCallOtherConstructors",
    "PMD.OnlyOneConstructorShouldDoInitialization",
    "PMD.LooseCoupling"
})
public final class ArrayMap<K, V> implements ConcurrentMap<K, V> {

    /**
     * All entries.
     */
    @Immutable.Array
    private final transient ImmutableEntry<K, V>[] entries;

    /**
     * Public ctor.
     */
    public ArrayMap() {
        this.entries = new ImmutableEntry[0];
    }

    /**
     * Public ctor.
     * @param map The original map
     */
    public ArrayMap(final Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException(
                "argument of ArrayMap ctor can't be NULL"
            );
        }
        final Set<ImmutableEntry<K, V>> entrs =
            new TreeSet<>(
                new Cmp<>()
            );
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            entrs.add(new ImmutableEntry<>(entry));
        }
        this.entries = entrs.toArray(new ImmutableEntry[0]);
    }

    /**
     * Make a new one with an extra entry.
     * @param key The key
     * @param value The value
     * @return New map
     */
    public ArrayMap<K, V> with(final K key, final V value) {
        if (key == null) {
            throw new IllegalArgumentException(
                "first argument of ArrayMap#with() can't be NULL"
            );
        }
        if (value == null) {
            throw new IllegalArgumentException(
                "second argument of ArrayMap#with() can't be NULL"
            );
        }
        final ConcurrentMap<K, V> map =
            new ConcurrentHashMap<>(this.entries.length);
        map.putAll(this);
        map.put(key, value);
        return new ArrayMap<>(map);
    }

    /**
     * Make a new one with these extra entries.
     * @param ents Entries
     * @return New map
     * @since 0.11
     */
    public ArrayMap<K, V> with(final Map<K, V> ents) {
        if (ents == null) {
            throw new IllegalArgumentException(
                "arguments of ArrayMap#with() can't be NULL"
            );
        }
        final ConcurrentMap<K, V> map =
            new ConcurrentHashMap<>(this.entries.length);
        map.putAll(this);
        map.putAll(ents);
        return new ArrayMap<>(map);
    }

    /**
     * Make a new one without this key.
     * @param key The key
     * @return New map
     */
    public ArrayMap<K, V> without(final K key) {
        if (key == null) {
            throw new IllegalArgumentException(
                "argument of ArrayMap#without() can't be NULL"
            );
        }
        final ConcurrentMap<K, V> map =
            new ConcurrentHashMap<>(this.entries.length);
        map.putAll(this);
        map.remove(key);
        return new ArrayMap<>(map);
    }

    /**
     * Make a new one without these keys.
     * @param keys The keys to remove
     * @return New map
     * @since 0.11
     */
    public ArrayMap<K, V> without(final Collection<K> keys) {
        if (keys == null) {
            throw new IllegalArgumentException(
                "arguments of ArrayMap#without() can't be NULL"
            );
        }
        final ConcurrentMap<K, V> map =
            new ConcurrentHashMap<>(this.entries.length);
        map.putAll(this);
        for (final K key : keys) {
            map.remove(key);
        }
        return new ArrayMap<>(map);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.entries);
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof ArrayMap
            && Arrays.deepEquals(
                this.entries, ArrayMap.class.cast(object).entries
            );
    }

    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder(100);
        for (final Map.Entry<K, V> item : this.entries) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(item);
        }
        return text.toString();
    }

    @Override
    public int size() {
        return this.entries.length;
    }

    @Override
    public boolean isEmpty() {
        return this.entries.length == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        boolean contains = false;
        for (final Map.Entry<K, V> entry : this.entries) {
            if (entry.getKey().equals(key)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    @Override
    public boolean containsValue(final Object value) {
        boolean contains = false;
        for (final Map.Entry<K, V> entry : this.entries) {
            if (entry.getValue().equals(value)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    @Override
    public V get(final Object key) {
        V value = null;
        for (final Map.Entry<K, V> entry : this.entries) {
            if (entry.getKey().equals(key)) {
                value = entry.getValue();
                break;
            }
        }
        return value;
    }

    @Override
    @Deprecated
    public V put(final K key, final V value) {
        throw new UnsupportedOperationException(
            "put(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public V remove(final Object key) {
        throw new UnsupportedOperationException(
            "remove(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException(
            "putAll(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException(
            "clear(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public V putIfAbsent(final K key, final V value) {
        throw new UnsupportedOperationException(
            "putIfAbsent(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public boolean remove(final Object key, final Object value) {
        throw new UnsupportedOperationException(
            "remove(): ArrayMap is immutable, can't change"
        );
    }

    @Override
    @Deprecated
    public boolean replace(final K key, final V old, final V value) {
        throw new UnsupportedOperationException(
            "replace(): ArrayMap is immutable"
        );
    }

    @Override
    @Deprecated
    public V replace(final K key, final V value) {
        throw new UnsupportedOperationException(
            "replace(): ArrayMap is immutable, can't replace"
        );
    }

    @Override
    public Set<K> keySet() {
        final Set<K> keys = new LinkedHashSet<>(this.entries.length);
        for (final Map.Entry<K, V> entry : this.entries) {
            keys.add(entry.getKey());
        }
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public Collection<V> values() {
        final Collection<V> values = new ArrayList<>(this.entries.length);
        for (final Map.Entry<K, V> entry : this.entries) {
            values.add(entry.getValue());
        }
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(
            new LinkedHashSet<>(Arrays.asList(this.entries))
        );
    }
}
