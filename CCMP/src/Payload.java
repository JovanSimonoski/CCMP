import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Payload implements Serializable {
    private String payload;

    public Payload(String payload) {
        this.payload = payload;
    }

    public byte[] getPayloadAsBytes() {
        return payload.getBytes();
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "payload='" + payload + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload1 = (Payload) o;
        return Objects.equals(payload, payload1.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }
}
