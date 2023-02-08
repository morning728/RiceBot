package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class RegistrationHandler implements InputMessageHandler{
    private final UserServiceImpl userService;
    private final LocaleMessageService messageService;
    private final State FAIL_STATE = State.FAIL_REG;

    public RegistrationHandler(UserServiceImpl userService, LocaleMessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        User user = userService.getUserById(message.getChatId());
        reply.setChatId(user.getId());
        if (user.getState() == null) {
            reply.setText(startRegistration(user));
        }
        else {
            switch (user.getState()) {
                case ASK_LOGIN:
                    reply.setText(handleLogin(message.getText(), user));
                    break;
                case ASK_PASSWORD:
                    reply.setText(handlePassword(message.getText(), user));
                    break;
                default:
                    reply.setText("default");
                    break;
            }
        }


        return reply;
    }

    private String handleLogin(String message, User user){
        if(userService.isFreeUsername(message)){
            userService.setUsernameById(user.getId(), message);
            userService.setStateById(user.getId(), user.getState().next());
            return messageService.getMessage("registration." + user.getState().next().toString());
        }
        else{
            userService.setStateById(user.getId(), FAIL_STATE.next());
            return messageService.getMessage("registration." + FAIL_STATE.toString());
        }
    }
    private String handlePassword(String message, User user){
            userService.setPasswordById(user.getId(), message);
            userService.setStateById(user.getId(), null);
            return messageService.getMessage("registration." + user.getState().next().toString());
    }
    private String startRegistration(User user){
        userService.setStateById(user.getId(), State.ASK_LOGIN);
        return messageService.getMessage("registration." + State.ASK_LOGIN);
    }

}