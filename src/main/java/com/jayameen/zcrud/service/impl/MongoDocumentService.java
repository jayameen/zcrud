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
    public Page<Document> searchAllDocument(String collectionName, String filterField, String filterType, String filterValue, int page, int size) {
        Pageable   pageable = PageRequest.of(page, size);
        Query         query = getFilterCriteria(filterField, filterType, filterValue).with(pageable);
        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    private Query getFilterCriteria(String filterField, String filterType, String filterValue) {
        if(filterType.equals("=")){
            if(filterValue!=null && (filterValue.trim().toLowerCase().equals("true") || filterValue.trim().toLowerCase().equals("false"))){
                return Query.query(Criteria.where(filterField).is(Boolean.parseBoolean(filterValue)));
            }else{
                return Query.query(Criteria.where(filterField).is(filterValue));
            }
        }else if(filterType.equals("<")){
            return Query.query(Criteria.where(filterField).lt(filterValue));
        }else if(filterType.equals(">")){
            return Query.query(Criteria.where(filterField).gt(filterValue));
        }else if(filterType.equals("<=")){
            return Query.query(Criteria.where(filterField).lte(filterValue));
        }else if(filterType.equals(">=")){
            return Query.query(Criteria.where(filterField).gte(filterValue));
        }else if(filterType.equals("!=")){
            return Query.query(Criteria.where(filterField).ne(filterValue));
        }else if(filterType.equals("!=")){
            return Query.query(Criteria.where(filterField).ne(filterValue));
        }else if(filterType.equals("like")) {
            return Query.query(Criteria.where(filterField).regex(filterValue));
        }else{
            return new Query();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page<Document> findAllDocument(String collectionName, int page, int size){

        Pageable   pageable = PageRequest.of(page, size);
        Query         query = new Query();
        query.with(pageable).with(Sort.by(Sort.Direction.DESC, "_id"));

        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Page<Document> findAllDocumentForParentID(String collectionName, String parentID, int page, int size){
        Pageable   pageable = PageRequest.of(page, size);
        Query         query = new Query();
        query.with(pageable).with(Sort.by(Sort.Direction.DESC, "_id"));
        query.addCriteria(Criteria.where("parentId").is(parentID));

        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Document.class, collectionName),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), collectionName));

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document findDocumentByKeyValue(String collectionName, String key, String value){
        Query query = new Query();
        query.addCriteria(Criteria.where(key).is(value));
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
        List<Document> docs = mongoTemplate.find(query, Document.class, collectionName);

        if(Objects.nonNull(docs) && docs.size() > 0 ) {
            return docs.get(0);
        }else{
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document createDocument(String collectionName, Document document){
        document = mongoTemplate.save(document, collectionName);
        return document;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BulkWriteResult createBulkDocuments(String collectionName, List<Document> objects){
        List<WriteModel<Document>> writeOperations = new ArrayList<>();
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        BulkWriteOptions writeOptions = new BulkWriteOptions().ordered(false);
        if(objects!=null){  for(Document doc :objects){
            writeOperations.add(new InsertOneModel<>(doc));
        } }
        BulkWriteResult bulkWriteResult = collection.bulkWrite(writeOperations,writeOptions);
        return bulkWriteResult;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document replaceDocument(String collectionName, String ID, Document doc){

        ObjectId objectId = new ObjectId(ID);
        Query      query  = new Query();
        query.addCriteria(Criteria.where("_id").is(objectId));

        doc.put("_id", objectId);
        FindAndReplaceOptions options = new FindAndReplaceOptions().returnNew();
        return mongoTemplate.findAndReplace(query, doc, options, collectionName);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Document updateDocument(String collectionName, String ID, Document doc){

        ObjectId objectId = new ObjectId(ID);
        Query      query  = Query.query(Criteria.where("_id").is(objectId));
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
    public Long removeDocument(String collectionName, String id) throws Exception {
        //Check if files exists & throw exception
        Page<Document> files = findAllDocumentForParentID("files", id, 0, 1);
        if(files!=null && files.getTotalElements() > 0){
            throw new Exception("Cannot Delete - Files exists for this document, delete all files first.");
        }

        ObjectId objectId = new ObjectId(id);
        Query      query  = Query.query(Criteria.where("_id").is(objectId));
        return mongoTemplate.remove(query, collectionName).getDeletedCount();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
