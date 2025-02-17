package blog.javamastery.datastructures.queue;

import blog.javamastery.datastructures.common.Streamable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimpleQueue<T> implements Iterable<T>, Streamable<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public SimpleQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T data = head.data;
        head = head.next;
        size--;

        if (isEmpty()) {
            tail = null; // Empty queue
        }

        return data;
    }

    public T peek() {
        return isEmpty() ? null : head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return String.format("%s[]", getClass().getSimpleName());
        }

        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        return String.format("%s[%s]", getClass().getSimpleName(), sb);
    }
}
