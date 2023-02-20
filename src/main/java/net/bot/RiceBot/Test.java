package net.bot.RiceBot;

import net.bot.RiceBot.service.PhotoHandler;
import org.springframework.boot.SpringApplication;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.bot.RiceBot.service.PhotoHandler.parseDate;

public class Test {
    public static void main(String[] args) {
        //System.out.println(parseDate("1.2.2020"));
        List<String> s= Arrays.asList("aa", "ab", "ac", "ad");
        System.out.println(s.stream().sorted().collect(Collectors.toList()));

    }
}
