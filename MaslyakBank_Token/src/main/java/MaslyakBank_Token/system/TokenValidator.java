package MaslyakBank_Token.system;

import MaslyakBank_Token.dto.ResponseDTO;
import MaslyakBank_Token.entity.TokenTable;
import MaslyakBank_Token.enums.TokenRole;
import MaslyakBank_Token.enums.TokenStatus;
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
