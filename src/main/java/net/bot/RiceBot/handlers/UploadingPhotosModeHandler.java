package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.repository.PhotoRepository;
import net.bot.RiceBot.service.PhotoHandler;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class UploadingPhotosModeHandler implements InputMessageHandler {
    //TEEEEEEMP
    private final PhotoRepository photoRepository;
    private final LocaleMessageService messageService;
    private final UserServiceImpl userService;
    private final BotConfig bot;

    private Date date = null;

    @Autowired
    public UploadingPhotosModeHandler(PhotoRepository photoRepository, LocaleMessageService messageService, UserServiceImpl userService, BotConfig bot) {
        this.photoRepository = photoRepository;
        this.messageService = messageService;
        this.userService = userService;
        this.bot = bot;
    }


    @Override
    public SendMessage handle(Message message) throws IOException {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        if(message.hasDocument()){
            try {
                String fileName = uploadFile(message.getDocument().getFileId());
                photoRepository.saveAndFlush(new Photo(fileName, userService.getUserById(message.getChatId()).getUsername(), date));
            }
            catch(Exception e){
                log.info(e.toString());
            }

            //reply.setText("uploaded Photo");
            return reply;
        }
        else if(message.hasText()){
            List<String> splitedMessage = Arrays.asList(message.getText().split(" "));
            switch(splitedMessage.get(0)){
                case "/stop":
                    reply = stopPhotoUploadMode(message);
                    break;
                case "/set_date":
                    try {
                        date = PhotoHandler.parseDate(splitedMessage.get(1));
                        reply.setText("Дата переустановлена на " + date + " (yyyy.mm.dd)");
                    }
                    catch (Exception e){
                        reply.setText("ЧЕт ты накосячил с форматом, видимо");
                    }
                    break;
            }
        }
        else{
            reply.setText("msg without text n docs (mb u compressed ur photos kozel?)");
        }
        return reply;
    }


    private SendMessage stopPhotoUploadMode(Message message){
        SendMessage reply = new SendMessage(message.getChatId().toString(), "Состояние переведено в нулевое, режим загрузки фото отключен!");
        userService.setStateById(message.getChatId(), null);
        date = null;
        return reply;
    }

    private String uploadFile(String fileId) throws IOException {
        String fileResponse;

        URL url = new URL("https://api.telegram.org/bot" + bot.getBotToken() + "/getFile?file_id=" + fileId);

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            fileResponse = reader.readLine();
        }

        JSONObject jresult = new JSONObject(fileResponse);
        String filePath =jresult.getJSONObject("result").getString("file_path");
        String fileName = PhotoHandler.getRandomName(filePath);

        File localFile = new File("src/main/resources/uploaded/" + fileName);
        try(InputStream is = new URL("https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + filePath).openStream()){
            FileUtils.copyInputStreamToFile(is, localFile);
        }
        return fileName;
    }
}
