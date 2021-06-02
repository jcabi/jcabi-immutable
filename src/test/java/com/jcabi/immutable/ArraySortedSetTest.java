/*
 * Copyright (c) 2012-2021, jcabi.com
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

import com.jcabi.aspects.Tv;
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

    /**
     * ArraySortedSet can work as a sorted set.
     * @throws Exception If some problem inside
     */
    @Test
    void worksAsNormalSortedSet() throws Exception {
        final SortedSet<Integer> set = new ArraySortedSet<>(
            Arrays.asList(Tv.TEN, Tv.FIVE)
        );
        MatcherAssert.assertThat(set, Matchers.hasItem(Tv.TEN));
        MatcherAssert.assertThat(set, Matchers.hasSize(2));
        MatcherAssert.assertThat(set.first(), Matchers.equalTo(Tv.FIVE));
        MatcherAssert.assertThat(set.last(), Matchers.equalTo(Tv.TEN));
    }

    /**
     * ArraySortedSet can build set fluently.
     * @throws Exception If some problem inside
     */
    @Test
    void buildsSetFluently() throws Exception {
        MatcherAssert.assertThat(
            new ArraySortedSet<Integer>(ArraySortedSetTest.CMP)
                .with(Tv.TEN)
                .with(Tv.FIVE)
                .with(Tv.FIVE)
                .with(Tv.THOUSAND)
                .without(Tv.TEN)
                .without(Tv.THREE)
                .without(Tv.THOUSAND),
            Matchers.allOf(
                Matchers.<Integer>iterableWithSize(1),
                Matchers.hasItem(Tv.FIVE)
            )
        );
    }

    /**
     * ArraySortedSet can compare correctly with another set.
     * @throws Exception If some problem inside
     */
    @Test
    void comparesWithAnotherArraySortedSet() throws Exception {
        MatcherAssert.assertThat(
            new ArraySortedSet<Integer>(ArraySortedSetTest.CMP)
                .with(Tv.TEN).with(2),
            Matchers.equalTo(
                new ArraySortedSet<Integer>(ArraySortedSetTest.CMP)
                    .with(2).with(Tv.TEN)
            )
        );
    }

    /**
     * ArraySortedSet can encapsulate iterables.
     * @throws Exception If some problem inside
     * @since 0.12
     */
    @Test
    void encapsulatesIterables() throws Exception {
        final Iterable<Integer> list = Arrays.asList(Tv.TEN, Tv.FIVE, Tv.SEVEN);
        MatcherAssert.assertThat(
            new ArraySortedSet<Integer>(list),
            Matchers.contains(Tv.FIVE, Tv.SEVEN, Tv.TEN)
        );
    }

    /**
     * ArraySortedSet can work with a custom comparator.
     * @throws Exception If some problem inside
     */
    @Test
    void worksWithCustomComparator() throws Exception {
        final String first = "some text that is long";
        final String second = "short text";
        MatcherAssert.assertThat(
            new ArraySortedSet<String>(
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

    /**
     * ArraySortedSet can replace a comparator of an another ArraySortedSet.
     * @throws Exception If some problem inside
     */
    @Test
    void replacesComparator() throws Exception {
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
            new ArraySortedSet<String>(
                origin,
                new ArrayComparator.Default<String>()
            ),
            Matchers.contains(second, first)
        );
    }

}
