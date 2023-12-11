import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Define a classe TCPServer, que representa o servidor TCP para o jogo da velha.
 */
public class TCPServer {

    /** Número da porta utilizado para a comunicação via socket no jogo.*/
    private static final int SOCKET_PORT = 6789;

    /**
     * Método principal para iniciar o servidor do jogo da velha.
     * Estabelece a conexão via socket com o cliente, configura os fluxos de entrada e saída de dados, e inicia a interface gráfica.
     */
    public static void main(String[] args) {
        try(ServerSocket welcomeSocket = new ServerSocket(SOCKET_PORT)){
            // Aguarda e aceita a conexão de um cliente.
            Socket connectionSocket = welcomeSocket.accept();

            // Configura os fluxos de entrada e saída de dados.
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Inicia a interface gráfica do jogo para o jogador xis com a vez de jogar.
            var gameFrame = new GameFrame(GameFrame.CROSS, true, outToClient);

            // Aguarda mensagens do cliente e as manipula conforme necessário.
            String message;
            while ((message = inFromClient.readLine()) != null) {
                handleClientMessage(message, gameFrame, connectionSocket);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Manipula as mensagens recebidas do cliente e atualiza a interface gráfica do jogo conforme necessário.
     *  mensagens podem indicar o término da partida, o reinício do jogo, mensagens de chat ou movimentos do oponente.
     * @param message A mensagem recebida do cliente.
     * @param gameFrame A instância da interface gráfica do jogo.
     * @param serverSocket Socket do servidor.
     * @throws IOException Exceção de E/S que pode ocorrer durante a comunicação.
     */
    private static void handleClientMessage(String message, GameFrame gameFrame, Socket serverSocket) throws IOException {
        // Verifica se a mensagem indica o término da partida.
        // Se for, notifica o jogador sobre a saída do oponente e encerra o programa.
        if (message.startsWith("END")) {
            gameFrame.opponentLeftGame();
            closeSocket(serverSocket);
        }
        // Verifica se a mensagem indica o reinício do jogo.
        // Se for, Reinícia o tabuleiro para iniciar um novo jogo.
        else if (message.startsWith("RESTART")) {
            gameFrame.restartGame();
        }
        // Verifica se a mensagem é uma mensagem de chat.
        // Se for, remove o marcador "CHAT" da mensagem e exibe no chat do jogador atual com o círculo.
        else if (message.startsWith("CHAT")) {
            message = message.replaceFirst("CHAT", "");
            gameFrame.opponentMessage(message, GameFrame.CIRCLE);
        }
        // Se a mensagem não se encaixa nos casos anteriores então é um movimento do oponente.
        // Realiza a jogada do oponente no tabuleiro do jogador atual com o círculo.
        else {
            gameFrame.opponentMove(message, GameFrame.CIRCLE);
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
