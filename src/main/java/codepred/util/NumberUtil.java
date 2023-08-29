package codepred.util;

import java.util.Random;

public class NumberUtil {

    public static String getRandomNumberString() {
        // It will generate 5 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99999);

        // this will convert any number sequence into 5 character.
        return String.format("%05d", number);
    }

}
