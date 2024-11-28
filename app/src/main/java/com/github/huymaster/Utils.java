package com.github.huymaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class Utils {
    public static String CANCEL = "\u0123\u1234\u2345\u3456";

    public static void print(Object o) {
        System.out.print(o);
    }

    public static void println() {
        println("\n");
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static String readLine() {
        return readLine("Input: ");
    }

    public static String readLine(String prompt) {
        if (prompt.trim().isEmpty())
            return readLine();
        else
            return readLine(prompt, String::trim, s -> true);
    }

    public static <T> T readLine(Transformer<String, T> transformer) {
        return readLine("Input: ", transformer, s -> true);
    }

    public static String readLine(Predicate<String> condition) {
        return readLine("Input: ", String::trim, condition);
    }

    public static <T> T readLine(String prompt, Transformer<String, T> transformer) {
        return readLine(prompt, transformer, s -> true);
    }

    public static <T> T readLine(Transformer<String, T> transformer, Predicate<T> condition) {
        return readLine("Input: ", transformer, condition);
    }

    public static <T> T readLine(String prompt, Transformer<String, T> transformer, Predicate<T> condition) {

        JFrame frame = new JFrame("Input reader");
        AtomicReference<String> input = new AtomicReference<>("");
        Object lock = new Object();

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                print(prompt);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                println("\nFocus lost. Please focus the window to continue typing.");
            }
        };
        frame.addFocusListener(focusListener);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                synchronized (lock) {
                    super.keyTyped(e);
                    if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                        input.set(CANCEL);
                        println("\nCancelled.");
                        frame.removeFocusListener(focusListener);
                        frame.dispose();
                        lock.notifyAll();
                    } else if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                        print("\n");
                        frame.removeFocusListener(focusListener);
                        frame.dispose();
                        lock.notifyAll();
                    } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                        if (!input.get().isEmpty()) {
                            input.set(input.get().substring(0, input.get().length() - 1));
                            print(e.getKeyChar());
                        }
                    } else {
                        input.set(input.get() + e.getKeyChar());
                        print(e.getKeyChar());
                    }
                }
            }
        });
        frame.setLayout(new BorderLayout());
        JLabel label = new JLabel("Type input here and press Enter to confirm");
        label.setHorizontalAlignment(JLabel.CENTER);

        frame.add(label, BorderLayout.CENTER);
        frame.setSize(320, 240);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        synchronized (lock) {
            try {
                lock.wait();
                String line = input.get().trim();
                T result = transformer.transform(line);
                if (!condition.test(result)) {
                    println("Input is not matching the condition");
                    return readLine(prompt, transformer, condition);
                }
                return result;
            } catch (Exception e) {
                println("Error " + e.getClass().getSimpleName() + ": " + e.getMessage());
                return readLine(prompt, transformer, condition);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] clearNulls(T[] array) {
        int notNullCount = 0;
        for (T value : array) {
            if (value != null)
                notNullCount++;
        }
        T[] result = (T[]) new Object[notNullCount];
        int index = 0;
        for (T t : array) {
            if (t != null)
                result[index++] = t;
        }
        return result;
    }

    public static <T> void bubbleSort(T[] array, Comparator<T> comparator) {
        T[] notNullArray = clearNulls(array);
        for (int i = 0; i < notNullArray.length - 1; i++) {
            for (int j = 0; j < notNullArray.length - i - 1; j++) {
                if (comparator.compare(notNullArray[j], notNullArray[j + 1]) > 0) {
                    T temp = notNullArray[j];
                    notNullArray[j] = notNullArray[j + 1];
                    notNullArray[j + 1] = temp;
                }
            }
        }
        System.arraycopy(notNullArray, 0, array, 0, notNullArray.length);
    }

    public static <T> void quickSort(T[] array, int low, int high, Comparator<T> comparator) {
        T[] notNullArray = clearNulls(array);
        if (notNullArray.length != array.length)
            quickSort(notNullArray, 0, notNullArray.length - 1, comparator);
        else if (low < high) {
            int pi = partition(notNullArray, low, high, comparator);
            quickSort(notNullArray, low, pi - 1, comparator);
            quickSort(notNullArray, pi + 1, high, comparator);
        }
        System.arraycopy(notNullArray, 0, array, 0, notNullArray.length);
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator) {
        T pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public interface Transformer<I, O> {
        O transform(I input);
    }
}
