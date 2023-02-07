package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@Component
public class ChangeDataHandler implements InputMessageHandler{

    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;

    public ChangeDataHandler(LocaleMessageService messageService, UserServiceImpl userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        User user = userService.getUserById(message.getChatId());
        reply.setChatId(user.getId());
        if (user.getState() == null) {
            reply.setText(startChanging(message));
        }
        else {
            switch (user.getState()) {
                case ASK_OLD_PWD:
                    reply.setText(handleOldPwd(message));
                    break;
                case ASK_NEW_PWD:
                    reply.setText(handleNewPwd(message));
                    break;
                default:
                    reply.setText("default1");
                    break;
            }
        }
        return reply;
    }

    private String startChanging(Message message){
        userService.setStateById(message.getChatId(), State.ASK_OLD_PWD);
        return messageService.getMessage("changingData." + State.ASK_OLD_PWD);
    }
    private String handleOldPwd(Message message){
        if(Objects.equals(userService.getUserById(message.getChatId()).getPassword(), message.getText())){
            userService.setStateById(message.getChatId(), State.ASK_NEW_PWD);
            return messageService.getMessage("changingData." + State.ASK_NEW_PWD);
        }
        else{
            userService.setStateById(message.getChatId(),State.FAIL_CHANGES.next());
            return messageService.getMessage("changingData." + State.FAIL_CHANGES);
        }
    }
    private String handleNewPwd(Message message){
        userService.setPasswordById(message.getChatId(),message.getText());
        userService.setStateById(message.getChatId(), null);
        return messageService.getMessage("changingData." + State.SUCCESS_CHANGES);
    }

}
