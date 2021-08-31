package com.hybridss.utilities.utilities.utils;


import com.hybridss.utilities.logger.LGFileLogger;
import com.hybridss.utilities.logger.LGLogger;

import java.io.File;
import java.util.ArrayList;
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

    private String username = "kops90@live.com";
    private String password = "migueloo2550082";
    private final ArrayList<String>  correos;


    public UTMail(ArrayList<String> correos) {
        this.correos = correos;

    }

    public UTMail(ArrayList<String> correos, String username, String password) {
        this.correos = correos;
        this.username = username;
        this.password = password;
    }

    public boolean enviarBitacora(String subject) {
        boolean response = false;
        try {
            String path = LGFileLogger.getmLogsPath();

            File file = new File(path);
            File[] logs = file.listFiles();

            MimeMultipart multipart = new MimeMultipart();

            for (File log : logs) {
                addAttachment(multipart,log.getAbsolutePath());
            }

            Message message = new MimeMessage(getSesion());
            message.setFrom(new InternetAddress(username));

            String[] recipientList = correos.toArray(new String[0]);
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddress);

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

    private boolean enviarHTML(String html, String subject){
        boolean response = false;

        try {

            Message message = new MimeMessage(getSesion());
            message.setFrom(new InternetAddress(username));

            for (String correo : correos){
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(correo));
            }

            message.setSubject(subject);
            message.setContent(html, "text/html; charset=utf-8");

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

    private void addAttachment(Multipart multipart, String filename) {
        try {
            DataSource source = new FileDataSource(filename);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        } catch (Exception e) {
            LGLogger.e(this.getClass().getSimpleName(),e);
        }

    }

    private Session getSesion(){
        Session session = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }

    private Properties getProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");
        return props;
    }
}
