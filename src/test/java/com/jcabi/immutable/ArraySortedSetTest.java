/*
 * Copyright (c) 2012-2025, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
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
