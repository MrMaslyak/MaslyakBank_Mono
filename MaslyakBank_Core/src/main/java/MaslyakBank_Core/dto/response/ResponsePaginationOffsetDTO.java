package MaslyakBank_Core.dto.response;


import entity.UsersTable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ResponsePaginationOffsetDTO {

    private int page;
    private int size;
    private int totalPage;
    private int totalElements;
    private List<UsersTable> data;

}
