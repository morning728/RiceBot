package net.bot.RiceBot.service.db.Implementations;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.service.db.PhotoService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class PhotoServiceImplJSON implements PhotoService {

    private final String PATH = "src/main/resources/data/test.json";
    @Override
    public List<Photo> getPhotos(String owner_username, Date date) {
        List<Photo> photoList = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            photoList = List.of(mapper.readValue(new File(PATH), Photo[].class));
            photoList = photoList.stream().filter(p -> (Objects.equals(p.getOwner_username(), owner_username) && p.getDate().equals(date))).toList();
        } catch (IOException e){
            System.out.println("safd");
            log.error(e.toString());
        }
        return photoList;
    }

    @Override
    public List<Photo> getPhotos(String owner_username, Date firstDate, Date secondDate) {
        return null;
    }

    @Override
    public void addPhoto(Photo photo) {

    }
}
