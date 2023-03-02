package net.bot.RiceBot.service.db.Implementations.JSONImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.service.db.AccountService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImplJSON implements AccountService {
    private final String PATH = "src/main/resources/data/accounts.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dbDile = new File(PATH);

    @Override
    public boolean isFreeUsername(String username) {
        List<Account> accountList = new ArrayList<>();
        try {
            accountList = List.of(mapper.readValue(dbDile, Account[].class));
        } catch (IOException e) {
            log.error(e.toString());
        }
        return accountList.stream().noneMatch(a -> Objects.equals(a.getUsername(), username));
    }

    @Override
    public Account getAccountByLonAndPass(String username, String password) {
        List<Account> accountList = new ArrayList<>();
        try {
            accountList = List.of(mapper.readValue(dbDile, Account[].class));
        } catch (IOException e) {
            log.error(e.toString());
        }
        if (accountList.stream().anyMatch(a -> Objects.equals(a.getUsername(), username)
                && Objects.equals(a.getPassword(), password))) {
            return accountList.stream().filter(a -> Objects.equals(a.getUsername(), username)
                            && Objects.equals(a.getPassword(), password))
                    .findAny()
                    .get();
        }
        return null;

    }

    @Override
    public void addAccount(Account account) {
        try {
            List<Account> accountList = Arrays.stream((mapper.readValue(dbDile, Account[].class))).collect(Collectors.toList());
            accountList.add(account);
            mapper.writeValue(dbDile, accountList);
        } catch (IOException e){
            log.error(e.toString());
        }
    }
}
