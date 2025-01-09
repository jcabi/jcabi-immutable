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
