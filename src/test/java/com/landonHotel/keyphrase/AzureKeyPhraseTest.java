package com.landonHotel.keyphrase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages  = { "com.landonHotel"})
@SpringBootTest(classes = AzureKeyPhraseTest.class)
public class AzureKeyPhraseTest {


    public String azureApikey = "";
    private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback01.cognitiveservices.azure.com";
    private static final String AZURE_ENDPOINT_PATH = "/text/analytics/v3.1/keyPhrases";
    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";
    private static final String CONTENT_TYPE =
            "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String EXAMPLE_JSON =
            "{\"documents\":[{\"language\":\"en\",\"id\":\"1\",\"text\":\"Hello world. This is some input text that I love.\"}]}";

    private static final String textForAnalysis = "In an e360 interview, Carlos Nobre, Brazil's leading expert on the Amazon and climate change,\n" +
            "discusses the key perils facing the world's largest rainforest, where a record number of fires are now raging, and lays out what can be done to stave off a ruinous transformation of the region.";

    @Autowired
    public ObjectMapper mapper;

    @Test
    public void getKeyPhrases() throws IOException, InterruptedException {
        TextDocument document = new TextDocument("1", textForAnalysis, "en" );
        TextAnalyticsRequest requestBody = new TextAnalyticsRequest();
        requestBody.getDocuments().add(document);


        HttpClient client = HttpClient.newHttpClient();


        HttpRequest request = HttpRequest.newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(API_KEY_HEADER_NAME, this.azureApikey)
                .uri(URI.create(AZURE_ENDPOINT + AZURE_ENDPOINT_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                .build();


        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.body() )
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
    }

}
