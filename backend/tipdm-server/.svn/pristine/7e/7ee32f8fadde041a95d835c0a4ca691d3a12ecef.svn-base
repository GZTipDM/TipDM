package com.tipdm.framework.controller;

import com.alibaba.fastjson.JSON;
import com.tipdm.framework.common.Constants;
import com.tipdm.framework.common.controller.Result;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.exception.DuplicateException;
import com.tipdm.framework.dmserver.exception.ElementNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.postgresql.util.PSQLException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by TipDM on 2016/12/8.
 * E-mail:devp@tipdm.com
 */
@SuppressWarnings("all")
@ControllerAdvice
public class ExceptionHandlerController {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleUnAuthorizedException(UnauthorizedException ex, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setMessage("您没有权限执行此操作，权限代号：" + StringKit.substringBetween(ex.getMessage(), "[", "]"));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        printErrorMsg(result, response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public void handleUnAuthenticationException(UnauthenticatedException ex, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setMessage("无效的token或token已过期");
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAuthenticationException(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setMessage("无效的token或token已过期");
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setMessage("操作失败," + formatErrMsg(ex));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandler(NoHandlerFoundException ex) {
        return "404";
    }
    /**
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handlerException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        result.setStatus(Result.Status.FAIL);
        result.setMessage(formatErrMsg(exception));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlerFileTooBigException(MaxUploadSizeExceededException exception, HttpServletRequest request, HttpServletResponse response) {

        Result result = new Result();
        result.setStatus(Result.Status.FAIL);
        result.setMessage("数据文件不能大于" + ((MaxUploadSizeExceededException) exception).getMaxUploadSize() /1024/1024 + "M");
        printErrorMsg(result, response);
    }
    /**
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handlerPSQLException(PSQLException exception, HttpServletRequest request, HttpServletResponse response){

        Result result = new Result();
        result.setMessage(formatErrMsg(exception));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlerDuplicateException(DuplicateException exception, HttpServletRequest request, HttpServletResponse response){

        Result result = new Result();
        result.setMessage(formatErrMsg(exception));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerElementNotFoundException(ElementNotFoundException exception, HttpServletRequest request, HttpServletResponse response){

        Result result = new Result();
        result.setMessage(formatErrMsg(exception));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        printErrorMsg(result, response);
    }

    @ExceptionHandler(SchedulerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerSchedulerException(SchedulerException exception, HttpServletRequest request, HttpServletResponse response){

        Result result = new Result();
        result.setMessage(formatErrMsg(exception));
        result.setStatus(Result.Status.FAIL);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        printErrorMsg(result, response);
    }

    private void printErrorMsg(Result result, HttpServletResponse response){
        response.setCharacterEncoding(Constants.CHARACTER);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setContentType("application/json;charset=utf-8");

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(result));
            out.flush();
        } catch (Exception e) {

        } finally {
            if(null != out) {
                out.close();
            }
        }
    }

    private String formatErrMsg(Exception exception){
        String errorMsg = ExceptionUtils.getRootCauseMessage(exception);
        if(StringKit.contains(errorMsg, ": ")){
            errorMsg = StringKit.substringAfter(errorMsg, ": ");
        }
        return errorMsg;
    }
}
