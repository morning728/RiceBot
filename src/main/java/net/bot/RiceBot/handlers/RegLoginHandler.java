package net.bot.RiceBot.handlers;


import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.service.db.Implementations.AccountServiceImplDB;
import net.bot.RiceBot.service.db.Implementations.UserServiceImplDB;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class RegLoginHandler implements InputMessageHandler {

    //TEMP
    private final AccountServiceImplDB accountService;
    private final UserServiceImplDB userService;
    private final LocaleMessageService messageService;
    private final State FAIL_STATE = State.FAIL_REG;

    public RegLoginHandler(AccountServiceImplDB accountService, UserServiceImplDB userService, LocaleMessageService messageService) {
        this.accountService = accountService;
        this.userService = userService;
        this.messageService = messageService;
    }


    @Override
    public SendMessage handle(Message message){
        SendMessage reply = new SendMessage();
        User user = userService.getUserById(message.getChatId());
        reply.setChatId(user.getId().toString());
        if (user.getState() == State.NULL) {
            switch(message.getText()){
                case "/registration" -> reply = startProcess(message, State.ASK_LOGIN);
                case "/login" -> reply = startProcess(message, State.LOGIN);
            }
        }
        else{
            switch (user.getState()) {
                case ASK_LOGIN -> reply.setText(handleLogin(message.getText(), user));
                case ASK_PASSWORD -> reply.setText(handlePassword(message.getText(), user));
                case LOGIN -> {
                    reply.setText(login(message));
                    userService.setStateById(message.getChatId(), State.NULL);
                }
                default -> reply.setText("Default Login/Registration Handler");
            }
        }


        return reply;
    }
    SendMessage startProcess(Message message, State state){
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        reply.setText(messageService.getMessage(state.getCode() + "." + state.toString()));

        userService.setStateById(message.getChatId(), state);

        return reply;
    }


    String handleLogin(String message, User user) {
        if (accountService.isFreeUsername(message)) {
            userService.setUsernameById(user.getId(), message);
            userService.setStateById(user.getId(), user.getState().next());
            return messageService.getMessage("registration." + user.getState().next().toString());
        } else {
            userService.setStateById(user.getId(), FAIL_STATE.next());
            return messageService.getMessage("registration." + FAIL_STATE.toString());
        }
    }

    String handlePassword(String message, User user) {
        userService.setPasswordById(user.getId(), message);
        userService.setStateById(user.getId(), State.NULL);
        userService.setRoleById(user.getId(), Role.USER);
        User added_account = userService.getUserById(user.getId());
        accountService.addAccount(new Account(added_account.getUsername(), message, Role.USER));
        return messageService.getMessage("registration." + "SUCCESS_REG");
    }


    String login(Message message)  {
        List<String> data = Arrays.asList(message.getText().split(" "));
        User user = userService.getUserById(message.getChatId());
        if (data.size() != 2) {
            return messageService.getMessage("login.FAIL");
        }

        Account account = accountService.getAccountByLonAndPass(data.get(0), data.get(1));
        if (account != null) {
            userService.setUsernameById(message.getChatId(), account.getUsername());
            userService.setPasswordById(message.getChatId(), account.getPassword());
            userService.setRoleById(message.getChatId(), account.getRole());
            return messageService.getMessage("login.SUCCESS");
        }

        return messageService.getMessage("login.FAIL");
    }

}
