package com.tipdm.framework.controller.dmserver;

import com.alibaba.fastjson.JSON;
import com.tipdm.framework.common.controller.Result;
import com.tipdm.framework.common.controller.base.BaseController;
import com.tipdm.framework.common.token.TokenManager;
import com.tipdm.framework.common.token.model.TokenModel;
import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.controller.dmserver.dto.Category;
import com.tipdm.framework.dmserver.exception.ElementNotFoundException;
import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.ComponentExtra;
import com.tipdm.framework.model.dmserver.ComponentIO;
import com.tipdm.framework.model.dmserver.Widget;
import com.tipdm.framework.service.dmserver.ComponentService;
//import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
@RestController
//@Api(tags = "组件管理", description = "组件管理", position = 6)
@PropertySource(value = "classpath:sysconfig/system.properties")
public class ComponentController extends BaseController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ComponentService componentService;

    @RequiresPermissions("cat:addCategory")
    @RequestMapping(value = "/api/cat", method = RequestMethod.POST, produces = "application/json")
//    @ApiOperation(value = "添加组件分类")
    public Result addCategory(/*@ApiParam(required = true, value = "用户访问令牌") */@RequestHeader("accessToken") String accessToken,
                              /*@ApiParam(required = true, value = "分类对象")*/ @RequestBody Category category) throws Exception {

        Result result = new Result();

        TokenModel tokenModel = tokenManager.getPermissions(accessToken);

        Component cat = new Component();
        cat.setName(category.getName());
        cat.setCreatorId(tokenModel.getUserId());
        cat.setCreatorName(tokenModel.getUsername());
        cat.setParentId(category.getParentId());
        cat.setComponent(Boolean.FALSE);
        cat.setInBuilt(Boolean.TRUE);

        componentService.save(cat);
        result.setMessage("添加组件分类成功");
        result.setData(cat.getId());
        return result;
    }

    @RequiresPermissions("component:addComponent")
    @RequestMapping(value = "/api/component", method = RequestMethod.POST, produces = "application/json")
//    @ApiOperation(value = "添加组件")
    public Result addComponent(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                               /*@ApiParam(required = true)*/ @RequestBody com.tipdm.framework.controller.dmserver.dto.Component comp) throws Exception {

        Result result = new Result();

        TokenModel tokenModel = tokenManager.getPermissions(accessToken);

        Component component = new Component();
        component = JSON.parseObject(JSON.toJSONString(comp), Component.class);

        component.setInBuilt(Boolean.TRUE);
        component.setCreatorId(tokenModel.getUserId());
        component.setCreatorName(tokenModel.getUsername());
        component.setComponent(Boolean.TRUE);
        ComponentExtra extra = new ComponentExtra();
        extra.setType(ComponentExtra.ExtraType.SCRIPT);
        component.setExtra(extra);
        componentService.save(component);
        result.setMessage("组件添加成功");
        result.setData(component.getId());
        return result;
    }

    @RequiresPermissions("component:get")
    @RequestMapping(value = "/api/component/{componentId}", method = RequestMethod.GET, produces = "application/json")
