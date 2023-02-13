package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.Account;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface PhotoRepository  extends JpaRepository<Photo, Long> {
    @Modifying
    @Transactional
    @Query("FROM Photo p WHERE  p.owner_username = :owner_username AND p.date = :date")
    public List<Photo> getPhotos(@Param("owner_username")String owner_username, @Param("date")Date date);

    @Modifying
    @Transactional
    @Query("FROM Photo p WHERE  p.owner_username = :owner_username AND p.date >= :firstDate AND p.date <= :secondDate")
    public List<Photo> getPhotos(@Param("owner_username")String owner_username, @Param("firstDate")Date firstDate, @Param("secondDate")Date secondDate);
}
