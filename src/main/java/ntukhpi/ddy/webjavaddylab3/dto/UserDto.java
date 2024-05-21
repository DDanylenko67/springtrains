package ntukhpi.ddy.webjavaddylab3.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String username;
    @NotEmpty(message = "Почта не повинена бути пустою")
    @Email
    private String email;
    @NotEmpty(message = "Пароль не повинен бути пустим")
    private String password;
}