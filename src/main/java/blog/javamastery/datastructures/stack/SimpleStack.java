package blog.javamastery.datastructures.stack;

import blog.javamastery.datastructures.common.Streamable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimpleStack<T> implements Iterable<T>, Streamable<T> {
    private static final int minLength = 16;
    private static final int growthRatio = 2;
    private static final int shrinkThreshold = 4;

    private T[] elements;
    private int size;

    private class SimpleStackIterator implements Iterator<T> {
        private int cursor;

        SimpleStackIterator() {
            this.cursor = size - 1;
        }

        @Override
        public boolean hasNext() {
            return cursor >= 0;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return elements[cursor--];
        }
    }

    public SimpleStack() {
        this.elements = createArray(minLength);
        this.size = 0;
    }

    public void push(T item) {
        if (elements.length == size) {
            elements = resizeArray(elements, size, size * growthRatio);
        }

        elements[size++] = item;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        var item = elements[--size];
        elements[size] = null;

        if (size <= elements.length / shrinkThreshold && elements.length > minLength) {
            elements = resizeArray(elements, size, elements.length / growthRatio);
        }

        return item;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return elements[size - 1];
    }

    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return elements[index];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    private T[] resizeArray(T[] srcArray, int oldSize, int newSize) {
        if (newSize < oldSize) {
            throw new IllegalArgumentException("Operation could result in data lost");
        }
        var tmpArray = createArray(newSize);
        System.arraycopy(srcArray, 0, tmpArray, 0, oldSize);
        return tmpArray;
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int size) {
        return (T[]) new Object[size];
    }

    @Override
    public String toString() {
        return String.format("[%s] (Size: %d) (Capacity: %d)",
                getClass().getSimpleName(), size(), capacity());
    }

    @Override
    public Iterator<T> iterator() {
        return new SimpleStackIterator();
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
