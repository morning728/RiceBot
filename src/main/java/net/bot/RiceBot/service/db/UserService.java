package net.bot.RiceBot.service.db;

import net.bot.RiceBot.model.User;
import org.springframework.lang.Nullable;

public interface UserService {
    @Nullable
    public User getById(long id);
}
