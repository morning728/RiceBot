package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.repository.PhotoRepository;
import net.bot.RiceBot.service.PhotoHandler;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import net.bot.RiceBot.service.messages.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class PhotoRequestHandler implements InputMessageHandler{

    private final UserServiceImpl userService;
    //TEMP
    private final BotConfig bot;
    private final PhotoRepository photoRepository;
    private final LocaleMessageService messageService;
    private final DefaultAbsSender sender = new DefaultAbsSender(new DefaultBotOptions()) {
        @Override
        public String getBotToken() {
            return bot.getBotToken();
        }
    };

    public PhotoRequestHandler(UserServiceImpl userService, BotConfig bot, PhotoRepository photoRepository, LocaleMessageService messageService) {

        this.userService = userService;
        this.bot = bot;
        this.photoRepository = photoRepository;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        List<String> splitedMessage = Arrays.asList(message.getText().split(" "));
        if(splitedMessage.size() != 2){
            reply.setText("Invalid request (Ошибка с датой, скорее всего, дундук)");
        }
        else{
            SendMediaGroup photoMessage = new SendMediaGroup();
            photoMessage.setChatId(message.getChatId().toString());
            List<InputMedia> medias;
            if(!splitedMessage.get(1).contains("-")){
                medias = getMediasByDate(message);
            } else {
                medias = getMediasByRange(message);
            }

            try{
                sendPhotos(medias, message);
            } catch (Exception e){
                reply.setText("Какая-то ошибка, терпи");
            }
        }
        return reply;
    }

    private List<InputMedia> getMediasByDate(Message message) {
        int fileNum = 0;
        Date date = PhotoHandler.parseDate(Arrays.asList(message.getText().split(" ")).get(1));
        List<Photo> photos = photoRepository.getPhotos(userService.getUserById(message.getChatId()).getUsername(), date);
        //SendPhoto photoMessage = new SendPhoto();
        List<InputMedia> mediaPhotos = new ArrayList<>();
        for (Photo photo: photos
             ) {
            InputMedia singlePhoto = new InputMediaPhoto();
            singlePhoto.setMedia(new File("src/main/resources/uploaded/" + photo.getName()), "file" + fileNum++);
            mediaPhotos.add(singlePhoto);
        }
        return mediaPhotos;

    }
    private List<InputMedia> getMediasByRange(Message message) {
        int fileNum = 0;
        Date firstDate = PhotoHandler.parseDate(Arrays.asList(message.getText().split(" ")).get(1).split("-")[0]);
        Date secondDate = PhotoHandler.parseDate(Arrays.asList(message.getText().split(" ")).get(1).split("-")[1]);
        List<Photo> photos = photoRepository.getPhotos(userService.getUserById(message.getChatId()).getUsername(), firstDate, secondDate);
        List<InputMedia> mediaPhotos = new ArrayList<>();
        for (Photo photo: photos
        ) {
            InputMedia singlePhoto = new InputMediaPhoto();
            singlePhoto.setMedia(new File("src/main/resources/uploaded/" + photo.getName()), "file" + fileNum++);
            mediaPhotos.add(singlePhoto);
        }
        return mediaPhotos;
    }

    private void sendPhotos(List<InputMedia> medias, Message message) throws TelegramApiException, IOException {
        if (medias.size() == 0) {
            sender.execute(new SendMessage(message.getChatId().toString(), "Ни одного фото найдено не было"));
        } else if (medias.size() == 1) {
            sender.execute(new SendPhoto(message.getChatId().toString(), new InputFile(medias.get(0).getNewMediaFile())));
        } else if (medias.size() <= 10) {
            sender.execute(new SendMediaGroup(message.getChatId().toString(), medias));
        } else if (medias.size() == 11) {
            sender.execute(new SendMediaGroup(message.getChatId().toString(), medias.subList(0, 9)));
            sender.execute(new SendPhoto(message.getChatId().toString(), new InputFile(medias.get(10).getNewMediaFile())));
        } else if (medias.size() <= 20) {
            sender.execute(new SendMediaGroup(message.getChatId().toString(), medias.subList(0, 10)));
            sender.execute(new SendMediaGroup(message.getChatId().toString(), medias.subList(10, medias.size())));
        } else {
            String name = PhotoHandler.getRandomName("rand.zip");
            int i = 0;
            try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("src/main/resources/" + name));)
            {
                for (InputMedia media: medias
                ) {
                    FileInputStream fis= new FileInputStream(media.getNewMediaFile());
                    ZipEntry entry = new ZipEntry("photo" + i++ + ".png");
                    zout.putNextEntry(entry);
                    // считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    // добавляем содержимое к архиву
                    zout.write(buffer);
                    // закрываем текущую запись для новой записи
                    zout.closeEntry();
                }
            }
            sender.execute(new SendMessage(message.getChatId().toString(), "Фото слишком много (>20), так что скину архивом >w<"));
            sender.execute(new SendDocument(message.getChatId().toString(), new InputFile(new File("src/main/resources/" + name))));
            File deletingFile = new File("src/main/resources/" + name);
            deletingFile.delete();
        }
    }
}
