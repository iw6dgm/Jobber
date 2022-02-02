package it.deepnet.joshua.job;

public class Status {

    private final String project_id;
    private final Integer event_id;

    public Status() {
        project_id = null;
        event_id = null;
    }

    public Status(String project_id, Integer event_id) {
        this.project_id = project_id;
        this.event_id = event_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public boolean isActive() {
        return project_id != null && event_id != null;
    }

}
