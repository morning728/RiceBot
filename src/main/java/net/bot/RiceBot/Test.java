package net.bot.RiceBot;


import java.io.IOException;
import java.sql.Date;
import java.util.List;

import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.service.PhotoHandler;
import net.bot.RiceBot.service.db.Implementations.JSONImpl.PhotoServiceImplJSON;


public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
//        ObjectMapper mapper = new ObjectMapper();
        String PATH = "src/main/resources/data/photos.json";
//        List<Photo> jsonString = List.of(mapper.readValue(new File("src/main/resources/data/photos.json"), Photo[].class));
        Photo p  = new Photo(12L, "name", "owner", new Date(PhotoHandler.parseDate("12.12.2012").getTime()));
        List<Photo> l = List.of(p, new Photo(12L, "nam234e", "owner", new Date(PhotoHandler.parseDate("12.12.2012").getTime())));
//        mapper.writeValue(new File("src/main/resources/data/photos.json"), l );
//        System.out.println(jsonString.get(0).getId());


        PhotoServiceImplJSON s = new PhotoServiceImplJSON();
        List<Photo> allPhotos = s.getPhotos(
                "f",
                PhotoHandler.parseDate("1.1.1970"),
                new Date(System.currentTimeMillis())
        );
        System.out.println(allPhotos);
        //mapper.writeValue(new File(PATH), photoList);

    }
}
