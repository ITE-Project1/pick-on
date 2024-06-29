package com.ite.pickon.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EnvConfig {
    private static Dotenv dotenv;

    @PostConstruct
    public void init() {
        // 클래스패스에서 .env 파일을 로드
        dotenv = Dotenv.configure().load();
    }

    public static String getEnv(String key) {
        return dotenv.get(key);
    }
}