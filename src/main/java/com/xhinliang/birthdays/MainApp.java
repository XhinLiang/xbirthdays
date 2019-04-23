package com.xhinliang.birthdays;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.phantomthief.scope.Scope;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.xhinliang.jugg.handler.IJuggInterceptor;
import com.xhinliang.jugg.handler.JuggCheckHandler;
import com.xhinliang.jugg.handler.JuggEvalHandler;
import com.xhinliang.jugg.handler.JuggLoginHandler;
import com.xhinliang.jugg.loader.FlexibleBeanLoader;
import com.xhinliang.jugg.loader.IBeanLoader;
import com.xhinliang.jugg.parse.IJuggEvalKiller;
import com.xhinliang.jugg.parse.mvel.JuggMvelEvalKiller;
import com.xhinliang.jugg.plugin.alias.JuggAliasHandler;
import com.xhinliang.jugg.plugin.dump.JuggDumpHandler;
import com.xhinliang.jugg.plugin.help.JuggHelpHandler;
import com.xhinliang.jugg.plugin.history.JuggHistoryHandler;
import com.xhinliang.jugg.plugin.insight.JuggInsightHandler;
import com.xhinliang.jugg.plugin.preload.JuggPreloadHandler;
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

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private static final int JUGG_PORT = 10010;

    private static final List<String> PREFER_FQCN = ImmutableList.<String> builder() //
            .add("java.") //
            .add("javax.") //
            .add("com.google.common") //
            .add("org.springframework") //
            .add("org.apache.commons.collections4") //
            .add("org.apache.commons.lang3") //
            .add("com.github.phantomthief") //
            .build();

    private static final Map<String, String> USER_PASSWORD = ImmutableMap.<String, String> builder() //
            .put("xhinliang", "1l1l1l") //
            .build();

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = new SpringApplication(MainApp.class).run(args);

        // jugg threads.
        IBeanLoader beanLoader = new FlexibleBeanLoader(PREFER_FQCN) {

            @Nullable
            @Override
            protected Object getActualBean(String name) {
                try {
                    return configurableApplicationContext.getBean(name);
                } catch (Exception ex) {
                    return null;
                }
            }

            @Nullable
            @Override
            public Object getBeanByClass(@Nonnull Class<?> clazz) {
                try {
                    return configurableApplicationContext.getBean(clazz);
                } catch (Exception ex) {
                    return null;
                }
            }
        };

        IJuggEvalKiller evalKiller = new JuggMvelEvalKiller(beanLoader) {

            @Override
            public Object eval(String command, String username) {
                logger.info("user:{}, call:{}", username, command);
                return super.eval(command, username);
            }
        };

        // 配置 Handlers
        List<IJuggInterceptor> handlers = Lists.newArrayList(//
                context -> {
                    logger.info("begin mvel scope");
                    Scope.beginScope();
                },
                // 自带的登录 handler
                new JuggLoginHandler((username, password) -> password.equals(USER_PASSWORD.get(username))), //
                // check
                new JuggCheckHandler(commandContext -> true), //
                // alias handler
                new JuggAliasHandler(beanLoader), //
                // history
                new JuggHistoryHandler(evalKiller), //
                // insight
                new JuggInsightHandler(evalKiller), //
                new JuggDumpHandler(evalKiller), //
                new JuggPreloadHandler(evalKiller), //
                // eval handler
                new JuggEvalHandler(evalKiller), //
                context -> {
                    logger.info("end mvel scope");
                    Scope.endScope();
                });

        handlers.add(0, new JuggHelpHandler(handlers));
        new JuggWebSocketServer(JUGG_PORT, handlers, MainApp::getResourceAsFile).startOnNewThread();
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
