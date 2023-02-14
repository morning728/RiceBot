package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpHandler implements InputMessageHandler{
    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;

    public HelpHandler(LocaleMessageService messageService, UserServiceImpl userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        switch(message.getText()){
            case "/help":
                reply.setText(messageService.getMessage("standard.HELP_PHRASE"));
                break;
            case "/upload_file":
                reply = askForUpload(message);
                break;
            case "/uploading_photos_mode":
                reply = startUploadingPhotosMode(message);
                break;
            default:
                reply.setText("Что-то на помогательском...");
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
