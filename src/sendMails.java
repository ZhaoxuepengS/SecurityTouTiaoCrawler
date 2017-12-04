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
	   /*
     * mailAdr �ռ��˵�ַ
     * subject �ʼ�����
     * content �ʼ��ı�����
     */
    public void sendMail(String mailAdr,String subject,String content){
        //���÷����ʼ��Ļ�������
        final Properties props = new Properties();
        // ��ʾSMTP�����ʼ�����Ҫ���������֤
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        //smtp��½���˺š����� ���迪��smtp��½
        props.put("mail.user", "495149700@qq.com");
        // ����SMTP����ʱ��Ҫ�ṩ������,���������½���룬һ�㶼�ж���smtp�ĵ�½����
        props.put("mail.password", "XXXXX");
        
        // ������Ȩ��Ϣ�����ڽ���SMTP���������֤
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // �û���������
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        
        // ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
        Session mailSession = Session.getInstance(props, authenticator);
        // �����ʼ���Ϣ
        MimeMessage message = new MimeMessage(mailSession);
        // ���÷�����
        try{
            InternetAddress form = new InternetAddress(
                    props.getProperty("mail.user"));
            message.setFrom(form);

            // �����ռ���
            InternetAddress to = new InternetAddress(mailAdr);
            message.setRecipient(RecipientType.TO, to);
            
            // ���ó���
            //InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
            //message.setRecipient(RecipientType.CC, cc);

            // �������ͣ��������ռ��˲��ܿ������͵��ʼ���ַ
            //InternetAddress bcc = new InternetAddress("aaaaa@163.com");
            //message.setRecipient(RecipientType.CC, bcc);
            
            // �����ʼ�����
            message.setSubject(subject);
            // �����ʼ���������
            message.setContent(content, "text/html;charset=UTF-8");
            // �����ʼ�
            Transport.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
}
