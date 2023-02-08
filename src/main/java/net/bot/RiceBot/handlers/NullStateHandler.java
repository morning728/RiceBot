package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Enums.State;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.net.URL;

@Slf4j
@Component
public class NullStateHandler implements InputMessageHandler{

    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;
    private final ChangeDataHandler changeDataHandler;
    private final RegistrationHandler registrationHandler;

    private final BotConfig bot;

    public NullStateHandler(LocaleMessageService messageService, UserServiceImpl userService, ChangeDataHandler changeDataHandler, RegistrationHandler registrationHandler, BotConfig bot) {
        this.messageService = messageService;
        this.userService = userService;
        this.changeDataHandler = changeDataHandler;
        this.registrationHandler = registrationHandler;
        this.bot = bot;
    }

    @Override
    public SendMessage handle(Message message) throws IOException {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId());
        if(message.getDocument() != null){
            reply.setText("uploaded");
            uploadFile(message.getDocument().getFileName(), message.getDocument().getFileId());
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
            default:
                reply.setText("nullHandlerDefault");
                break;
        }
        return reply;
    }

    private void uploadFile(String fileName, String fileId) throws IOException {
        String fileResponse;

        URL url = new URL("https://api.telegram.org/bot" + bot.getBotToken() + "/getFile?file_id=" + fileId);

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            fileResponse = reader.readLine();
        }

        JSONObject jresult = new JSONObject(fileResponse);
        String filePath =jresult.getJSONObject("result").getString("file_path");

        File localFile = new File("src/main/resources/uploaded/" + fileName);
        try(InputStream is = new URL("https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + filePath).openStream()){
            FileUtils.copyInputStreamToFile(is, localFile);
        }
    }

    private SendMessage askForUpload(Message message){
        SendMessage reply = new SendMessage();

        reply.setChatId(message.getChatId());
        reply.setText(messageService.getMessage(State.ASK_FOR_UPLOAD.getCode() + "." + State.ASK_FOR_UPLOAD));

        userService.setStateById(message.getChatId(), State.ASK_FOR_UPLOAD);

        return reply;
    }
}
