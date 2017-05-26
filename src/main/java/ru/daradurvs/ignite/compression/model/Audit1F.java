package ru.daradurvs.ignite.compression.model;

import java.util.UUID;
import org.apache.ignite.internal.binary.compression.BinaryCompression;

/**
 * Duplicate of {@link Audit}, differs string fields which is annotated by {@link BinaryCompression}.
 */
public class Audit1F implements Identifiable {
    private long personId;
    private UUID uuid;

    @BinaryCompression
    private String message;

    public Audit1F(long personId, UUID uuid, String message) {
        this.personId = personId;
        this.uuid = uuid;
        this.message = message;
    }

    @Override public long getId() {
        return personId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Audit1F audit = (Audit1F)o;

        if (personId != audit.personId)
            return false;
        if (uuid != null ? !uuid.equals(audit.uuid) : audit.uuid != null)
            return false;
        return message != null ? message.equals(audit.message) : audit.message == null;
    }

    @Override public int hashCode() {
        int result = (int)(personId ^ (personId >>> 32));
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Audit1F{" +
            "personId=" + personId +
            ", uuid=" + uuid +
            ", message='" + message + '\'' +
            '}';
    }
}

