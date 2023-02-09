package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository  extends JpaRepository<Photo, Long> {
}
