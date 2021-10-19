public class Encryption {
    public static String encrypt(String message, int token) {
        String ret = "";
        for (char c : message.toCharArray()) {
            int p = ((int) c)/token;
            int q = (char) (c % token);
            if (q < 34) {
                p--;
                q += token;
            }

            if (p < 0) {
                return encrypt(message, (int) (Math.random()*14+33));
            }

            ret += p;
            ret += (char) q;
        }

        return ret + ((token < 10) ? "0" + token : token);
    }
}