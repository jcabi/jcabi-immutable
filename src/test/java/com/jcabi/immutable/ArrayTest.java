/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Array}.
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
final class ArrayTest {

    @Test
    void worksAsANormalArrayContainingItems() {
        final Collection<Integer> list = new ArrayList<>(2);
        list.add(10);
        list.add(5);
        MatcherAssert.assertThat(
            "should contain item",
            new Array<>(list),
            Matchers.hasItem(10)
        );
    }

    @Test
    void worksAsANormalArrayWithCorrectSize() {
        final Collection<Integer> list = new ArrayList<>(2);
        list.add(10);
        list.add(5);
        MatcherAssert.assertThat(
            "should have correct size",
            new Array<>(list),
            Matchers.hasSize(2)
        );
    }

    @Test
    void buildsArrayFluently() {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(5)
                .with(10)
                .with(1000)
                .with(0, 10)
                .with(3, 3)
                .with(1, 1000),
            Matchers.allOf(
                Matchers.<Integer>iterableWithSize(4),
                Matchers.contains(10, 1000, 1000, 3)
            )
        );
    }

    @Test
    void removesElementsByIndex() {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(5)
                .with(10)
                .with(1000)
                .withoutIndex(0)
                .withoutIndex(0)
                .withoutIndex(0),
            Matchers.empty()
        );
    }

    @Test
    void removesElements() {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(5)
                .with(10)
                .with(1000)
                .without(5)
                .without(3)
                .without(1_000_000),
            Matchers.hasSize(2)
        );
    }

    @Test
    void encapsulatesIterables() {
        MatcherAssert.assertThat(
            "should encapsulate iterable",
            new Array<>(Arrays.asList(10, 5)),
            Matchers.hasItem(10)
        );
    }

    @Test
    void encapsulatesArrays() {
        final Array<Integer> array = new Array<>(10, 5);
        array.with(1_000_000);
        MatcherAssert.assertThat(
            new Array<>(array),
            Matchers.hasItem(10)
        );
    }

    @Test
    void findsIndexOfExistingObject() {
        MatcherAssert.assertThat(
            "should find index of existing element",
            new Array<>(5, 2, 2, 3).indexOf(2),
            Matchers.equalTo(1)
        );
    }

    @Test
    void returnsNegativeForMissingObject() {
        MatcherAssert.assertThat(
            "should return -1 for missing element",
            new Array<>(5, 2, 2, 3).indexOf(0),
            Matchers.equalTo(-1)
        );
    }

    @Test
    void findsFirstOccurrenceOfObject() {
        MatcherAssert.assertThat(
            "should find first occurrence",
            new Array<>(1, 1, 10, 10, 3).indexOf(10),
            Matchers.equalTo(2)
        );
    }

    @Test
    void returnsNegativeForMissingElement() {
        MatcherAssert.assertThat(
            "should return -1 for missing",
            new Array<>(1, 1, 10, 10, 3).indexOf(0),
            Matchers.equalTo(-1)
        );
    }

    @Test
    void makesListIterator() {
        MatcherAssert.assertThat(
            new Array<>(5, 2, 2, 3).listIterator().next(),
            Matchers.equalTo(5)
        );
    }

    @Test
    void makesListIteratorWithIndex() {
        MatcherAssert.assertThat(
            new Array<>(5, 1, 2, 3).listIterator(2).next(),
            Matchers.equalTo(2)
        );
    }

    @Test
    void makesSubList() {
        MatcherAssert.assertThat(
            new Array<>(5, 1, 2).subList(1, 3),
            Matchers.contains(1, 2)
        );
    }

    @Test
    void isIndependentFromCtorParam() {
        final Integer[] ints = {1, 2, 3};
        ints[1] = 0;
        MatcherAssert.assertThat(
            "should be independent from ctor param",
            Arrays.equals(new Array<>(1, 2, 3).toArray(), new Integer[]{1, 2, 3}),
            Matchers.is(true)
        );
    }
}
