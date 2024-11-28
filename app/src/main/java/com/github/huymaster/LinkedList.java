package com.github.huymaster;

import java.util.function.Function;

public class LinkedList<T> {
    Node<T> root = null;
    Node<T> tail = null;
    int size = 0;

    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    public T get(int index) {
        Node<T> node = root;
        for (int i = 0; i < index; i++) {
            if (node == null) {
                return null;
            }
            node = node.next;
        }
        if (node == null) return null;
        return node.value;
    }

    public void set(int index, T value) {
        Node<T> node = root;
        for (int i = 0; i < index; i++) {
            if (node == null) {
                return;
            }
            node = node.next;
        }
        node.value = value;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            return;
        }
        Node<T> node = root;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        T result = node.value;
        if (index == 0) {
            root = node.next;
        } else {
            Node<T> prev = root;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            prev.next = node.next;
        }
        size--;
    }

    public int find(Object value, Function<T, Object> selector) {
        Node<T> node = root;
        for (int i = 0; i < size; i++) {
            if (node == null)
                return -1;
            if (selector.apply(node.value).equals(value)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    public int size() {
        return size;
    }

    public void applyArray(T[] array) {
        root = null;
        for (T t : array) {
            add(t);
        }
    }

    public T[] toArray(Function<Integer, T[]> arrayCreator) {
        Node<T> node = root;
        T[] array = arrayCreator.apply(size);
        for (int i = 0; i < size; i++) {
            array[i] = node.value;
            node = node.next;
            if (node == null) {
                break;
            }
        }
        return array;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedList ");
        Node<T> node = root;
        while (node != null) {
            sb.append("[").append(node.value).append("]");
            if (node.next != null) {
                sb.append(" - ");
            }
            node = node.next;
        }
        return sb.toString();
    }

    private static class Node<T> {
        T value;
        Node<T> next;

        public Node() {
            this(null);
        }

        public Node(T value) {
            this(value, null);
        }

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }
}

