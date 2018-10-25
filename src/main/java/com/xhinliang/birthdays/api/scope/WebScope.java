package com.xhinliang.birthdays.api.scope;

import com.github.phantomthief.scope.ScopeKey;
import com.xhinliang.framework.component.util.JsonMapperUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * @author xhinliang
 */
public class WebScope {

    private static final ScopeKey<Map<String, Object>> JSON_PARAMS = ScopeKey.withInitializer(WebScope::parseRequestJson);

    @SuppressWarnings("unchecked")
    public static <T> T getJsonParam(String name) {
        Object value = JSON_PARAMS.get().get(name);
        return (T) value;
    }

    private static Map<String, Object> parseRequestJson() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //noinspection ConstantConditions
        if (request == null || !isJsonRequest(request)) {
            return Collections.emptyMap();
        }
        try {
            InputStream input = request.getInputStream();
            Throwable var2 = null;
            Map<String, Object> var6;
            try {
                byte[] bytes = IOUtils.toByteArray(input);
                String encoding = StringUtils.defaultIfBlank(request.getCharacterEncoding(), "UTF-8");
                String content = new String(bytes, encoding);
                var6 = JsonMapperUtils.fromJson(content);
            } catch (Throwable var16) {
                var2 = var16;
                throw var16;
            } finally {
                if (input != null) {
                    if (var2 != null) {
                        try {
                            input.close();
                        } catch (Throwable var15) {
                            var2.addSuppressed(var15);
                        }
                    } else {
                        input.close();
                    }
                }
            }
            return var6;
        } catch (Exception var18) {
            return Collections.emptyMap();
        }
    }

    private static boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType != null && contentType.toLowerCase().contains("application/json");
    }
}
