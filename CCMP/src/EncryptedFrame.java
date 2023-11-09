import java.io.Serializable;

public class EncryptedFrame implements Serializable {
    private FrameHeader frameHeader;
    private byte[] encryptedPayload;
    private byte[] mic;

    public EncryptedFrame(FrameHeader frameHeader, byte[] encrypedPayload, byte[] mic) {
        this.frameHeader = frameHeader;
        this.encryptedPayload = encrypedPayload;
        this.mic = mic;
    }

    public FrameHeader getFrameHeader() {
        return frameHeader;
    }

    public void setFrameHeader(FrameHeader frameHeader) {
        this.frameHeader = frameHeader;
    }

    public byte[] getEncryptedPayload() {
        return encryptedPayload;
    }

    public void setEncryptedPayload(byte[] encryptedPayload) {
        this.encryptedPayload = encryptedPayload;
    }

    public byte[] getMic() {
        return mic;
    }

    public void setMic(byte[] mic) {
        this.mic = mic;
    }
}
