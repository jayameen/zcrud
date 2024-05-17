package com.jayameen.zcrud;

/**
 * @author Madan KN
 */

import com.jayameen.zcrud.dto.ZToken;
import com.jayameen.zcrud.service.ZCrudFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
public class ViewFrontController extends BaseController{

    @Value("${app.mongo.collections.metadata}") protected String collectionsMetaDataName;
    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}") protected String authorizationUri;
    @Value("${spring.security.oauth2.client.registration.zcrud.client-id}") protected String clientID;
    @Value("${app.key-cloak-logout-url}") protected String logoutUrl;
    private final ZCrudFactory factory;

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/login/authcode")
    public ModelAndView loginAuthCode(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "code", required = false, defaultValue = "") String code) {
        ModelAndView mv = new ModelAndView("index");
        if(StringUtils.isNotBlank(code)){
            ZToken tokens = fetchAndSetToken(request, response, code);
            mv.addObject("access_token", tokens.getAccessToken());
            mv.addObject("refresh_token", tokens.getRefreshToken());
        }
        return mv;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String loginUrl = authorizationUri+"?client_id="+clientID+"&redirect_uri="+redirectUri+"&response_type=code";
        return "redirect:"+loginUrl;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(path="/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout() throws Exception {
        return "redirect:"+logoutUrl;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/{collection}/list.html")
    public ModelAndView listView(HttpServletRequest req, @PathVariable("collection") String collectionName){
        ModelAndView mv = new ModelAndView("list");
        mv.addObject("collection",collectionName);
        mv.addObject("collectionName", CaseUtils.toCamelCase(collectionName, true));
        Document document = null;
        try{
            document = factory.getDocumentService().findDocumentByKeyValue(collectionsMetaDataName, "name", collectionName);
            mv.addObject("metaData", document.toJson());
        }
        catch (Exception ex){
            mv.addObject("serverError", ex.getMessage() + " Check if collection metadata is created");
        }

        return mv;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/error-401.html")
    public String e401() {
        return "error-401";
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

}
