/**
 * Define a classe GameStatus, responsável por armazenar o estado atual do jogo.
 */
public class GameStatus {

    /** Array que armazena o estado das posições atuais do jogo.*/
    public char[] gamePosition;

    /**
     * Construtor da classe GameStatus.
     */
    public GameStatus() {
        // Inicializa o array de posições do jogo com um tamanho de 9.
        this.gamePosition = new char[9];
    }


    /**
     * Realiza uma jogada atualizando o array de posições.
     * @param position A posição onde a jogada será realizada.
     * @param playerId Símbolo do jogador (X ou O).
     */
    public void makeMove(int position, char playerId){
        // Atualiza o array das posições do jogo com o símbolo do jogador na posição especificada.
        gamePosition[position] = playerId;
    }

    /**
     * Verifica se o jogador venceu o jogo, analisando as linhas, colunas e diagonais do tabuleiro.
     * @param playerId Símbolo do jogador a ser verificado (X ou O).
     * @return Verdadeiro se o jogador venceu, falso caso contrário.
     */
    public boolean checkWinner(char playerId) {
        // Verifica se venceu pelas linhas do tabuleiro.
        var winner = checkRows(playerId);
        if (!winner) {
            // Se não, verifica se venceu pelas colunas do tabuleiro.
            winner = checkColumns(playerId);
            if (!winner) {
                // Se não, verifica se venceu pelas diagonais do tabuleiro.
                winner = checkDiagonals(playerId);
            }
        }
        // Retorna 'true' se tiver vencido, 'false' se não.
        return winner;
    }

    /**
     * Verifica se o jogador venceu por meio das linhas do tabuleiro.
     * @param playerId Símbolo do jogador a ser verificado (X ou O).
     * @return Verdadeiro se o jogador venceu através das linhas, falso caso contrário.
     */
    private boolean checkRows(char playerId) {
        // Itera sobre as linhas do tabuleiro pelo array de status do jogo, pulando 3 posições cada loop.
        for (int x = 0; x <= 6; x = x + 3) {
            // Verifica se todas as posições na mesma linha contêm o símbolo do jogador.
            if ((gamePosition[x] == playerId) && (gamePosition[x + 1] == playerId) && (gamePosition[x + 2] == playerId)) {
                return true; // Se sim, retorna 'true'.
            }
        }
        return false; // Se não tiver vencido por nenhuma linha, retorna 'false'.
    }

    /**
     * Verifica se o jogador venceu por meio das colunas do tabuleiro.
     * @param playerId Símbolo do jogador a ser verificado (X ou O).
     * @return Verdadeiro se o jogador venceu através das colunas, falso caso contrário.
     */
    private boolean checkColumns(char playerId) {
        // Itera sobre as colunas do tabuleiro pelo array de status do jogo, pulando 1 posição cada loop.
        for (int x = 0; x <= 2; x++) {
            // Verifica se todas as posições na mesma coluna contêm o símbolo do jogador.
            if ((gamePosition[x] == playerId) && (gamePosition[x + 3] == playerId) && (gamePosition[x + 6] == playerId)) {
                return true; // Se sim, retorna 'true'.
            }
        }
        return false; // Se não tiver vencido por nenhuma coluna, retorna 'false'.
    }

    /**
     * Verifica se o jogador venceu por meio das diagonais do tabuleiro.
     * @param playerId Símbolo do jogador a ser verificado (X ou O).
     * @return Verdadeiro se o jogador venceu através das diagonais, falso caso contrário.
     */
    private boolean checkDiagonals(char playerId) {
        // Retorna 'true' se todas as posições na diagonal principal contêm o símbolo do jogador.
        if ((gamePosition[0] == playerId) && (gamePosition[4] == playerId) && (gamePosition[8] == playerId)) {
            return true;
            // Retorna 'true' se todas as posições na diagonal secundária contêm o símbolo do jogador.
        } else if ((gamePosition[2] == playerId) && (gamePosition[4] == playerId) && (gamePosition[6] == playerId)) {
            return true;
            // Retorna 'false' se não  tiver vencido por nenhuma diagonal.
        } else {
            return false;
        }
    }

    /**
     * Verifica se o jogo terminou em empate, se todas as posições do tabuleiro estão preenchidas
     * por um símbolo e não um caracter nulo ('\0'), e não houve nenhum vencedor.
     * @return Verdadeiro se o jogo terminou em empate, falso caso contrário.
     */
    public boolean checkDraw() {
        // Conta o número de posições ocupadas no tabuleiro pelo array de status do jogo;
        int count = 0;
        for (char value : gamePosition) {
            if (value != '\0') {
                count++;
            }
        }
        // Retorna 'true' se todas as posições estiverem ocupadas.
        return count == 9;
    }
}
