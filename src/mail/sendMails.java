package mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.sun.mail.util.MailSSLSocketFactory;

public class sendMails {
	public static Logger log = Logger.getLogger("Test");
    public void sendMail(String mailAdr,String subject,String content,File attachment) throws UnsupportedEncodingException, GeneralSecurityException{
      
        final Properties props = new Properties();
    
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
       
        props.put("mail.user", "495149700@qq.com");

        props.put("mail.password", "sfletzinvplfbije");
        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        

        Session mailSession = Session.getInstance(props, authenticator);

        MimeMessage message = new MimeMessage(mailSession);

        try{
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"));
            message.setFrom(form);


            InternetAddress to = new InternetAddress(mailAdr);
            message.setRecipient(RecipientType.TO, to);
            
            String cc[] = { "1543913085@qq.com", "zhaoxuepeng@uniview.com" };
            InternetAddress[] ccTo = new InternetAddress[cc.length];  
            for (int i = 0; i < cc.length; i++) {  
                System.out.println("发送到:" + cc[i]);  
                ccTo[i] = new InternetAddress(cc[i]);  
            }  
            message.setRecipients(RecipientType.CC, ccTo);


            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
               
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            
            message.setSubject(subject);

            message.setContent(content, "text/html;charset=UTF-8");

            Transport.send(message, message.getAllRecipients());
        }catch(Exception e){
        	log.error(e.getStackTrace());
        }finally {  
           
     }  
    }
}
