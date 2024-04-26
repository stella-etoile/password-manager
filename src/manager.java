import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/* 
    NEWLY UPDATED PASSWORD MANAGER
    ---
    Current functionalities include:
    1) Recovering passwords
    2) Adding new passwords
    3) Changing previous passwords

    Functionalities to be added:
    1) Sorting passwords by name
        - i.e. Given a set of passwords
            {'google-stellarium01','google-stellarium02', 'github-stellarium01'}
        You can sort by 'google' and retrieve
            {'stellarium01', 'stellarium02'}
    
    Known bugs:
    1) Exit key doesn't work
*/

public class manager {
    final static int REC_PWD = 1;
    final static int ADD_PWD = 2;
    final static int CHNG_PWD = 3;
    final static int EXIT = 4;
    final static HashSet<Integer> valid_cmds = new HashSet<Integer>(Arrays.asList(REC_PWD, ADD_PWD, CHNG_PWD, EXIT));
    public static void main(String[] args) throws IOException, InterruptedException {
        // reads input
        Scanner input = new Scanner(System.in);

        // reads option line
        String str_ans = "";
        int ans = -1;
        System.out.println("Enter a command: Recover password (1), Add password (2), Change password (3), Exit (4)");
        str_ans = input.nextLine();
        try {
            ans = Integer.parseInt(str_ans);
            // if not valid command, throws an exception
            if (! valid_cmds.contains(ans)) {
                throw new InvalidInputException("Invalid command.");
            }
        }
        catch (Exception e) {
            // exception clears the screen, prints the error, and reruns the program
            helper.clear();
            System.out.println(e);
            manager.main(args);
        }
        
        // option: 1, recovering password
        if (ans == REC_PWD) {
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> wslist = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                wslist.put(line.split(" ")[0], line.split(" ")[1]);
            }
            helper.clear();

            int i = 1;
            for (String wname : wslist.keySet()) {
                System.out.println(i + ": " + wname);
                i++;
            }

            System.out.println();
            System.out.println("Enter the index or the name of the website you wish to retrieve the password of: ");
            // index of or the name of the website
            String ws = input.nextLine();
            int index = -1;

            while (true) {
                try {
                    index = Integer.parseInt(ws);
                    if (index <= 0 && index > wslist.size()) {
                        System.out.println("Invalid index. Enter the index or the name of the website you wish to retrieve the password of: ");
                        ws = input.nextLine();
                    }
                    else {
                        break;
                    }
                }
                catch (Exception e) {
                    break;
                }
            }

            boolean printed = false;

            if (index != -1) {
                int j = 0;
                for (String wname : wslist.keySet()) {
                    j++;

                    if (index == j) {
                        String e = Encryption.encrypt(wname, (int) (Math.random()*14+33));
                        e = e.replaceAll("[^a-zA-Z0-9]","");
                        e += ".txt";
                        PrintWriter tempFile = new PrintWriter(e);
                        tempFile.println(Decryption.decrypt(wslist.get(wname)));
                        tempFile.close();
                        Runtime runtime = Runtime.getRuntime();
                        Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe " + e);
                        TimeUnit.MILLISECONDS.sleep(500);
                        File f = new File(e);
                        f.delete();
                        printed = true;
                        System.out.println(Decryption.decrypt(wslist.get(wname)));
                        break;
                    }
                }   
            }

            if (!printed) {
                for (String wname : wslist.keySet()) {
                    if (wname.equalsIgnoreCase(ws)) {
                        String e = Encryption.encrypt(wname, (int) (Math.random()*14+33));
                        e = e.replaceAll("[^a-zA-Z0-9]","");
                        e += ".txt";
                        PrintWriter tempFile = new PrintWriter(e);
                        tempFile.println(Decryption.decrypt(wslist.get(wname)));
                        tempFile.close();
                        Runtime runtime = Runtime.getRuntime();
                        Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe " + e);
                        TimeUnit.MILLISECONDS.sleep(500);
                        File f = new File(e);
                        f.delete();
                        printed = true;
                        break;
                    }
                }
            }

            if (!printed) {
                System.out.println("No index/website with value \"" + ws + "\" found.");
            }
        }
        else if (ans == ADD_PWD) {
            // import list
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> wslist = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                wslist.put(line.split(" ")[0], line.split(" ")[1]);
            }
            listRead.close();

