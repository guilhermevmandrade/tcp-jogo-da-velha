import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        int position;
        int opponentPosition;

        try(ServerSocket welcomeSocket = new ServerSocket(6789)){
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            var clientForm = new ClientForm(ClientForm.CROSS, outToClient);
            clientForm.myTurn = true;

            String message;
            try {
                while ((message = inFromClient.readLine()) != null) {
                    if (message.startsWith("END")) {
                        System.exit(0);
                    }
                    else if (message.startsWith("RESTART")) {
                        //System.exit(0);
                    }
                    else if (message.startsWith("CHAT")) {
                        message = message.replace("CHAT", "");
                        clientForm.opponentMessage(message, ClientForm.CIRCLE);
                    }
                    else {
                        clientForm.opponentMove(message, ClientForm.CIRCLE);
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
