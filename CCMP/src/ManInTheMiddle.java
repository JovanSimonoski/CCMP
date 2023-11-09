import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ManInTheMiddle {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(54321);
            System.out.println("Man in the middle trying to catch the traffic...");


            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Receive the frame
            EncryptedFrame receivedFrame = (EncryptedFrame) in.readObject();

            System.out.println("Successfully caught traffic");

            // Random manipulation of data for the MIC to fail
            receivedFrame.getEncryptedPayload()[0] = (byte) (Math.random() * 10);
            receivedFrame.getEncryptedPayload()[1] = (byte) (Math.random() * 10);
            receivedFrame.getEncryptedPayload()[2] = (byte) (Math.random() * 10);

            in.close();
            clientSocket.close();

            // Resend the frame
            Socket socketData = new Socket("127.0.0.1", 55555);
            ObjectOutputStream outData = new ObjectOutputStream(socketData.getOutputStream());

            System.out.println("Press ENTER to resend the data:");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            outData.writeObject(receivedFrame);
            System.out.println("Successful resend of traffic");

            socketData.close();
            outData.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

