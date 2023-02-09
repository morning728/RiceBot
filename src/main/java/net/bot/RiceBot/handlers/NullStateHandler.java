package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.repository.PhotoRepository;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.net.URL;
import java.sql.Date;

@Slf4j
@Component
public class NullStateHandler implements InputMessageHandler{



    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;
    private final ChangeDataHandler changeDataHandler;
    private final RegistrationHandler registrationHandler;



    public NullStateHandler(LocaleMessageService messageService, UserServiceImpl userService, ChangeDataHandler changeDataHandler, RegistrationHandler registrationHandler) {
        this.messageService = messageService;
        this.userService = userService;
        this.changeDataHandler = changeDataHandler;
        this.registrationHandler = registrationHandler;
    }

    @Override
    public SendMessage handle(Message message) throws IOException {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId());
        if(message.hasDocument() || message.hasPhoto()){
            reply.setText("photos inside without needed state");
            return reply;
        }
        switch (message.getText()) {
            case "/help":
                reply.setText(messageService.getMessage("standard.HELP_PHRASE"));
                break;
            case "/registration":
                reply = registrationHandler.handle(message);
                break;
            case "/change_password":
                reply = changeDataHandler.handle(message);
                break;
            case "/upload_file":
                reply = askForUpload(message);
                break;
            case "/uploading_photos_mode":
                reply = startUploadingPhotosMode(message);
                break;
            default:
                reply.setText("nullHandlerDefault");
                break;
        }
        return reply;
    }



    private SendMessage askForUpload(Message message){
        SendMessage reply = new SendMessage();

        reply.setChatId(message.getChatId());
        reply.setText(messageService.getMessage(State.ASK_FOR_UPLOAD.getCode() + "." + State.ASK_FOR_UPLOAD));

        userService.setStateById(message.getChatId(), State.ASK_FOR_UPLOAD);

        return reply;
    }
    private SendMessage startUploadingPhotosMode(Message message){
        SendMessage reply = new SendMessage();

        reply.setChatId(message.getChatId());

        reply.setText(messageService.getMessage(State.PHOTO_UPLOAD_MODE.getCode() + "." + State.PHOTO_UPLOAD_MODE));

        userService.setStateById(message.getChatId(), State.PHOTO_UPLOAD_MODE);

        return reply;
    }
}
