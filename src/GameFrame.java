import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Define a classe GameFrame, responsável pela interface gráfica do jogo
 */
public class GameFrame extends JFrame implements ActionListener {

    /** Símbolo que representa marca do jogador "X" no jogo da velha.*/
    public static final char CROSS = 'X';

    /** Símbolo que representa marca do jogador "O" no jogo da velha.*/
    public static final char CIRCLE = 'O';

    /** Fluxo de saída de dados para comunicação com o oponente.*/
    private final DataOutputStream dataOutputStream;

    /** Array de botões representando as células do tabuleiro do jogo da velha.*/
    private final JButton[] buttons = new JButton[9];

    /** Quadro principal que contém a interface gráfica do jogo.*/
    private final JFrame frame;

    /** Área de exibição do chat do jogo.*/
    private JTextArea chatArea;

    /** Campo de entrada de texto para o chat do jogo.*/
    private JTextField chatInput;

    /** Classe que armazena o estado atual do jogo.*/
    private final GameStatus gameStatus;

    /** Símbolo do jogador atual (X ou O).*/
    private final char playerId;

    /** Indica se é a vez do jogador atual realizar uma jogada.*/
    private boolean myTurn;

    /**
     * Cria a interfaxe gráfica do jogo da velha com um chat ao lado.
     * @param playerId Símbolo do jogador atual(X ou O).
     * @param myTurn Indica se é a vez do jogador atual.
     * @param dataOutputStream Fluxo de saída de dados para comunicação.
     */
    public GameFrame(char playerId, boolean myTurn, DataOutputStream dataOutputStream) {
        // Inicializa os atributos.
        this.playerId = playerId;
        this.myTurn = myTurn;
        this.dataOutputStream = dataOutputStream;
        this.gameStatus = new GameStatus();

        // Cria o painel do jogo e o painel de chat.
        var gamePanel = createGamePanel();
        var chatPanel = createChatPanel();

        // Cria um painel dividido com os painéis do jogo e do chat um do lado do outro.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gamePanel, chatPanel);
        splitPane.setResizeWeight(0.0); // Define o peso da divisão para o painel do jogo
        splitPane.setDividerLocation(400); // Define a posição inicial do divisor

        // Cria o quadro principal passando o painel dividido como conteúdo.
        frame = createMainFrame(splitPane);

        // Exibe uma mensagem de boas-vindas no início do jogo.
        showWelcomeMessage();
    }

    /**
     * Cria e configura o quadro principal (main frame) para o jogo da velha.
     * @param splitPane Um painel dividido contendo a interface gráfica do jogo.
     * @return O quadro principal criado e configurado.
     */
    private JFrame createMainFrame(JSplitPane splitPane){
        // Cria um novo quadro principal com o título indicando o símbolo do jogador
        JFrame mainFrame = new JFrame("JOGO DA VELHA - " + playerId);

        // Define a ação de encerrar o programa ao fechar o quadro
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adiciona um ouvinte de evento que envia uma mensagem ao oponente avisando que o jogador atual fechou e saiu do jogo.
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dataOutputStream.writeBytes("END\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Adiciona o painel dividido ao quadro principal, define suas dimensões e o torna visível.
        mainFrame.add(splitPane);
        mainFrame.setSize(700, 400);
        mainFrame.setVisible(true);

        // Retorna o quadro principal criado e configurado
        return mainFrame;
    }

    /**
     * Cria e configura o painel do jogo da velha composto por uma grade 3x3 de botões.
     * @return Um JPanel configurado com os elementos visuais do jogo da velha.
     */
    private JPanel createGamePanel(){
        // Cria um novo painel para o jogo.
        JPanel gamePanel = new JPanel();

        // Configura o layout do painel como uma grade 3x3.
        gamePanel.setLayout(new GridLayout(3, 3));

        // Define a dimensão do painel.
        gamePanel.setPreferredSize(new Dimension(400, 400));

        // Adiciona margens ao redor do painel.
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Itera criando os nove botões do tabuleiro
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton(); // Cria um novo botão
            buttons[i].setName(String.valueOf(i)); // Define o nome do botão como a posição no tabuleiro
            buttons[i].setForeground(Color.BLACK); // Define a cor do texto do botão como preta
            buttons[i].setBackground(Color.WHITE); // Define a cor de fundo do botão como branca
            buttons[i].setFont(new Font("", Font.PLAIN, 60)); // Define a fonte do botão
            buttons[i].addActionListener(this); // Adiciona um ouvinte de ação ao botão
            gamePanel.add(buttons[i]); // Adiciona o botão ao painel do jogo
        }

        // Retorna o painel do jogo configurado.
        return gamePanel;
    }


