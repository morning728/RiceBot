package net.bot.RiceBot.repository;

import net.bot.RiceBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
