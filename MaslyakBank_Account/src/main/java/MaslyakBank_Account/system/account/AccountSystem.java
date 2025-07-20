package MaslyakBank_Account.system.account;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class AccountSystem {

    private static final Logger log = LogManager.getLogger(AccountSystem.class);
    private final String CODE_BANK = "120608";

    public String generateIBAN() {
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.UA)
                .bankCode(CODE_BANK)
                .accountNumber(generateNumber())
                .build();

        if (isValid(iban.toString())) {
            log.info("✅ IBAN успешно сгенерирован и прошёл валидацию: {}", iban.toFormattedString());
        }

        return iban.toFormattedString();
    }

    public static String generateNumber(){
        StringBuilder sb = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"));
        for (int i = 0; i < timestamp.length(); i++) {
            int num = Integer.parseInt(timestamp.substring(i, i + 1));
            if (num % 2 == 0) {
                sb.append(num + 1);
            } else {
                sb.append(num);
            }
        }
        sb.append(timestamp);

        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < sb.length(); i++) {
            chars.add(sb.charAt(i));
        }
        Collections.shuffle(chars);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 19; i++) {
            result.append(chars.get(i));
        }

        return result.toString();
    }

    public static boolean isValid(String ibanString) {
        try {
            Iban.valueOf(ibanString);
            return true;
        } catch (IbanFormatException | IllegalArgumentException e) {
            log.error("❌ Неверный формат IBAN: {}", ibanString);
            return false;
        }
    }



}
