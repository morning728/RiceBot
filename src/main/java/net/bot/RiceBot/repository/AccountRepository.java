package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Transactional
    @Query("FROM Account a WHERE  a.username = :username")
    public List<Account> isFreeUsername(@Param("username")String username);
}
