package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.repository.AccountRepository;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RegLoginHandlerTest {
    @Autowired
    private RegLoginHandler handler;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private LocaleMessageService messageService;

    @Test
    public void handleLogin(){
        User usr = new User(12L);
        usr.setState(State.ASK_LOGIN);
        String msg = "testLogin";
        Mockito.doReturn(new ArrayList<Account>())
                .when(accountRepository)
                .isFreeUsername(msg);
        Mockito.doReturn("Логин зафиксировал, теперь пароль давай, екарный бабай")
                .when(messageService)
                .getMessage("registration.ASK_PASSWORD");
        String res = handler.handleLogin(msg, usr);

        Mockito.verify(userService, Mockito.times(1)).setStateById(usr.getId(), State.ASK_PASSWORD);
        Mockito.verify(userService, Mockito.times(1)).setUsernameById(usr.getId(), msg);

        Assert.assertEquals("Логин зафиксировал, теперь пароль давай, екарный бабай", res);

    }

}