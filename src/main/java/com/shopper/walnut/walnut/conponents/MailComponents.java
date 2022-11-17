package com.shopper.walnut.walnut.conponents;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class MailComponents {
    private final JavaMailSender javaMailSender;

    public boolean sendMail(String mail, String subject, String text) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
    public void sendMailWithFiles(String email, String subject, String urlFileName) throws MessagingException, IOException {
        MailHandler mailHandler = new MailHandler(javaMailSender);

        mailHandler.setTo(email);
        mailHandler.setSubject(subject);
        String htmlContent = " <img src='cid:eventImg'>";
        mailHandler.setText(htmlContent, true);
        mailHandler.setInline("eventImg", urlFileName);
        mailHandler.send();
    }
}
