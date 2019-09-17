package io.github.cgh.ws;

import java.util.Objects;

class JobId {

    private final String id;

    JobId(String id) {
        this.id = Objects.requireNonNull(id);
    }

    String value() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobId jobId = (JobId) o;
        return id.equals(jobId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
