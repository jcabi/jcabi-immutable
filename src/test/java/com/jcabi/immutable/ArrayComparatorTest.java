/*
 * SPDX-FileCopyrightText: Copyright (c) 2012-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.immutable;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ArrayComparator}.
 * @since 1.5
 */
final class ArrayComparatorTest {

    @Test
    void reverseComparatorCanCompare() {
        MatcherAssert.assertThat(
            new ArrayComparator.Reverse<Integer>().compare(1, 2),
            Matchers.greaterThan(0)
        );
    }
}