            System.out.println("Enter the name of the website (for sorting purposes 'website-username' is optimal - i.e. google-stellarium");
            System.out.println("and please refrain from using '-' instead of spaces and use '.' instead): ");
            String ws = input.nextLine();
            while (wslist.containsKey(ws)) {
                System.out.println("The password manager contains a password for \""+ws+"\" already. Please enter another website or type 'c' to continue with replacing the previous value.");
                String res = input.nextLine();
                if (res.equals("c")) {
                    break;
                }
                else {
                    ws = res;
                }
            }

            System.out.println("Enter the password for \"" + ws + "\" (enter 0 to generate a random password): ");
            String pw = input.nextLine();
            int len = -1;
            
            // int token = (int) (Math.random()*67+33);
            int token = (int) (Math.random()*14+33);

            try {
                if (Integer.parseInt(pw) == 0) {
                    System.out.println("How long do you want your password to be?");

                    // TODO: need clause to error check length
                    len = Integer.parseInt(input.nextLine());

                    // idk why it's in a while block, need to check interactions and can remove if needed
                    while (pw.equals("0")) {
                        pw = RandomPassword.generatePassword(len);
                    }
                }
            }
            catch (Exception e) {
            }

            String epw = Encryption.encrypt(pw, token);
            PrintWriter tempFile = new PrintWriter("temp.txt");
            tempFile.println("The password for " + ws + " is " + pw);
            tempFile.close();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe temp.txt");
            TimeUnit.MILLISECONDS.sleep(500);
            File f = new File("temp.txt");
            f.delete();
            wslist.put(ws, epw);

            PrintWriter listExport = new PrintWriter("list.txt");
            for (String wname : wslist.keySet()) {
                listExport.println(wname + " " + wslist.get(wname));
            }
            listExport.close();
        }
        else if (ans == CHNG_PWD) {
            // import list
            Scanner listRead = new Scanner(new File("list.txt"));
            HashMap<String, String> wslist = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                wslist.put(line.split(" ")[0], line.split(" ")[1]);
            }
            listRead.close();

            String ws = "";

            while (true) {
                int i = 1;
                for (String wname : wslist.keySet()) {
                    System.out.println(i + ": " + wname);
                    i++;
                }

                System.out.println("Enter the name or index of the website you want to change: ");
                ws = input.nextLine();
                int index = -1;
                boolean isIndex = false;

                while (true) {
                    try {
                        index = Integer.parseInt(ws);
                        if (index <= 0 && index > wslist.size()) {
                            System.out.println("Invalid index. Enter the name or index of the website you want to change: ");
                            ws = input.nextLine();
                        }
                        else {
                            int j = 1;
                            for (String wname : wslist.keySet()) {
                                if (j == index) {
                                    ws = wname;
                                    break;
                                }
                                j++;
                            }
                            isIndex = true;
                            break;
                        }
                    }
                    catch (Exception e) {
                        break;
                    }
                }
    
                while (! wslist.containsKey(ws) && ! isIndex) {
                    System.out.println("The password manager doesn't have a password for \""+ws+"\" already.");
                    System.out.println("Enter the name or index of the website you want to change: ");
                    String res = input.nextLine();
                    ws = res;
                }

                System.out.println("Can you confirm the website you are trying to change is \""+ws+"\"? Type 'y'");
                if (input.nextLine().equals("y")) {
                    break;
                }
            }

            System.out.println("Enter the password for \"" + ws + "\" (enter 0 to generate a random password): ");
            String pw = input.nextLine();
            int len = -1;
            
            // int token = (int) (Math.random()*67+33);
            int token = (int) (Math.random()*14+33);

            try {
                if (Integer.parseInt(pw) == 0) {
                    System.out.println("How long do you want your password to be?");

                    // TODO: need clause to error check length
                    len = Integer.parseInt(input.nextLine());

                    // idk why it's in a while block, need to check interactions and can remove if needed
                    while (pw.equals("0")) {
                        pw = RandomPassword.generatePassword(len);
                    }
                }
            }
            catch (Exception e) {
            }

            String epw = Encryption.encrypt(pw, token);
            PrintWriter tempFile = new PrintWriter("temp.txt");
            tempFile.println("The password for " + ws + " is " + pw);
            tempFile.close();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe temp.txt");
            TimeUnit.MILLISECONDS.sleep(500);
            File f = new File("temp.txt");
            f.delete();
            wslist.put(ws, epw);

            PrintWriter listExport = new PrintWriter("list.txt");
            for (String wname : wslist.keySet()) {
                listExport.println(wname + " " + wslist.get(wname));
            }
            listExport.close();
        }

        if (ans != EXIT) {
            helper.clear();
            manager.main(args);
        }
    }
}