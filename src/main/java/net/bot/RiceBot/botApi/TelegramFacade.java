package net.bot.RiceBot.botApi;

import lombok.extern.slf4j.Slf4j;

import net.bot.RiceBot.handlers.ChangeDataHandler;
import net.bot.RiceBot.handlers.NullStateHandler;
import net.bot.RiceBot.handlers.RegistrationHandler;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

    private final RegistrationHandler registrationHandler;
    private final NullStateHandler nullStateHandler;
    private final ChangeDataHandler changeDataHandler;
    private final UserServiceImpl userService;
    @Autowired
    public TelegramFacade(RegistrationHandler registrationHandler, NullStateHandler nullStateHandler, ChangeDataHandler changeDataHandler, UserServiceImpl userService) {
        this.registrationHandler = registrationHandler;
        this.nullStateHandler = nullStateHandler;
        this.changeDataHandler = changeDataHandler;
        this.userService = userService;
    }


    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        SendMessage replyMessage = new SendMessage();
        if(userService.getUserById(message.getChatId()).getState() == null) {
            replyMessage = nullStateHandler.handle(message);
        }
        else{
            switch (userService.getUserById(message.getChatId()).getState().getCode()){
                case "registration":
                    replyMessage = registrationHandler.handle(message);
                    break;
                case "changingData":
                    replyMessage = changeDataHandler.handle(message);
                    break;
                default:
                    log.info("pizdec");
                    break;
            }
        }



        return replyMessage;
    }


}
