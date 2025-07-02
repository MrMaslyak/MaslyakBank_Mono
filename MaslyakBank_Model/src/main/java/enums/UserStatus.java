package enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    REGISTERED("Только зарегистрирован"){
        @Override
        public UserStatus next() {
            return PARTIALLY_COMPLETED;
        }
    },
    PARTIALLY_COMPLETED("Создан только профиль или счёт"){
        @Override
        public UserStatus next() {
            return COMPLETED;
        }

    },
    COMPLETED("Профиль и счёт созданы"){
        @Override
        public UserStatus next() {
            return COMPLETED;
        }
    },
    BLOCKED("Заблокирован"){
        @Override
        public UserStatus next() {
            return DELETED;
        }
    },
    DELETED("Удалён"){
        @Override
        public UserStatus next() {
            return DELETED;
        }
    };

    private final String description;
    UserStatus(String description) {
        this.description = description;
    }

    public abstract UserStatus next();
}
