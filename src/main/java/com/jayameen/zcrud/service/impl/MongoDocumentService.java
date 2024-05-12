package com.jayameen.zcrud.service.impl;

import com.jayameen.zcrud.service.DocumentService;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Madan KN
 */
@Service
@RequiredArgsConstructor
@Component("mongoDocumentManager")
public class MongoDocumentService implements DocumentService {

    @Autowired private MongoTemplate mongoTemplate;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page<Document> searchAllDocument(String collectionName, String tokens, int page, int size){
        Pageable   pageable = PageRequest.of(page, size);

        TextCriteria criteria = TextCriteria.forDefaultLanguage();
        criteria.matchingAny(tokens);

        Query query = TextQuery.queryText(criteria)
                .sortByScore()
                .with(pageable);
        // query.addCriteria(Criteria.where("customerId").is(userID));


        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page<Document> findAllDocument(String collectionName, int page, int size){

        Pageable   pageable = PageRequest.of(page, size);
        Query         query = new Query();
        query.with(pageable).with(Sort.by(Sort.Direction.DESC, "_id"));
        //query.addCriteria(Criteria.where("customerId").is(userID));

        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page<Document> findAllDocumentForParentID(String collectionName, String parentID, int page, int size){
        Pageable   pageable = PageRequest.of(page, size);
        Query         query = new Query();
        query.with(pageable).with(Sort.by(Sort.Direction.DESC, "_id"));
        //query.addCriteria(Criteria.where("customerId").is(userID));
        query.addCriteria(Criteria.where("parentId").is(parentID));

        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document findDocumentByKeyValue(String collectionName, String key, String value){
        Query query = new Query();
        query.addCriteria(Criteria.where(key).is(value));
        //query.addCriteria(Criteria.where("customerId").is(userID));
        List<Document> result = mongoTemplate.find(query, Document.class, collectionName);
        if(Objects.nonNull(result) && result.size() > 0){
            return result.get(0);
        }else{
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document getDocument(String collectionName, String id){
        ObjectId objectId   = new ObjectId(id);
        Query      query    = new Query();
        query.addCriteria(Criteria.where("_id").is(objectId));
        //query.addCriteria(Criteria.where("customerId").is(userID));
        List<Document> docs = mongoTemplate.find(query, Document.class, collectionName);

        if(Objects.nonNull(docs) && docs.size() > 0 ) {
            return docs.get(0);
        }else{
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document createDocument(String collectionName, Document document){
        //document.put("customerId", userID);
        document = mongoTemplate.save(document, collectionName);
        return document;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BulkWriteResult createBulkDocuments(String collectionName, List<Document> objects){
        List<WriteModel<Document>> writeOperations = new ArrayList<>();
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        BulkWriteOptions writeOptions = new BulkWriteOptions().ordered(false);
        if(objects!=null){  for(Document doc :objects){
            //doc.put("customerId", userID);
            writeOperations.add(new InsertOneModel<>(doc));
        } }
        BulkWriteResult bulkWriteResult = collection.bulkWrite(writeOperations,writeOptions);
        //System.out.println("bulkWriteResult:- " + bulkWriteResult);
        return bulkWriteResult;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document replaceDocument(String collectionName, String ID, Document doc){

        ObjectId objectId = new ObjectId(ID);
        Query      query  = new Query();
        query.addCriteria(Criteria.where("_id").is(objectId));
        //query.addCriteria(Criteria.where("customerId").is(userID));

        doc.put("_id", objectId);
        FindAndReplaceOptions options = new FindAndReplaceOptions().returnNew();
        return mongoTemplate.findAndReplace(query, doc, options, collectionName);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document updateDocument(String collectionName, String ID, Document doc){

        ObjectId objectId = new ObjectId(ID);
        Query      query  = Query.query(Criteria.where("_id").is(objectId));
        //query.addCriteria(Criteria.where("customerId").is(userID));
        doc.put("_id", objectId);
        Update update = new Update();

        Iterator<String> keys = doc.keySet().iterator();
        while (keys.hasNext()){
            String attrKey = keys.next();
            update.set(attrKey, doc.get(attrKey));
        }

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return mongoTemplate.findAndModify(query, update, options, Document.class, collectionName);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Long removeDocument(String collectionName, String id){

        ObjectId objectId = new ObjectId(id);
        Query      query  = Query.query(Criteria.where("_id").is(objectId));
        //query.addCriteria(Criteria.where("customerId").is(userID));
        return mongoTemplate.remove(query, collectionName).getDeletedCount();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
