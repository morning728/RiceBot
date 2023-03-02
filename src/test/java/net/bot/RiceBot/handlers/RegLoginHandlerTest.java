package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;

import net.bot.RiceBot.service.db.AccountService;
import net.bot.RiceBot.service.db.UserService;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
class RegLoginHandlerTest {
    @Autowired
    private RegLoginHandler handler;
    @MockBean
    private AccountService accountRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private LocaleMessageService messageService;

    @Test
    public void handleLogin(){
        User usr = new User(12L);
        usr.setState(State.ASK_LOGIN);
        String msg = "testLogin";
        Mockito.doReturn(true)
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

    @Test
    public void handle() {
        Message message = new Message();
        message.setText("/registration");
        message.setChat(new Chat(1L, "type"));

        Mockito.doReturn(new User(1L))
                .when(userService)
                .getUserById(message.getChatId());
        Mockito.doReturn("Введи логин, ек макарек")
                .when(messageService)
                .getMessage("registration.ASK_LOGIN");

        SendMessage res = handler.handle(message);

        Assert.assertEquals("Введи логин, ек макарек", res.getText());
        Assert.assertEquals("1", res.getChatId());

    }
}