# Jogo da Velha - TCP

Este projeto implementa um jogo da velha (também conhecido como "tic-tac-toe") utilizando comunicação via TCP. O jogo permite que dois jogadores se conectem via rede, joguem juntos e interajam por meio de uma interface gráfica simples, com comunicação entre servidor e cliente.

## Descrição

O jogo é jogado entre dois jogadores que se conectam ao servidor. O servidor gerencia o jogo, recebe as jogadas de cada jogador e verifica as condições de vitória, empate ou término da partida. A comunicação entre o cliente e o servidor é realizada através de sockets TCP.

### Funcionalidades:
- Conexão entre servidor e cliente via TCP.
- Interface gráfica simples para o jogo da velha.
- Verificação de condições de vitória, empate ou término do jogo.
- Sistema de chat entre os jogadores.
- Reinício do jogo após vitória, empate ou término.

## Componentes

O projeto é composto por três classes principais:

1. **GameStatus**: Responsável por armazenar o estado atual do jogo e verificar as condições de vitória e empate.
2. **TCPClient**: Implementa o cliente TCP, que conecta ao servidor, envia e recebe mensagens, e gerencia a interface gráfica do jogo.
3. **TCPServer**: Implementa o servidor TCP, que aguarda a conexão do cliente, recebe e envia mensagens, e gerencia o andamento do jogo.

## Pré-requisitos

- Java 8 ou superior.
- IDE como IntelliJ IDEA, Eclipse ou outra de sua preferência.

## Como executar o projeto

### 1. Compilar e executar o servidor

- Navegue até o diretório do servidor e execute o servidor TCP:

```bash
javac TCPServer.java
java TCPServer
```

O servidor irá aguardar a conexão do cliente na porta `6789`.

### 2. Compilar e executar o cliente

- Navegue até o diretório do cliente e execute o cliente TCP:

```bash
javac TCPClient.java
java TCPClient
```

O cliente irá tentar se conectar ao servidor no endereço `127.0.0.1` (localhost) na porta `6789`.

## Como Jogar

1. Inicie o servidor executando `TCPServer`.
2. Inicie o cliente executando `TCPClient` (repita este passo em outra máquina ou instância para permitir que dois jogadores joguem).
3. O jogador "X" começará a partida e o jogador "O" aguardará a sua vez.
4. As jogadas são feitas clicando nas células do tabuleiro. As jogadas são sincronizadas entre os dois jogadores através da comunicação TCP.
5. O jogo terminará quando houver um vencedor, empate ou quando um jogador sair.
