package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class NullStateHandler implements InputMessageHandler{
    private final ChangeDataHandler changeDataHandler;
    private final RegistrationHandler registrationHandler;
    private final PhotoRequestHandler photoRequestHandler;
    private final PhotoMiniHandler photoMiniHandler;
    private final HelpHandler helpHandler;

    private final Map<String, InputMessageHandler> connections = new HashMap<>();




    public NullStateHandler(ChangeDataHandler changeDataHandler, RegistrationHandler registrationHandler, PhotoRequestHandler photoRequestHandler, PhotoMiniHandler photoMiniHandler, HelpHandler helpHandler) {
        this.changeDataHandler = changeDataHandler;
        this.registrationHandler = registrationHandler;
        this.photoRequestHandler = photoRequestHandler;
        this.photoMiniHandler = photoMiniHandler;
        this.helpHandler = helpHandler;

        connections.put("/registration", registrationHandler);
        connections.put("/change_password", changeDataHandler);
        connections.put("/get_photos", photoRequestHandler);
        connections.put("/get_all_dates", photoMiniHandler);
        connections.put("/help", helpHandler);
        connections.put("/upload_file", helpHandler);
        connections.put("/uploading_photos_mode", helpHandler);
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
        if(connections.containsKey(splitedMessage.get(0)))
            reply = connections.get(splitedMessage.get(0)).handle(message);
        else
            reply.setText("nullHandlerDefault");
        return reply;
    }
}
