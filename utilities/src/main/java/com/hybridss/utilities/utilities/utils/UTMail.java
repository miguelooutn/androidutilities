package com.hybridss.utilities.utilities.utils;


import com.hybridss.utilities.logger.LGFileLogger;

import java.io.File;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import eu.ocathain.javax.activation.DataHandler;
import eu.ocathain.javax.activation.DataSource;
import eu.ocathain.javax.activation.FileDataSource;

public class UTMail {

    private final String username = "kops90@live.com";
    private final String password = "migueloo2550082";
    private final String correo;



    public UTMail(String correo) {
        this.correo = correo;

    }

    public boolean enviarBitacora(String subject) {
        boolean response = false;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            String path = LGFileLogger.getmLogsPath();

            File file = new File(path);
            File[] logs = file.listFiles();


            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            DataSource source = new FileDataSource(logs[0]);
            messageBodyPart.setDataHandler(new DataHandler(source));

            multipart.addBodyPart(messageBodyPart);


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correo));
            message.setSubject(subject);
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");
            response = true;

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
        return response;
    }

}
