package net.bot.RiceBot.service.db.Implementations;

import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.repository.PhotoRepository;
import net.bot.RiceBot.service.db.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PhotoServiceImplDB implements PhotoService {
    private final PhotoRepository repository;

    @Autowired
    public PhotoServiceImplDB(PhotoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Photo> getPhotos(String owner_username, Date date) {
        return repository.getPhotos(owner_username, date);
    }

    @Override
    public List<Photo> getPhotos(String owner_username, Date firstDate, Date secondDate) {
        return repository.getPhotos(owner_username, firstDate, secondDate);
    }

    @Override
    public void addPhoto(Photo photo) {
        repository.saveAndFlush(photo);
    }
}
