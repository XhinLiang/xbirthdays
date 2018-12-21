package com.xhinliang.birthdays;

import java.io.File;
import java.io.InputStream;

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

import com.kuaishou.xcall.XcallServer;
import com.kuaishou.xcall.core.parse.EvalKiller;
import com.kuaishou.xcall.core.parse.EvalOgnlKiller;
import com.kuaishou.xcall.core.parse.IEvalKiller;
import com.kuaishou.xcall.core.util.FunctionUtils;
import com.kuaishou.xcall.interfaze.FileLoader;
import com.kuaishou.xcall.interfaze.IBeanLoader;
import com.kuaishou.xcall.websocket.XcallWebSocketServer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.xhinliang.birthdays.common.db.repo"})
@EntityScan(basePackages = {"com.xhinliang.birthdays.common.db.model"})
@EnableScheduling
@EnableTransactionManagement
public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplication(MainApp.class).run(args);

        // fire threads.
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

        IEvalKiller evalKiller = new EvalOgnlKiller(beanLoader);
        FileLoader fileLoader = MainApp::getResourceAsFile;

        // CHECKSTYLE:OFF
        XcallWebSocketServer webSocketServer = new XcallWebSocketServer(10010, evalKiller, fileLoader);
        XcallServer xcallServer = new XcallServer(10086, evalKiller);
        // CHECKSTYLE:ON
        new Thread(() -> {
            try {
                webSocketServer.start();
            } catch (InterruptedException e) {
                logger.error("ops.", e);
            }
        }).start();

        new Thread(xcallServer::start).start();
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
