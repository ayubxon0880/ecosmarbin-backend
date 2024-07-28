package uz.ecobin.ecobin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecobin.ecobin.annotations.ValidPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordResponse {
    private String key;
    private String email;

    @ValidPassword
    private String password;
    @ValidPassword
    private String newPassword;
}
