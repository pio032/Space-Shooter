import java.io.*;
import java.net.*;  


public class Server_Socket {
//verifica esistenza file utente
    public static int FileUser(){
    String path = "provaJava"; 
    return 1;
    }
    public static void main(String[] args) {
        // Porta su cui il server ascolta
        int port = 1890;
        boolean a=true;

        try {
            // Crea un ServerSocket che ascolta sulla porta specificata
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);
            // Il server attende che un client si connetta
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connesso da " + clientSocket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            do{
           

            
           
            // Crea un BufferedReader per leggere i dati inviati dal client
            
            
            // Legge un messaggio inviato dal client
            String clientMessage = in.readLine();
            System.out.println("Messaggio dal client: " + clientMessage);

            // Invia una risposta al client e risposta 
            
            if(clientMessage.equals("stop")){
            //stop server
            a=false;
            out.println("Ottimo! La nostra conversazione termina qui!");
            }else{
            out.println("Ciao, client! Ho ricevuto il tuo messaggio. Quello che ho ricevuto è: "+clientMessage);
            }
            
            // Chiudi le connessioni e gli stream
            
            
           
        }while(a);
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