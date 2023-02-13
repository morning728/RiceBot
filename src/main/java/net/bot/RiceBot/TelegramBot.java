package net.bot.RiceBot;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.botApi.TelegramFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Slf4j
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
      SendMessage replyMessageToUser = new SendMessage();
      try {
          replyMessageToUser = telegramFacade.handleUpdate(update);
      } catch (Exception e) {//ioexeption
          replyMessageToUser.setChatId(update.getMessage().getChatId().toString());
          replyMessageToUser.setText("Произошла какая-то жесткая ошибка, скорее всего по твоей вине, курдюк недобитый");
      }
      //log.info(replyMessageToUser.getChatId());
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
