package dto;

import lombok.Data;

public record TokenPair(String accessToken, String refreshToken) {
    @Override
    public String accessToken() {
        return accessToken;
    }

    @Override
    public String refreshToken() {
        return refreshToken;
    }
}
