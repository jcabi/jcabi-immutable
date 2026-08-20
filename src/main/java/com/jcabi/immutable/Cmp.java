/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator.
 * @param <K> Key type.
 * @param <V> Value type.
 * @since 0.1
 */
final class Cmp<K, V> implements
    Comparator<ImmutableEntry<K, V>>, Serializable {

    /**
     * The Serial version UID.
     */
    private static final long serialVersionUID = 4064118000237204080L;

    @Override
    @SuppressWarnings("unchecked")
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
