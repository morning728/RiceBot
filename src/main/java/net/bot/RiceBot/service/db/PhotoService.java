package net.bot.RiceBot.service.db;

import net.bot.RiceBot.model.Photo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface PhotoService {
    public List<Photo> getPhotos(String owner_username, Date date);

    public List<Photo> getPhotos(String owner_username, Date firstDate, Date secondDate);
    public void addPhoto(Photo photo);
}
