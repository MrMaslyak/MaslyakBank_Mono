package MaslyakBank_Core.system;

import MaslyakBank_Core.dto.requests.UserFilterDTO;
import entity.UsersTable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;


public class UserSpecifications {

    public static Specification<UsersTable> buildFromFilter(UserFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getRole() != null && !filter.getRole().isEmpty()) {
                predicates.add(cb.equal(root.get("role"), filter.getRole()));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
