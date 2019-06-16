package com.tipdm.framework.controller.dmserver;

import com.tipdm.framework.common.controller.Result;
import com.tipdm.framework.common.controller.base.BaseController;
import com.tipdm.framework.common.token.TokenManager;
import com.tipdm.framework.dmserver.utils.GlobalSeqGenerator;
import com.tipdm.framework.service.dmserver.ProjectService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by TipDM on 2016/12/21.
 * E-mail:devp@tipdm.com
 */
@Controller
@RequestMapping("/api/seq")
//@Api(value = "/api/seq", position = 1, tags = "序列生成器")
public class SequenceController extends BaseController{

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ApiOperation(value = "获取序列", notes = "该接口用来生成序列")
    public @ResponseBody Result check(/*@ApiParam(value = "用户访问令牌", required = true)*/ @RequestHeader("accessToken") String accessToken) {

        Result result = new Result();
        result.setMessage("序列生成成功");
        result.setData(GlobalSeqGenerator.getNextId());
        return result;
    }

}
