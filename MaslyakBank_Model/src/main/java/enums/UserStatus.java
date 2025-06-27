package enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    REGISTERED("Только зарегистрирован"),
    PARTIALLY_COMPLETED("Создан только профиль или счёт"),
    COMPLETED("Профиль и счёт созданы"),
    BLOCKED("Заблокирован"),
    DELETED("Удалён");

    private final String description;
    UserStatus(String description) {
        this.description = description;
    }

}
