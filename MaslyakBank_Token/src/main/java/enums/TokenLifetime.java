package enums;

public enum TokenLifetime {


    REGISTRATION(1 * 60 * 1000), // 1 min
    AUTHENTICATION(5 * 60 * 60 * 1000), // 5 hours
    SUPER_ADMIN(60 * 60 * 1000);// 1 hours

    private final long millis;

    TokenLifetime(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }
}
