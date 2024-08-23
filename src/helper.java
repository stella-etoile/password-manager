public class helper {
    public static void clear() {
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }

    public static String fillMessage(String message, int length) {
        if (length < 0) {
            return fillMessage(message, message.length() - length);
        }

        String ret = message;
        while (ret.length() < length) {
            ret += " ";
        }
        return ret;
    }

    public static String reverseFillMessage(String message, int length) {
        if (length < 0) {
            return reverseFillMessage(message, message.length() - length);
        }

        String ret = message;
        while (ret.length() < length) {
            ret = " " + ret;
        }
        return ret;
    }

    public static void split(String message, int length) {
        if (message.length() <= length) {
            System.out.println(message);
            return;
        }
        System.out.println(message.substring(0, length));
        split(message.substring(length), length);
        return;
    }
}
