/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.Arrays;
import java.util.SortedSet;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArraySortedSet}.
 * @since 0.1
 */
final class ArraySortedSetTest {

    /**
     * Simple comparator.
     */
    private static final ArrayComparator<Integer> CMP =
        new ArrayComparator.Default<>();

    @Test
    void worksAsNormalSortedSet() {
        final SortedSet<Integer> set = new ArraySortedSet<>(
            Arrays.asList(10, 5)
        );
        MatcherAssert.assertThat(set, Matchers.hasItem(10));
        MatcherAssert.assertThat(set, Matchers.hasSize(2));
        MatcherAssert.assertThat(set.first(), Matchers.equalTo(5));
        MatcherAssert.assertThat(set.last(), Matchers.equalTo(10));
    }

    @Test
    void buildsSetFluently() {
        MatcherAssert.assertThat(
            new ArraySortedSet<>(ArraySortedSetTest.CMP)
                .with(10)
                .with(5)
                .with(5)
                .with(1000)
                .without(10)
                .without(3)
                .without(1000),
            Matchers.allOf(
                Matchers.iterableWithSize(1),
                Matchers.hasItem(5)
            )
        );
    }

    @Test
    void comparesWithAnotherArraySortedSet() {
        MatcherAssert.assertThat(
            new ArraySortedSet<>(ArraySortedSetTest.CMP)
                .with(10).with(2),
            Matchers.equalTo(
                new ArraySortedSet<>(ArraySortedSetTest.CMP)
                    .with(2).with(10)
            )
        );
    }

    @Test
    void encapsulatesIterables() {
        final Iterable<Integer> list = Arrays.asList(10, 5, 7);
        MatcherAssert.assertThat(
            new ArraySortedSet<>(list),
            Matchers.contains(5, 7, 10)
        );
    }

    @Test
    void worksWithCustomComparator() {
        final String first = "some text that is long";
        final String second = "short text";
        MatcherAssert.assertThat(
            new ArraySortedSet<>(
                Arrays.asList(second, first),
                new ArrayComparator<String>() {
                    @Override
                    public int compare(final String left, final String right) {
                        return right.length() - left.length();
                    }
                }
            ),
            Matchers.contains(first, second)
        );
    }

    @Test
    void replacesComparator() {
        final String first = "B very long long text";
        final String second = "A short text";
        final SortedSet<String> origin = new ArraySortedSet<>(
            Arrays.asList(second, first),
            new ArrayComparator<String>() {
                @Override
                public int compare(final String left, final String right) {
                    return right.length() - left.length();
                }
            }
        );
        MatcherAssert.assertThat(
            new ArraySortedSet<>(
                origin,
                new ArrayComparator.Default<>()
            ),
            Matchers.contains(second, first)
        );
    }

}
