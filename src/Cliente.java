import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket cliente = new Socket("localhost", 1234);

        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome de usuário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        saida.println(nome);
        saida.println(senha);

        String respostaLogin = entrada.readLine();
        System.out.println("Retorno do servidor: " + respostaLogin);

        cliente.close();
    }
}