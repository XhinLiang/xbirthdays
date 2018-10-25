package com.xhinliang.framework.mvc.resolver;

import com.xhinliang.birthdays.api.scope.WebScope;
import com.xhinliang.framework.component.util.JsonMapperUtils;
import com.xhinliang.framework.mvc.annotations.JsonParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.xhinliang.framework.mvc.annotations.JsonParam.DEFAULT_HOLDER;

/**
 * @author xhinliang
 */
public class JsonParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@Nonnull MethodParameter parameter) {
        return parameter.getParameterAnnotation(JsonParam.class) != null;
    }

    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter, ModelAndViewContainer mvContainer,
                                  @Nonnull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JsonParam jsonParam = parameter.getParameterAnnotation(JsonParam.class);
        if (jsonParam == null) {
            return null;
        }
        Object result = resolveArgFromRequestJson(parameter, jsonParam);
        return result;
    }

    private Object resolveArgFromRequestJson(MethodParameter parameter, JsonParam jsonParam) {
        String paramName = jsonParam.value();
        if (parameter.getParameterType() == List.class) {
            return getJsonList(paramName);
        } else if (parameter.getParameterType() == Map.class) {
            return getJsonMap(paramName);
        } else {
            Class<?> type = parameter.getParameterType();
            Object value = getJson(paramName, type);
            if (value != null) {
                return value;
            }
            if (!StringUtils.equals(jsonParam.defaultValue(), DEFAULT_HOLDER)) {
                return JsonMapperUtils.value(jsonParam.defaultValue(), type);
            }
            return null;
        }
    }

    private static <T> T getJson(String key, Class<T> type) {
        Object value = WebScope.getJsonParam(key);
        return JsonMapperUtils.value(value, type);
    }

    private static <T> List<T> getJsonList(String key) {
        return WebScope.getJsonParam(key);
    }

    private static <K, V> Map<K, V> getJsonMap(String key) {
        return WebScope.getJsonParam(key);
    }
}
