import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {

        try (Socket clientSocket = new Socket("127.0.0.1", 6789)) {
            clientSocket.setKeepAlive(true);

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            var clientForm = new ClientForm(ClientForm.CIRCLE, outToServer);

            String message;
            try {
                while ((message = inFromServer.readLine()) != null) {
                    if (message.startsWith("END")) {
                        System.exit(0);
                    }
                    else if (message.startsWith("RESTART")) {
                        //System.exit(0);
                    }
                    else if (message.startsWith("CHAT")) {
                        message = message.replace("CHAT", "");
                        clientForm.opponentMessage(message, ClientForm.CROSS);
                    } else {
                        clientForm.opponentMove(message, ClientForm.CROSS);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
