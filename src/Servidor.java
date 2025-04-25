import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(1234);
        System.out.println("Aguardando conexão...");
        Socket cliente = servidor.accept();
        System.out.println("Cliente conectado.");
    }
}