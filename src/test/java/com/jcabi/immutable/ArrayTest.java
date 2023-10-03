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

    @Test
    void worksAsANormalArray() {
        final Collection<Integer> list = new LinkedList<>();
        list.add(10);
        list.add(5);
        final Collection<Integer> array = new Array<>(list);
        MatcherAssert.assertThat(array, Matchers.hasItem(10));
        MatcherAssert.assertThat(array, Matchers.hasSize(2));
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
        final Iterable<Integer> list = Arrays.asList(10, 5);
        MatcherAssert.assertThat(
            new Array<>(list),
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
    void findsIndexOfObject() {
        MatcherAssert.assertThat(
            new Array<>(5, 2, 2, 3).indexOf(2),
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            new Array<>(5, 2, 2, 3).indexOf(0),
            Matchers.equalTo(-1)
        );
    }

    @Test
    void findsLastIndexOfObject() {
        MatcherAssert.assertThat(
            new Array<>(1, 1, 10, 10, 3).indexOf(10),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
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
        final Integer[] ints = new Integer[] {1, 2, 3};
        final Array<Integer> array = new Array<>(ints);
        ints[1] = 0;
        Assertions.assertTrue(
            Arrays.equals(array.toArray(), new Integer[]{1, 2, 3})
        );
    }

}
