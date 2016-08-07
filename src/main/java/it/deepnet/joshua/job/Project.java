package it.deepnet.joshua.job;

public class Project {
    private final int key;
    private final String description;

    public String getDescription() {
        return description;
    }

    public Project(final int k, final String d) {
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