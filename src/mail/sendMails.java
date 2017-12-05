package mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendMails {

    public void sendMail(String mailAdr,String subject,String content){
      
        final Properties props = new Properties();
    
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
       
        props.put("mail.user", "495149700@qq.com");

        props.put("mail.password", "sfletzinvplfbije");
 
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
            
            String cc[] = { "wangyuqi@uniview.com", "zhaoxuepeng@qq.com" };
            InternetAddress[] ccTo = new InternetAddress[cc.length];  
            for (int i = 0; i < cc.length; i++) {  
                System.out.println("发送到:" + cc[i]);  
                ccTo[i] = new InternetAddress(cc[i]);  
            }  
            message.setRecipients(RecipientType.CC, ccTo);

            
            //InternetAddress bcc = new InternetAddress("aaaaa@163.com");
            //message.setRecipient(RecipientType.CC, bcc);
            

            message.setSubject(subject);

            message.setContent(content, "text/html;charset=UTF-8");

            Transport.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
}
