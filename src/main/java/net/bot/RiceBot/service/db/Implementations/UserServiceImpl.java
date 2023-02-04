package net.bot.RiceBot.service.db.Implementations;

import net.bot.RiceBot.model.User;
import net.bot.RiceBot.repository.UserRepository;
import net.bot.RiceBot.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Nullable
    public User getById(long id) {
        return repository.findById(id).isEmpty() ?  null :  repository.findById(id).get();
    }
}
