package it.deepnet.joshua.job;

public class Project {
    private int key;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Project(int k, String d) {
        key = k;
        description = d;
    }

    public String toString() {
        return description;
    }

    public int getKey() {
        return key;
    }
}