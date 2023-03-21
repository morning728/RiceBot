package net.bot.RiceBot.service.db.Implementations;

import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.repository.AccountRepository;
import net.bot.RiceBot.service.db.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImplDB implements AccountService {
    private final AccountRepository repository;

    @Autowired
    public AccountServiceImplDB(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isFreeUsername(String username) {

        return repository.isFreeUsername(username).size() == 0;
    }

    @Override
    public Account getAccountByLonAndPass(String username, String password) {
        if(repository.getAccountByLonAndPass(username, password).isEmpty()){
            return null;
        }
        else{
            return repository.getAccountByLonAndPass(username, password).get(0);
        }
    }

    @Override
    public void addAccount(Account account) {
        repository.saveAndFlush(account);
    }
}
