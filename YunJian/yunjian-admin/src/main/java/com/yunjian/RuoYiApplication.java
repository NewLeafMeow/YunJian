package com.yunjian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("╔════════════════════════════╗\n" +
                "║    successfully booted√    ║\n" +
                "║      ┌────────────────┐    ║\n" +
                "║      │  ▄▄▄· ▄▄▄ .    │    ║\n" +
                "║      │ ▐█ ▀█ ▀▄.▀·    │    ║\n" +
                "║      │ ▄█▀▀█ ▐▀▀▪▄    │    ║\n" +
                "║      │ ▐█▄▪▐█▐█▄▄▌    │    ║\n" +
                "║      │  ▀▀▀· ▀▀▀▀     │    ║\n" +
                "║      └────────────────┘    ║\n" +
                "║     v2.0.0        in:8081  ║\n" +
                "╚════════════════════════════╝\n" +
                "┌────────────────────────────┐\n" +
                "  系统已启动，等待前端连接....\n" +
                "└────────────────────────────┘");
    }
}
