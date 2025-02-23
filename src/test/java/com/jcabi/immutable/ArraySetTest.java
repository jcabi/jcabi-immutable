/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArraySet}.
 * @since 0.1
 */
final class ArraySetTest {

    @Test
    void worksAsANormalSortedSet() {
        final Collection<Integer> list = new LinkedList<>();
        list.add(10);
        list.add(5);
        final Set<Integer> set = new ArraySet<>(list);
        MatcherAssert.assertThat(set, Matchers.hasItem(10));
        MatcherAssert.assertThat(set, Matchers.hasSize(2));
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
        final Iterable<Integer> list = Arrays.asList(10, 5);
        MatcherAssert.assertThat(
            new ArraySet<>(list),
            Matchers.hasItem(10)
        );
    }

}
