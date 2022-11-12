package com.landonHotel.challange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landonHotel.keyphrase.AzureKeyPhraseTest;
import com.landonHotel.keyphrase.TextAnalyticsRequest;
import com.landonHotel.keyphrase.TextDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service

public class AzureSentimentService {
    public String azureApikey = "";
    @Autowired
    private ObjectMapper mapper;
    private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback01.cognitiveservices.azure.com";
    private static final String AZURE_ENDPOINT_PATH = "/text/analytics/v3.1/sentiment";
    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";
    private static final String CONTENT_TYPE =
            "Content-Type";
    private static final String APPLICATION_JSON = "application/json";


    public SentimentAnalysis requestSentimentAnalysis(String text, String language) throws IOException, InterruptedException {


        TextDocument document = new TextDocument("1", text, language);
        TextAnalyticsRequest requestBody = new TextAnalyticsRequest();
        requestBody.getDocuments().add(document);


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .proxy(ProxySelector.getDefault())
                .connectTimeout(Duration.ofSeconds(5))
                .build();

       /* HttpClient client = HttpClient.newHttpClient();
       */


        HttpRequest request = HttpRequest.newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(API_KEY_HEADER_NAME, this.azureApikey)
                .uri(URI.create(AZURE_ENDPOINT + AZURE_ENDPOINT_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!=200){
            System.out.println(response.body());
            throw new RuntimeException("An issue occured making the API call");
        }

        String sentimentValue = this.mapper
                .readValue(response.body(), JsonNode.class)
                .get("documents")
                .get(0)
                .get("sentiment")
                .asText();
        SentimentAnalysis analysis = new SentimentAnalysis(document, sentimentValue);
        return analysis;


       /* client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.body())
                .thenAccept(body -> {

                    JsonNode node = null;
                    try {
                        node = mapper.readValue(body, JsonNode.class);
                        String value = node.get("documents")
                                .get(0)
                                .get("keyPhrases")
                                .get(0)
                                .asText();
                        System.out.println("The first keyphrase is " + value);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                });
        System.out.println("This will be caught first because our call is asyc");
        Thread.sleep(5000);

        */




    }

}
