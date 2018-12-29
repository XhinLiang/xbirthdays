package com.xhinliang.birthdays;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
import com.xhinliang.godcall.handler.GodCallCoreHandler;
import com.xhinliang.godcall.handler.GodCallLoginHandler;
import com.xhinliang.godcall.handler.IGodCallHandler;
import com.xhinliang.godcall.interfaze.FileLoader;
import com.xhinliang.godcall.interfaze.IBeanLoader;
import com.xhinliang.godcall.parse.GodCallEvalKiller;
import com.xhinliang.godcall.util.FunctionUtils;
import com.xhinliang.godcall.websocket.GodCallWebSocketServer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.xhinliang.birthdays.common.db.repo" })
@EntityScan(basePackages = { "com.xhinliang.birthdays.common.db.model" })
@EnableScheduling
@EnableTransactionManagement
public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    private static final Map<String, Map> LOCAL_CONTEXT = new HashMap<>();

    private static final Map<String, String> USER_PASSWORD = ImmutableMap.<String, String> builder().put("xhinliang", "1l1l1l") //
            .build();

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplication(MainApp.class).run(args);

        // godcall threads.
        IBeanLoader beanLoader = new IBeanLoader() {

            @Nullable
            @Override
            public Object getBeanByName(String name) {
                try {
                    return configurableApplicationContext.getBean(name);
                } catch (NoSuchBeanDefinitionException catchE) {
                    return null;
                }
            }

            @Nonnull
            @Override
            public Class<?> getClassByName(String name) throws ClassNotFoundException {
                return Class.forName(name);
            }

            @Nullable
            @Override
            public Object getBeanByClass(@Nonnull Class<?> clazz) {
                try {
                    return configurableApplicationContext.getBean(clazz);
                } catch (NoSuchBeanDefinitionException e) {
                    return null;
                }
            }
        };

        GodCallEvalKiller evalKiller = new GodCallEvalKiller(beanLoader);
        FileLoader fileLoader = MainApp::getResourceAsFile;

        List<IGodCallHandler> handlers = Lists.newArrayList(//
                new GodCallLoginHandler((username, password) -> password.equals(USER_PASSWORD.get(username))), //
                new GodCallCoreHandler(evalKiller) //
        );

        evalKiller.setLocalContextSupplier(commandContext -> LOCAL_CONTEXT //
                .computeIfAbsent(commandContext.getGodCallUser().getUserName(), (user) -> new HashMap()) //
        );

        evalKiller.setChecker(commandContext -> {
            logger.info("user:{}, eval:{}", commandContext.getGodCallUser().getUserName(), commandContext.getCommand());
            return true;
        });

        // CHECKSTYLE:OFF
        GodCallWebSocketServer webSocketServer = new GodCallWebSocketServer(10010, handlers, fileLoader);
        // CHECKSTYLE:ON
        new Thread(() -> {
            try {
                webSocketServer.start();
            } catch (InterruptedException e) {
                logger.error("ops.", e);
            }
        }).start();
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
