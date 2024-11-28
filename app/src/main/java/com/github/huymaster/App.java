package com.github.huymaster;

import com.formdev.flatlaf.FlatDarkLaf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.huymaster.Utils.println;

public class App {
    static LinkedList<Student> students = new LinkedList<>();
    private static boolean brokerRunning = false;
    private static final Task[] tasks = {
            Task.newTask("1", "Add student", App::addStudent),
            Task.newTask("2", "Update student", App::updateStudent),
            Task.newTask("3", "Delete student", App::deleteStudent),
            Task.newTask("4", "Find student", App::findStudent),
            Task.newTask("5", "List students", App::listStudents),
            Task.newTask("6", "Sort students (bubble sort)", App::bubbleSort),
            Task.newTask("7", "Sort students (quick sort)", App::quickSort),
            Task.newTask("0", "Exit", () -> {
                brokerRunning = true;
                Arrays.stream(JFrame.getWindows()).forEach(Window::dispose);
                System.exit(0);
            })
    };

    private static void addStudent() {
        println("Add student");
        Student student = Student.newStudent();
        students.add(student);
        println("Student added.");
    }

    private static void updateStudent() {
        println("Update student");
        String id = Utils.readLine("Student ID to update: ", String::toUpperCase);
        int index = students.find(id, (s) -> s.id);
        if (index == -1) {
            println("Student not found.");
            return;
        }
        Pattern namePattern = Pattern.compile("[A-Za-z ]+");
        String name = Utils.readLine("Name: ", (s) -> s, (s) -> namePattern.matcher(s).matches());
        double score = Utils.readLine("Score: ", Double::parseDouble, (s) -> s >= 0.0 && s <= 10.0);
        students.set(index, students.get(index).copy(name, score));
        println("Student updated.");
    }

    private static void deleteStudent() {
        println("Delete student");
        Pattern idPattern = Pattern.compile("[Bb][Hh][0-9]{5}");
        String id = Utils.readLine("Student ID to delete: ", String::toUpperCase, (s) -> idPattern.matcher(s).matches());
        int index = students.find(id, (s) -> s.id);
        if (index == -1) {
            println("Student not found.");
            return;
        }
        students.remove(index);
        println("Student deleted.");
    }

    private static void findStudent() {
        println("Find student");
        Pattern idPattern = Pattern.compile("[Bb][Hh][0-9]{5}");
        String id = Utils.readLine("Student ID to find: ", String::toUpperCase, (s) -> idPattern.matcher(s).matches());
        int index = students.find(id, (s) -> s.id);
        if (index == -1) {
            println("Student not found.");
            return;
        }
        println(students.get(index).toString());
    }

    private static void listStudents() {
        println("List students");
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i) != null)
                println(students.get(i));
        }
    }

    private static void bubbleSort() {
        println("Sort students (bubble sort)");
        Student[] array = students.toArray(Student[]::new);
        Utils.bubbleSort(array, selectSort());
        students.applyArray(array);
    }

    private static void quickSort() {
        println("Sort students (quick sort)");
        Student[] array = students.toArray(Student[]::new);
        Utils.quickSort(array, 0, array.length - 1, selectSort());
        students.applyArray(array);
    }

    private static Comparator<Student> selectSort() {
        Map<String, Comparator<Student>> comparators = new HashMap<>(3);
        comparators.put("1", Comparator.comparing(s -> s.id));
        comparators.put("2", Comparator.comparing(s -> s.name));
        comparators.put("3", Comparator.comparingDouble(s -> s.score));
        println("Sort by: \n1. ID\n2. Name\n3. Score");
        String choice = Utils.readLine("Sort type: ");
        println("Sort order: \n1. Ascending\n2. Descending");
        String order = Utils.readLine("Order: ");
        if (order.equals("1")) {
            return comparators.get(choice);
        } else {
            return comparators.get(choice).reversed();
        }
    }

    public static void main(String[] args) throws Exception {
        FlatDarkLaf.setup();
        Console console = System.console();
        if (console == null) {
            String command = String.join(" ", getArgs());
            JOptionPane.showMessageDialog(null, "Console not found. Restart command copied to clipboard. Paste it to the console.", "Error", JOptionPane.ERROR_MESSAGE);
            StringSelection selection = new StringSelection(command);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            Runtime.getRuntime().exec("cmd /c start");
            System.exit(-1);
        }
        do {
            for (Task task : tasks) {
                println(task.key + ". " + task.description);
            }
            String choice = Utils.readLine("Choice: ");
            Task task = Arrays.stream(tasks).filter(t -> t.key.equals(choice)).findFirst().orElse(null);
            if (task != null) {
                println('\n');
                task.execute();
                println('\n');
            } else {
                println("Invalid choice.");
            }
        } while (!brokerRunning);
    }

    @Contract(" -> new")
    private static String @NotNull [] getArgs() {
        try {
            String path = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            return new String[]{"java", "-jar", "\"" + path + "\"", "--no-restart"};
        } catch (Exception ignore) {
            return new String[0];
        }
    }
}
