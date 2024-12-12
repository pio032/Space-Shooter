import java.io.*;
import java.net.*;

public class Server_Socket {

    public static void main(String[] args) {
        // Porta su cui il server ascolta
        int port = 1255;

        try {
            // Crea un oggetto ServerSocket che ascolta sulla porta specificata
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);

            // Accetta la connessione di un client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connessione accettata da " + clientSocket.getInetAddress());

            // Crea gli stream di input e output per comunicare con il client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Comunica con il client
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Messaggio dal client: " + clientMessage);
                
                // Rispondi al client
                out.println("Server ha ricevuto: " + clientMessage);
                
                if (clientMessage.equalsIgnoreCase("bye")) {
                    break;  // Esci se il client invia "bye"
                }
            }

            // Chiudi la connessione
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Connessione chiusa.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
