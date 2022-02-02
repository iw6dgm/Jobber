package it.deepnet.joshua.job;

public class Project {
    private final String key;
    private final String description;

    public String getDescription() {
        return description;
    }

    public Project(final String k, final String d) {
        key = k;
        description = d;
    }

    public String toString() {
        return description;
    }

    public String getKey() {
        return key;
    }
}