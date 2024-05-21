package ntukhpi.ddy.webjavaddylab3.service;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.webjavaddylab3.dto.UserDto;
import ntukhpi.ddy.webjavaddylab3.entity.User;


import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}