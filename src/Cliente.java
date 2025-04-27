import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket cliente = new Socket("localhost", 1234);

        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
        DataOutputStream dataOut = new DataOutputStream(cliente.getOutputStream());

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome de usuário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        saida.println(nome);
        saida.println(senha);

        String respostaLogin = entrada.readLine();
        System.out.println("Resposta do servidor: " + respostaLogin);

        if (respostaLogin.equals("Login bem-sucedido.")) {
            System.out.println();
            System.out.println("Arquivos no servidor:");

            String linha;
            while ((linha = entrada.readLine()) != null && !linha.equals("Fim")) {
                System.out.println(linha);
            }
        }
        System.out.println();
        System.out.print("Deseja baixar um arquivo do servidor? (s/n): ");
        String opcaoDownload = scanner.nextLine();
        if (opcaoDownload.equalsIgnoreCase("s")) {
            System.out.print("Digite o nome exato do arquivo (com extensão): ");
            String nomeArquivoDownload = scanner.nextLine();

            saida.println("Download");
            saida.println(nomeArquivoDownload);

            DataInputStream dataIn = new DataInputStream(cliente.getInputStream());
            long tamanho = dataIn.readLong();

            if (tamanho == -1) {
                System.out.println("Arquivo não encontrado no servidor.");
            } else {
                FileOutputStream fos = new FileOutputStream("downloaded_" + nomeArquivoDownload);
                byte[] buffer = new byte[4096];
                int bytesLidos;
                long totalRecebido = 0;

                while (totalRecebido < tamanho && (bytesLidos = dataIn.read(buffer, 0, (int) Math.min(buffer.length, tamanho - totalRecebido))) != -1) {
                    fos.write(buffer, 0, bytesLidos);
                    totalRecebido += bytesLidos;
                }
                fos.close();
                System.out.println("Download concluído. Arquivo salvo como: downloaded_" + nomeArquivoDownload);
            }
            cliente.close();
        }
    }
}