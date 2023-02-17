package net.bot.RiceBot.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.bot.RiceBot.FormInputDTO;
import net.bot.RiceBot.OpenAiApiClient;
import net.bot.RiceBot.model.ChatGPT.CompletionRequest;
import net.bot.RiceBot.model.ChatGPT.CompletionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class ChatGPTHandler implements InputMessageHandler{

    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private OpenAiApiClient client;

    @Override
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage();
        reply.setChatId(message.getChatId().toString());
        FormInputDTO dto = new FormInputDTO(message.getText().split(" ", 2)[1]);
        try{
            log.info(dto.prompt());
            reply.setText(chatWithGpt3(dto.prompt()));
        }catch (Exception e){
            log.info(e.toString());
            reply.setText("ERROR");
        }


        return reply;
    }

    private String chatWithGpt3(String message) throws Exception {
        var completion = CompletionRequest.defaultWith(message);
        var postBodyJson = jsonMapper.writeValueAsString(completion);
        var responseBody = client.postToOpenAiApi(postBodyJson, OpenAiApiClient.OpenAiService.GPT_3);
        var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
        return completionResponse.firstAnswer().orElseThrow();
    }
}
