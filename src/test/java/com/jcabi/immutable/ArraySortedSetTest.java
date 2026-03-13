/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.Arrays;
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
    void worksAsNormalSortedSetContainingItem() {
        MatcherAssert.assertThat(
            "should work as sorted set",
            new ArraySortedSet<>(Arrays.asList(10, 5)),
            Matchers.hasItem(10)
        );
    }

    @Test
    void worksAsNormalSortedSetWithCorrectSize() {
        MatcherAssert.assertThat(
            "should have correct size",
            new ArraySortedSet<>(Arrays.asList(10, 5)),
            Matchers.hasSize(2)
        );
    }

    @Test
    void providesSortedFirst() {
        MatcherAssert.assertThat(
            "should return first element",
            new ArraySortedSet<>(Arrays.asList(10, 5)).first(),
            Matchers.equalTo(5)
        );
    }

    @Test
    void providesSortedLast() {
        MatcherAssert.assertThat(
            "should return last element",
            new ArraySortedSet<>(Arrays.asList(10, 5)).last(),
            Matchers.equalTo(10)
        );
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
        MatcherAssert.assertThat(
            "should encapsulate iterable",
            new ArraySortedSet<>(Arrays.asList(10, 5, 7)),
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
        MatcherAssert.assertThat(
            "should replace comparator",
            new ArraySortedSet<>(
                new ArraySortedSet<>(
                    Arrays.asList("A short text", "B very long long text"),
                    new ArrayComparator<String>() {
                        @Override
                        public int compare(final String left,
                            final String right) {
                            return right.length() - left.length();
                        }
                    }
                ),
                new ArrayComparator.Default<>()
            ),
            Matchers.contains("A short text", "B very long long text")
        );
    }

}
