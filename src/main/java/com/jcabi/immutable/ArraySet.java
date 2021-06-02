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

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Set on top of array.
 *
 * <p>This class is truly immutable. This means that it never changes
 * its encapsulated values and is annotated with {@code @Immutable}
 * annotation.
 * <p>
 * Limitations:
 * Encapsulated objects with exposed mutators can mutate their state.
 *
 * Since this Set implementation is backed by array,<br>
 * complexity of {@link ArraySet#contains} is (<b>O(n)</b>).
 *
 * @param <T> Value key type
 * @since 0.1
 * @checkstyle MissingDeprecatedCheck (270 lines)
 */
@Immutable
@Loggable(Loggable.DEBUG)
@SuppressWarnings({ "unchecked", "PMD.TooManyMethods" })
public final class ArraySet<T> implements Set<T> {

    /**
     * All vals.
     */
    @Immutable.Array
    private final transient T[] values;

    /**
     * Public ctor.
     */
    public ArraySet() {
        this.values = (T[]) new Object[0];
    }

    /**
     * Public ctor.
     * @param set Original set
     * @since 0.12
     */
    public ArraySet(final ArraySet<T> set) {
        this.throwIfArgumentIsNull(
            set, "ArraySet argument of ArraySet ctor can't be NULL"
        );
        this.values = set.values;
    }

    /**
     * Public ctor.
     * @param set Original set
     * @since 0.12
     */
    public ArraySet(final Collection<T> set) {
        this.throwIfArgumentIsNull(
            set, "Collection argument of ArraySet ctor can't be NULL"
        );
        final Set<T> hset = new HashSet<>(Collection.class.cast(set));
        this.values = hset.toArray((T[]) new Object[hset.size()]);
    }

    /**
     * Public ctor.
     * @param set Original set
     * @since 0.12
     */
    public ArraySet(final Iterable<T> set) {
        this.throwIfArgumentIsNull(
            set, "Iterable argument of ArraySet ctor can't be NULL"
        );
        final Set<T> hset = new HashSet<>(0);
        for (final T item : set) {
            hset.add(item);
        }
        this.values = hset.toArray((T[]) new Object[hset.size()]);
    }

    /**
     * Make a new one with an extra entry.
     * @param value The value
     * @return New set
     */
    public ArraySet<T> with(final T value) {
        this.throwIfArgumentIsNull(
            value, "argument of ArraySet#with() can't be NULL"
        );
        final Collection<T> list = new HashSet<>(this.size() + 1);
        list.addAll(this);
        list.remove(value);
        list.add(value);
        return new ArraySet<T>(list);
    }

    /**
     * Make a new one with some extra entries.
     * @param vals Values to add
     * @return New set
     */
    public ArraySet<T> with(final Collection<T> vals) {
        this.throwIfArgumentIsNull(
            vals, "arguments of ArraySet#with() can't be NULL"
        );
        final Collection<T> list = new HashSet<>(this.size());
        list.addAll(this);
        list.removeAll(vals);
        list.addAll(vals);
        return new ArraySet<T>(list);
    }

    /**
     * Make a new one without an extra entry.
     * @param value The value
     * @return New set
     */
    public ArraySet<T> without(final T value) {
        this.throwIfArgumentIsNull(
            value, "argument of ArraySet#without() can't be NULL"
        );
        final Collection<T> list = new LinkedList<>();
        list.addAll(this);
        list.remove(value);
        return new ArraySet<T>(list);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.values);
    }

    @Override
    public boolean equals(final Object object) {
        final boolean equals;
        if (object instanceof ArraySet) {
            equals = Arrays.deepEquals(
                this.values, ArraySet.class.cast(object).values
            );
        } else {
            equals = false;
        }
        return equals;
    }

    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder();
        for (final T item : this.values) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(item);
        }
        return text.toString();
    }

    @Override
    public int size() {
        return this.values.length;
    }

    @Override
    public boolean isEmpty() {
        return this.values.length == 0;
    }

    @Override
    public boolean contains(final Object key) {
        return Arrays.asList(this.values).contains(key);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(
            Arrays.asList(this.values)
        ).iterator();
    }

    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.values.length];
        System.arraycopy(this.values, 0, array, 0, this.values.length);
        return array;
    }

    @Override
    public <T> T[] toArray(final T[] array) {
        final T[] dest;
        if (array.length == this.values.length) {
            dest = array;
        } else {
            dest = (T[]) new Object[this.values.length];
        }
        System.arraycopy(this.values, 0, dest, 0, this.values.length);
        return dest;
    }

    @Override
    @Deprecated
    public boolean add(final T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean remove(final Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Collection<?> col) {
        return Arrays.asList(this.values).containsAll(col);
    }

    @Override
    @Deprecated
    public boolean addAll(final Collection<? extends T> col) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean retainAll(final Collection<?> col) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean removeAll(final Collection<?> col) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Throws IllegalArgumentException if the input parameter is null.
     * @param obj Object to check its nullity
     * @param which Appellation of object
     */
    private void throwIfArgumentIsNull(final Object obj, final String which) {
        if (obj == null) {
            throw new IllegalArgumentException(
                String.format(
                    "%s argument of ArraySortedSet ctor can't be NULL", which
                )
            );
        }
    }
}
