package MaslyakBank_Account.system.account;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iban4j.*;
import org.springframework.stereotype.Component;



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

    private String generateNumber(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 19; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
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
