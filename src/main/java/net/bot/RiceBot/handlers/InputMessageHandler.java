package net.bot.RiceBot.handlers;

import net.bot.RiceBot.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    public SendMessage handle(Message message);

}
