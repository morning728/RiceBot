package net.bot.RiceBot.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.TelegramBot;
import net.bot.RiceBot.botApi.TelegramFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//import org.telegram.telegrambots.meta.ApiContext;

@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private static String webHookPath;
    private String botUserName;
    public String botToken;

    @Value("${ngrok.api_key}")
    private String ngrokKey;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    @Bean
    public TelegramBot TelegramBot(TelegramFacade telegramFacade)
            throws IOException, InterruptedException {
        DefaultBotOptions options = new DefaultBotOptions();//ApiContext
                //.getInstance(DefaultBotOptions.class);

        TelegramBot bot = new TelegramBot(options, telegramFacade);
        bot.setBotUserName(botUserName);
        bot.setBotToken(botToken);
        bot.setWebHookPath(getWebHook(webHookPath));
        log.info("Webhook was set on bot: " + getWebHook(webHookPath));
        if(setTelegramWebHook(getWebHook(webHookPath)) == 200){
            log.info("Webhook was set on telegram service");
        }
        else{
            log.error("Webhook was NOT set on Telegram service!");
        }
        return bot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    public String getWebHook(String analogWebHook)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder().uri(URI.create("https://api.ngrok.com/tunnels"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Ngrok-Version", "2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ngrokKey)
                .GET().build();

        String str = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        if (str.contains("public_url\":\"https")) {
            return (str.substring(str.indexOf("public_url\":\"https") + 13,
                    str.indexOf("\",", str.indexOf("public_url\":\"https") + 13)));
        }
        else{
            return analogWebHook;
        }
    }

    public int setTelegramWebHook(String webhook)
            throws IOException, InterruptedException
    {

        HttpClient client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder().
                uri(URI.create("https://api.telegram.org/bot" + botToken + "/setWebhook?url=" + webhook))
                .GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
    }

}
