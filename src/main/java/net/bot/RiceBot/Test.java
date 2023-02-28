package net.bot.RiceBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.service.PhotoHandler;
import net.bot.RiceBot.service.db.Implementations.PhotoServiceImplJSON;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.bot.RiceBot.service.PhotoHandler.parseDate;

public class Test {
    public static void main(String[] args) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//
//        List<Photo> jsonString = List.of(mapper.readValue(new File("src/main/resources/data/test.json"), Photo[].class));
//        Photo p  = new Photo(12L, "name", "owner", new Date(PhotoHandler.parseDate("12.12.2012").getTime()));
//        List<Photo> l = List.of(p, p, p);
//        mapper.writeValue(new File("src/main/resources/data/test.json"), l );
//        System.out.println(jsonString.get(0).getId());

        PhotoServiceImplJSON s = new PhotoServiceImplJSON();
        System.out.println(s.getPhotos("owner1", new Date(1255256000000L)));

    }
}
