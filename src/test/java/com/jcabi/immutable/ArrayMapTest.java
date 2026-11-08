/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArrayMap}.
 *
 * @since 0.1
 */
final class ArrayMapTest {

    @Test
    void findsValueAfterNullValue() {
        final Map<String, String> source = new LinkedHashMap<>(2);
        source.put("a", null);
        source.put("b", "present");
        MatcherAssert.assertThat(
            "A null value must not prevent finding a later value",
            new ArrayMap<>(source).containsValue("present"),
            Matchers.is(true)
        );
    }

    @Test
    void findsNullValue() {
        final Map<String, String> source = new LinkedHashMap<>(1);
        source.put("a", null);
        MatcherAssert.assertThat(
            "A copied null value must be found",
            new ArrayMap<>(source).containsValue(null),
            Matchers.is(true)
        );
    }

    @Test
    void rejectsAbsentValueWhenNullValueExists() {
        final Map<String, String> source = new LinkedHashMap<>(1);
        source.put("a", null);
        MatcherAssert.assertThat(
            "A null value must not prevent rejecting an absent value",
            new ArrayMap<>(source).containsValue("absent"),
            Matchers.is(false)
        );
    }

    @Test
    void preservesUnequalKeysWithEqualNaturalOrdering() {
        final BigDecimal tenths = new BigDecimal("1.0");
        final BigDecimal hundredths = new BigDecimal("1.00");
        final Map<BigDecimal, String> source = new LinkedHashMap<>(2);
        source.put(tenths, "tenths");
        source.put(hundredths, "hundredths");
        MatcherAssert.assertThat(
            "Copying a map must retain both unequal keys",
            new ArrayMap<>(source),
            Matchers.allOf(
                Matchers.aMapWithSize(2),
                Matchers.hasEntry(tenths, "tenths"),
                Matchers.hasEntry(hundredths, "hundredths")
            )
        );
    }

    @Test
    void replacesOnlyTheEqualKeyWhenAddingEntries() {
        final BigDecimal first = new BigDecimal("2.0");
        final BigDecimal second = new BigDecimal("2.00");
        MatcherAssert.assertThat(
            "Fluent updates must use equality to identify a key",
            new ArrayMap<BigDecimal, String>()
                .with(first, "original")
                .with(second, "second")
                .with(first, "replacement"),
            Matchers.allOf(
                Matchers.aMapWithSize(2),
                Matchers.hasEntry(first, "replacement"),
                Matchers.hasEntry(second, "second")
            )
        );
    }

    @Test
    void preservesDifferentKeysWithTheSameText() {
        final StringBuilder first = new StringBuilder("same");
        final StringBuilder second = new StringBuilder("same");
        final Map<StringBuilder, Integer> source = new LinkedHashMap<>(2);
        source.put(first, 1);
        source.put(second, 2);
        MatcherAssert.assertThat(
            "Textual ordering must not collapse distinct non-comparable keys",
            new ArrayMap<>(source),
            Matchers.allOf(
                Matchers.aMapWithSize(2),
                Matchers.hasEntry(first, 1),
                Matchers.hasEntry(second, 2)
            )
        );
    }

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
                .with(40, "forty")
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
    void comparesCorrectlyWithAnotherMapOfIntegers() {
        MatcherAssert.assertThat(
            "should compare integer maps",
            new ArrayMap<Integer, Integer>().with(1, 1).with(2, 2),
            Matchers.equalTo(
                new ArrayMap<Integer, Integer>().with(2, 2).with(1, 1)
            )
        );
    }

    @Test
    void comparesCorrectlyWithAnotherMapOfClasses() {
        MatcherAssert.assertThat(
            "should compare class maps",
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
