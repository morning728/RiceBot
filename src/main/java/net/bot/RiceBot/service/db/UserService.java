package net.bot.RiceBot.service.db;

import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {
    public User add(User user);
    public void deleteById(Long id);
    public User getUserById(Long id);

    public void setStateById(Long id, State state);
    public void setRoleById(Long id, Role role);
    public boolean isRegistered(Long id);

    public boolean isFreeUsername(String username);
    public void setUsernameById(Long id, String username);
    public void setPasswordById(Long id, String password);


}
