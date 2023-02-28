package net.bot.RiceBot.service.db;

import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {
    public boolean isFreeUsername(String username);

    public Account getAccountByLonAndPass(String username, String password);

    public void addAccount(Account account);


}
