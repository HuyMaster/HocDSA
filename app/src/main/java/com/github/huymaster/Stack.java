package com.github.huymaster;

@SuppressWarnings("unchecked")
public class Stack<T> {
    private Object[] items = new Object[2];
    private int capacity = items.length;
    private int top = -1;

    public synchronized void push(T item) {
        if (top + 1 >= capacity) {
            grow();
        }
        items[++top] = item;
    }

    public synchronized T pop() {
        if (top < capacity / 4) {
            shrink();
        }
        return (T) items[top--];
    }

    public synchronized T peek() {
        return (T) items[top];
    }

    public int size() {
        return top + 1;
    }

    @Override
    public String toString() {
        return String.format("Stack[%5s/%5s]", size(), capacity);
    }

    private synchronized void grow() {
        capacity *= 2;
        int maxSize = 16 * 1024;
        if (capacity > maxSize)
            throw new StackOverflowError("Stack overflow");
        Object[] newItems = new Object[capacity];
        System.arraycopy(items, 0, newItems, 0, top + 1);
        items = newItems;
    }

    private synchronized void shrink() {
        capacity /= 2;
        Object[] newItems = new Object[capacity];
        System.arraycopy(items, 0, newItems, 0, top + 1);
        items = newItems;
    }
}
