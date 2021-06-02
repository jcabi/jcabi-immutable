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
import java.util.Collection;
import java.util.LinkedList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Array}.
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
final class ArrayTest {

    /**
     * Array can work as an array.
     * @throws Exception If some problem inside
     */
    @Test
    void worksAsANormalArray() throws Exception {
        final Collection<Integer> list = new LinkedList<>();
        list.add(Tv.TEN);
        list.add(Tv.FIVE);
        final Collection<Integer> array = new Array<>(list);
        MatcherAssert.assertThat(array, Matchers.hasItem(Tv.TEN));
        MatcherAssert.assertThat(array, Matchers.hasSize(2));
    }

    /**
     * Array can build array fluently.
     * @throws Exception If some problem inside
     */
    @Test
    void buildsArrayFluently() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(Tv.FIVE)
                .with(Tv.TEN)
                .with(Tv.THOUSAND)
                .with(0, Tv.TEN)
                .with(Tv.THREE, Tv.THREE)
                .with(1, Tv.THOUSAND),
            Matchers.allOf(
                Matchers.<Integer>iterableWithSize(Tv.FOUR),
                Matchers.contains(Tv.TEN, Tv.THOUSAND, Tv.THOUSAND, Tv.THREE)
            )
        );
    }

    /**
     * Array can remove items by index.
     * @throws Exception If some problem inside
     */
    @Test
    void removesElementsByIndex() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(Tv.FIVE)
                .with(Tv.TEN)
                .with(Tv.THOUSAND)
                .withoutIndex(0)
                .withoutIndex(0)
                .withoutIndex(0),
            Matchers.empty()
        );
    }

    /**
     * Array can remove items.
     * @throws Exception If some problem inside
     */
    @Test
    void removesElements() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>()
                .with(Tv.FIVE)
                .with(Tv.TEN)
                .with(Tv.THOUSAND)
                .without(Tv.FIVE)
                .without(Tv.THREE)
                .without(Tv.MILLION),
            Matchers.hasSize(2)
        );
    }

    /**
     * Array can encapsulate iterables.
     * @throws Exception If some problem inside
     * @since 0.12
     */
    @Test
    void encapsulatesIterables() throws Exception {
        final Iterable<Integer> list = Arrays.asList(Tv.TEN, Tv.FIVE);
        MatcherAssert.assertThat(
            new Array<Integer>(list),
            Matchers.hasItem(Tv.TEN)
        );
    }

    /**
     * Array can encapsulate another Array instance.
     */
    @Test
    void encapsulatesArrays() {
        final Array<Integer> array = new Array<>(Tv.TEN, Tv.FIVE);
        array.with(Tv.MILLION);
        MatcherAssert.assertThat(
            new Array<Integer>(array),
            Matchers.hasItem(Tv.TEN)
        );
    }

    /**
     * Array can find index of an object.
     * @throws Exception If some problem inside
     */
    @Test
    void findsIndexOfObject() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>(Tv.FIVE, 2, 2, Tv.THREE).indexOf(2),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            new Array<Integer>(Tv.FIVE, 2, 2, Tv.THREE).indexOf(0),
            Matchers.equalTo(-1)
        );
    }

    /**
     * Array can find last index of an object.
     * @throws Exception If some problem inside
     */
    @Test
    void findsLastIndexOfObject() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>(1, 1, Tv.TEN, Tv.TEN, Tv.THREE).indexOf(Tv.TEN),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            new Array<Integer>(1, 1, Tv.TEN, Tv.TEN, Tv.THREE).indexOf(0),
            Matchers.equalTo(-1)
        );
    }

    /**
     * Array can build a list iterator.
     * @throws Exception If some problem inside
     */
    @Test
    void makesListIterator() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>(Tv.FIVE, 2, 2, Tv.THREE).listIterator().next(),
            Matchers.equalTo(Tv.FIVE)
        );
    }

    /**
     * Array can build a list iterator with an index.
     * @throws Exception If some problem inside
     */
    @Test
    void makesListIteratorWithIndex() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>(Tv.FIVE, 1, 2, Tv.THREE).listIterator(2).next(),
            Matchers.equalTo(2)
        );
    }

    /**
     * Array can build a sub-list.
     * @throws Exception If some problem inside
     */
    @Test
    void makesSubList() throws Exception {
        MatcherAssert.assertThat(
            new Array<Integer>(Tv.FIVE, 1, 2).subList(1, Tv.THREE),
            Matchers.contains(1, 2)
        );
    }

    /**
     * Array must be independent from data we passed to its ctor.
     * @throws Exception If some problem inside
     * @checkstyle MagicNumberCheck (10 lines)
     */
    @Test
    void isIndependentFromCtorParam() throws Exception {
        final Integer[] ints = new Integer[] {1, 2, 3};
        final Array<Integer> array = new Array<>(ints);
        ints[1] = 0;
        Assertions.assertTrue(
            Arrays.equals(array.toArray(), new Integer[]{1, 2, 3})
        );
    }

}
