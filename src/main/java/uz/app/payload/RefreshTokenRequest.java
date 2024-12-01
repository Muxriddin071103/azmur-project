package uz.app.payload;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
