package MaslyakBank_Core.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TokenRequestDTO {

    private UUID userId;
}
