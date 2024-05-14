package com.jayameen.zcrud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayameen.zcrud.dto.AppResponse;
import com.jayameen.zcrud.dto.FileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;

/**
 *
 * @author Madan KN
 */

public class BaseController<T>{

    @Autowired protected RestTemplate restTemplate;
    @Autowired protected ObjectMapper jacksonObjectMapper;
    @Value("${app.file-service-base64-upload-api}") protected String fileServiceAPIUrl;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void handleListSuccess(AppResponse response, Page<T> pageRec){
        response.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        response.setCode(HttpStatus.OK.toString());
        response.setDescription("Operation Completed Successfully!");
        response.setRecordsTotal(pageRec.getTotalElements());
        response.setPagesTotal(new Long(pageRec.getTotalPages()));
        response.setRecordsFiltered(pageRec.getTotalElements());
        response.setData(pageRec.getContent());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void handleObjectSuccess(AppResponse response, Object object){
        response.setData(Arrays.asList(object));
        response.setStatus(HttpStatus.OK.getReasonPhrase().toLowerCase());
        response.setCode(HttpStatus.OK.toString());
        response.setDescription("Operation Completed Successfully!");
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected String uploadFile(byte fileData[], String prefixPath, String fileName, String privateFlag) throws  Exception{
        String fileUrl = null;
        if(fileData!=null && fileData.length > 0){

            FileRequest fileRequest = new FileRequest();
            fileRequest.setBase64Data(Base64.getEncoder().encodeToString(fileData));
            fileRequest.setIsPrivate(Boolean.parseBoolean(privateFlag));
            fileRequest.setFilePath(prefixPath);
            fileRequest.setFileName(fileName);

            ResponseEntity<AppResponse> result = restTemplate.postForEntity(fileServiceAPIUrl, fileRequest, AppResponse.class);
            if(result!=null && result.getBody().getData().size() > 0){
                fileUrl = (String)result.getBody().getData().get(0);
            }
        }
        return fileUrl;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}