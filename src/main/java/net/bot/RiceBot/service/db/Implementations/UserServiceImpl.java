package net.bot.RiceBot.service.db.Implementations;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.repository.UserRepository;
import net.bot.RiceBot.service.db.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User add(User user) {
        if(!repository.existsById(user.getId())){
            return repository.saveAndFlush(user);
        } else{
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isRegistered(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void setStateById(Long id, State state) {
        repository.setStateById(id, state);
    }
}
