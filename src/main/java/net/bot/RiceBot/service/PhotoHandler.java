package net.bot.RiceBot.service;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;



@Slf4j
public class PhotoHandler {
    public static String getRandomName(String str){
        str = Arrays.asList(str.split("\\.")).get(Arrays.asList(str.split("\\.")).size() - 1);
        String symbols = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        String random = new Random().ints(48, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return random + "." + str;
    }

    public static java.sql.Date parseDate(String strDate)  {
        List<String> dateList = Arrays.asList(strDate.split("\\."));

        SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy");

        try {
            Date date = format.parse(dateList.get(1) + " " + dateList.get(0) + " " + dateList.get(2));
            return new java.sql.Date(date.getTime());
        }
        catch(Exception e) {
            log.error("Invalid data");
        }
        return null;
    }


}
