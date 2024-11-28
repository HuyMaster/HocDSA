package com.github.huymaster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

enum Rank {
    FAIL("Fail", 0),
    MEDIUM("Medium", 1),
    GOOD("Good", 2),
    VERY_GOOD("Very Good", 3),
    EXCELLENT("Excellent", 4);

    private final String label;
    private final int level;

    Rank(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static Rank getRank(double score) {
        if (score < 0.0 || score > 10.0) {
            throw new IllegalArgumentException("Score must be between 0 and 10 but was " + score);
        }
        if (score < 5.0) {
            return FAIL;
        }
        if (score < 6.5) {
            return MEDIUM;
        }
        if (score < 7.5) {
            return GOOD;
        }
        if (score < 8.5) {
            return VERY_GOOD;
        }
        return EXCELLENT;
    }

    @Override
    public String toString() {
        return label;
    }
}

public class Student implements Comparable<Student> {
    final String id;
    final String name;
    final double score;
    final Rank rank;

    public Student(String id, String name, double score) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID must not be empty or blank");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty or blank");
        }
        if (!(score >= 0.0 && score <= 10.0)) {
            throw new IllegalArgumentException("Score must be between 0 and 10 but was " + score);
        }
        this.id = id;
        this.name = name;
        this.score = new BigDecimal(score).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        this.rank = Rank.getRank(score);
    }

    static Student newStudent() {
        Pattern idPattern = Pattern.compile("[Bb][Hh][0-9]{5}");
        Pattern namePattern = Pattern.compile("[A-Za-z ]+");

        String id = Utils.readLine("Student ID: ", String::toUpperCase, (s) -> idPattern.matcher(s).matches());
        String name = Utils.readLine("Name: ", (s) -> s, (s) -> namePattern.matcher(s).matches());
        double score = Utils.readLine("Score: ", Double::parseDouble, (s) -> s >= 0.0 && s <= 10.0);
        return new Student(id, name, score);
    }

    static Student newRandomStudent() {
        String id = "BH" + String.format("%05d", (int) (Math.random() * 100000));
        String name = "Test";
        double score = Math.random() * 10.0;
        return new Student(id, name, score);
    }

    public Student copy(String name, double score) {
        return new Student(id, name, score);
    }

    @Override
    public int compareTo(Student o) {
        return id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return String.format("Student[id=%s, name=%s, score=%s, rank=%s]", id, name, score, rank);
    }
}