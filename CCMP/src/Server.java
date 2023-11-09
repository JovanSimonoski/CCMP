import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class Server {
    private SecurityParameters securityParameters;
    private Frame frame;

    public static void main(String[] args) {
        try {
            Server server = new Server();

            ServerSocket serverSocketSecurityParameters = new ServerSocket(12345);
            System.out.println("Server listening for security parameters on port 12345...");


            Socket clientSocketSecurityParameters = serverSocketSecurityParameters.accept();
            System.out.println("Client connected.");

            ObjectInputStream inSecurityParameters = new ObjectInputStream(clientSocketSecurityParameters.getInputStream());

            // Receive the security parameters
            SecurityParameters securityParametersReceived = (SecurityParameters) inSecurityParameters.readObject();

            server.securityParameters = securityParametersReceived;

            System.out.println("Secret parameters received successfully.");

            inSecurityParameters.close();
            clientSocketSecurityParameters.close();

            // Uncomment for man in the middle attack simulation
            /*
            ServerSocket serverSocketData = new ServerSocket(55555);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Press ENTER to start receiving data:");
            scanner.nextLine();

            System.out.println("Server listening for data on port 55555...");
            */

            // Comment for man in the middle attack simulation
            ServerSocket serverSocketData = new ServerSocket(54321);
            System.out.println("Server listening for data on port 54321...");


            Socket clientSocketData = serverSocketData.accept();
            System.out.println("Client connected.");

            ObjectInputStream inData = new ObjectInputStream(clientSocketData.getInputStream());

            // Receive the frame
            EncryptedFrame receivedFrame = (EncryptedFrame) inData.readObject();

            inData.close();
            clientSocketData.close();

            System.out.println("--------------------------------------------------------------------------------------------");
            StringBuilder encryptedPayloadString = new StringBuilder();
            for (byte b : receivedFrame.getEncryptedPayload()) {
                encryptedPayloadString.append(String.format("%02X ", b));
            }
            System.out.println("ENCRYPTED PAYLOAD:");
            System.out.println(encryptedPayloadString.toString());
            System.out.println("--------------------------------------------------------------------------------------------");

            try {
                // Decrypt the frame
                Frame decryptedFrame = CCMP.decryptFrame(receivedFrame, server.securityParameters);

                System.out.println("--------------------------------------------------------------------------------------------");
                System.out.println("FRAME HEADER:");
                System.out.println(decryptedFrame.getFrameHeader().toString());

                System.out.println("--------------------------------------------------------------------------------------------");
                System.out.println("DECRYPTED PAYLOAD:");
                System.out.println(decryptedFrame.getPayload().getPayload());

                System.out.println("--------------------------------------------------------------------------------------------");
            } catch (MICErrorException exception) {
                System.out.println(exception.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
