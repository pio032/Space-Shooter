import java.io.*;
import java.net.*;
public class Server_Socket {
    //scrittra messaggi sul file
    public static void WrtiteFile(String usr, String msg, boolean fine) throws IOException {
    
    String path = "C:\\Users\\PietroArdizzone\\OneDrive - ITS Angelo Rizzoli\\Documenti\\ProgettiVsCode\\progetto_UFS02";
    usr=path+"//"+usr+".txt";
    FileWriter writer = new FileWriter(usr, true);
    if(!fine){
    writer.write(msg+" ");
    writer.close();
    }else{
        writer.write("\n");
        writer.close();
    }
    }
     // verifica esistenza file utente, caso contrario lo crea
    public static void CreationFile(String usr) throws IOException {
        String path = "C:\\Users\\PietroArdizzone\\OneDrive - ITS Angelo Rizzoli\\Documenti\\ProgettiVsCode\\progetto_UFS02";
        usr=path+"//"+usr+".txt";
        File a = new File(usr);
        if (!a.exists()) {
        FileWriter writer = new FileWriter(usr, true);
        }
    }

    public static void main(String[] args) {
        // Porta su cui il server ascolta
        int port = 1234;
        boolean a = true;

        try {
            // Crea un ServerSocket che ascolta sulla porta specificata
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);
            // Il server attende che un client si connetta
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connesso da " + clientSocket.getInetAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //crea il file nellla stessa cartella del programma, OCCHIO VALE SOLO SUL PC DELLA SCUOLA, CAMBIARE IL PATH
            CreationFile(String.valueOf(clientSocket.getInetAddress()));
            // inizio del ciclo
            do {
                // Crea un BufferedReader per leggere i dati inviati dal client

                // Legge un messaggio inviato dal client
                String clientMessage = in.readLine();
                System.out.println("Messaggio dal client: " + clientMessage);
                //scrivi sul file il messaggio del client
                
                // Invia una risposta al client e risposta

                if (clientMessage.equals("stop")) {
                    // stop server
                    a = false;
                    WrtiteFile(String.valueOf(clientSocket.getInetAddress()), clientMessage, true);
                    out.println("Ottimo! La nostra conversazione termina qui!");
                } else {
                    out.println(
                            "Ciao, client! Ho ricevuto il tuo messaggio. Quello che ho ricevuto è: " + clientMessage);
                            WrtiteFile(String.valueOf(clientSocket.getInetAddress()), clientMessage, false);
                }

                // Chiudi le connessioni e gli stream

            } while (a);
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