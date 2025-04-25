import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(1234);
        System.out.println("Aguardando conexão e login...");
        Socket cliente = servidor.accept();
        System.out.println("Cliente conectado.");

        //lê o que o que cliente escreve
        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
        //envia mensagens para o cliente

        String nome = entrada.readLine();
        String senha = entrada.readLine();

        //grupo de usuários cadastrados por array
        String[][] usuarios = {
                {"emerson", "170506"},
                {"janaina", "310797"},
                {"acad", "acad"}
        };

        //definir padrão para login: inválido
        boolean loginValido = false;

        //validação de ‘login’
        for (String[] usuario : usuarios) {
            if (usuario[0].equals(nome) && usuario[1].equals(senha)) {
                loginValido = true;
                saida.println("Login bem-sucedido.");
                break;
            }
        else {
        saida.println("Falha ao login.");
        }

        cliente.close();
        servidor.close();
        }
    }
}