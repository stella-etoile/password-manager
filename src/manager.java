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
    2) Checking github repo to see if there is a new update

    Known bugs:
    1) Exit key doesn't work

    CHECK README.MD FOR MORE UP-TO-DATE PATCHES
*/

// TODO: Update README.md
// TODO: Update checker by checking last-updated.txt file
// TODO: Allow for different file extensions

public class manager {
    // CHANGE THIS
    private final static String LIST_NAMES = "list-names.txt";
    private final static String FOLDER = "./lists/";

    // DON'T TOUCH BELOW
    // main menu
    private final static int REC_PWD = 1;
    private final static int ADD_PWD = 2;
    private final static int CHNG_PWD = 3;
    private final static int SETTINGS = 4;
    private final static int EXIT = 0;
    private final static HashSet<Integer> valid_cmds = new HashSet<Integer>(Arrays.asList(REC_PWD, ADD_PWD, CHNG_PWD, SETTINGS, EXIT));

    // settings
    private final static int CHNG_DEF = 1;
    private final static int CHNG_TGT = 2;
    private final static int ADD_LST = 3;
    private final static int RESET = 9;
    private final static HashSet<Integer> valid_settings = new HashSet<>(Arrays.asList(CHNG_DEF, CHNG_TGT, ADD_LST, RESET, EXIT));
    // private final static String FILE_NAME = "list.txt";
    private static String TARGET = "";
    private static boolean first_time_setup = false;
    private static boolean exit_all = false;

    // modes
    private final static String MAIN = "0";
    private final static String SETT = "1";
    // private static int EXEC = 0;

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException, InterruptedException {
        // reads user input
        Scanner input = new Scanner(System.in);
        // System.out.println(Arrays.toString(args));

        // import list names
        Scanner lists = new Scanner(new File(LIST_NAMES));
        HashMap<Integer, String> names = new HashMap<>();
        int i = 0;
        
        // stores in hashmap
        while (lists.hasNextLine()) {
            String cur = lists.nextLine();
            if (cur.equals("first-time-setup")) {
                first_time_setup = true;
                break;
            }
            // System.out.println(i + ": " + cur);
            names.put(i, cur);
            i++;
        }
        lists.close();

        // asks to choose a target list
        String str_ans = "";
        int ans = -1;

        if (first_time_setup) {
            Scanner README = new Scanner(new File("../README.md"));
            while (README.hasNextLine()) {
                System.out.println(README.nextLine());
            }
            README.close();

            System.out.println();
            System.out.println("Here is the most up-to-date version of the README.md file associated with the program.");

            if (args.length > 0 && args[0].equals(Integer.toString(RESET))) {
                System.out.println("Reset has been successfully completed.");
            }
            else {
                System.out.println();
                System.out.println("It seems that this is your first time running this program.");
                System.out.println("If this is a mistake, please check 'list-names.txt' or go to the menu to delete 'first-time-setup' from the first line of the list names.");
            }

            System.out.println();
            boolean ready = false;
            
            String new_target = "";
            while (! ready) {
                System.out.println();
                System.out.println("Please provide the name of the file you want to be storing your username and passwords without the .txt extension..");
                str_ans = input.nextLine();
                new_target = str_ans+".txt";
                System.out.println();
                System.out.println("Your file name will be '" + new_target + "'. Please confirm with 'y'.");
                str_ans = input.nextLine();
                if (str_ans.equalsIgnoreCase("y")) {
                    ready = true;
                }
            }
            
            PrintWriter update_names = new PrintWriter("list-names.txt");
            update_names.println(new_target);
            update_names.close();

            helper.clear();
            first_time_setup = false;
            manager.main(new String[] {MAIN});
        }
        else {
            // System.out.println(Arrays.toString(args));
            if (args.length < 2) {
                TARGET = FOLDER+names.get(0);
                
                // if (args.length == 1) {
                //     EXEC = Integer.parseInt(args[0]);
                // }
            }
            else if (args.length == 2) {
                TARGET = FOLDER+args[1];
            }
            // System.out.println(names.get(0));
        }

        // reads option line
        str_ans = "";
        ans = -1;
        if (args.length == 0 || args[0].equals(MAIN)) {
            System.out.println("You are currently browsing the file: " + TARGET);
            System.out.println("Enter a command: Recover password (1), Add password (2), Change password (3), Settings (4), Exit (0)");
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
                manager.main(new String[] {MAIN});
            }
        }
        else if (args[0].equals(SETT)) {
            ans = 4;
        }
        System.out.println();
        
        Scanner listRead = new Scanner(new File(TARGET));
        // option: 1, recovering password
        if (ans == REC_PWD) {
            HashMap<String, String> wslist = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                wslist.put(line.split(" ")[0], line.split(" ")[1]);
            }
            helper.clear();

            i = 1;
            for (String wname : wslist.keySet()) {
                System.out.println(i + ": " + wname);
                i++;
            }

