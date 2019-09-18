package io.github.cgh.ws.entity;

import java.util.Objects;

public class Job {

    private final String id;

    public Job(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job jobId = (Job) o;
        return id.equals(jobId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
