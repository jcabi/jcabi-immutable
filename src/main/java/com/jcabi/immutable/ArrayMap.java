/*
 * Copyright (c) 2012-2021, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.immutable;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
 * <p>
 * Limitation:
 * Encapsulated objects with exposed mutators can mutate their state.
 *
 * @param <K> Map key type
 * @param <V> Value key type
 * @since 0.1
 * @checkstyle MissingDeprecatedCheck (400 lines)
 */
@Immutable
@Loggable(Loggable.DEBUG)
@SuppressWarnings({ "rawtypes", "unchecked", "PMD.TooManyMethods" })
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
        this.entries = new ArrayMap.ImmutableEntry[0];
    }

    /**
     * Public ctor.
     * @param map The original map
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public ArrayMap(final Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException(
                "argument of ArrayMap ctor can't be NULL"
            );
        }
        final Set<ArrayMap.ImmutableEntry<K, V>> entrs =
            new TreeSet<>(
                new ArrayMap.Cmp<>()
            );
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            entrs.add(new ArrayMap.ImmutableEntry<>(entry));
        }
        this.entries = entrs.toArray(new ArrayMap.ImmutableEntry[entrs.size()]);
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
        return new ArrayMap<K, V>(map);
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
        final StringBuilder text = new StringBuilder(0);
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
            new LinkedHashSet<Map.Entry<K, V>>(Arrays.asList(this.entries))
        );
    }

    /**
     * Comparator.
     * @param <K> Key type.
     * @param <V> Value type.
     * @since 0.1
     */
    private static final class Cmp<K, V> implements
        Comparator<ArrayMap.ImmutableEntry<K, V>>, Serializable {
        /**
         * The Serial version UID.
         */
        private static final long serialVersionUID = 4064118000237204080L;

        @Override
        public int compare(final ImmutableEntry<K, V> left,
            final ImmutableEntry<K, V> right) {
            final int compare;
            if (left.getKey() instanceof Comparable) {
                compare = Comparable.class.cast(left.getKey())
                    .compareTo(right.getKey());
            } else {
                compare = left.getKey().toString()
                    .compareTo(right.getKey().toString());
            }
            return compare;
        }
    }

    /**
     * Immutable map entry.
     * @param <K> Key type.
     * @param <V> Value type.
     * @since 0.1
     */
    @Immutable
    private static final class ImmutableEntry<K, V> extends
        AbstractMap.SimpleImmutableEntry<K, V> {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Public ctor.
         * @param entry Entry to encapsulate
         */
        private ImmutableEntry(final Map.Entry<K, V> entry) {
            this(entry.getKey(), entry.getValue());
        }

        /**
         * Public ctor.
         * @param key The key
         * @param value The value
         */
        private ImmutableEntry(final K key, final V value) {
            super(key, value);
        }

        @Override
        public String toString() {
            return String.format("%s=%s", this.getKey(), this.getValue());
        }
    }

}
