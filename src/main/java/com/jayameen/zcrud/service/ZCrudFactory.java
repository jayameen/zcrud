package com.jayameen.zcrud.service;

import com.jayameen.zcrud.service.impl.MongoDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Madan KN
 */
@Component
@RequiredArgsConstructor
public class ZCrudFactory {

    @Value("${app.database-type}") private String databaseType;
    private DocumentService documentService;
    private final MongoDocumentService mongoDocumentService;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DocumentService getDocumentService(){
        if(Objects.isNull(documentService)){  init();  }
        return documentService;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private synchronized void init(){
        switch (databaseType) {
            case "mongo": this.documentService = mongoDocumentService; break;
        }
    }

}
