package net.bot.RiceBot;


import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.botApi.TelegramFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class TelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private TelegramFacade telegramFacade;


    public TelegramBot(DefaultBotOptions botOptions, TelegramFacade telegramFacade) {
        super(botOptions);

        this.telegramFacade = telegramFacade;
    }


    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

  @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
      SendMessage replyMessageToUser = null;
      try {
          replyMessageToUser = telegramFacade.handleUpdate(update);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

      return replyMessageToUser;
    }


    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
