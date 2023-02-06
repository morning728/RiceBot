package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("update User u set u.state = :state where u.id = :id")
    public void setStateById(@Param("id")Long id, @Param("state")State state);
}
