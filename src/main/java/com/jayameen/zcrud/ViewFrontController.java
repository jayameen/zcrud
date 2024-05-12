package com.jayameen.zcrud;

/**
 * @author Madan KN
 */

import com.jayameen.zcrud.service.ZCrudFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.CaseUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class ViewFrontController extends BaseController{

    @Value("${app.mongo.collections.metadata}") protected String collectionsMetaDataName;

    private final ZCrudFactory factory;

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/")
    public String index() {
        return "index";
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/{collection}/list.html")
    public ModelAndView listView(HttpServletRequest req, @PathVariable("collection") String collectionName){
        ModelAndView mv = new ModelAndView("list");
        mv.addObject("collection",collectionName);
        mv.addObject("collectionName", CaseUtils.toCamelCase(collectionName, true));
        Document document = null;
        try{
            document = factory.getDocumentService().findDocumentByKeyValue(getUserId(req), collectionsMetaDataName, "name", collectionName);
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
