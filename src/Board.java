public class Board {
    private final char[] values;
    public void setValues(char value, int position) {
        this.values[position] = value;
    }

    public Board() {
        this.values = new char[9];
        for (int i = 0; i < 9; i++) {
            this.values[i] = Character.forDigit(i, 10);
        }
    }

    public void printBoard() {
        int position = 0;

        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.print("\t");
            for (int j = 0; j < 3; j++) {
                if (j > 0) {
                    System.out.print("|");
                }
                System.out.print(" " + this.values[position] + " ");
                position++;
            }
            System.out.println();
            if (i < 2) {
                System.out.println("\t———|———|———");
            } else {
                System.out.println();
            }
        }
    }

    public void printStartGame(char player){
        System.out.println("JOGO INICIADO!");
        System.out.println("Voce eh o " + player);
    }

    public void printYourTurn(){
        System.out.println("Sua vez, escolha a posição que ira jogar:");
        printBoard();
    }

    public void printWait(){
        printBoard();
        System.out.println("Aguarde o outro jogador...");
    }
}
