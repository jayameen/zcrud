package com.jayameen.zcrud.api;

import com.jayameen.zcrud.BaseController;
import com.jayameen.zcrud.dto.AppResponse;
import com.jayameen.zcrud.service.ZCrudFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author Madan KN
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ZCrudAPI extends BaseController {

    private final ZCrudFactory factory;
    @Value("${app.mongo.collections.files}") protected String filesCollectionsName;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{collection}")
    public ResponseEntity<?> findAll(HttpServletRequest req, @PathVariable("collection") String collectionName,
                                     @RequestParam(value = "p", required = false, defaultValue = "0") Short page,
                                     @RequestParam(value = "s", required = false, defaultValue = "10") Short size,
                                     @RequestParam(value = "search[value]", required = false, defaultValue = "") String searchTokens,
                                     @RequestParam(value = "draw", required = false, defaultValue = "1") Long draw) {
        AppResponse<Document> myResponse = new AppResponse<>();
        if(StringUtils.isBlank(searchTokens)) {
            handleListSuccess(myResponse, factory.getDocumentService().findAllDocument(collectionName, page-1, size), draw);
        }else{
            handleListSuccess(myResponse, factory.getDocumentService().searchAllDocument(collectionName, searchTokens, page-1, size), draw);
        }
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{collection}/{key}/{value}")
    public ResponseEntity<?> findAllByKeyAndValue(HttpServletRequest req, @PathVariable("collection") String collectionName,@PathVariable("key") String key,@PathVariable("value") String value) {
        AppResponse<Document> myResponse = new AppResponse<>();
        handleObjectSuccess(myResponse, factory.getDocumentService().findDocumentByKeyValue(collectionName,key,value));
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{collection}/{ID}")
    public ResponseEntity<?> getDocumentByID(HttpServletRequest req, @PathVariable("collection") String collectionName, @PathVariable("ID") String ID) {
        AppResponse<Document> myResponse = new AppResponse<>();
        handleObjectSuccess(myResponse, factory.getDocumentService().getDocument(collectionName, ID));
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/{collection}/parent/{parentID}")
    public ResponseEntity<?> findAllByParentId(HttpServletRequest req, @PathVariable("collection") String collectionName,
                                               @PathVariable("parentID") String parentID,
                                               @RequestParam(value = "p", required = false, defaultValue = "0") Short page,
                                               @RequestParam(value = "s", required = false, defaultValue = "10") Short size,
                                               @RequestParam(value = "draw", required = false, defaultValue = "1") Long draw) {
        AppResponse<Document> myResponse = new AppResponse<>();
        handleListSuccess(myResponse, factory.getDocumentService().findAllDocumentForParentID(collectionName, parentID, page-1, size), draw);
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/{collection}")
    public ResponseEntity<?> createDocument(HttpServletRequest req, @PathVariable("collection") String collectionName, @RequestBody Document document) {
        AppResponse<Document> myResponse = new AppResponse<>();
        handleObjectSuccess(myResponse, factory.getDocumentService().createDocument(collectionName, document));
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/{collection}/bulk")
    public ResponseEntity<?> createBulkDocument(HttpServletRequest req, @PathVariable("collection") String collectionName, @RequestBody List<Document> documents) {
        AppResponse<Document> myResponse = new AppResponse<>();
        handleObjectSuccess(myResponse, factory.getDocumentService().createBulkDocuments(collectionName, documents));
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/replace/{collection}/{ID}")
    public ResponseEntity<?> replaceDocument(HttpServletRequest req, @PathVariable("collection") String collectionName, @PathVariable("ID") String ID,  @RequestBody Document document) {
        AppResponse<Document> myResponse = new AppResponse<>();
        Document doc = factory.getDocumentService().replaceDocument(collectionName, ID, document);
        handleObjectSuccess(myResponse, doc);
        if(Objects.isNull(doc) || doc.size() < 1){
            myResponse.setStatus("error");
            myResponse.setDescription("Document not found");
        }
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/update/{collection}/{ID}")
    public ResponseEntity<?> updateDocument(HttpServletRequest req, @PathVariable("collection") String collectionName, @PathVariable("ID") String ID,  @RequestBody Document document) {
        AppResponse<Document> myResponse = new AppResponse<>();
        Document doc = factory.getDocumentService().updateDocument(collectionName, ID, document);
        handleObjectSuccess(myResponse, doc);
        if(Objects.isNull(doc) || doc.size() < 1){
            myResponse.setStatus("error");
            myResponse.setDescription("Document not found");
        }
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/{collection}/{ID}")
    public ResponseEntity<?> removeDocument(HttpServletRequest req, @PathVariable("collection") String collectionName, @PathVariable("ID") String ID) {
        AppResponse<Long> myResponse = new AppResponse<>();
        Long count = factory.getDocumentService().removeDocument(collectionName, ID);
        handleObjectSuccess(myResponse, count);
        if(count < 1){
            myResponse.setStatus("error");
            myResponse.setDescription("Document not found");
        }
        return ResponseEntity.ok(myResponse);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    @PostMapping("/files/{parentObjectID}")
    public ResponseEntity<?> createDocumentUsingParentID(HttpServletRequest req, @PathVariable("parentObjectID") String parentObjectID) throws  Exception{
        AppResponse<Document> myResponse = new AppResponse<>();
        MultipartData mpData = extractUserMultiPartInfo(req);
        String     filePath  = "/customers/"+getUserId(req)+"/"+filesCollectionsName+"/"+parentObjectID;
        String      fileName = mpData.getFileName();
        String     isPrivate = "false";
        String       fileUrl = uploadFile(mpData,filePath, isPrivate);
        Document         doc = new Document();
        doc.put("parentId",parentObjectID);
        doc.put("url", fileUrl);
        doc.put("fileName", fileName);
        handleObjectSuccess(myResponse, factory.getDocumentService().createDocument(getUserId(req),filesCollectionsName,doc));
        return ResponseEntity.ok(myResponse);
    }
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
