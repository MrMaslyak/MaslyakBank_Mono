package MaslyakBank_Account.system;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class CardSystem {


    public static String generateCardNumber(String bin) {
        StringBuilder number = new StringBuilder(bin);
        number.append(generateNumber());
        int checkDigit = calculateLuhnDigit(number.toString());
        number.append(checkDigit);
        return number.toString();
    }

    public static Object generateNumber(){
        Random random = new Random();
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            char digit = (char) ('0' + random.nextInt(10));
            chars.add(digit);
        }

        Collections.shuffle(chars);

        StringBuilder result = new StringBuilder();
        for (char ch : chars) {
            result.append(ch);
        }

        return result.toString();
    }


    private static int calculateLuhnDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        boolean alternate = true;

        for (int i = numberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numberWithoutCheckDigit.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
