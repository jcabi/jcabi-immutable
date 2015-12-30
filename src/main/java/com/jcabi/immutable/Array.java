/**
 * Copyright (c) 2012-2015, jcabi.com
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Array as an object.
 *
 * <p>This class is truly immutable. This means that it never changes
 * its encapsulated values and is annotated with {@code &#64;Immutable}
 * annotation.
 *
 * @param <T> Value key type
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@Loggable(Loggable.DEBUG)
@SuppressWarnings({ "unchecked", "PMD.TooManyMethods" })
public final class Array<T> implements List<T> {

    /**
     * All values.
     */
    @Immutable.Array
    private final transient T[] values;

    /**
     * Public ctor, for an zero-length empty array.
     */
    public Array() {
        this((T[]) new Object[0]);
    }

    /**
     * Public ctor, from an array of values.
     * @param list Items to encapsulate
     */
    public Array(final T... list) {
        this.values = list;
    }

    /**
     * Public ctor.
     * @param list Items to encapsulate
     * @since 0.12
     */
    public Array(final Iterable<T> list) {
        if (list == null) {
            throw new IllegalArgumentException(
                "list of objects can't be NULL"
            );
        }
        if (list instanceof Array) {
            this.values = ((Array<T>) list).values;
        } else if (list instanceof Collection) {
            final Collection<T> col = Collection.class.cast(list);
            this.values = (T[]) new Object[col.size()];
            col.toArray(this.values);
        } else {
            final Collection<T> items = new LinkedList<T>();
            for (final T item : list) {
                items.add(item);
            }
            this.values = (T[]) new Object[items.size()];
            items.toArray(this.values);
        }
    }

    /**
     * Make a new one with an extra entry, at the end of array (will be
     * extended by one extra element).
     * @param value The value
     * @return New vector
     */
    public Array<T> with(final T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                "argument of Array#with() can't be NULL"
            );
        }
        final T[] items = (T[]) new Object[this.values.length + 1];
        System.arraycopy(this.values, 0, items, 0, this.values.length);
        items[this.values.length] = value;
        return new Array<T>(items);
    }

    /**
     * Make a new extra entries, at the end of array.
     * @param vals The values
     * @return New vector
     */
    public Array<T> with(final Iterable<T> vals) {
        if (vals == null) {
            throw new IllegalArgumentException(
                "arguments of Array#with() can't be NULL"
            );
        }
        final Array<T> array;
        if (vals instanceof Collection) {
            final T[] items = (T[]) new Object[
                this.values.length + Collection.class.cast(vals).size()
            ];
            System.arraycopy(this.values, 0, items, 0, this.values.length);
            int idx = this.values.length;
            for (final T value : vals) {
                items[idx] = value;
                ++idx;
            }
            array = new Array<T>(items);
        } else {
            final Collection<T> list = new LinkedList<T>();
            list.addAll(Arrays.asList(this.values));
            for (final T value : vals) {
                list.add(value);
            }
            array = new Array<T>(list);
        }
        return array;
    }

    /**
     * Make a new one with an extra entry at the given position.
     * @param pos Position to replace
     * @param value The value
     * @return New array
     */
    public Array<T> with(final int pos, final T value) {
        if (value == null) {
            throw new IllegalArgumentException(
                "second argument of Array#with() can't be NULL"
            );
        }
        final T[] temp = (T[]) new Object[
            Math.max(this.values.length, pos + 1)
        ];
        System.arraycopy(this.values, 0, temp, 0, this.values.length);
        temp[pos] = value;
        return new Array<T>(temp);
    }

    /**
     * Make a new array, without element on specific index.
     *
     * <p>The method throws {@link ArrayIndexOutOfBoundsException} if such
     * position is absent in the array.
     *
     * @param idx The position to remove
     * @return New array
     */
    public Array<T> withoutIndex(final int idx) {
        if (idx >= this.values.length) {
            throw new ArrayIndexOutOfBoundsException(
                String.format(
                    "index %d is out of bounds: [0..%d]",
                    idx, this.values.length
                )
            );
        }
        if (idx < 0) {
            throw new ArrayIndexOutOfBoundsException(
                String.format("index can't be negative: %d", idx)
            );
        }
        final T[] items = (T[]) new Object[this.values.length - 1];
        System.arraycopy(this.values, 0, items, 0, idx);
        System.arraycopy(
            this.values, idx + 1, items, idx, this.values.length - idx - 1
        );
        return new Array<T>(items);
    }

    /**
     * Make a new array, without this element (or the same array if such
     * an element is absent).
     * @param item The element to remove
     * @return New array
     * @since 1.4
     */
    public Array<T> without(final T item) {
        int idx = -1;
        for (int pos = 0; pos < this.values.length; ++pos) {
            if (this.values[pos].equals(item)) {
                idx = pos;
                break;
            }
        }
        final Array<T> array;
        if (idx >= 0) {
            array = this.withoutIndex(idx);
        } else {
            array = this;
        }
        return array;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.values);
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof Array
            && Arrays.deepEquals(this.values, Array.class.cast(object).values);
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
        final T[] target;
        if (array.length == this.values.length) {
            target = array;
        } else {
            target = (T[]) new Object[this.values.length];
        }
        System.arraycopy(this.values, 0, target, 0, this.values.length);
        return target;
    }

    @Override
    public boolean add(final T element) {
        throw new UnsupportedOperationException(
            "add(): Array is immutable"
        );
    }

    @Override
    public boolean remove(final Object obj) {
        throw new UnsupportedOperationException(
            "remove(): Array is immutable"
        );
    }

    @Override
    public boolean containsAll(final Collection<?> col) {
        return Arrays.asList(this.values).containsAll(col);
    }

    @Override
    public boolean addAll(final Collection<? extends T> col) {
        throw new UnsupportedOperationException(
            "addAll(): Array is immutable"
        );
    }

    @Override
    public boolean retainAll(final Collection<?> col) {
        throw new UnsupportedOperationException(
            "retainAll(): Array is immutable"
        );
    }

    @Override
    public boolean removeAll(final Collection<?> col) {
        throw new UnsupportedOperationException(
            "removeAll(): Array is immutable"
        );
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(
            "clear(): Array is immutable"
        );
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> col) {
        throw new UnsupportedOperationException(
            "addAll(): Array is immutable, can't change"
        );
    }

    @Override
    public T get(final int index) {
        if (index < 0 || index >= this.values.length) {
            throw new IndexOutOfBoundsException(
                String.format(
                    "index %d is out of bounds, length=%d",
                    index,
                    this.values.length
                )
            );
        }
        return this.values[index];
    }

    @Override
    public T set(final int index, final T element) {
        throw new UnsupportedOperationException(
            "set(idx): Array is immutable"
        );
    }

    @Override
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException(
            "add(idx): Array is immutable"
        );
    }

    @Override
    public T remove(final int index) {
        throw new UnsupportedOperationException(
            "remove(idx): Array is immutable"
        );
    }

    @Override
    public int indexOf(final Object obj) {
        return Arrays.asList(this.values).indexOf(obj);
    }

    @Override
    public int lastIndexOf(final Object obj) {
        return Arrays.asList(this.values).lastIndexOf(obj);
    }

    @Override
    public ListIterator<T> listIterator() {
        return Collections.unmodifiableList(
            Arrays.asList(this.values)
        ).listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return Collections.unmodifiableList(
            Arrays.asList(this.values)
        ).listIterator(index);
    }

    @Override
    public List<T> subList(final int from, final int till) {
        return Collections.unmodifiableList(
            Arrays.asList(this.values).subList(from, till)
        );
    }

}
