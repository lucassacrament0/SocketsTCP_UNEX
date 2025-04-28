import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(1234);
        System.out.println("Aguardando conexão e login...");
        Socket cliente = servidor.accept();
        System.out.println("Cliente conectado.");

        BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
        DataInputStream dataIn = new DataInputStream(cliente.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(cliente.getOutputStream());

        String nome = entrada.readLine();
        String senha = entrada.readLine();

        String[][] usuarios = {
                {"emerson", "170506"},
                {"janaina", "310797"},
                {"acad", "acad"}
        };

        boolean loginValido = false;

        for (String[] usuario : usuarios) {
            if (usuario[0].equals(nome) && usuario[1].equals(senha)) {
                loginValido = true;
                break;
            }
        }

        if (loginValido) {
            saida.println("Login bem-sucedido.");
            String localUsuario = "usuarios/" + nome;
            String[] tipos = {"pdf", "jpg", "txt"};

            for (String tipo : tipos) {
                File local = new File(localUsuario + "/" + tipo);
                if (!local.exists()) {
                    if (local.mkdirs()) {
                        System.out.println("Local criado: " + local.getPath());
                    } else {
                        System.out.println("Erro ao criar local: " + local.getPath());
                    }
                }
            }

            for (String tipo : tipos) {
                File local = new File(localUsuario + "/" + tipo);
                String[] arquivos = local.list();

                saida.println("Tipo de arquivo: " + tipo);
                if (arquivos != null && arquivos.length > 0) {
                    for (String arquivo : arquivos) {
                        saida.println(" - " + arquivo);
                    }
                } else {
                    saida.println(" (nenhum arquivo encontrado)");
                }
            }

            saida.println("Fim");

            String comando;
            while ((comando = entrada.readLine()) != null) {
                if (comando.equalsIgnoreCase("Download")) {
                    String nomeArquivo = entrada.readLine();
                    File arquivo = null;

                for (String tipo : tipos) {
                    File possivel = new File(localUsuario + "/" + tipo + "/" + nomeArquivo);
                    if (possivel.exists()) {
                        arquivo = possivel;
                        break;
                    }
                }

                if (arquivo != null) {
                    dataOut.writeLong(arquivo.length());
                    FileInputStream fis = new FileInputStream(arquivo);
                    byte[] buffer = new byte[4096];
                    int bytesLidos;
                    while ((bytesLidos = fis.read(buffer)) != -1) {
                        dataOut.write(buffer, 0, bytesLidos);
                    }
                    fis.close();
                    System.out.println("Arquivo '" + nomeArquivo + "' enviado com sucesso.");
                } else {
                    dataOut.writeLong(-1);
                    System.out.println("Arquivo '" + nomeArquivo + "' não encontrado.");
                }

                } else if (comando.equalsIgnoreCase("Upload")) {
                    String nomeArquivo = entrada.readLine();
                    long tamanho = dataIn.readLong();
                    String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1).toLowerCase();
                    File destino = new File(localUsuario + "/" + extensao + "/" + nomeArquivo);
                    FileOutputStream fos = new FileOutputStream(destino);
                    byte[] buffer = new byte[4096];
                    int bytesLidos;
                    long totalRecebido = 0;

                    while (totalRecebido < tamanho && (bytesLidos = dataIn.read(buffer, 0, (int) Math.min(buffer.length, tamanho - totalRecebido))) != -1) {
                        fos.write(buffer, 0, bytesLidos);
                        totalRecebido += bytesLidos;
                    }

                    fos.close();
                System.out.println("Arquivo '" + nomeArquivo + "' recebido com sucesso.");
                }
            }
        } else {
            saida.println("Falha ao login.");
        }

        cliente.close();
        servidor.close();
    }
}
