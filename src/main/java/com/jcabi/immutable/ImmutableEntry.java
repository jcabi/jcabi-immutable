/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import com.jcabi.aspects.Immutable;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Immutable map entry.
 * @param <K> Key type.
 * @param <V> Value type.
 * @since 0.1
 */
@Immutable
final class ImmutableEntry<K, V> extends
    AbstractMap.SimpleImmutableEntry<K, V> {

    /**
     * Serialization marker.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Public ctor.
     * @param entry Entry to encapsulate
     */
    ImmutableEntry(final Map.Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    /**
     * Public ctor.
     * @param key The key
     * @param value The value
     */
    ImmutableEntry(final K key, final V value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return String.format("%s=%s", this.getKey(), this.getValue());
    }
}