//    @ApiOperation(value = "获取组件数据")
    public Result get(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                      /*@ApiParam(required = true, value = "组件id")*/ @PathVariable(name = "componentId") Long componentId,
                      HttpServletResponse response) throws Exception {
        Result result = new Result();
        Component component = componentService.findOne(componentId);
        if (null == component) {
            throw new ElementNotFoundException("组件不存在或已被删除");
        }
        List<ComponentIO> inputs = component.getInputs().stream().filter(distinctByKey(ComponentIO::getId)).collect(Collectors.toList());
        List<ComponentIO> outputs = component.getOutputs().stream().filter(distinctByKey(ComponentIO::getId)).collect(Collectors.toList());
        component.setInputs(inputs);
        component.setOutputs(outputs);
        result.setMessage("组件数据加载成功");
        result.setData(component);
        return result;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @RequiresPermissions("component:updateComponent")
    @RequestMapping(value = "/api/component/{componentId}", method = RequestMethod.PUT, produces = "application/json")
//    @ApiOperation(value = "更新组件数据")
    public Result updateComponent(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                                  /*@ApiParam(required = true, value = "要更新的组件id") */@PathVariable(name = "componentId") Long componentId,
                                  /*@ApiParam(required = true, value = "更新的内容")*/ @RequestBody com.tipdm.framework.controller.dmserver.dto.Component comp) throws Exception {

        Result result = new Result();

        TokenModel tokenModel = tokenManager.getPermissions(accessToken);

        Component component = new Component();
        component = JSON.parseObject(JSON.toJSONString(comp), Component.class);
        component.setInBuilt(Boolean.TRUE);
        component.setComponent(Boolean.TRUE);
        component.setCreatorId(tokenModel.getUserId());
        component.setCreatorName(tokenModel.getUsername());
        ComponentExtra extra = new ComponentExtra();
        extra.setType(ComponentExtra.ExtraType.SCRIPT);
        component.setExtra(extra);
        componentService.update(componentId, component);
        result.setMessage("更新成功");
        return result;
    }

    @RequiresPermissions("component:delete")
    @RequestMapping(value = "/api/component/{componentId}", method = RequestMethod.DELETE, produces = "application/json")
//    @ApiOperation(value = "删除组件")
//    @ApiResponses({
//            @ApiResponse(code = 400, message = "提交的数据错误"),
//            @ApiResponse(code = 401, message = "无效的token或token已过期"),
//            @ApiResponse(code = 403, message = "您没有权限执行此操作"),
//            @ApiResponse(code = 404, message = "组件不存在")
//    })
    public Result delete(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                         /*@ApiParam(required = true, value = "组件id")*/ @PathVariable(name = "componentId") Long componentId,
                         HttpServletResponse response) throws Exception {

        Result result = new Result();
        Component com = componentService.findOne(componentId);//查询，先获取com.getSequence()
        if (com == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            throw new ElementNotFoundException("组件不存在");
        }
        List<Component> child = componentService.findChild(com.getParentId(), com.getCreatorId());//根据父节点的ID，获取该父节点下所有子节点
        Integer sequence = com.getSequence();
        sequence = sequence == null ? 0 : sequence;
        if (sequence == child.size() - 1) {//已是最底下不需要更新
        } else {
            Integer number = sequence + 1;//删除一个，从下一个排序的数据开始更新所有sequence
            for (int i = number; i < child.size(); i++) {
                Component component = child.get(i);
                Integer seq = component.getSequence();
                seq = seq == null ? 0 : seq - 1;
                component.setSequence(seq);
                componentService.update(component.getId(), component);
            }
        }
        componentService.delete(componentId);

        result.setMessage("组件删除成功");
        return result;
    }

    @RequiresPermissions("cat:childs")
    @RequestMapping(value = "/api/cat/{catId}/childs", method = RequestMethod.GET, produces = "application/json")
//    @ApiOperation(value = "获取分类下的子分类和算法")
    public Result findChilds(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                             /*@ApiParam(required = true, value = "类别id") */@PathVariable(name = "catId") Long catId) throws Exception {

        Result result = new Result();
        TokenModel tokenModel = tokenManager.getPermissions(accessToken);
        List<Component> child = componentService.findChild(catId, Boolean.TRUE);
        result.setMessage("数据加载成功");
        result.setData(child);
        return result;
    }

    @RequiresPermissions("cat:updateCatName")
    @RequestMapping(value = "/api/cat/{catId}", method = RequestMethod.PATCH, produces = "application/json")
//    @ApiOperation(value = "修改类别名")
    public Result updateCatName(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                                /*@ApiParam(required = true, value = "类别id")*/ @PathVariable(name = "catId") Long catId,
                                /*@ApiParam(required = true, value = "新的类别名称")*/ @RequestParam String name) throws Exception {

        Result result = new Result();

        componentService.modifyCatName(catId, name);
        result.setMessage("修改成功");
        return result;
    }

    @RequiresPermissions("cat:deleteCat")
    @RequestMapping(value = "/api/cat/{catId}", method = RequestMethod.DELETE, produces = "application/json")
//    @ApiOperation(value = "删除组件分类")
    public Result deleteCat(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                            /*@ApiParam(required = true)*/ @PathVariable(name = "catId") Long catId) throws Exception {

        Result result = new Result();

        Component com = componentService.findOne(catId);//查询，先获取com.getSequence()
        List<Component> child = componentService.findChild(com.getParentId(), com.getCreatorId());//根据父节点的ID，获取该父节点下所有子节点
        Integer sequence = com.getSequence();
        if (sequence == child.size() - 1) {//已是最底下不需要更新
        } else {
            Integer number = sequence + 1;//删除一个，从下一个排序的数据开始更新所有sequence
            for (int i = number; i < child.size(); i++) {
                Component comList = child.get(i);
                comList.setSequence(comList.getSequence() - 1);
                componentService.update(comList.getId(), comList);
            }
        }

        componentService.delete(catId);
        result.setMessage("组件分类删除成功");
        return result;
    }

    @RequiresPermissions("element:deleteElement")
    @RequestMapping(value = "/api/element/{eleId}", method = RequestMethod.DELETE, produces = "application/json")
//    @ApiOperation(value = "删除组件参数")
    public Result deleteElement(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                                /*@ApiParam(required = true)*/ @PathVariable(name = "eleId") Long eleId) throws Exception {

        Result result = new Result();

        componentService.delete(eleId);
        result.setMessage("参数删除成功");
        return result;
    }

    @RequiresPermissions("widget:add")
    @RequestMapping(value = "/api/widget", method = RequestMethod.POST, produces = "application/json")
//    @ApiOperation(value = "控件注册", notes = "注册WEB控件，注册成功后不允许删除")
    public Result addElementType(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken,
                                 /*@ApiParam(required = true)*/ @RequestBody Widget widget) throws Exception {

        Result result = new Result();
        Widget  widget1 = new com.tipdm.framework.model.dmserver.Widget();
        BeanUtils.copyProperties(widget, widget1);
        componentService.saveWidget(widget1);
        result.setMessage("WEB控件注册成功");
        return result;
    }

    @RequiresPermissions("widget:list")
    @RequestMapping(value = "/api/widget/list", method = RequestMethod.GET, produces = "application/json")
//    @ApiOperation(value = "获取控件集合", notes = "获取平台上注册的WEB控件")
    public Result getElementTypes(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken) throws Exception {

        Result result = new Result();
        List<com.tipdm.framework.model.dmserver.Widget> types = componentService.findWidgetList();
        result.setMessage("数据加载成功");
        result.setData(types);
        return result;
    }

    @RequiresPermissions("algorithm:list")
    @RequestMapping(value = "/api/algorithm/list", method = RequestMethod.GET, produces = "application/json")
//    @ApiOperation(value = "获取算法集合")
    public Result getAlgorithms(/*@ApiParam(required = true, value = "用户访问令牌")*/ @RequestHeader("accessToken") String accessToken) throws Exception {

        Result result = new Result();

        LinkedHashMap<String, String> algorithms = PropertiesUtil.getProperties("/algoconfig/algorithms.properties");
        result.setMessage("数据加载成功");
        result.setData(algorithms);
        return result;
    }

}
