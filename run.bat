@echo off

rem Compile as classes se necessário
javac -d out\production\EP1 -sourcepath src src\TCPServer.java
javac -d out\production\EP1 -sourcepath src src\TCPClient.java

rem Inicie o servidor em segundo plano
start java -cp out\production\EP1 TCPServer

rem Aguarde um momento para garantir que o servidor tenha iniciado
ping -n 3 127.0.0.1 >nul

rem Inicie o cliente
java -cp out\production\EP1 TCPClient
