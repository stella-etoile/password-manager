public class RandomPassword {
    public static String generatePassword(int length) {
        String ret = "";
        for (int i = 0; i < length; i++) {
            ret += (char) (Math.random()*94+33);
        }
        return ret;
    }
}
