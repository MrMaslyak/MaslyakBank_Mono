package enums;

public enum TokenLifetime {

    REGISTRATION(15 * 60 * 1000), // 15 min
    AUTHENTICATION(5 * 60 * 60 * 1000); // 5 hours

    private final long millis;

    TokenLifetime(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }
}
