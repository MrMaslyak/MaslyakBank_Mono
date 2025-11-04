package MaslyakBank_Core.dao;

import entity.UsersTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserFilterRepository extends JpaRepository<UsersTable, UUID>, JpaSpecificationExecutor<UsersTable> {}

