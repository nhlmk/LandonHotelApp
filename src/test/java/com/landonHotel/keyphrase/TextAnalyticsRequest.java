package com.landonHotel.keyphrase;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class TextAnalyticsRequest {

    private List<TextDocument> documents = new ArrayList<>();
    public List<TextDocument> getDocuments(){
        return documents;
    }
    public void setDocuments(List<TextDocument> documents){
        this.documents = documents;
    }

}
