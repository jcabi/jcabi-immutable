/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArraySet}.
 *
 * @since 0.1
 */
final class ArraySetTest {

    @Test
    void worksAsANormalSortedSetContainingItems() {
        final Collection<Integer> list = new ArrayList<>(2);
        list.add(10);
        list.add(5);
        MatcherAssert.assertThat(
            "should contain item",
            new ArraySet<>(list),
            Matchers.hasItem(10)
        );
    }

    @Test
    void worksAsANormalSortedSetWithCorrectSize() {
        final Collection<Integer> list = new ArrayList<>(2);
        list.add(10);
        list.add(5);
        MatcherAssert.assertThat(
            "should have correct size",
            new ArraySet<>(list),
            Matchers.hasSize(2)
        );
    }

    @Test
    void buildsSetFluently() {
        MatcherAssert.assertThat(
            new ArraySet<Integer>()
                .with(10)
                .with(5)
                .with(5)
                .with(1000)
                .without(10)
                .without(3)
                .without(1000),
            Matchers.allOf(
                Matchers.<Integer>iterableWithSize(1),
                Matchers.hasItem(5)
            )
        );
    }

    @Test
    void comparesWithAnotherArraySet() {
        MatcherAssert.assertThat(
            new ArraySet<Integer>().with(10).with(2),
            Matchers.equalTo(new ArraySet<Integer>().with(2).with(10))
        );
    }

    @Test
    void encapsulatesIterables() {
        MatcherAssert.assertThat(
            "should encapsulate iterable",
            new ArraySet<>(Arrays.asList(10, 5)),
            Matchers.hasItem(10)
        );
    }

    @Test
    void reportsNullArraySetConstructorArgument() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>((ArraySet<String>) null)
            ).getMessage(),
            Matchers.equalTo("ArraySet argument of ArraySet ctor can't be NULL")
        );
    }

    @Test
    void reportsNullCollectionConstructorArgument() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>((Collection<String>) null)
            ).getMessage(),
            Matchers.equalTo("Collection argument of ArraySet ctor can't be NULL")
        );
    }

    @Test
    void reportsNullIterableConstructorArgument() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>((Iterable<String>) null)
            ).getMessage(),
            Matchers.equalTo("Iterable argument of ArraySet ctor can't be NULL")
        );
    }

    @Test
    void reportsNullValueToAdd() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>().with((String) null)
            ).getMessage(),
            Matchers.equalTo("argument of ArraySet#with() can't be NULL")
        );
    }

    @Test
    void reportsNullCollectionToAdd() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>().with((Collection<String>) null)
            ).getMessage(),
            Matchers.equalTo("arguments of ArraySet#with() can't be NULL")
        );
    }

    @Test
    void reportsNullValueToRemove() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ArraySet<String>().without((String) null)
            ).getMessage(),
            Matchers.equalTo("argument of ArraySet#without() can't be NULL")
        );
    }

    @Test
    void preservesSeparatorAfterEmptyFirstElement() {
        MatcherAssert.assertThat(
            "An empty first element must still be followed by a separator",
            new ArraySet<>(Arrays.asList("", "a", "b")).toString(),
            Matchers.equalTo(", a, b")
        );
    }

    @Test
    void equalsEquivalentJdkSetSymmetrically() {
        final Set<String> immutable = new ArraySet<>(
            Arrays.asList("Aa", "BB")
        );
        final Set<String> jdk = new HashSet<>(Arrays.asList("Aa", "BB"));
        MatcherAssert.assertThat(
            "ArraySet and an equivalent JDK set must be equal both ways",
            immutable.equals(jdk) && jdk.equals(immutable),
            Matchers.equalTo(true)
        );
    }

    @Test
    void matchesJdkSetHashCode() {
        MatcherAssert.assertThat(
            "ArraySet hash code must follow the Set contract",
            new ArraySet<>(Arrays.asList("Aa", "BB")).hashCode(),
            Matchers.equalTo(
                new HashSet<>(Arrays.asList("Aa", "BB")).hashCode()
            )
        );
    }

    @Test
    void comparesTwoArraySetsRegardlessOfElementOrder() {
        MatcherAssert.assertThat(
            "ArraySets with identical elements must be equal",
            new ArraySet<>(Arrays.asList("Aa", "BB")),
            Matchers.equalTo(new ArraySet<>(Arrays.asList("BB", "Aa")))
        );
    }
}
