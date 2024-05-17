package com.jayameen.zcrud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayameen.zcrud.dto.AppResponse;
import com.jayameen.zcrud.dto.FileRequest;
import com.jayameen.zcrud.dto.ZToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}") protected String tokenServiceAPIUrl;
    @Value("${spring.security.oauth2.client.registration.zcrud.client-secret}") protected String clientSecret;
    @Value("${spring.security.oauth2.client.registration.zcrud.redirect-uri}") protected String redirectUri;

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
    protected ZToken fetchAndSetToken(HttpServletRequest request, HttpServletResponse response, String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("client_id", "zcrud");
        map.add("redirect_uri", redirectUri);
        map.add("client_secret", clientSecret);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        return restTemplate.postForEntity( tokenServiceAPIUrl, requestEntity , ZToken.class ).getBody();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void setAccessTokenInCookie(HttpServletResponse response, ZToken tokens){
        Cookie atCookie = new Cookie("access_token", tokens.getAccessToken());
        Cookie rtCookie = new Cookie("refresh_token", tokens.getRefreshToken());

        atCookie.setMaxAge(tokens.getAccessTokenExpiry());
        atCookie.setPath("/");
        response.addCookie(atCookie);

        rtCookie.setMaxAge(tokens.getRefreshTokenExpiry());
        rtCookie.setPath("/");
        response.addCookie(rtCookie);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}