package net.bot.RiceBot.controller;

import net.bot.RiceBot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;



@RestController
public class WebHookController {
    private final TelegramBot telegramBot;

    @Autowired
    public WebHookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        //execute(new SendMessage("12", "23"));
        //execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "sdfd"))
        //execute(new SendMessage(update.getMessage().getChatId(), "Hi! " + update.getMessage().getText()));
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
