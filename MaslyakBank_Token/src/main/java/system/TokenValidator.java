package system;

import entity.TokenTable;
import enums.TokenRole;
import enums.TokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidator {


    public void validate(TokenTable tokenTable) {

        if (tokenTable == null) {
           throw new RuntimeException("Token is not found");
        }

        if (tokenTable.getStatus() != TokenStatus.ACTIVE) {
            throw new RuntimeException("Token status is not active");
        }

        if (tokenTable.getRole() != TokenRole.REGISTRATION) {
            throw new RuntimeException("Token role is not valid");
        }
    }
}
