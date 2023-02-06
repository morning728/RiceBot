package net.bot.RiceBot.service.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    @Autowired
    public LocaleMessageService(@Value("${localeTag}") Locale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;
    }
    public String getMessage(String message){
        return messageSource.getMessage(message, null, locale);
    }
    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }
}