    /**
     * Cria e configura o painel do chat, que inclui uma área de visualização do chat,
     * um campo de entrada de texto e um botão de envio.
     * @return Um JPanel configurado com os elementos visuais do chat.
     */
    private JPanel createChatPanel(){
        // Cria um novo painel para o chat com layout de borda.
        JPanel chatPanel = new JPanel(new BorderLayout());

        // Define a dimensão do painel de chat.
        chatPanel.setPreferredSize(new Dimension(400, 300));

        // Adiciona margens ao redor do painel.
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cria uma area de texto, não editável e com quebra de texto, para exibir as mensagens do chat.
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        // Cria um painel com barra de rolagem a partir da area de texto e adiciona no centro do painel de chat.
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        // Cria um campo de entrada de texto para digitar mensagens.
        chatInput = new JTextField();

        // Cria um botão de envio e adiciona um ouvinte de ação para executar ao clicar no botão.
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(this::sendMessagePerformed);

        // Cria um painel para organizar o campo de entrada e o botão de envio um do lado do outro.
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(chatInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Adiciona esse painel conjunto abaixo do painel da área de texto no painel de chat.
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Retorna o painel de chat configurado.
        return chatPanel;
    }

    /**
     * Exibe uma mensagem de boas-vindas no início do jogo,
     * informando o símbolo do jogador e se é a vez do jogador atual fazer uma jogada.
     */
    private void showWelcomeMessage() {
        // Constrói a mensagem de boas-vindas.
        String welcomeMessage = "Bem vindo ao Jogo da Velha\n" +
                                "Seu símbolo é: " + playerId + "\n\n" +
                                (myTurn ? "Você começa jogando" : "Seu oponente começa jogando");

        // Exibe a mensagem em uma janela de diálogo.
        JOptionPane.showMessageDialog(frame,
                welcomeMessage,
                "JOGO DA VELHA INICIADO!",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Atualiza o tabuleiro com a jogada do jogador atual quando um botão do tabuleiro é clicado,
     * verifica se houve um vencedor ou empate, e notifica o outro jogador sobre a jogada realizada.
     * @param e O evento de ação que desencadeou o método.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            // Verifica se é a vez do jogador atual.
            if (myTurn) {
                // Define que não é mais a vez do jogador atual, para que não possa jogar novamente antes do oponente.
                myTurn = false;

                // Obtém a posição do botão no tabuleiro a partir de seu nome.
                JButton button = (JButton) e.getSource();
                var position = button.getName();

                // Define o texto do botão clicado como o símbolo do jogador (X ou O) na cor azul.
                button.setText(String.valueOf(playerId));
                button.setUI(new MetalButtonUI() {
                    protected Color getDisabledTextColor() {
                        return Color.BLUE;
                    }
                });
                button.setEnabled(false); // Desabilita o botão para evitar mais cliques

                // Envia a posição da jogada ao oponente.
                dataOutputStream.writeBytes(position + "\n");

                // Atualiza o estado do jogo com o símbolo do jogador na posição da jogada.
                gameStatus.makeMove(Integer.parseInt(position), playerId);

                // Verifica se o jogador venceu ou o jogo empatou.
                if (gameStatus.checkWinner(playerId)) {
                    showWinnerFrame(playerId, true); // Se venceu, exibe o diálogo de vitória.
                    dataOutputStream.writeBytes("WON" + "\n"); // Envia mensagem ao oponente notificando a vitória.
                } else if (gameStatus.checkDraw()) {
                    showDrawFrame(); // Se empatou, exibe o diálogo de empate.
                    dataOutputStream.writeBytes("DRAW" + "\n"); // Envia mensagem ao oponente notificando o empate.
                }
            }
            else {
                // Se não for a vez do jogador, impede a ação do jogador e exibe uma mensagem de aviso.
                JOptionPane.showMessageDialog(frame,
                        "Aguarde o movimento do outro jogador",
                        "Espere!",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Envia a mensagem digitada no campo de chat para o oponente e exibe a mesma mensagem
     * no painel de chat local. Limpa o campo de entrada de texto após o envio.
     * @param e O evento de ação que desencadeou o método.
     */
    public void sendMessagePerformed(ActionEvent e) {
        try {
            // Obtém a mensagem digitada no campo de input.
            String message = chatInput.getText();

            // Verifica se a mensagem não está vazia.
            if(!message.isBlank()) {
                // Adiciona a mensagem formatada com o símbolo do jogador no painel de chat local
                chatArea.append("~" + playerId + ": ");
                chatArea.append(message + "\n");

                // Envia a mensagem ao oponente com 'CHAT' concatenado no início para identificar o tipo de mensagem.
                dataOutputStream.writeBytes("CHAT" + message + "\n");
            }

            // Limpa o campo de input.
            chatInput.setText("");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Atualiza o tabuleiro com a jogada do oponente e verifica se houve um vencedor ou empate.
     * @param buttonName O nome do botão que representa a posição da jogada do oponente.
     * @param opponentId O símbolo do oponente (X ou O).
     * @throws IOException Exceção de E/S que pode ocorrer durante a comunicação.
     */
    public void opponentMove(String buttonName, char opponentId) throws IOException {
        // Obtém a posição do botão no tabuleiro a partir de seu nome.
        int buttonPosition = Integer.parseInt(buttonName);

        // Atualiza o texto do botão com o símbolo do oponente (X ou O) na cor vermelha.
        buttons[buttonPosition].setText(String.valueOf(opponentId));
        buttons[buttonPosition].setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.RED;
            }
        });
        buttons[buttonPosition].setEnabled(false); // Desabilita o botão para evitar mais cliques

        // Atualiza estado do jogo com o símbolo do oponente na posição da jogada.
        gameStatus.makeMove(buttonPosition, opponentId);

        // Define que é agora é a vez do jogador atual.
        myTurn = true;
    }

    /**
     * Exibe uma mensagem enviada pelo oponente no painel de chat.
     * @param message A mensagem recebida do oponente.
     * @param opponentId O símbolo do oponente (X ou O).
     */
    public void opponentMessage(String message, char opponentId) {
        // Adiciona a mensagem formatada do oponente no painel do chat.
        chatArea.append("~" + opponentId + ": ");
        chatArea.append(message + "\n");
    }

    /**
     * Reinicia o jogo, limpando o conteúdo dos botões no tabuleiro e reabilitando-os,
     * e também reinicia o estado do jogo, zerando o array de status.
     */
    public void restartGame(){
        // Limpa e habilita todos botões do tabuleiro para o novo jogo.
        for (JButton button: buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        // Reseta o estado do jogo para valores iniciais ('\0' representa vazio).
        for (int i = 0; i < 9; i++) {
            gameStatus.gamePosition[i] = '\0';
        }
    }

    /**
     * Notifica o jogador sobre a saída do oponente da partida, exibindo uma mensagem de encerramento,
     * e encerra o programa após a notificação.
     */
    public void opponentLeftGame(){
        // Exibe uma caixa de diálogo informando que o oponente saiu do jogo.
        JOptionPane.showMessageDialog(frame,
                "Seu oponente deixou a partida",
                "FIM DE JOGO",
                JOptionPane.WARNING_MESSAGE
        );

        // Encerra o programa.
        frame.dispose();
    }

    /**
     * Exibe uma janela de diálogo informando sobre o vencedor da partida e oferece a opção
     * de reiniciar o jogo ou encerrar a partida.
     * @param playerId Símbolo do jogador que venceu (X ou O).
     * @param thisPlayerWon Indica se o jogador atual venceu a partida.
     * @throws IOException Exceção de E/S que pode ocorrer durante a comunicação.
     */
    public void showWinnerFrame(char playerId, boolean thisPlayerWon) throws IOException {
        // Exibe uma caixa de diálogo com opções para reiniciar ou encerrar o jogo.
        int option = JOptionPane.showOptionDialog(frame,
                "O jogador " + playerId + " venceu a partida. Deseja jogar outra vez?",
                thisPlayerWon ? "VITÓRIA!" : "DERROTA!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Sim", "Não"},
                "Sim"
        );

        // Verifica a opção escolhida pelo jogador.
        if (option == JOptionPane.YES_OPTION) {
            // Reinicia o estado do jogo e o tabuleiro.
            restartGame();
        } else {
            // Encerra o programa e envia mensagem ao oponente informando que o jogador saiu.
            dataOutputStream.writeBytes("END" + "\n");
            frame.dispose();
        }
    }

    /**
     * Exibe uma janela de diálogo informando que o jogo resultou em empate e oferece a opção
     * de reiniciar o jogo ou encerrar a partida.
     * @throws IOException Exceção de E/S que pode ocorrer durante a comunicação.
     */
    public void showDrawFrame() throws IOException {
        // Exibe uma caixa de diálogo com opções para reiniciar ou encerrar o jogo.
        int option = JOptionPane.showOptionDialog(frame,
                "O jogo empatou. Deseja jogar outra vez?",
                "VELHA!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Sim", "Não"},
                "Sim"
        );

        // Verifica a opção escolhida pelo jogador.
        if (option == JOptionPane.YES_OPTION) {
            // Reinicia o estado do jogo e o tabuleiro.
            restartGame();
        } else {
            // Encerra o programa e envia mensagem ao oponente informando que o jogador saiu.
            dataOutputStream.writeBytes("END" + "\n");
            frame.dispose();
        }
    }
}