package com.jayameen.zcrud.security;

import com.jayameen.zcrud.BaseController;
import com.jayameen.zcrud.dto.AppResponse;
import com.jayameen.zcrud.dto.ZToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Madan KN
 */
@Controller
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {

    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}") protected String authorizationUri;
    @Value("${spring.security.oauth2.client.registration.zcrud.client-id}") protected String clientID;

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/login/authcode", method = RequestMethod.GET)
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
    @RequestMapping(value = "/api/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<AppResponse> loginRefreshToken(@RequestBody ZToken tokensRequest) {
       AppResponse appResponse = new AppResponse<>();
       if(StringUtils.isNotBlank(tokensRequest.getRefreshToken())){
           ZToken tokenResponse = refreshToken(tokensRequest.getRefreshToken());
           handleObjectSuccess(appResponse, tokenResponse);
       }
       return ResponseEntity.ok(appResponse);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String loginUrl = authorizationUri+"?client_id="+clientID+"&redirect_uri="+redirectUri+"&response_type=code";
        return "redirect:"+loginUrl;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequestMapping(path="/api/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<AppResponse> logout(@RequestBody ZToken token) throws Exception {
        AppResponse appResponse = new AppResponse<>();
        handleObjectSuccess(appResponse, logoutToken(token));
        return ResponseEntity.ok(appResponse);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
}
