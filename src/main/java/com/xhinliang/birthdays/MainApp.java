package com.xhinliang.birthdays;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.xhinliang.jugg.handler.IJuggInterceptor;
import com.xhinliang.jugg.handler.JuggAliasHandler;
import com.xhinliang.jugg.handler.JuggCheckHandler;
import com.xhinliang.jugg.handler.JuggEvalHandler;
import com.xhinliang.jugg.handler.JuggLoginHandler;
import com.xhinliang.jugg.loader.FlexibleBeanLoader;
import com.xhinliang.jugg.loader.IBeanLoader;
import com.xhinliang.jugg.parse.JuggEvalKiller;
import com.xhinliang.jugg.util.FunctionUtils;
import com.xhinliang.jugg.websocket.JuggWebSocketServer;

/**
 * @author xhinliang
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.xhinliang.birthdays.common.db.repo" })
@EntityScan(basePackages = { "com.xhinliang.birthdays.common.db.model" })
@EnableScheduling
@EnableTransactionManagement
public class MainApp {

    private static final int JUGG_PORT = 10010;

    private static final Map<String, String> USER_PASSWORD = ImmutableMap.<String, String> builder() //
            .put("xhinliang", "1l1l1l") //
            .build();

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplication(MainApp.class).run(args);

        // jugg threads.
        IBeanLoader beanLoader = new FlexibleBeanLoader() {

            @Override
            protected Object getActualBean(String name) {
                return configurableApplicationContext.getBean(name);
            }

            @Override
            public Object getBeanByClass(@Nonnull Class<?> clazz) {
                return configurableApplicationContext.getBean(clazz);
            }
        };

        JuggEvalKiller evalKiller = new JuggEvalKiller(beanLoader);

        List<IJuggInterceptor> handlers = Lists.newArrayList(//
                new JuggLoginHandler((username, password) -> password.equals(USER_PASSWORD.get(username))), //
                new JuggAliasHandler(beanLoader), //
                new JuggCheckHandler(commandContext -> true), //
                new JuggEvalHandler(evalKiller) //
        );

        JuggWebSocketServer webSocketServer = new JuggWebSocketServer(JUGG_PORT, handlers, MainApp::getResourceAsFile);
        webSocketServer.startOnNewThread();
    }

    @SuppressWarnings("Duplicates")
    private static File getResourceAsFile(String resourcePath) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(resourcePath);
            InputStream in = classPathResource.getInputStream();
            return FunctionUtils.getTempFileFromInputStream(in);
        } catch (Exception e) {
            return null;
        }
    }
}
