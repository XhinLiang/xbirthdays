package com.xhinliang.birthdays;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.kuaishou.xcall.core.alias.AliasService;
import com.kuaishou.xcall.core.alias.AliasServiceImpl;
import com.kuaishou.xcall.core.parse.CommandParser;
import com.kuaishou.xcall.core.parse.IBeanLoader;
import com.kuaishou.xcall.core.parse.ICommandParser;
import com.kuaishou.xcall.core.parse.ParameterHelper;
import com.kuaishou.xcall.core.security.IAllowInvokeChecker;
import com.kuaishou.xcall.websocket.FileLoader;
import com.kuaishou.xcall.websocket.XcallWebSocketServer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.xhinliang.birthdays.common.db.repo" })
@EntityScan(basePackages = { "com.xhinliang.birthdays.common.db.model" })
@EnableScheduling
@EnableTransactionManagement
public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplication(MainApp.class).run(args);

        // fire threads.
        AliasService aliasService = AliasServiceImpl.instance();
        IAllowInvokeChecker alwaysAllowChecker = (target, method) -> true;
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

        ICommandParser commandParser = new CommandParser(aliasService, new ParameterHelper(aliasService), //
                alwaysAllowChecker, beanLoader);

        FileLoader fileLoader = MainApp::getResourceAsFile;

        // CHECKSTYLE:OFF
        XcallWebSocketServer webSocketServer = new XcallWebSocketServer(10010, commandParser, fileLoader);
        XcallServer xcallServer = new XcallServer(10086, commandParser);
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

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                // copy stream
                // CHECKSTYLE:OFF
                byte[] buffer = new byte[1024];
                // CHECKSTYLE:ON
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            return null;
        }
    }
}
