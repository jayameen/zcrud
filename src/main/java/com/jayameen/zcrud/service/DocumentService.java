package com.jayameen.zcrud.service;

import com.mongodb.bulk.BulkWriteResult;
import org.bson.Document;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Madan KN
 */
public interface DocumentService {

    Page<Document> findAllDocument(String collectionName, int page, int size);

    Page<Document> findAllDocumentForParentID(String collectionName, String parentID, int page, int size);

    Page<Document> searchAllDocument(String collectionName, String filterField, String filterType, String filterValue, int page, int size);

    Document findDocumentByKeyValue(String collectionName, String key, String value);

    Document getDocument(String collectionName, String ID);

    Document createDocument(String collectionName, Document object);

    BulkWriteResult createBulkDocuments(String collectionName, List<Document> objectList);

    Document replaceDocument(String collectionName, String ID, Document document);

    Document updateDocument(String collectionName, String ID, Document document);

    Long removeDocument(String collectionName, String ID) throws Exception;
}
