package com.github.huymaster;

import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BinaryTree<T extends Comparable<T>> {
    private Node<T> root = null;

    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
        } else {
            insert(root, value);
        }
    }

    @Nullable
    public T search(Predicate<T> predicate) {
        Node<T> node = root;
        return search(node, predicate);
    }

    public void remove(Predicate<T> predicate) {
        Node<T> node = root;
        if (node != null) {
            if (predicate.test(root.value)) {
                root = null;
            } else
                remove(node, predicate);
        }
    }

    public int size() {
        return size(root);
    }

    public Node<T> getRoot() {
        return root;
    }

    private int size(Node<T> node) {
        if (node != null)
            return (1 + size(node.left) + size(node.right));
        return 0;
    }

    private void insert(Node<T> node, T value) {
        Node<T> newNode = new Node<>(value);
        if (value.compareTo(node.value) <= 0) {
            if (node.left == null) {
                node.left = newNode;
            } else {
                insert(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = newNode;
            } else {
                insert(node.right, value);
            }
        }
    }

    private T search(Node<T> node, Predicate<T> predicate) {
        if (node != null) {
            if (predicate.test(node.value)) {
                return node.value;
            }
            T left = search(node.left, predicate);
            if (left != null)
                return left;
            return search(node.right, predicate);
        }
        return null;
    }

    private void remove(Node<T> node, Predicate<T> predicate) {
        if (node != null) {
            if (predicate.test(node.value)) {
                node.value = null;

            } else {
                remove(node.left, predicate);
                remove(node.right, predicate);
            }
        }
    }

    @Override
    public String toString() {
        return "BinaryTree[" + size() + ']';
    }

    public static class Node<T extends Comparable<T>> {
        T value;
        Node<T> left = null;
        Node<T> right = null;

        public Node() {
            this(null);
        }

        public Node(T value) {
            this.value = value;
        }
    }
}
