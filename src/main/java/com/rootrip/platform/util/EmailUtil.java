package com.rootrip.platform.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.rootrip.platform.Application;
import com.rootrip.platform.Platform;

public class EmailUtil {
	
	protected final static Logger log = Logger.getLogger(EmailUtil.class);
	
	public static boolean sendEmail(String[] targets, String title, String content) {

		Application app = Platform.getInstance().getApp();
		if(app == null) return false;
		
		String smtpHost = app.getConfigValue("email.smtpHost");
		String username = app.getConfigValue("email.username");
		String password = app.getConfigValue("email.password");
		String from = app.getConfigValue("email.from");
		
		if(StringUtils.isBlank(smtpHost) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			log.error("邮箱参数没有正确设置,邮件发送失败!");
			return false;
		}
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "true");
			Session s = Session.getDefaultInstance(props);

			// s.setDebug(true);//打开调试信息
			Transport transport = s.getTransport("smtp");
			transport.connect(smtpHost, username, password);
			// 生成邮件
			MimeMessage mimeMessage = new MimeMessage(s);
			mimeMessage.setSubject(title);
			mimeMessage.setFrom(new InternetAddress(from));
			InternetAddress[] addresses = new InternetAddress[targets.length];
			for(int i=0; i<targets.length; i++) {
				addresses[i] = new InternetAddress(targets[i]);
			}
			mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
			if (transport.isConnected()) {
				// 给消息对象设置内容
				// 新建一个存放信件内容的BodyPart对象
				BodyPart mdp = new MimeBodyPart();
				// 给BodyPart对象设置内容和格式/编码方式
				mdp.setContent(content, "text/html;charset=UTF-8");
				// 新建一个MimeMultipart对象用来存放BodyPart对象(事实上可以存放多个)
				Multipart mm = new MimeMultipart();
				// 将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
				mm.addBodyPart(mdp);
				// 把mm作为消息对象的内容
				mimeMessage.setContent(mm);
				// 设置发信时间
				mimeMessage.setSentDate(new Date());

				// 发送邮件
				// 存储邮件信息
				mimeMessage.saveChanges();
				// 发送邮件,其中第二个参数是所有
				transport.sendMessage(mimeMessage, mimeMessage
						.getAllRecipients());
				transport.close();
				return true;
			} else {
				throw new Exception("transport已关闭");
			}
		} catch (Exception e) {
			log.error("邮件发送失败", e);
			return false;
		}

	}
	
	public static boolean sendEmail(String target, String title, String content) {
		return sendEmail(new String[]{target}, title, content);
	}

}
