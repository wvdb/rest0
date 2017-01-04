package config;

import domain.Greeting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import service.MailService;

import java.util.Properties;

/**
 * Class JavaMailBean.
 *
 * @author Wim Van den Brande
 * @since 12/12/2016 - 14:45
 */
@Configuration
public class MailConfig {
    public JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPort(8025);
        javaMailSender.setHost("localhost");
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    public SimpleMailMessage getSimpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("wimvandenbrande.home@gmail.com");
        simpleMailMessage.setFrom("wim@hpcds.com");
        simpleMailMessage.setText("Dear %s, we are %s today");
        simpleMailMessage.setSubject("dit is een test");
        return simpleMailMessage;
    }

    @Bean
    public MailService mailService() {
        return new MailService(this.getMailSender(), this.getSimpleMailMessage());
    }

    @Bean
    public Greeting greeting() {
        return new Greeting(1l, "dit is een test");
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.starttls.enable", "false");
        properties.setProperty("mail.debug", "false");
        return properties;
    }
}
