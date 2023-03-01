package net.bot.RiceBot.service.db.Implementations;


import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.repository.UserRepository;
import net.bot.RiceBot.service.db.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplDB implements UserService {
    private final UserRepository repository;
    @Autowired
    public UserServiceImplDB(UserRepository repository) {
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

    @Override
    public void setRoleById(Long id, Role role) {
        repository.setRoleById(id, role);
    }

    @Override
    public boolean isFreeUsername(String username){
        return repository.isFreeUsername(username).size() == 0;
    }
    @Override
    public void setUsernameById(Long id, String username){
        repository.setUsernameById(id, username);
    }


    @Override
    public User getUserById(Long id) {
        if (repository.findById(id).isEmpty()){
            repository.saveAndFlush(new User(id));
        }
        return repository.findById(id).get();
    }
    @Override
    public void setPasswordById(Long id, String password){
        repository.setPasswordById(id, password);
    }
}
