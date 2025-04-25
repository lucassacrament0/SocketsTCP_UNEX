import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(1234);
        System.out.println("Aguardando conexão e login...");
        Socket cliente = servidor.accept();
        System.out.println("Cliente conectado.");

        //canais de entrada e saída na comunicação entre cliente e servidor.
        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);

        String nome = entrada.readLine();
        String senha = entrada.readLine();

        //grupo de usuários cadastrados usando arrays.
        String[][] usuarios = {
                {"emerson", "170506"},
                {"janaina", "310797"},
                {"acad", "acad"}
        };

        //padrão para login definido como inválido.
        boolean loginValido = false;

        for (String[] usuario : usuarios) {
            if (usuario[0].equals(nome) && usuario[1].equals(senha)) {
                loginValido = true;
                break;
            }
        }

        if (loginValido) {
            saida.println("Login bem-sucedido.");

            //após o login, criação de pasta do usuário e subpastas no servidor.
            String localUsuario = "usuarios/" + nome;
            String[] tipos = {"pdf", "jpg", "txt"};

            for (String tipo : tipos) {
                File local = new File(localUsuario + "/" + tipo);

                //verificar se a pasta do usuário já existe e, se não, criá-la.
                if (!local.exists()) {
                    if (local.mkdirs()) {
                        System.out.println("Local criado: " + local.getPath());
                    } else {
                        System.out.println("Erro ao criar local: " + local.getPath());
                    }
                }
            }
        } else {
            saida.println("Falha ao login.");
        }

        cliente.close();
        servidor.close();
    }
}
