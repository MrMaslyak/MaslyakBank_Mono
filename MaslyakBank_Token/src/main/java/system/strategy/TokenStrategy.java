package system.strategy;

public interface TokenStrategy<T> {
    String createToken(T principal);
}
