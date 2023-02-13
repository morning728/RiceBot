package net.bot.RiceBot.handlers;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class NullStateHandler implements InputMessageHandler{

//TEMP
    private final BotConfig bot;

    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;
    private final ChangeDataHandler changeDataHandler;
    private final RegistrationHandler registrationHandler;
    private final PhotoRequestHandler photoRequestHandler;




    public NullStateHandler(BotConfig bot, LocaleMessageService messageService, UserServiceImpl userService, ChangeDataHandler changeDataHandler, RegistrationHandler registrationHandler, PhotoRequestHandler photoRequestHandler) {
        this.bot = bot;
        this.messageService = messageService;
        this.userService = userService;
        this.changeDataHandler = changeDataHandler;
        this.registrationHandler = registrationHandler;
        this.photoRequestHandler = photoRequestHandler;
    }

    @Override
    public SendMessage handle(Message message) throws IOException {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        if(message.hasDocument() || message.hasPhoto()){
            reply.setText("photos inside without needed state");
            return reply;
        }
        List<String> splitedMessage = Arrays.asList(message.getText().split(" "));
        switch (splitedMessage.get(0)) {
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
            case "/get_photos":
                reply = photoRequestHandler.handle(message);
                break;
            case "test": {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(message.getChatId().toString());
                photo.setPhoto(new InputFile(new File("src/main/resources/uploaded/m6FctBXC4CaLX4iZabXt1fLtjEzekdYSxg5ryWfJRJt0Ma4K.png")));
                DefaultAbsSender sender = new DefaultAbsSender(new DefaultBotOptions()) {
                    @Override
                    public String getBotToken() {
                        return bot.getBotToken();
                    }
                };
                try {
                    sender.execute(photo);
                } catch (Exception e) {
                }
                break;
            }
            default:
                reply.setText("nullHandlerDefault");
                break;
        }
        return reply;
    }



    private SendMessage askForUpload(Message message){
        SendMessage reply = new SendMessage();

        reply.setChatId(message.getChatId().toString());
        reply.setText(messageService.getMessage(State.ASK_FOR_UPLOAD.getCode() + "." + State.ASK_FOR_UPLOAD));

        userService.setStateById(message.getChatId(), State.ASK_FOR_UPLOAD);

        return reply;
    }
    private SendMessage startUploadingPhotosMode(Message message){
        SendMessage reply = new SendMessage();

        reply.setChatId(message.getChatId().toString());

        reply.setText(messageService.getMessage(State.PHOTO_UPLOAD_MODE.getCode() + "." + State.PHOTO_UPLOAD_MODE));

        userService.setStateById(message.getChatId(), State.PHOTO_UPLOAD_MODE);

        return reply;
    }
}
