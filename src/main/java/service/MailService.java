package service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Class MailService.
 *
 * @author Wim Van den Brande
 * @since 16/12/2016 - 15:53
 */
public class MailService {
    private JavaMailSender  mailSender;
   	private SimpleMailMessage simpleMailMessage;

    public void sendMail(String title, String day) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(simpleMailMessage.getTo());
            helper.setSubject(simpleMailMessage.getSubject());
            helper.setText(String.format(simpleMailMessage.getText(), title, day));

            FileSystemResource file = new FileSystemResource("C:\\temp\\new 1.txt");
            helper.addAttachment(file.getFilename(), file);

        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public SimpleMailMessage getSimpleMailMessage() {
        return simpleMailMessage;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void setMailSender(JavaMailSender  mailSender) {
        this.mailSender = mailSender;
    }

}
