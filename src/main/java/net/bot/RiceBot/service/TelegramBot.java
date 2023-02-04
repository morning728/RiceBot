package net.bot.RiceBot.service;


import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    public TelegramBot(BotConfig config) {

        this.config = config;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "Getting a greeting^^"));
        commands.add(new BotCommand("/get_info", "Get a short description of bot"));
        commands.add(new BotCommand("/get_weather", "Temporary"));
        commands.add(new BotCommand("/chat_gpt", "Temporary"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error while setting commands: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start": {
                    sendMessage("Hello, niggar", chatId);
                    log.info("Dialog was started with " + update.getMessage().getFrom().getFirstName());
                    break;
                }
                case "/get_info":{
                    sendMessage("Temporary information", chatId);
                    break;
                }
                default: {
                    sendMessage(message, chatId);
                    break;
                }
            }
        }
    }

    private void sendMessage(String msg, long chatId) {
        try {
            execute(new SendMessage(String.valueOf(chatId), msg));
        } catch (TelegramApiException e) {
            log.error("Error while sending a message: " + e.getMessage());
        }
    }
}
