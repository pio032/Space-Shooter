import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Server_Socket {

    // creazione variabile di controllo globale
    public static boolean lose = false;
    public static boolean colpo = false;

    // scrittra messaggi sul file
    public static void WriteFile(String username, String password, String nome, int win) throws IOException {
        String[] users;
        String completa = "";
        try {
            // Leggi il file JSON
            String contenuto = ReadFile("user");
            // Rimuovi le parentesi quadre (array JSON)
            contenuto = contenuto.trim();
            if (contenuto.startsWith("[") && contenuto.endsWith("]")) {
                contenuto = contenuto.substring(1, contenuto.length() - 1).trim();
            }

            // Dividi il contenuto in singoli oggetti JSON (ogni oggetto è un utente)
            String[] utentiJson = contenuto.split("\\},\\{");

            // Crea una lista per memorizzare gli utenti
            List<String> utenti = new ArrayList<>();

            // Elenco degli utenti
            for (String utenteJson : utentiJson) {
                // Aggiungi le parentesi graffe mancanti per ogni oggetto
                utenteJson = "{" + utenteJson + "}";
                utenti.add(utenteJson);
            }

            // Per ogni utente, estrai i dettagli
            System.out.println(utenti.size() * 2);

            // verifica dati
            /*
             * System.out.println("user: " + nomeUtente);
             * System.out.println("pw: " + password);
             * System.out.println("----------");
             */

            for (int j = 0; j < utenti.size(); j++) {
                String utente = utenti.get(j);
                completa = completa + "{\n" +
                        "  \"username\": \"" + extractKey(utente, "username") + "\",\n" +
                        "  \"password\": \"" + extractKey(utente, "password") + "\",\n" +
                        "  \"vittorie\": \"" + extractKey(utente, "win") + "\"\n" +
                        "},";

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * leggo il file, concateno la stringa e riscrivo --> stesso codice che ho nel
         * main per la lettura
         */

        String jsonContent = "[" + completa + "{\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"vittorie\": \"" + String.valueOf(win) + "\"\n" +

                "}" + "]";

        // Scrivere nel file JSON
        try (FileWriter writer = new FileWriter("./" + nome + ".json")) {
            writer.write(jsonContent);
            System.out.println("File JSON scritto correttamente!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // aggiungi vittoria, aggionra file json e cincrementa la vittoria
    public static void updateWin(String username, String nuoveVittorie){
          try {
            // Leggi il contenuto del file JSON come stringa
            File file = new File("user.json");
            String jsonString = new String(Files.readAllBytes(file.toPath()));

            // Trova la parte corrispondente all'utente con lo username specificato
            int startIndex = jsonString.indexOf("\"username\": \"" + username + "\"");

            if (startIndex != -1) {
                // Trova l'inizio e la fine del campo "vittorie" per quell'utente
                int vittorieIndex = jsonString.indexOf("\"vittorie\":", startIndex);
                if (vittorieIndex != -1) {
                    // Trova la fine del valore di vittorie
                    int endVittorieIndex = jsonString.indexOf(",", vittorieIndex);
                    if (endVittorieIndex == -1) {
                        endVittorieIndex = jsonString.indexOf("}", vittorieIndex);
                    }

                    // Costruisci la nuova stringa con il valore aggiornato
                    String updatedJsonString = jsonString.substring(0, vittorieIndex + 11) // fino a "vittorie":
                            + "\"" + nuoveVittorie + "\"" // nuovo valore di vittorie
                            + jsonString.substring(endVittorieIndex); // il resto della stringa

                    // Scrive la stringa aggiornata nel file
                    Files.write(file.toPath(), updatedJsonString.getBytes());

                    System.out.println("Vittorie aggiornate per l'utente " + username);
                }
            } else {
                System.out.println("Utente non trovato: " + username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // verifica esistenza file utente, caso contrario lo crea
    public static void CreationFile(String usr) throws IOException {
        // String path = "C:\\Users\\PietroArdizzone\\OneDrive - ITS Angelo
        // Rizzoli\\Documenti\\ProgettiVsCode\\progetto_UFS02";
        usr = "./" + usr + ".json";
        File a = new File(usr);
        if (!a.exists()) {
            FileWriter writer = new FileWriter(usr, true);
        }
    }

    // leggo dal file json
    public static String ReadFile(String usr) throws IOException {
        usr = "./" + usr + ".json";
        StringBuilder contenuto = new StringBuilder(); // oggetto lettura
        try (BufferedReader reader = new BufferedReader(new FileReader(usr))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contenuto.append(line).append("\n");
            }
        }
        return contenuto.toString();
    }

    public static String extractKey(String estratto, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        String valore = "";
        // cerco la corrispondenza del pattern con la chiave e il valore
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(estratto);
        if (m.find()) {
            valore = m.group(1); // Estrai il valore catturato
        }
        return valore;
    }

    // SPOSTAMENTI NAVICELLA E CAMPO  !!

    public static char[][] scendi(char[][] campo) {
        for (int i = 0; i < 10; i++) {
            for (int j = 9; j > -1; j--) {
                try {
                    if (campo[j][i] == '#') {
                        if (campo[j + 1][i] == '@') {
                            lose = true;
                            campo[j][i] = '.';
                            campo[j + 1][i] = '#';
                        }
                        campo[j][i] = '.';
                        campo[j + 1][i] = '#';
                    }
                } catch (IndexOutOfBoundsException e) {
                    lose = true;
                }
            }
        }
        return campo;
    }

    // generazione campo
    public static char[][] StartGame() {
        char[][] campo = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 9 && j == 9) {
                    campo[i][j] = '@';
                } else {

                    campo[i][j] = '.';
                }

            }
        }
        return campo;
    }

    // generzioneNemici
    public static char[][] gen(char[][] campo) {
        for (int i = 0; i < 2; i++) {
            int c = (int) (Math.random() * 10);
            int r = (int) (Math.random() * 5);
            if (campo[r][c] == '#') {
                i--;
            } else {
                campo[r][c] = '#';
            }
        }
        return campo;
    }

    // spostamento della navicella
    public static char[][] updateCampo(char[][] campo, String a) {

        if ('a' == a.charAt(0)) {
            // spostamento sx
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (campo[i][j] == '@') {
                        int spostamento = a.length();
                        try {
                            if (campo[i][j - spostamento] == '#') {
                                lose = true;
                            } else {
                                campo[i][j] = '.';
                                campo[i][j - spostamento] = '@';
                            }
                        } catch (IndexOutOfBoundsException e) {
                            campo[i][j] = '@';

                        }
                    }
                }
            }

        } else {
            // spostamento dx
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (campo[i][j] == '@') {
                        try {
                            int spostamento = a.length();
                            campo[i][j] = '.';
                            campo[i][j + spostamento] = '@';
                        } catch (IndexOutOfBoundsException e) {
                            campo[i][j] = '@';

                        }
                        break;
                    }
                }
            }
        }

        return campo;
    }

    // comparsa del colpo sulla matrice
    public static char[][] shot(char[][] campo) {
        // ricerca della navicella
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (campo[i][j] == '@') {
                    if (campo[i - 1][j] == '#') {
                        campo[i - 1][j] = '.';
                        colpo = true;
                    } else {
                        campo[i - 1][j] = '-';
                    }

                }
            }
        }
        return campo;
    }

    // il colpo viene fatto salire verso l alto
    public static char[][] colpisci_nemico(char[][] campo) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (campo[i][j] == '-') {
                    try {
                        if (campo[i - 1][j] == '.') {
                            campo[i - 1][j] = '-';
                            campo[i][j] = '.';
                        } else {
                            campo[i - 1][j] = '.';
                            campo[i][j] = '.';
                            colpo = true;
                            break;
                        }
                    } catch (IndexOutOfBoundsException e) {

                        campo[i][j] = '.';
                        colpo = true;
                        break;

                    }
                    break;
                }
            }
        }
        return campo;
    }

    public static boolean check(char[][] campo) {
        boolean tr = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (campo[i][j] == '#') {
                    tr = true;
                }
            }
        }
        if (!tr) {
            return true;
        } else {
            return false;
        }
    }

    // MAIN
    public static void main(String[] args) throws InterruptedException, IOException {
        // Porta su cui il server ascolta
        int port = 1234;
        boolean a = true;
        // creazione del file
        CreationFile("user");
        int win = 0;
        try {
            // Crea un ServerSocket che ascolta sulla porta specificata
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server in ascolto sulla porta " + port);
            // Il server attende che un client si connetta
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connesso da " + clientSocket.getInetAddress());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String userF = "";
            // LOGIN
            out.print("Ciao! Dimmi il tuo nome utente: \n");
            out.flush(); // Forza il flush del buffer per stampare subito il messaggio
            String user = in.readLine();
            out.print("Ciao! Dimmi la tua password: \n");
            out.flush(); // Forza il flush del buffer per stampare subito il messaggio
            String pw = (String) in.readLine();
            // lettura di piu utenti dal file json
            boolean ctrl = false;
            boolean UExist = false;
            try {
                // Leggi il file JSON
                String contenuto = ReadFile("user");
                // Rimuovi le parentesi quadre (array JSON)
                contenuto = contenuto.trim();
                if (contenuto.startsWith("[") && contenuto.endsWith("]")) {
                    contenuto = contenuto.substring(1, contenuto.length() - 1).trim();
                }

                // Dividi il contenuto in singoli oggetti JSON (ogni oggetto è un utente)
                String[] utentiJson = contenuto.split("\\},\\{");

                // Creo una lista per memorizzare gli utenti
                List<String> utenti = new ArrayList<>();

                // Elenco degli utenti
                for (String utenteJson : utentiJson) {
                    // Aggiungi le parentesi graffe mancanti per ogni oggetto
                    utenteJson = "{" + utenteJson + "}";
                    utenti.add(utenteJson);
                }

                // Per ogni utente, estrai i dettagli
                for (String utente : utenti) {

                    String nomeUtente = extractKey(utente, "username");
                    String password = extractKey(utente, "password");
                    String vitt = extractKey(utente, "vittorie");
                    if (!vitt.equals("")) {
                        win = win + Integer.parseInt(vitt);
                    }

                    // verifica utenti

                    System.out.println("user: " + nomeUtente);
                    System.out.println("pw: " + password);
                    System.out.println("----------");
                    if (nomeUtente.equals(user) && password.equals(pw)) {
                        ctrl = true;
                    }
                    if (nomeUtente.equals(user) && !password.equals(pw)) {
                        UExist = true;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ctrl) {
                if (UExist) {

                } else {
                    out.print("Benvenuto " + user + "\n");
                    userF = user;
                }

            } else {
                if (UExist) {
                    out.print("Hai sbagliato la password! \n");
                } else {
                    out.print("Non hai ancora un utente? nessun probelma te lo creo io! il tuo username è: " + user
                            + "\n\n");
                    userF = user;
                    if (!user.equals("null") && !pw.equals("")) {
                        WriteFile(user, pw, "user", 0);
                        System.out.println("sono passato!");
                    }
                }

            }

            // stampo il campo di gioco
            char[][] campo = StartGame();
            // lo riempio di nemici
            char[][] finito = gen(campo);
            while (a) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (finito[i][j] == '@') {
                            out.print("🚀");
                            

                        } else {
                            if (finito[i][j] == '#') {
                                out.print("👽");
                                /*
                                 * if (j != 9) {
                                 * j++;
                                 * }
                                 */

                            } else {
                                out.print("🌟");

                            }

                        }

                    }
                    out.println();
                }
                // in utente
                String clientMessage = (String) in.readLine();
                System.out.println(clientMessage);
                if (!clientMessage.equals(" ")) {
                    // sposto navicella
                    finito = updateCampo(finito, clientMessage);
                } else {
                    // colpo
                    finito = shot(finito);
                    // sale il colpo
                    while (!colpo) {
                        finito = colpisci_nemico(finito);
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (finito[i][j] == '@') {
                                    out.print("🚀");

                                } else {
                                    if (finito[i][j] == '#') {
                                        out.print("👽");
                                        /*
                                         * if (j != 9) {
                                         * j++;
                                         * }
                                         */

                                    } else {
                                        if (finito[i][j] == '-') {
                                            out.print("💣");
                                        } else {
                                            out.print("🌟");
                                        }

                                    }

                                }
                            }
                            out.println();
                        }
                        Thread.sleep(900);
                        out.println();
                        out.println();
                    }
                    colpo = false;
                }

                finito = scendi(finito);
                // conotrollo perdita
                if (lose) {
                    a = false;
                    out.println("mi dispiace hai perso, i nemici sono arrivati fino alla fine!");
                }
                Thread.sleep(1000);
                // verifica vittoria
                if (check(finito)) {
                    out.println("Hai vinto!!!");
                    win = win + 1;
                    String vittoria = String.valueOf(win);
                    System.out.println("ctrl controllo vittoria: "+vittoria);
                    //invio dei dati al metodo
                    updateWin(user, String.valueOf(win));
                    a = false;
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