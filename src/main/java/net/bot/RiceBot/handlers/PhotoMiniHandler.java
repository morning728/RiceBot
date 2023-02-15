package net.bot.RiceBot.handlers;

import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.model.Photo;
import net.bot.RiceBot.repository.PhotoRepository;
import net.bot.RiceBot.service.PhotoHandler;
import net.bot.RiceBot.service.db.Implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Date;
import java.text.ParseException;
import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Slf4j
public class PhotoMiniHandler implements InputMessageHandler{
    private final UserServiceImpl userService;

    private final PhotoRepository photoRepository;
    @Autowired
    public PhotoMiniHandler(UserServiceImpl userService, PhotoRepository photoRepository) {
        this.userService = userService;
        this.photoRepository = photoRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        List<String> splitedMessage = Arrays.asList(message.getText().split(" "));
        try {
            if (splitedMessage.size() == 1) {
                reply.setText(getAllDates(message));
            } else if (splitedMessage.size() == 2) {
                reply.setText(getDatesA(message));
            } else {
                reply.setText("Что-то пошло не так, проверь формат (/get_all_dates, /get_all_dates yyyy, /get_all_dates mm.yyyy)");
            }
        } catch (Exception e){
            reply.setText("Что-то пошло не так, проверь формат (/get_all_dates, /get_all_dates yyyy, /get_all_dates mm.yyyy)");
        }
        return reply;
    }

    private String getAllDates(Message message) throws ParseException {
        StringBuilder answer = new StringBuilder("");
        int currentYear = 0;
        List<Photo> allPhotos = photoRepository.getPhotos(
                userService.getUserById(message.getChatId()).getUsername(),
                PhotoHandler.parseDate("1.1.1970"),
                new Date(System.currentTimeMillis())
        );
        List<Date> dates = allPhotos.stream().
                map(Photo::getDate).
                collect(Collectors.toSet()).stream().
                sorted(java.util.Date::compareTo).
                collect(Collectors.toList());



        for (Date date: dates
             ) {
            if(currentYear < date.getYear() + 1900){
                currentYear = date.getYear() + 1900;
                answer.append(currentYear).
                        append(" year:\n");
            }
            answer.append("                ").
                    append(date.getDate()).
                    append(".").
                    append(date.getMonth() + 1).
                    append("\n");
        }
        return answer.toString();
    }

    private String getDatesA(Message message) throws ParseException {
        StringBuilder answer = new StringBuilder("");
        String current = message.getText().split(" ")[1], firstDate, secondDate, type;
        if(current.length() == 4){
            firstDate = "1.1." + current;
            secondDate = "31.12." + current;
            type = " year:\n";
        }
        else if(current.length() == 7){
            firstDate = "1." + current;
            secondDate = "31." + current;
            type = " month:\n";
        }
        else{
            return "Что-то пошло не так, проверь формат (/get_all_dates, /get_all_dates yyyy, /get_all_dates mm.yyyy)";
        }
        List<Photo> allPhotos = photoRepository.getPhotos(
                userService.getUserById(message.getChatId()).getUsername(),
                PhotoHandler.parseDate(firstDate),
                PhotoHandler.parseDate(secondDate)
        );
        List<Date> dates = allPhotos.stream().
                map(Photo::getDate).
                collect(Collectors.toSet()).stream().
                sorted(java.util.Date::compareTo).
                collect(Collectors.toList());
        answer.append(current).
                append(type);
        for (Date date: dates
        ) {
            answer.append("                ").
                    append(date.getDate()).
                    append(".").
                    append(date.getMonth() + 1).
                    append("\n");
        }
        return answer.toString();
    }
}
