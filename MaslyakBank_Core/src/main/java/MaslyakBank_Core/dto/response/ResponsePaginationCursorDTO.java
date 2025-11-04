package MaslyakBank_Core.dto.response;

import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponsePaginationCursorDTO {

    private int limit;
    private int totalElements;
    private List<UsersTable> data;
    private String cursor;

}
