import java.math.BigInteger;

public class CCMP {
    private static byte[][] get128bitBlocks(byte[] data) {
        byte[][] blocks;

        if (data.length % 16 == 0) {
            blocks = new byte[data.length / 16][16];
        } else {
            blocks = new byte[data.length / 16 + 1][16];
        }

        int k = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < 16; j++) {
                if (k == data.length) {
                    return blocks;
                }
                blocks[i][j] = data[k];
                k++;
            }
        }
        return blocks;
    }

    private static byte[] mergeBlocks(byte[][] blocks) {
        byte[] data = new byte[blocks.length * blocks[0].length];
        int k = 0;
        for (byte[] block : blocks) {
            for (byte b : block) {
                data[k] = b;
                k++;
            }
        }
        return data;
    }

    private static byte[] removePadding(byte[] data) {
        int counter = 0;

        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] == 0) {
                counter++;
            } else {
                break;
            }
        }

        byte[] dataToReturn = new byte[data.length - counter];
        System.arraycopy(data, 0, dataToReturn, 0, dataToReturn.length);
        return dataToReturn;
    }

    private static byte[] counterModeEncryptDecrypt(byte[] data, String key, BigInteger counter) {
        byte[][] blocks = get128bitBlocks(data);
        for (int i = 0; i < blocks.length; i++) {
            byte[][] counterEncrypted = get128bitBlocks(counter.toByteArray());

            counterEncrypted[0] = AES.encrypt(counterEncrypted[0], key);

            blocks[i] = xor(blocks[i],counterEncrypted[0]);

            counter.add(BigInteger.valueOf(1));
        }
        return mergeBlocks(blocks);
    }

    private static byte[] xor(byte[] data1, byte[] data2) {
        for (int i = 0; i < 16; i++) {
            data1[i] = (byte) (data1[i] ^ data2[i]);
        }
        return data1;
    }

    private static byte[] mic(Frame frame, String key, String IV) {
        byte[][] blocksFrameHeader = get128bitBlocks(frame.getFrameHeader().getFrameHeaderAsBytes());
        byte[][] blocksPayload = get128bitBlocks(frame.getPayload().getPayloadAsBytes());
        byte[][] blocksIV = get128bitBlocks(IV.getBytes());

        blocksIV[0] = AES.encrypt(blocksIV[0], key);
        blocksFrameHeader[0] = xor(blocksFrameHeader[0], blocksIV[0]);
        blocksFrameHeader[0] = AES.encrypt(blocksFrameHeader[0], key);

        for (int i = 1; i < blocksFrameHeader.length; i++) {
            blocksFrameHeader[i] = xor(blocksFrameHeader[i], blocksFrameHeader[i - 1]);
            blocksFrameHeader[i] = AES.encrypt(blocksFrameHeader[i], key);
        }

        blocksPayload[0] = xor(blocksFrameHeader[blocksFrameHeader.length - 1], blocksPayload[0]);
        blocksPayload[0] = AES.encrypt(blocksPayload[0], key);

        for (int i = 1; i < blocksPayload.length; i++) {
            blocksPayload[i] = xor(blocksPayload[i], blocksPayload[i - 1]);
            blocksPayload[i] = AES.encrypt(blocksPayload[i], key);
        }

        return blocksPayload[blocksPayload.length - 1];
    }

    public static EncryptedFrame encryptFrame(Frame frame, SecurityParameters securityParameters) {
        byte[] encryptedPayload = counterModeEncryptDecrypt(frame.getPayload().getPayloadAsBytes(), securityParameters.getKey(), securityParameters.getCounter());
        byte[] mic = mic(frame, securityParameters.getKey(), securityParameters.getIV());
        EncryptedFrame encryptedFrame = new EncryptedFrame(frame.getFrameHeader(), encryptedPayload, mic);
        return encryptedFrame;
    }

    public static Frame decryptFrame(EncryptedFrame encryptedFrame, SecurityParameters securityParameters) throws MICErrorException {
        byte[] decryptedPayload = counterModeEncryptDecrypt(encryptedFrame.getEncryptedPayload(), securityParameters.getKey(), securityParameters.getCounter());
        decryptedPayload = removePadding(decryptedPayload);

        FrameHeader frameHeader = encryptedFrame.getFrameHeader();
        Payload payload = new Payload(new String(decryptedPayload));

        Frame decryptedFrame = new Frame(frameHeader, payload);

        byte[] mic = mic(decryptedFrame, securityParameters.getKey(), securityParameters.getIV());

        StringBuilder micCalculated = new StringBuilder();
        for (byte b : mic) {
            micCalculated.append(String.format("%02X ", b));
        }
        StringBuilder micReceived = new StringBuilder();
        for (byte b : encryptedFrame.getMic()) {
            micReceived.append(String.format("%02X ", b));
        }

        if (!micReceived.toString().equals(micCalculated.toString())) {
            throw new MICErrorException(micCalculated.toString(), micReceived.toString());
        }

        System.out.println("MIC Calculated: " + micCalculated.toString());

        return decryptedFrame;
    }
}