            int index = -1;
            String ws;
            while (true) {
                System.out.println();
                System.out.println("Enter the index or the name of the website you wish to retrieve the password of (or '-' to search): ");
                // index of or the name of the website
                ws = input.nextLine();

                if (ws.equals("-")) {
                    i = 1;
                    System.out.println("Enter the name of the website you are trying to sort: ");
                    String search = input.nextLine();
                    for (String wname : wslist.keySet()) {
                        if (wname.toLowerCase().contains(search.toLowerCase())) {
                            System.out.println(i + ": " + wname);
                        }
                        i++;
                    }
                    continue;
                }

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
                        // System.out.println(Decryption.decrypt(wslist.get(wname)));
                        process.destroy();
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
                        process.destroy();
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

            PrintWriter listExport = new PrintWriter(TARGET);
            for (String wname : wslist.keySet()) {
                listExport.println(wname + " " + wslist.get(wname));
            }
            listExport.close();
            process.destroy();
        }
        else if (ans == CHNG_PWD) {
            // import list
            HashMap<String, String> wslist = new HashMap<>();
            while (listRead.hasNextLine()) {
                String line = listRead.nextLine();
                wslist.put(line.split(" ")[0], line.split(" ")[1]);
            }
            listRead.close();

            String ws = "";

            while (true) {
                i = 1;
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
            process.destroy();
        }
        else if (ans == SETTINGS) {
            str_ans = "";
            ans = -1;

            System.out.println("You are currently browsing the file: " + TARGET);
            System.out.println("Enter a command: Change default target file (1), Change target file (2), Add lists (3), Reset lists indices (9), Back (0)");
            str_ans = input.nextLine();

            try {
                ans = Integer.parseInt(str_ans);
                // if not valid command, throws an exception
                if (! valid_settings.contains(ans)) {
                    throw new InvalidInputException("Invalid command.");
                }
            }
            catch (Exception e) {
                // exception clears the screen, prints the error, and reruns the program
                helper.clear();
                System.out.println(e);
                manager.main(new String[] {SETT});
            }
            System.out.println();

            if (ans == CHNG_DEF) {
                String new_def = "";
                while (true) {
                    System.out.println();
                    for (i = 0; i < names.size(); i++) {
                        String cur = names.get(i);
                        String print = (i+1) + ": " + cur;
    
                        // if default file
                        if (i == 0) {
                            print += " (CURRENT TARGET)";
                        }
                        System.out.println(print);
                    }
                    System.out.println("Pick your new default target file.");
                    str_ans = input.nextLine();
                    try {
                        ans = Integer.parseInt(str_ans);
                        ans--;
    
                        if (ans < 0 || ans > names.size()-1) {
                            System.out.println("Invalid index. Please try again.");
                        }
                        else {
                            new_def = names.get(ans);
                            break;
                        }
                    }
                    catch (Exception e) {}
                }
                PrintWriter update_names = new PrintWriter("list-names.txt");
                update_names.println(new_def);
                for (i = 0; i < names.size(); i++) {
                    String cur = names.get(i);
                    if (! cur.equals(new_def)) {
                        update_names.println(cur);
                    }
                }
                update_names.close();
    
    
                helper.clear();
                manager.main(new String[] {MAIN, new_def});
            }
            else if (ans == CHNG_TGT) {
                String new_target = "";
                while (true) {
                    System.out.println();
                    for (i = 0; i < names.size(); i++) {
                        String cur = names.get(i);
                        String print = (i+1) + ": " + cur;
    
                        // if default file
                        if (i == 0) {
                            print += " (DEFAULT TARGET)";
                        }
    
                        // if current file
                        if (cur.equals(TARGET.substring(FOLDER.length()))) {
                            print += " (current file)";
                        }
                        System.out.println(print);
                    }
                    System.out.println("Pick your target file.");
                    str_ans = input.nextLine();
                    try {
                        ans = Integer.parseInt(str_ans);
                        ans--;
    
                        if (ans < 0 || ans > names.size()-1) {
                            System.out.println("Invalid index. Please try again.");
                        }
                        else {
                            new_target = names.get(ans);
                            break;
                        }
                    }
                    catch (Exception e) {}
                }
                helper.clear();
                manager.main(new String[] {MAIN, new_target});
            }
            else if (ans == ADD_LST) {
                System.out.println("Enter the name of your new file without the .txt extension.");
                String name = input.nextLine()+".txt";
                System.out.println("Please type 'y' to confirm you want to add '" + name+"'.");
                str_ans = input.nextLine();
                if (str_ans.equalsIgnoreCase("y")) {
                    PrintWriter update_names = new PrintWriter("list-names.txt");
                    for (i = 0; i < names.size(); i++) {
                        update_names.println(names.get(i));
                    }
                    update_names.println(name);
                    update_names.close();

                    helper.clear();
                    manager.main(new String[] {Integer.toString(RESET)});
                }
            }
            else if (ans == RESET) {
                // TODO: Implement Delete text file option
                System.out.println("Your '" + LIST_NAMES + "' will be reset to its original state. To get rid of your passwords, please go to " + FOLDER + " and manually delete them or use the Reset Folder option.");
                System.out.println("Please type 'y' to confirm resettings " + FOLDER+LIST_NAMES+".");
                str_ans = input.nextLine();
                if (str_ans.equalsIgnoreCase("y")) {
                    PrintWriter update_names = new PrintWriter("list-names.txt");
                    update_names.println("first-time-setup");
                    update_names.close();

                    helper.clear();
                    manager.main(new String[] {Integer.toString(RESET)});
                }
            }
        }
        else if (ans == EXIT) {
            exit_all = true;
        }

        if (! (ans == EXIT || exit_all)) {
            helper.clear();
            manager.main(new String[]{MAIN});
        }
    }
}