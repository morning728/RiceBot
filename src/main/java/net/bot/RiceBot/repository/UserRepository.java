package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("update User u set u.state = :state where u.id = :id")
    public void setStateById(@Param("id")Long id, @Param("state")State state);

    @Modifying
    @Transactional
    @Query("update User u set u.role = :role where u.id = :id")
    public void setRoleById(@Param("id")Long id, @Param("role") Role role);

    @Modifying
    @Transactional
    @Query("FROM User u WHERE  u.username = :username")
    public List<User> isFreeUsername(@Param("username")String username);

    @Modifying
    @Transactional
    @Query("update User u set u.username = :username where u.id = :id")
    public void setUsernameById(@Param("id")Long id, @Param("username")String username);
    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.id = :id")
    public void setPasswordById(@Param("id")Long id, @Param("password")String password);
}
