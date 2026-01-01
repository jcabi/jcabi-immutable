/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArrayMap}.
 * @since 0.1
 */
final class ArrayMapTest {

    @Test
    void worksAsANormalMap() {
        final ConcurrentMap<Integer, String> map =
            new ConcurrentHashMap<>(0);
        final String value = "first value";
        map.put(1, value);
        map.put(2, "second");
        MatcherAssert.assertThat(
            new ArrayMap<>(map),
            Matchers.hasEntry(1, value)
        );
    }

    @Test
    void buildsMapFluently() {
        MatcherAssert.assertThat(
            new ArrayMap<Integer, String>()
                .with(5, "four")
                .with(5, Integer.toString(5))
                .with(40, "fourty")
                .without(40)
                .with(10, "ten"),
            Matchers.allOf(
                Matchers.not(Matchers.hasKey(40)),
                Matchers.hasValue(Integer.toString(5)),
                Matchers.hasKey(5),
                Matchers.hasEntry(5, Integer.toString(5))
            )
        );
    }

    @Test
    void comparesCorrectlyWithAnotherMap() {
        MatcherAssert.assertThat(
            new ArrayMap<Integer, Integer>().with(1, 1).with(2, 2),
            Matchers.equalTo(
                new ArrayMap<Integer, Integer>().with(2, 2).with(1, 1)
            )
        );
        MatcherAssert.assertThat(
            new ArrayMap<Class<?>, Integer>()
                .with(String.class, 1)
                .with(Integer.class, 2),
            Matchers.equalTo(
                new ArrayMap<Class<?>, Integer>()
                    .with(Integer.class, 2)
                    .with(String.class, 1)
            )
        );
    }

    @Test
    void sortsKeys() {
        MatcherAssert.assertThat(
            new ArrayMap<Integer, String>()
                .with(5, "")
                .with(4, "")
                .with(1, "")
                .with(10, "")
                .keySet(),
            Matchers.hasToString("[1, 4, 5, 10]")
        );
    }

    @Test
    void sortsEntries() {
        MatcherAssert.assertThat(
            new ArrayMap<Integer, String>()
                .with(5, "")
                .with(4, "")
                .with(1, "")
                .with(10, "")
                .entrySet(),
            Matchers.hasToString("[1=, 4=, 5=, 10=]")
        );
    }

    @Test
    void appendsEntireMapToItself() {
        MatcherAssert.assertThat(
            new ArrayMap<Integer, String>().with(5, "").with(
                new ArrayMap<Integer, String>().with(10, "")
            ),
            Matchers.hasKey(10)
        );
    }

}
