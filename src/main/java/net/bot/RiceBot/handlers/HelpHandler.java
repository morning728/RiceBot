package net.bot.RiceBot.handlers;


import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.model.User;
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
        switch (message.getText()) {
            case "/help" -> reply.setText(messageService.getMessage("standard.HELP_PHRASE"));
            case "/upload_file" -> reply = startProcess(message, State.ASK_FOR_UPLOAD);
            case "/uploading_photos_mode" -> reply = startProcess(message, State.PHOTO_UPLOAD_MODE);
            //case "/login" -> reply = startProcess(message, State.LOGIN);
            case "/my_data" -> {
                User user = userService.getUserById(message.getChatId());
                reply.setText("Login: " + user.getUsername() + "\n" + "Password: " + user.getPassword());
            }
            default -> reply.setText("Что-то на помогательском...");
        }

        return reply;
    }

//    private SendMessage askForUpload(Message message){
//        SendMessage reply = new SendMessage();
//        reply.setChatId(message.getChatId().toString());
//        reply.setText(messageService.getMessage(ASK_FOR_UPLOAD.getCode() + "." + ASK_FOR_UPLOAD));
//
//        userService.setStateById(message.getChatId(), ASK_FOR_UPLOAD);
//
//        return reply;
//    }
//
//    private SendMessage startUploadingPhotosMode(Message message){
//        SendMessage reply = new SendMessage();
//        reply.setChatId(message.getChatId().toString());
//        reply.setText(messageService.getMessage(PHOTO_UPLOAD_MODE.getCode() + "." + PHOTO_UPLOAD_MODE));
//
//        userService.setStateById(message.getChatId(), PHOTO_UPLOAD_MODE);
//
//        return reply;
//    }
    private SendMessage startProcess(Message message, State state){
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        reply.setText(messageService.getMessage(state.getCode() + "." + state.toString()));

        userService.setStateById(message.getChatId(), state);

        return reply;
    }
}
