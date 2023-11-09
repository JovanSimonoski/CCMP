import java.io.Serializable;
import java.util.Objects;

public class FrameHeader implements Serializable {
    private String sourceMac;
    private String destinationMac;

    public FrameHeader(String sourceMac, String destinationMac) {
        this.sourceMac = sourceMac;
        this.destinationMac = destinationMac;
    }

    public byte[] getFrameHeaderAsBytes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sourceMac);
        stringBuilder.append(destinationMac);
        return stringBuilder.toString().getBytes();
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public void setSourceMac(String sourceMac) {
        this.sourceMac = sourceMac;
    }

    public String getDestinationMac() {
        return destinationMac;
    }

    public void setDestinationMac(String destinationMac) {
        this.destinationMac = destinationMac;
    }

    @Override
    public String toString() {
        return "SOURCE MAC ADDRESS: " + sourceMac + '\n' +
                "DESTINATION MAC ADDRESS: " + destinationMac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrameHeader that = (FrameHeader) o;
        return Objects.equals(sourceMac, that.sourceMac) && Objects.equals(destinationMac, that.destinationMac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceMac, destinationMac);
    }
}
