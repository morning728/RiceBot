package net.bot.RiceBot.service.db.Implementations.JSONImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.service.db.UserService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//@Component
@Slf4j
public class UserServiceImplJSON implements UserService {
    private final String PATH = "src/main/resources/data/users.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dbDile = new File(PATH);
    @Override
    public User add(User user) {
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            usersList.add(user);
            mapper.writeValue(dbDile, usersList);
        } catch (IOException e){
            log.error(e.toString());
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if(usersList.stream().anyMatch(user -> Objects.equals(user.getId(), id))){
                usersList.remove(
                        usersList.
                                stream().
                                filter(user -> Objects.equals(user.getId(), id)).
                                findFirst().
                                get());
                mapper.writeValue(dbDile, usersList);
            }
        } catch (IOException e){
            log.error(e.toString());
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if (usersList.stream().noneMatch(usr -> Objects.equals(usr.getId(), id))) {
                add(new User(id));
                user = new User(id);
            }
            else{
                user = usersList.
                        stream().
                        filter(usr -> Objects.equals(usr.getId(), id)).
                        findFirst().
                        get();
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
        return user;
    }


    @Override
    public void setStateById(Long id, State state) {
        User user;
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if (usersList.stream().anyMatch(usr -> Objects.equals(usr.getId(), id))) {
                user = usersList.
                        stream().
                        filter(usr -> Objects.equals(usr.getId(), id)).
                        findFirst().
                        get();
                usersList.remove(user);
                user.setState(state);
                usersList.add(user);
                mapper.writeValue(dbDile, usersList);
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    @Override
    public void setRoleById(Long id, Role role) {
        User user;
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if (usersList.stream().anyMatch(usr -> Objects.equals(usr.getId(), id))) {
                user = usersList.
                        stream().
                        filter(usr -> Objects.equals(usr.getId(), id)).
                        findFirst().
                        get();
                usersList.remove(user);
                user.setRole(role);
                usersList.add(user);
                mapper.writeValue(dbDile, usersList);
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    @Override
    public boolean isRegistered(Long id) {

        List<User> usersList;
        try {
            usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return usersList.stream().anyMatch(usr -> Objects.equals(usr.getId(), id));
    }

    @Override
    public boolean isFreeUsername(String username) {
        List<User> usersList;
        try {
            usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return usersList.stream().noneMatch(usr -> Objects.equals(usr.getUsername(), username));
    }

    @Override
    public void setUsernameById(Long id, String username) {
        User user;
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if (usersList.stream().anyMatch(usr -> Objects.equals(usr.getId(), id))) {
                user = usersList.
                        stream().
                        filter(usr -> Objects.equals(usr.getId(), id)).
                        findFirst().
                        get();
                usersList.remove(user);
                user.setUsername(username);
                usersList.add(user);
                mapper.writeValue(dbDile, usersList);
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    @Override
    public void setPasswordById(Long id, String password) {
        User user;
        try {
            List<User> usersList = Arrays.stream((mapper.readValue(dbDile, User[].class))).collect(Collectors.toList());
            if (usersList.stream().anyMatch(usr -> Objects.equals(usr.getId(), id))) {
                user = usersList.
                        stream().
                        filter(usr -> Objects.equals(usr.getId(), id)).
                        findFirst().
                        get();
                usersList.remove(user);
                user.setPassword(password);
                usersList.add(user);
                mapper.writeValue(dbDile, usersList);
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}
