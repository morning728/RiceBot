package net.bot.RiceBot.handlers;

import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class NullStateHandler implements InputMessageHandler{

    private final LocaleMessageService messageService;
    private final ChangeDataHandler changeDataHandler;
    private final RegistrationHandler registrationHandler;

    public NullStateHandler(LocaleMessageService messageService, ChangeDataHandler changeDataHandler, RegistrationHandler registrationHandler) {
        this.messageService = messageService;
        this.changeDataHandler = changeDataHandler;
        this.registrationHandler = registrationHandler;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId());
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
            default:
                reply.setText("default1");
                break;
        }
        return reply;
    }
}
