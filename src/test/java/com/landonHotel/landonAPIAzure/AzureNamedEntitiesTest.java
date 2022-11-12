package com.landonHotel.landonAPIAzure;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static io.restassured.RestAssured.*;

//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils.MethodFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = AzureNamedEntitiesTest.class)

public class AzureNamedEntitiesTest {


    @Value("${AZURE_API_KEY}")
    private String azureApikey;
    private static final String AZURE_ENDPOINT = "https://landon-hotel-feedback01.cognitiveservices.azure.com";
    private static final String AZURE_ENDPOINT_PATH = "/text/analytics/v3.1/sentiment";
    private static final String API_KEY_HEADER_NAME = "Ocp-Apim-Subscription-Key";
    private static final String CONTENT_TYPE =
            "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String EXAMPLE_JSON =
            "{\"documents\":[{\"id\":\"1\",\"language\":\"en\",\"text\":\"Great atmosphere. Close to plenty of restaurants, hotels, and transit! Staff are friendly and helpful.\"}]}";


    @Test
    public void eetEntities() throws IOException, InterruptedException {
        // Create a client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request
        HttpRequest request = HttpRequest.newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(API_KEY_HEADER_NAME, azureApikey)
                .uri(URI.create(AZURE_ENDPOINT + AZURE_ENDPOINT_PATH))
                .POST(HttpRequest.BodyPublishers.ofString(EXAMPLE_JSON))
                .build();

        // Send the request and receive response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Work with the response
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

}
