import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Define a classe TCPClient, que representa o cliente TCP para o jogo da velha.
 */
public class TCPClient {

    /** Endereço IP utilizado para a comunicação via socket no jogo.*/
    private static final String SOCKET_IP = "127.0.0.1";

    /** Número da porta utilizado para a comunicação via socket no jogo.*/
    private static final int SOCKET_PORT = 6789;

    /**
     * Método principal para iniciar o cliente do jogo da velha.
     * Estabelece a conexão via socket com o servidor, configura os fluxos de entrada e saída de dados, e inicia a interface gráfica.
     */
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(SOCKET_IP, SOCKET_PORT)) {
            // Configura os fluxos de entrada e saída de dados.
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Inicia a interface gráfica do jogo para o jogador círculo sem a vez de jogar.
            var gameFrame = new GameFrame(GameFrame.CIRCLE, false, outToServer);

            // Aguarda mensagens do servidor e as manipula conforme necessário.
            String message;
            while ((message = inFromServer.readLine()) != null) {
                handleServerMessage(message, gameFrame, clientSocket);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Manipula as mensagens recebidas do servidor e atualiza a interface gráfica do jogo conforme necessário.
     * As mensagens podem indicar o término da partida, o reinício do jogo, mensagens de chat ou movimentos do oponente.
     * @param message A mensagem recebida do servidor.
     * @param gameFrame A instância da interface gráfica do jogo.
     * @param clientSocket Socket do cliente.
     * @throws IOException Exceção de E/S que pode ocorrer durante a comunicação.
     */
    private static void handleServerMessage(String message, GameFrame gameFrame, Socket clientSocket) throws IOException {
        // Verifica se a mensagem indica o término da partida.
        // Se for, notifica o jogador sobre a saída do oponente e encerra o programa.
        if (message.startsWith("END")) {
            gameFrame.opponentLeftGame();
            closeSocket(clientSocket);
        }
        // Verifica se a mensagem indica o que o oponente ganhou.
        // Se for, notifica o jogador sobre a vitória e prepara para iniciar um novo jogo.
        else if (message.startsWith("WON")) {
            gameFrame.showWinnerFrame(GameFrame.CIRCLE, false);
        }
        // Verifica se a mensagem indica o que o jogo deu velha.
        // Se for, notifica o jogador sobre o empate e prepara para iniciar um novo jogo.
        else if (message.startsWith("DRAW")) {
            gameFrame.showDrawFrame();
        }
        // Verifica se a mensagem é uma mensagem de chat.
        // Se for, remove o marcador "CHAT" da mensagem e exibe no chat do jogador atual com o xis.
        else if (message.startsWith("CHAT")) {
            message = message.replaceFirst("CHAT", "");
            gameFrame.opponentMessage(message, GameFrame.CROSS);
        }
        // Se a mensagem não se encaixa nos casos anteriores então é um movimento do oponente.
        // Realiza a jogada do oponente no tabuleiro do jogador atual com o xis.
        else {
            gameFrame.opponentMove(message, GameFrame.CROSS);
        }
    }

    /**
     * Fecha um socket.
     * @param socket O socket a ser fechado.
     */
    private static void closeSocket(Socket socket) {
        // Verifica se o socket não é nulo e se ainda está aberto
        if (socket != null && !socket.isClosed()) {
            try {
                // Fecha o socket.
                socket.close();
            } catch (IOException ex) {
                // Exibe mensagem de erro no console se houver uma exceção ao fechar o socket.
                System.out.println(ex.getMessage());
            }
        }
    }
}
