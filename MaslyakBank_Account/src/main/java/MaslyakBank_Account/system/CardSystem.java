package MaslyakBank_Account.system;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CardSystem {


    public static String generateCardNumber(String bin) {
        StringBuilder number = new StringBuilder(bin);
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            number.append(random.nextInt(10));
        }
        int checkDigit = calculateLuhnDigit(number.toString());
        number.append(checkDigit);
        return number.toString();
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
