package com.jayameen.zcrud.service;

import com.mongodb.bulk.BulkWriteResult;
import org.bson.Document;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Madan KN
 */
public interface DocumentService {

    Page<Document> findAllDocument(long userID, String collectionName, int page, int size);

    Page<Document> findAllDocumentForParentID(long userID, String collectionName, String parentID, int page, int size);

    Page<Document> searchAllDocument(long userID, String collectionName, String tokens, int page, int size);

    Document findDocumentByKeyValue(long userID, String collectionName, String key, String value);

    Document getDocument(long userID, String collectionName, String ID);

    Document createDocument(long userID, String collectionName, Document object);

    BulkWriteResult createBulkDocuments(long userID, String collectionName, List<Document> objectList);

    Document replaceDocument(long userID, String collectionName, String ID, Document document);

    Document updateDocument(long userID, String collectionName, String ID, Document document);

    Long removeDocument(long userid, String collectionName, String ID);
}
