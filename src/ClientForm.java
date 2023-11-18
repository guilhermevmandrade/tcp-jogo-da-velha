import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;

public class ClientForm extends JFrame implements ActionListener {
    private final JButton[] buttons = new JButton[9];
    private final JFrame frame;
    private JTextArea chatArea;
    private JTextField chatInput;
    private final char playerId;
    public static final char CROSS = 'X';
    public static final char CIRCLE = 'O';
    public boolean myTurn = false;
    public boolean endGame = false;
    DataOutputStream dataOutputStream;

    public ClientForm(char playerId, DataOutputStream dataOutputStream) {

        this.playerId = playerId;
        this.dataOutputStream = dataOutputStream;

        frame = new JFrame("Jogo da Velha - " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var gamePanel = createGamePanel();
        var chatPanel = createChatPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gamePanel, chatPanel);
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerLocation(400);


        frame.add(splitPane);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }

    private JPanel createGamePanel(){
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));
        gamePanel.setPreferredSize(new Dimension(400, 400));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setName(String.valueOf(i));
            buttons[i].setForeground(Color.BLACK);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setFont(new Font("", Font.PLAIN, 60));
            buttons[i].addActionListener(this);
            gamePanel.add(buttons[i]);
        }

        return gamePanel;
    }

    private JPanel createChatPanel(){
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(400, 300));
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        chatInput = new JTextField();
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(this::sendMessagePerformed);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(chatInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        return chatPanel;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (myTurn) {
                JButton button = (JButton) e.getSource();
                var position = button.getName();

                button.setText(String.valueOf(playerId));
                button.setUI(new MetalButtonUI() {
                    protected Color getDisabledTextColor() {
                        return Color.BLUE;
                    }
                });
                button.setEnabled(false);
                myTurn = false;

                dataOutputStream.writeBytes(position + "\n");
            }
            else {
                JOptionPane.showMessageDialog(frame, "Aguarde sua vez!");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void opponentMove(String buttonName, char opponentId) {
        int buttonPosition = Integer.parseInt(buttonName);

        buttons[buttonPosition].setText(String.valueOf(opponentId));
        buttons[buttonPosition].setUI(new MetalButtonUI() {
            protected Color getDisabledTextColor() {
                return Color.RED;
            }
        });
        buttons[buttonPosition].setEnabled(false);

        myTurn = true;
    }

    public void opponentMessage(String message, char opponentId) {
        chatArea.append(opponentId + ": ");
        chatArea.append(message + "\n");
    }

//    private void appendToChat(String message, Color color) {
//        StyledDocument doc = chatArea.getStyledDocument();
//        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
//        StyleConstants.setForeground(attributeSet, color);
//        try {
//            doc.insertString(doc.getLength(), message, attributeSet);
//        } catch (BadLocationException ex) {
//            ex.printStackTrace();
//        }
//    }

    private void sendMessagePerformed(ActionEvent e) {
        try {
            String message = chatInput.getText();

            chatArea.append(playerId + ": ");
            chatArea.append(message + "\n");

            dataOutputStream.writeBytes("CHAT" + message + "\n");

            chatInput.setText("");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}