/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientedechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Ronaldo
 */
public class ClienteDeChat extends Thread {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String mensagem="";
        
        try {
            
            // estabelecendo a conexao.
            Socket conexao = new Socket("192.168.4.110", 12345);

            // uma vez estabelecida a comunicação, deve-se obter os 
            // objetos que permitem controlar o fluxo de comunicação
            PrintStream saida = new PrintStream(conexao.getOutputStream());

            // enviar antes de tudo o nome do usuário
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Entre com alguma informação: ");
            mensagem = teclado.readLine();
            saida.println(mensagem);

            // Uma vez que tudo está pronto, antes de iniciar o loop
            // principal, executar a thread de recepção de mensagens.
            Thread t = new ClienteDeChat(conexao);
            t.start();
            
            // loop principal: obtendo uma linha digitada no teclado e
            // enviando-a para o servidor.
            String linha;
            while (true) {
                // ler a linha digitada no teclado
                System.out.print("> ");
                linha = teclado.readLine(); 
                saida.println(linha);
            }            
        } catch (IOException e) {
            // Caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println("IOException: " + e);
         }        
    }
    
    // parte que controla a recepção de mensagens deste cliente
    private Socket conexao;
    
    // construtor que recebe o socket deste cliente
    public ClienteDeChat(Socket s) {
        conexao = s;
    }
        
    // execução da thread
    public void run() {
        try {
            
            BufferedReader entrada = new BufferedReader (new InputStreamReader(conexao.getInputStream()));
            String linha;
                       
            while (true) {
                // pega o que o servidor enviou
                linha = entrada.readLine();
                
                // verifica se é uma linha válida. Pode ser que a conexão
                // foi interrompida. Neste caso, a linha é null. Se isso
                // ocorrer, termina-se a execução saindo com break
                if (linha == null) {
                    System.out.println("Conexão encerrada!");
                    break;
                }

                // caso a linha não seja nula, deve-se imprimi-la
                System.out.println();
                System.out.println(linha);
                System.out.print("...> ");
                
            }
        } catch (IOException e) {
            // caso ocorra alguma exceção de E/S, mostre qual foi.
            System.out.println("IOException: " + e);
        }
            // sinaliza para o main que a conexão encerrou.
            //done = true;
    }
}
