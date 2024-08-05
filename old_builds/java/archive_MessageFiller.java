public class archive_MessageFiller {
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
}
