package com.unik.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${email.host:smtp.example.com}")
    private String host;

    @Value("${email.port:587}")
    private int port;

    @Value("${email.username:your-email@example.com}")
    private String username;

    @Value("${email.password:your-password}")
    private String password;

    @Value("${email.protocol:smtp}")
    private String protocol;

    @Value("${email.auth:true}")
    private String auth;

    @Value("${email.starttls:true}")
    private String starttls;

    @Value("${email.debug:false}")
    private String debug;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
