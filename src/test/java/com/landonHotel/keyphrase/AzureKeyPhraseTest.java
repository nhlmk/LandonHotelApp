package com.landonHotel.keyphrase;

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

    private static final String textForAnalysis = "Hello world. This is some input text that I love.";

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


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

}
