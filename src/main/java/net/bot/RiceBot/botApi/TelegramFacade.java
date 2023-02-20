package net.bot.RiceBot.botApi;

import lombok.extern.slf4j.Slf4j;

import net.bot.RiceBot.handlers.ChangeDataHandler;
import net.bot.RiceBot.handlers.NullStateHandler;
import net.bot.RiceBot.handlers.RegLoginHandler;
import net.bot.RiceBot.handlers.UploadingPhotosModeHandler;
import net.bot.RiceBot.model.Enums.Role;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.util.Objects;

@Component
@Slf4j
public class TelegramFacade {

    private final RegLoginHandler regLoginHandler;
    private final NullStateHandler nullStateHandler;
    private final ChangeDataHandler changeDataHandler;
    private final UserServiceImpl userService;
    private final UploadingPhotosModeHandler uploadingPhotosModeHandler;

    @Autowired
    public TelegramFacade(RegLoginHandler regLoginHandler, NullStateHandler nullStateHandler, ChangeDataHandler changeDataHandler, UserServiceImpl userService, UploadingPhotosModeHandler uploadingPhotosModeHandler) {
        this.regLoginHandler = regLoginHandler;
        this.nullStateHandler = nullStateHandler;
        this.changeDataHandler = changeDataHandler;
        this.userService = userService;
        this.uploadingPhotosModeHandler = uploadingPhotosModeHandler;
    }


    public SendMessage handleUpdate(Update update) throws IOException {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        log.info(String.valueOf(message));
        if (message != null) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        else{
            log.info("NULL MSG");
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) throws IOException { // ASK FOR UPLOAD
        SendMessage replyMessage = new SendMessage();
        if(Objects.equals(message.getText(), "/stop")){
            replyMessage = resetState(message);
        }
        else if(Objects.equals(message.getText(), "/registration") ||
                Objects.equals(message.getText(), "/login") ||
                Objects.equals(userService.getUserById(message.getChatId()).getState().getCode(), "registration") ||
                Objects.equals(userService.getUserById(message.getChatId()).getState().getCode(), "login")){
            replyMessage = regLoginHandler.handle(message);
        }
        else if(userService.getUserById(message.getChatId()).getRole() == Role.UNREGISTERED){
            return new SendMessage(message.getChatId().toString(), "Зарегайся, чтоб пользоваться ботом(/registration)");
        }
        else if(userService.getUserById(message.getChatId()).getState() == State.NULL) {
            replyMessage = nullStateHandler.handle(message);
        }
        else{
            switch (userService.getUserById(message.getChatId()).getState().getCode()) {
                case "registration", "login" -> replyMessage = regLoginHandler.handle(message);
                case "changingData" -> replyMessage = changeDataHandler.handle(message);
                case "photoUploadMode" -> replyMessage = uploadingPhotosModeHandler.handle(message);
                default -> replyMessage.setText("default facade");
            }
        }
        return replyMessage;
    }

    private SendMessage resetState(Message message){
        userService.getUserById(message.getChatId());
        userService.setStateById(message.getChatId(), State.NULL);
        return new SendMessage(message.getChatId().toString(), "Состояние было сброшено");
    }


}
































