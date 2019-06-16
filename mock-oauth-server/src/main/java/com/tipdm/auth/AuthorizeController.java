package com.tipdm.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Controller
public class AuthorizeController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/authorize")
    public Object doAuthorize(
            HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException, IOException {
        try {
            //构建OAuth 授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String redirectURI = oauthRequest.getRedirectURI();
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String authorizationCode = oauthIssuerImpl.authorizationCode();

            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);

            //构建响应
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity("OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }

            //返回错误消息（如?error=）
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    @RequestMapping("/accessToken")
    public Object getAccessToken(
            HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        headers.add("Access-Control-Max-Age", "3600");
        headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String accessToken = oauthIssuerImpl.accessToken();
            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setRefreshToken(oauthIssuerImpl.refreshToken())
                    .setExpiresIn("3600")
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            ResponseEntity responseEntity = new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));

            //持久化道Redis
            String jsonString = "{\n" +
                    "    \"token\": \"" + accessToken + "\", \n" +
                    "    \"userId\": 100, \n" +
                    "    \"username\": \"admin\", \n" +
                    "    \"permissions\": [\n" +
                    "       \"model:child\",\n" +
                    "       \"project:addCat\",\n" +
                    "       \"component:up\",\n" +
                    "       \"project:up\",\n" +
                    "       \"project:sharedUrl\",\n" +
                    "       \"log:index\",\n" +
                    "       \"org:update\",\n" +
                    "       \"resource:detail\",\n" +
                    "       \"project:delete\",\n" +
                    "       \"datasource:previewShardTable\",\n" +
                    "       \"model:import\",\n" +
                    "       \"datasource:preview\",\n" +
                    "       \"project:deleteVersion\",\n" +
                    "       \"model:delete\",\n" +
                    "       \"role:menu\",\n" +
                    "       \"component:menu\",\n" +
                    "       \"resource:menu\",\n" +
                    "       \"project:apply\",\n" +
                    "       \"datasource:menu\",\n" +
                    "       \"algorithm:menu\",\n" +
                    "       \"model:list\",\n" +
                    "       \"task:update\",\n" +
                    "       \"model:share\",\n" +
                    "       \"project:syncProject\",\n" +
                    "       \"project:executeNode\",\n" +
                    "       \"mse:deploy\",\n" +
                    "       \"mse:redeploy\",\n" +
                    "       \"org:create\",\n" +
                    "       \"org:view\",\n" +
                    "       \"datasource:createTable\",\n" +
                    "       \"role:bind\",\n" +
                    "       \"project:execute\",\n" +
                    "       \"role:setAsDefault\",\n" +
                    "       \"task:start\",\n" +
                    "       \"client:menu\",\n" +
                    "       \"datasource:rdbms\",\n" +
                    "       \"project:import\",\n" +
                    "       \"model:save\",\n" +
                    "       \"setting:updateValue\",\n" +
                    "       \"setting:list\",\n" +
                    "       \"mse:showUnSaveMode\",\n" +
                    "       \"cat:updateCatName\",\n" +
                    "       \"file:exists\",\n" +
                    "       \"component:delete\",\n" +
                    "       \"cat:deleteCat\",\n" +
                    "       \"project:outputData\",\n" +
                    "       \"project:create\",\n" +
                    "       \"client:delete\",\n" +
                    "       \"model:pmml\",\n" +
                    "       \"task:get\",\n" +
                    "       \"user:delete\",\n" +
                    "       \"component:down\",\n" +
                    "       \"mse:csv\",\n" +
                    "       \"algorithm:list\",\n" +
                    "       \"org:menu\",\n" +
                    "       \"project:viewsource\",\n" +
                    "       \"user:upload\",\n" +
                    "       \"project:startAt\",\n" +
                    "       \"template:menu\",\n" +
                    "       \"client:view\",\n" +
                    "       \"task:stop\",\n" +
                    "       \"cat:addCategory\",\n" +
                    "       \"user:shardUsers\",\n" +
                    "       \"model:undeploy\",\n" +
                    "       \"user:download\",\n" +
                    "       \"role:create\",\n" +
                    "       \"model:menu\",\n" +
                    "       \"org:delete\",\n" +
                    "       \"resource:update\",\n" +
                    "       \"main:menu\",\n" +
                    "       \"project:child\",\n" +
                    "       \"project:recoverToVersion\",\n" +
                    "       \"datasource:previewData\",\n" +
                    "       \"mse:owned\",\n" +
                    "       \"mse:RDBMS\",\n" +
                    "       \"setting:menu\",\n" +
                    "       \"template:delete\",\n" +
                    "       \"project:log\",\n" +
                    "       \"scope:create\",\n" +
                    "       \"project:getProject\",\n" +
                    "       \"project:deleteCat\",\n" +
                    "       \"task:menu\",\n" +
                    "       \"datasource:delete\",\n" +
                    "       \"task:filter\",\n" +
                    "       \"banner:bbs\",\n" +
                    "       \"project:down\",\n" +
                    "       \"project:result\",\n" +
                    "       \"project:saveAs\",\n" +
                    "       \"mse:single\",\n" +
                    "       \"project:getByVersion\",\n" +
                    "       \"component:updateComponent\",\n" +
                    "       \"cat:childs\",\n" +
                    "       \"user:menu\",\n" +
                    "       \"datasource:dbconn\",\n" +
                    "       \"task:delete\",\n" +
                    "       \"banner:documentation\",\n" +
                    "       \"user:bind\",\n" +
                    "       \"resource:delete\",\n" +
                    "       \"template:list\",\n" +
                    "       \"component:addComponent\",\n" +
                    "       \"task:add\",\n" +
                    "       \"datasource:structure\",\n" +
                    "       \"file:uploadUDC\",\n" +
                    "       \"components:menu\",\n" +
                    "       \"project:clone\",\n" +
                    "       \"project:shutdown\",\n" +
                    "       \"setting:content\",\n" +
                    "       \"datasource:share\",\n" +
                    "       \"role:update\",\n" +
                    "       \"resource:create\",\n" +
                    "       \"task:list\",\n" +
                    "       \"project:version\",\n" +
                    "       \"resource:view\",\n" +
                    "       \"widget:add\",\n" +
                    "       \"project:export\",\n" +
                    "       \"element:deleteElement\",\n" +
                    "       \"user:create\",\n" +
                    "       \"project:visual\",\n" +
                    "       \"project:menu\",\n" +
                    "       \"file:uploadFlat\",\n" +
                    "       \"role:view\",\n" +
                    "       \"role:delete\",\n" +
                    "       \"evaluation:menu\",\n" +
                    "       \"datasource:flat\",\n" +
                    "       \"project:getOwnedModels\",\n" +
                    "       \"datasource:search\",\n" +
                    "       \"model:node\",\n" +
                    "       \"task:now\",\n" +
                    "       \"file:menu\",\n" +
                    "       \"model:validate\",\n" +
                    "       \"mse:shared\",\n" +
                    "       \"client:create\",\n" +
                    "       \"project:saveAsTemplate\",\n" +
                    "       \"project:getSharedModels\",\n" +
                    "       \"project:endAt\",\n" +
                    "       \"mse:root\",\n" +
                    "       \"component:get\",\n" +
                    "       \"datasource:shared\",\n" +
                    "       \"widget:list\",\n" +
                    "       \"log:search\",\n" +
                    "       \"datasource:syncTable\",\n" +
                    "       \"datasource:filter\",\n" +
                    "       \"datasource:exists\",\n" +
                    "       \"datasource:test\",\n" +
                    "       \"monitor:view\"" +
                    "    ]\n" +
                    "}";
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            redisTemplate.boundValueOps(accessToken).set(jsonNode, 12, TimeUnit.HOURS);
            return responseEntity;
        } catch (OAuthProblemException e) {
            //构建错误响应
            OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();

            return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

}
