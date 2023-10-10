package com.dark.muslimspro.Hadith;

public class Hadith {
    private int id;
    private String name;
    private String description;
    private String references;
    private String grade;

    public Hadith() {
        // Default constructor
    }

    public Hadith(int id, String name, String description, String references, String grade) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.references = references;
        this.grade = grade;
    }

    // Getters and setters for the Hadith class
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}


