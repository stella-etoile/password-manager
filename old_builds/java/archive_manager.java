
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;

/* 
    THIS IS AN OUTDATED / DEPRECATED VERSION OF THE PASSWORD MANAGER. PLEASE USE 'manager.java' for the updated password manager.
*/

public class archive_manager {
    final static int REC_PWD = 1;
    final static int ADD_PWD = 2;
    final static int CHNG_PWD = 3;
    final static int EXIT = 4;
    final static int GET_LST = 0;
    public static void main(String[] args) throws IOException, InterruptedException {
        // System.out.println(Decryption.decrypt("132)0%2'1%130'1;0(0*2,122.1@1(0D19120#0B1#1$250+1$0-1<1-1@0%35"));
        // System.out.println(Encryption.encrypt("Vo%mHV'^(*rUtcKD\\U#BFG{+G-_Pc%", (int) (Math.random()*14+33)));
        Scanner input = new Scanner(System.in);
        // System.out.println(MessageFiller.fillMessage("Website", -2) + "|" + MessageFiller.reverseFillMessage("Password", -2));
        System.out.println("Recover password (1), Add password (2), Change password (3), Exit (4)");
        int ans = input.nextInt();
        if (! (ans <= 4 && ans >= 0)) {
            System.out.println("Invalid command: Recover password (1), Add password (2), Change password (3), Exit (4)");
            System.out.println();
            ans = input.nextInt();
        }

        {
            System.out.println("-------------------------------------------------------------");
            for (int i = 0; i < 5; i++) {
                System.out.println();
            }
        }

        if (ans == REC_PWD) { // Recover password
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> list = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                list.put(line.split(" ")[0], line.split(" ")[1]);
            }

            System.out.println("Websites");
            System.out.println("-------------------------------------------------------------");
            int i = 1;
            for (String wname : list.keySet()) {
                System.out.println(i + ": " + wname);
                i++;
            }

            System.out.println();
            System.out.println("Enter the index or the name of the website you wish to retrieve the password of: ");
            input.nextLine();
            String resp = input.nextLine();
            while (Integer.parseInt(resp) <= 0 && Integer.parseInt(resp) > list.size()) {
                System.out.println("Invalid index. Enter the index of the website you wish to retrieve the password of: ");
                resp = input.next();
            }

            int j = 0;
            for (String wname : list.keySet()) {
                j++;

                if (Integer.parseInt(resp) == j) {
                    String e = Encryption.encrypt(wname, (int) (Math.random()*14+33));
                    e = e.replaceAll("[^a-zA-Z0-9]","");
                    e += ".txt";
                    PrintWriter tempFile = new PrintWriter(e);
                    tempFile.println(Decryption.decrypt(list.get(wname)));
                    tempFile.close();
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe " + e);
                    TimeUnit.MILLISECONDS.sleep(500);
                    File f = new File(e);
                    f.delete();
                    break;
                }
            }
        }
        else if (ans == ADD_PWD) { // Add password
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> list = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                list.put(line.split(" ")[0], line.split(" ")[1]);
            }
            listRead.close();

            System.out.println("Enter the name of the website: ");
            input.nextLine(); // skip line for int input before
            String web = input.nextLine();

            System.out.println("Enter the password for " + web + " (enter 0 to generate a random password): ");
            String pass = input.nextLine();
            System.out.println("How long do you want your password to be?");
            int len = input.nextInt();
            int token = (int) (Math.random()*67+33);

            while (pass.equals("0")) {
                pass = RandomPassword.generatePassword(len);
            }
            String epass = Encryption.encrypt(pass, (int) (Math.random()*14+33));
            PrintWriter tempFile = new PrintWriter("temp.txt");
            tempFile.println("The random generated password for " + web + " is " + pass);
            tempFile.close();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe temp.txt");
            TimeUnit.MILLISECONDS.sleep(500);
            File f = new File("temp.txt");
            f.delete();
            list.put(web, epass);

            PrintWriter listExport = new PrintWriter("list.txt");
            for (String wname : list.keySet()) {
                listExport.println(wname + " " + list.get(wname));
            }
            listExport.close();
        }
        else if (ans == GET_LST) { // Get list of all the passwords
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> list = new HashMap<>();
            int i = 1;
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                list.put(line.split(" ")[0], line.split(" ")[1]);
            }

            System.out.println("Websites");
            System.out.println("-------------------------------------------------------------");
            PrintWriter tempFile = new PrintWriter("temp.txt");
            for (String wname : list.keySet()) {
                tempFile.println(wname + " " + Decryption.decrypt(list.get(wname)));
            }
            tempFile.close();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe temp.txt");
            TimeUnit.MILLISECONDS.sleep(500);
            File f = new File("temp.txt");
            f.delete();
            // ans = EXIT;
        }
        else if (ans == CHNG_PWD) {
            System.out.println("test");
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> list = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                list.put(line.split(" ")[0], line.split(" ")[1]);
            }
            listRead.close();

            System.out.println("Websites");
            System.out.println("-------------------------------------------------------------");
            int i = 1;
            for (String wname : list.keySet()) {
                System.out.println(i + ": " + wname);
                i++;
            }

            System.out.println();
            System.out.println("Enter the index of the website you want to change the password of: ");
            input.nextLine();
            String resp = input.nextLine();
            while (Integer.parseInt(resp) <= 0 && Integer.parseInt(resp) > list.size()) {
                System.out.println("Invalid index. Enter the index of the website you wish to retrieve the password of: ");
                resp = input.next();
            }

            System.out.println("Enter the your new password: ");
            String pass = input.nextLine();
            System.out.println("How long do you want your password to be?");
            int len = input.nextInt();
            int token = (int) (Math.random()*67+33);

            while (pass.equals("0")) {
                pass = RandomPassword.generatePassword(len);
            }
            String epass = Encryption.encrypt(pass, (int) (Math.random()*14+33));

            int j = 0;
            for (String wname : list.keySet()) {
                j++;

                if (Integer.parseInt(resp) == j) {
                    list.put(list.get(wname),epass);

                    String e = Encryption.encrypt(wname, (int) (Math.random()*14+33));
                    e = e.replaceAll("[^a-zA-Z0-9]","");
                    e += ".txt";
                    PrintWriter tempFile = new PrintWriter(e);
                    tempFile.println(Decryption.decrypt(list.get(wname)));
                    tempFile.close();
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe " + e);
                    TimeUnit.MILLISECONDS.sleep(500);
                    File f = new File(e);
                    f.delete();
                    break;
                }
            }
            PrintWriter listExport = new PrintWriter("list.txt");
            for (String wname : list.keySet()) {
                listExport.println(wname + " " + list.get(wname));
            }
            listExport.close();
        }
        
        if (ans == EXIT) {
            
        }
        else {
            // Runtime runtime = Runtime.getRuntime(); 
            // Runtime.getRuntime().exec("C:\\Users\\19493\\Documents\\VSCode\\Password Manager\\src\\Password Manager.bat", null, new File("C:\\Users\\19493\\Documents\\VSCode\\Password Manager\\src\\"));
            archive_manager.main(args);
        }
    }
}
