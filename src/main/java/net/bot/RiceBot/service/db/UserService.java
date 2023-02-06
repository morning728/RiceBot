package net.bot.RiceBot.service.db;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;

public interface UserService {
    public User add(User user);
    public void deleteById(Long id);
    public void setStateById(Long id, State state);
    public boolean isRegistered(Long id);

}
