/**
 *
 */
package de.immomio.config;

import de.immomio.mailsender.VelocityInternalizationLoader;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Configuration
@ComponentScan(basePackages = {"de.immomio.mail"})
public class CustomMailConfiguration {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.debug}")
    private String debug;

    @Value("${mail.smtp.auth}")
    private String needsAuthentication;

    @Value("${mail.smtp.ssl.enable}")
    private String sslEnable;

    @Value("${mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${mail.smtp.userset}")
    private String userset;

    @Value("${mail.mime.charset}")
    private String charset;

    @Bean
    public Properties mailProperties() {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", this.host);
        mailProperties.put("mail.smtp.port", this.port);
        mailProperties.put("mail.smtp.user", this.username);
        mailProperties.put("mail.smtp.password", this.password);
        mailProperties.put("mail.transport.protocol", this.protocol);
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.port", this.port);

        mailProperties.put("mail.debug", debug);
        mailProperties.put("mail.smtp.auth", needsAuthentication);
        mailProperties.put("mail.smtp.starttls.enable", starttlsEnable);
        mailProperties.put("mail.smtp.ssl.enable", sslEnable);
        mailProperties.put("mail.smtp.userset", userset);
        mailProperties.put("mail.smtp.charset", charset);

        mailProperties.put("mail.smtp.ssl.trust", host);

        return mailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setPort(this.port);
        javaMailSenderImpl.setUsername(this.username);
        javaMailSenderImpl.setPassword(this.password);

        javaMailSenderImpl.setJavaMailProperties(mailProperties());

        return javaMailSenderImpl;
    }

    @Bean
    public VelocityEngine velocityEngine() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityEngine(properties);
    }

    @Bean
    public VelocityInternalizationLoader velocityInternalizationLoader() {
        return new VelocityInternalizationLoader();
    }
}
