package uz.ecobin.ecobin.mapper;

import org.springframework.stereotype.Service;
import uz.ecobin.ecobin.dto.UserDTO;
import uz.ecobin.ecobin.model.User;

@Service
public class UserMapper {

    public UserDTO toDto(User user){
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                null,
                user.getRole()
        );
    }
}
