public class Decryption {
    public static String decrypt(String message) {
        int token = Integer.parseInt(message.substring(message.length()-2));
        String ret = "";
        for (int i = 0; i < (message.length()-2)/2; i++) {
            String cur = message.substring(2*i, 2*i+2);
            // System.out.println(cur);
            int rep = Integer.parseInt(cur.substring(0,1));
            int ch = cur.charAt(1);
            ret += (char) ((rep*token) + ch);
        }
        return ret;
    }
}
