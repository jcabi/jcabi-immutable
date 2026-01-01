/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import com.jcabi.aspects.Immutable;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for arrays.
 *
 * @param <T> Value type
 * @since 1.0
 */
@Immutable
@SuppressWarnings("unchecked")
public interface ArrayComparator<T> extends Comparator<T> {

    /**
     * Default comparator.
     * @param <T> Type of argument
     * @since 1.0
     */
    @Immutable
    final class Default<T> implements ArrayComparator<T>, Serializable {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 0x54ef44fb3ef2efa3L;

        @Override
        public String toString() {
            return "DEFAULT";
        }

        @Override
        public int compare(final T left, final T right) {
            return ((Comparable<T>) left).compareTo(right);
        }
    }

    /**
     * Neutral comparator (never compares).
     * @param <T> Type of argument
     * @since 1.0
     */
    @Immutable
    final class Neutral<T> implements ArrayComparator<T>, Serializable {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 0x54ef489b3ef2eca3L;

        @Override
        public String toString() {
            return "NEUTRAL";
        }

        @Override
        public int compare(final T left, final T right) {
            return 1;
        }
    }

    /**
     * Reverse comparator.
     * @param <T> Type of argument
     * @since 1.0
     */
    @Immutable
    final class Reverse<T extends Comparable<T>> implements ArrayComparator<T>,
        Serializable {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = 0x545f489d3ed2eca3L;

        @Override
        public String toString() {
            return "REVERSE";
        }

        @Override
        public int compare(final T left, final T right) {
            return right.compareTo(left);
        }
    }

}
