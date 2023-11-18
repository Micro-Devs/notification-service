package com.microdevs.notificationservice.service;

import com.microdevs.notificationservice.dto.NotificationDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;

    public void sendEmail(String to, String subject, String htmlContext) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContext, true);
        mailSender.send(message);
    }

    public void sendRegisterMail(NotificationDto registerDto) throws MessagingException {
        String base64Image = imageConvertToBase64();
        Context context = new Context();
        context.setVariable("image", base64Image);
        context.setVariable("username", registerDto.getMap().get("username"));
        context.setVariable("activationLink", registerDto.getMap().get("activationLink"));
        String htmlContext = templateEngine.process("register-template.html", context);
        sendEmail(registerDto.getTo(), "Kayıt işlemini tamamla.", htmlContext);
    }

    private String imageConvertToBase64() {
        String imagePath = "classpath:logo.png";
        Resource resource = resourceLoader.getResource(imagePath);
        try (InputStream inputStream = resource.getInputStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] imageBytes = buffer.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
