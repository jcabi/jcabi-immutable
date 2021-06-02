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
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sorted Set on top of array.
 *
 * <p>This class is truly immutable. This means that it never changes
 * its encapsulated values and is annotated with {@code @Immutable}
 * annotation.
 * <p>
 * Limitation:
 * Encapsulated objects with exposed mutators can mutate their state.
 *
 * @param <T> Value key type
 * @since 0.1
 * @checkstyle MissingDeprecatedCheck (360 lines)
 */
@Immutable
@Loggable(Loggable.DEBUG)
@SuppressWarnings({ "unchecked", "PMD.TooManyMethods" })
public final class ArraySortedSet<T> implements SortedSet<T> {

    /**
     * All values.
     */
    @Immutable.Array
    private final transient T[] values;

    /**
     * Comparator to use.
     */
    private final transient ArrayComparator<T> cmp;

    /**
     * Public ctor.
     * @param comparator Comparator to use
     */
    public ArraySortedSet(final ArrayComparator<T> comparator) {
        this(comparator, (T[]) new Object[0]);
    }

    /**
     * Public ctor.
     * @param set Original set
     */
    public ArraySortedSet(final T... set) {
        this(Arrays.asList(set));
    }

    /**
     * Public ctor.
     * @param comparator The comparator to use
     * @param set Original set
     */
    public ArraySortedSet(final ArrayComparator<T> comparator,
        final T... set) {
        this(Arrays.asList(set), comparator);
    }

    /**
     * Public ctor, with default comparator.
     * @param set Original set
     * @since 0.12
     */
    public ArraySortedSet(final Iterable<T> set) {
        this(set, new ArrayComparator.Default<T>());
    }

    /**
     * Public ctor.
     * @param set Original set
     * @param comparator Comparator to use
     */
    public ArraySortedSet(final ArraySortedSet<T> set,
        final ArrayComparator<T> comparator) {
        this.throwIfArgumentIsNull(
            set, "arraySortedSet argument of ArraySortedSet ctor can't be NULL"
        );
        this.throwIfComparatorArgumentIsNull(comparator);
        this.cmp = comparator;
        final ArraySortedSet<T> origin = set;
        if (origin.cmp.equals(this.cmp)) {
            this.values = origin.values;
        } else {
            final Set<T> hset = new TreeSet<>(this.cmp);
            hset.addAll(Collection.class.cast(set));
            this.values = hset.toArray((T[]) new Object[hset.size()]);
        }
    }

    /**
     * Public ctor.
     * @param set Original set
     * @param comparator Comparator to use
     */
    public ArraySortedSet(
        final Collection<T> set,
        final ArrayComparator<T> comparator
    ) {
        this.throwIfArgumentIsNull(
            set, "collection argument of ArraySortedSet ctor can't be NULL"
        );
        this.throwIfComparatorArgumentIsNull(comparator);
        this.cmp = comparator;
        final Set<T> hset = new TreeSet<>(this.cmp);
        hset.addAll(Collection.class.cast(set));
        this.values = hset.toArray((T[]) new Object[hset.size()]);
    }

    /**
     * Public ctor.
     * @param set Original set
     * @param comparator Comparator to use
     */
    public ArraySortedSet(
        final Iterable<T> set,
        final ArrayComparator<T> comparator
    ) {
        this.throwIfArgumentIsNull(
            set, "iterable argument of ArraySortedSet ctor can't be NULL"
        );
        this.throwIfComparatorArgumentIsNull(comparator);
        this.cmp = comparator;
        final Set<T> hset = new TreeSet<>(this.cmp);
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
    public ArraySortedSet<T> with(final T value) {
        this.throwIfArgumentIsNull(
            value, "argument of ArraySortedSet#with() can't be NULL"
        );
        final Collection<T> list = new TreeSet<>(this.cmp);
        list.addAll(this);
        list.remove(value);
        list.add(value);
        return new ArraySortedSet<T>(list, this.cmp);
    }

    /**
     * Make a new one with some extra entries.
     * @param vals Values to add
     * @return New set
     */
    public ArraySortedSet<T> with(final Collection<T> vals) {
        this.throwIfArgumentIsNull(
            vals, "arguments of ArraySortedSet#with() can't be NULL"
        );
        final Collection<T> list = new TreeSet<>(this.cmp);
        list.addAll(this);
        list.removeAll(vals);
        list.addAll(vals);
        return new ArraySortedSet<T>(list, this.cmp);
    }

    /**
     * Make a new one without an extra entry.
     * @param value The value
     * @return New set
     */
    public ArraySortedSet<T> without(final T value) {
        this.throwIfArgumentIsNull(
            value, "argument of ArraySortedSet#without() can't be NULL"
        );
        final Collection<T> list = new TreeSet<>(this.cmp);
        list.addAll(this);
        list.remove(value);
        return new ArraySortedSet<T>(list, this.cmp);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.values);
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof ArraySortedSet
            && Arrays.deepEquals(
                this.values, ArraySortedSet.class.cast(object).values
            );
    }

    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder(0);
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
    public Comparator<? super T> comparator() {
        return this.cmp;
    }

    @Override
    public SortedSet<T> subSet(final T from, final T till) {
        return Collections.unmodifiableSortedSet(
            new TreeSet<T>(this).subSet(from, till)
        );
    }

    @Override
    public SortedSet<T> headSet(final T till) {
        return Collections.unmodifiableSortedSet(
            new TreeSet<T>(this)
        ).headSet(till);
    }

    @Override
    public SortedSet<T> tailSet(final T from) {
        return Collections.unmodifiableSortedSet(
            new TreeSet<T>(this)
        ).tailSet(from);
    }

    @Override
    public T first() {
        if (this.values.length == 0) {
            throw new NoSuchElementException("sorted set is empty, no first()");
        }
        return this.values[0];
    }

    @Override
    public T last() {
        if (this.values.length == 0) {
            throw new NoSuchElementException("sorted set is empty, not last()");
        }
        return this.values[this.values.length - 1];
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
        throw new UnsupportedOperationException(
            "add(): ArraySortedSet is immutable"
        );
    }

    @Override
    @Deprecated
    public boolean remove(final Object obj) {
        throw new UnsupportedOperationException(
            "remove(): ArraySortedSet is immutable"
        );
    }

    @Override
    public boolean containsAll(final Collection<?> col) {
        return Arrays.asList(this.values).containsAll(col);
    }

    @Override
    @Deprecated
    public boolean addAll(final Collection<? extends T> col) {
        throw new UnsupportedOperationException(
            "addAll(): ArraySortedSet is immutable"
        );
    }

    @Override
    @Deprecated
    public boolean retainAll(final Collection<?> col) {
        throw new UnsupportedOperationException(
            "retainAll(): ArraySortedSet is immutable"
        );
    }

    @Override
    @Deprecated
    public boolean removeAll(final Collection<?> col) {
        throw new UnsupportedOperationException(
            "removeAll(): ArraySortedSet is immutable"
        );
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException(
            "clear(): ArraySortedSet is immutable"
        );
    }

    /**
     * Throws IllegalArgumentException if the input parameter is null.
     * @param comp Comparator to check its nullity
     */
    private void throwIfComparatorArgumentIsNull(final Comparator<T> comp) {
        this.throwIfArgumentIsNull(
            comp, "comparator argument of ArraySortedSet ctor can't be NULL"
        );
    }

    /**
     * Throws IllegalArgumentException if the input parameter is null.
     * @param obj Object to check its nullity
     * @param message Message to send with the exception
     */
    private void throwIfArgumentIsNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
