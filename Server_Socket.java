import java.io.*;
import java.net.*;
public class Server_Socket {

    //creazione variabile di controllo globale
    public static boolean lose = false;
    public static boolean colpo = false;

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
    
    
    public static String ReadFile(String usr){
    String path = "C:\\Users\\PietroArdizzone\\OneDrive - ITS Angelo Rizzoli\\Documenti\\ProgettiVsCode\\progetto_UFS02";
    usr=path+"//"+usr+".txt";
    String testo="";
    try {
        // Crea un BufferedReader per leggere il file
        BufferedReader reader = new BufferedReader(new FileReader(usr));
        String linea;
        
        // Leggi ogni riga del file finché non raggiungi la fine
        while ((linea = reader.readLine()) != null) {
            testo=testo+linea+"\n"; 
        }

        // Chiudi il reader
        reader.close();
    } catch (IOException e) {
        // Gestione delle eccezioni (es. file non trovato)
        System.out.println("Errore nella lettura del file: " + e.getMessage());
    
    }
        return testo;
}
    public static char[][] scendi(char[][]campo){
        for(int i=0; i<10; i++){
            for(int j=9; j>-1; j--){
            try{
            if (campo[j][i]=='#') {
            if (campo[j+1][i]=='@') {
            lose=true;
            campo[j][i]='.';
            campo[j+1][i]='#'; 
            }
            campo[j][i]='.';
            campo[j+1][i]='#';
            }
            }catch(IndexOutOfBoundsException e){
            lose=true;
            }
            }
        }
    return campo;
    }
    //generazione campo
    public static char[][] StartGame(){
    char[][]campo = new char[10][10];
    for(int i =0; i<10; i++){
        for(int j=0; j<10; j++){
            if (i==9 && j==9) {
                campo[i][j]='@';
            }else{
              
                campo[i][j]='.';    
            }
            
        }
    }
    return campo;
    }   
    //generzioneNemici
    public static char[][] gen(char[][]campo){
    for(int i =0; i<10; i++){
        int c =  (int) (Math.random()*10);
        int r = (int) (Math.random()*5);
        if (campo[r][c]=='#') {
        i--;
        }else{
        campo[r][c]='#';
    }
    }  
    return campo;
    }
    
    //spostamento della navicella
    public static char[][] updateCampo(char[][]campo, String a){

        if('a'==a.charAt(0)){
            //spostamento sx
            for(int i =0; i<10; i++){
                for(int j=0; j<10; j++){
                if (campo[i][j]=='@') {
                 int spostamento=a.length();
                 try {
                    if(campo[i][j-spostamento]=='#'){
                     lose=true;
                    }else{
                  campo[i][j]='.'; 
                  campo[i][j-spostamento]='@';
                }
                 } catch (IndexOutOfBoundsException e) {
                 campo[i][j]='@';
                 
                 }  
                } 
                }
            }

        }else{
         //spostamento dx
         for(int i =0; i<10; i++){
            for(int j=0; j<10; j++){
            if (campo[i][j]=='@') {
             try {
                int spostamento=a.length();
              campo[i][j]='.'; 
              campo[i][j+spostamento]='@';
             } catch (IndexOutOfBoundsException e) {
             campo[i][j]='@';
             
             }
             break;  
            } 
            }
        }
        }
    

    return campo;
    }
    //comparsa del colpo sulla matrice
    public static char[][] shot(char[][]campo){
        //ricerca della navicella
        for(int i =0; i<10; i++){
            for(int j=0; j<10; j++){
             if (campo[i][j]=='@') {
                if (campo[i-1][j]=='#') {
                    campo[i-1][j]='.';
                    colpo=true;
                }else{
                    campo[i-1][j]='-';
                }
             
             }
            }
        }
    return campo;
    }
    //il colpo viene fatto salire verso l alto
    public static char[][] colpisci_nemico(char[][]campo){
        for(int i =0; i<10; i++){
            for(int j=0; j<10; j++){
             if(campo[i][j]=='-'){
                try{
                if(campo[i-1][j]=='.'){
                campo[i-1][j]='-';
                campo[i][j]='.';
            }else{
                campo[i-1][j]='.';
                campo[i][j]='.';
                colpo=true;
                break;
            }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("sono passato!");
                campo[i][j]='.';
                colpo=true;
                break;
                
                }
                break;
             }
            }
        }   
    return campo;
    }


    public static boolean check(char[][]campo){
    boolean tr=false;
    for(int i=0; i<10; i++){
    for(int j=0; j<10; j++){
    if (campo[i][j]=='#') {
    tr=true;    
    }
    }
    }
    if(!tr){
    return true;
    }else{
    return false;
    }
    }




    public static void main(String[] args) throws InterruptedException {
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
            // stampo il campo di gioco
            char[][]campo=StartGame();
            //lo riempio di nemici
            char[][]finito=gen(campo);
            while (a) { 
                for(int i =0; i<10; i++){
                    for(int j=0; j<10; j++){
                        if (finito[i][j]=='@') {
                        out.print("🚀");   
                        out.print(" "); 
                        }else{
                        if (finito[i][j]=='#') {
                        out.print("👾"); 
                        out.print(" "); 
                        }else{
                            out.print(finito[i][j]);
                            
                        }
                        
                        }
                       
                    }
                    out.println();
                }
            //in utente
            String clientMessage = (String)in.readLine();
            System.out.println(clientMessage);
            if(!clientMessage.equals(" ")){
            //sposto navicella
            finito=updateCampo(finito,clientMessage);
            }else{
            //colpo
            finito=shot(finito);
            //sale il colpo
            while (!colpo) { 
            finito=colpisci_nemico(finito);  
            for(int i =0; i<10; i++){
                for(int j=0; j<10; j++){
                    out.print(finito[i][j]);
                }
                out.println();
            }
            Thread.sleep(900);
            out.println();
            out.println();
            }
            colpo=false;
            }
            
            finito = scendi(finito);   
            //conotrollo perdita
            if(lose){
            a=false;
            out.println("mi dispiace hai perso, i nemici sono arrivati fino alla fine!");
            }
            Thread.sleep(1000);
             //verifica vittoria
             if(check(finito)){
                out.println("Hai vinto!!!");
                a=false;
                break;
                }
            }
           
            // Chiudi le connessioni e gli stream
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