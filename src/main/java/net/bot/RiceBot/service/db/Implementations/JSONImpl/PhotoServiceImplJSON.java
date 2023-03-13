package net.bot.RiceBot.service.db.Implementations.JSONImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.service.db.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PhotoServiceImplJSON implements PhotoService {

    private final String PATH = "BOOT-INF\\classes\\data\\accounts.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dbDile = new File(PATH);

    @Override
    public List<Photo> getPhotos(String owner_username, Date date) {
        List<Photo> photoList = new ArrayList<>();
        try {
            photoList = List.of(mapper.readValue(dbDile, Photo[].class));
            photoList = photoList.stream().filter(p -> (Objects.equals(p.getOwner_username(), owner_username) && p.getDate().equals(date))).toList();
        } catch (IOException e){
            log.error(e.toString());
        }
        return photoList;
    }

    @Override
    public List<Photo> getPhotos(String owner_username, Date firstDate, Date secondDate) {
        List<Photo> photoList = new ArrayList<>();
        try {
            photoList = List.of(mapper.readValue(dbDile, Photo[].class));
            photoList = photoList.stream().filter(p ->
                    (Objects.equals(p.getOwner_username(), owner_username)
                            && (p.getDate().equals(firstDate) || p.getDate().after(firstDate))
                            && (p.getDate().equals(secondDate) || p.getDate().before(secondDate))))
                            .toList();
        } catch (IOException e){
            log.error(e.toString());
        }
        return photoList;
    }

    @Override
    public void addPhoto(Photo photo) {
        try {
            List<Photo> photoList = Arrays.stream((mapper.readValue(dbDile, Photo[].class))).collect(Collectors.toList());
            photoList.add(photo);
            mapper.writeValue(dbDile, photoList);
        } catch (IOException e){
            log.error(e.toString());
        }
    }
}
