import java.io.Serializable;

public class Frame implements Serializable {
    private FrameHeader frameHeader;
    private Payload payload;

    public Frame(FrameHeader frameHeader, Payload payload) {
        this.frameHeader = frameHeader;
        this.payload = payload;
    }

    public FrameHeader getFrameHeader() {
        return frameHeader;
    }

    public void setFrameHeader(FrameHeader frameHeader) {
        this.frameHeader = frameHeader;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
