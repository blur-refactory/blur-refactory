package com.blur.auth.config.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.protocol}")
    private String smtp;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.debug}")
    private String debug;

    @Value("${mail.smtp.starttls-tls-enable}")
    private String startles;

    @Value("${mail.smtp.ssl.trust}")
    private String sslTrust;

    @Value("${mail.smtp.ssl.enable}")
    private String sslEnable;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String userName;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.port}")
    private String port;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);

        javaMailSender.setPort(Integer.parseInt(port));

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", smtp);
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", startles);
        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.smtp.ssl.trust", sslTrust);
        properties.setProperty("mail.smtp.ssl.enable", sslEnable);
        return properties;
    }
}