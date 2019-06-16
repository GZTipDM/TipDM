package com.tipdm.framework.dmserver.interceptor;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.RateLimiter;
import com.tipdm.framework.dmserver.interceptor.model.URLLimitMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流器
 * Created by TipDM on 2018/1/28.
 * E-mail:devp@tipdm.com
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private RateLimiter globalRateLimiter;

    private Properties urlProperties;

    private Map<PatternsRequestCondition, RateLimiter> urlRateMap;

    private UrlPathHelper urlPathHelper;

    private int globalRate;

    private List<URLLimitMapping> limitMappings;

    public RateLimitInterceptor() {
        this(0);
    }

    /**
     * @param globalRate 全局请求限制qps
     */
    public RateLimitInterceptor(int globalRate) {
        urlPathHelper = new UrlPathHelper();
        this.globalRate = globalRate;
        if (globalRate > 0)
            globalRateLimiter = RateLimiter.create(globalRate);
    }

    /**
     * @param globalRate 全局请求限制qps
     */
    public void setGlobalRate(int globalRate) {
        this.globalRate = globalRate;
        if (globalRate > 0)
            globalRateLimiter = RateLimiter.create(globalRate);
    }

    /**
     * url限流
     *
     * @param urlProperties url表达式->qps propertis
     */
    public void setUrlProperties(Properties urlProperties) {
        if (urlRateMap == null)
            urlRateMap = new ConcurrentHashMap();
        fillRateMap(urlProperties, urlRateMap);
        this.urlProperties = urlProperties;
    }

    /**
     * url限流
     *
     * @param limitMappings url表达式 array->qps mapping
     */
    public void setLimitMappings(List<URLLimitMapping> limitMappings) {
        if (urlRateMap == null)
            urlRateMap = new ConcurrentHashMap();
        fillRateMap(limitMappings, urlRateMap);
        this.limitMappings = limitMappings;
    }

    /**
     * 将url表达转换为PatternsRequestCondition，并生成对应RateLimiter
     * 保存
     *
     * @param controllerProperties
     * @param map
     */
    private void fillRateMap(Properties controllerProperties, Map<PatternsRequestCondition, RateLimiter> map) {
        if (controllerProperties != null) {
            for (String key : controllerProperties.stringPropertyNames()) {
                String value = controllerProperties.getProperty(key);
                if (value.matches("[0-9]*[1-9][0-9]*")) {
                    map.put(new PatternsRequestCondition(key), RateLimiter.create(Double.valueOf(value)));
                } else {
                    logger.info(key + " 的值" + value + " 不是一个合法的限制");
                }
            }
        }
    }

    /**
     * 将url表达转换为PatternsRequestCondition，并生成对应RateLimiter
     * 保存
     *
     * @param limitMappings
     * @param map
     */
    private void fillRateMap(List<URLLimitMapping> limitMappings, Map<PatternsRequestCondition, RateLimiter> map) {
        if (limitMappings != null) {
            for (URLLimitMapping mapping : limitMappings) {
                map.put(new PatternsRequestCondition(mapping.getUrls()), RateLimiter.create(Double.valueOf(mapping.getRate())));
            }
        }
    }




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (urlRateMap != null) {
            String lookupPath = urlPathHelper.getLookupPathForRequest(request);
            for (PatternsRequestCondition patternsRequestCondition : urlRateMap.keySet()) {
                //使用spring DispatcherServlet的匹配器PatternsRequestCondition进行匹配
                List<String> matches = patternsRequestCondition.getMatchingPatterns(lookupPath);
                if (!matches.isEmpty()) {
                    if (urlRateMap.get(patternsRequestCondition).tryAcquire()) {
                        logger.info(lookupPath + " 请求匹配到" + Joiner.on(",").join(patternsRequestCondition.getPatterns()) + "限流器");
                    } else {
                        logger.info(lookupPath + " 请求超过" + Joiner.on(",").join(patternsRequestCondition.getPatterns()) + "限流器速率");
                        return false;
                    }

                }
            }
        }

        //全局限流
        if (globalRateLimiter != null) {
            if (!globalRateLimiter.tryAcquire()) {
                return false;
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
