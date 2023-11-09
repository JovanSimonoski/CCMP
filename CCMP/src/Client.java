import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter the secret key to encrypt the message:");
            String key = scanner.nextLine();

            BigInteger counter = new BigInteger(String.valueOf(0));
            String IV = String.valueOf((Math.random() * 100));

            // Wrapping the secret parameters
            SecurityParameters securityParameters = new SecurityParameters(key,counter,IV);

            // Sending the security parameters (Only for simulation purposes, should be implemented with another protocol)
            Socket socketSecurityParameters = new Socket("127.0.0.1", 12345);
            ObjectOutputStream outSecurityParameters = new ObjectOutputStream(socketSecurityParameters.getOutputStream());

            outSecurityParameters.writeObject(securityParameters);

            // Closing
            socketSecurityParameters.close();
            outSecurityParameters.close();

            System.out.println("Enter the message you want to send:");
            String message = scanner.nextLine();

            // Creating a frame
            FrameHeader frameHeader = new FrameHeader("1234","5678");
            Payload payload = new Payload(message);
            Frame frame = new Frame(frameHeader,payload);

            // Encrypting the frame
            EncryptedFrame encryptedFrame = CCMP.encryptFrame(frame,securityParameters);

            // Using different port for the data transport
            Socket socketData = new Socket("127.0.0.1", 54321);
            ObjectOutputStream outData = new ObjectOutputStream(socketData.getOutputStream());

            outData.writeObject(encryptedFrame);

            // Closing
            socketData.close();
            outData.close();

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("FRAME HEADER:");
            System.out.println(encryptedFrame.getFrameHeader().toString());

            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("PAYLOAD:");
            System.out.println(payload.getPayload());


            System.out.println("--------------------------------------------------------------------------------------------");
            StringBuilder encryptedPayloadString = new StringBuilder();
            for (byte b : encryptedFrame.getEncryptedPayload()) {
                encryptedPayloadString.append(String.format("%02X ", b));
            }
            System.out.println("ENCRYPTED PAYLOAD:");
            System.out.println(encryptedPayloadString.toString());

            System.out.println("--------------------------------------------------------------------------------------------");
            StringBuilder micStringBuilder = new StringBuilder();
            for (byte b : encryptedFrame.getMic()) {
                micStringBuilder.append(String.format("%02X ", b));
            }
            System.out.println("MIC:");
            System.out.println(micStringBuilder.toString());

            System.out.println("-------------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
