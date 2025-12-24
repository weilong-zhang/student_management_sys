package com.itzhang.management.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * @param email code
     * @return java.lang.Boolean
     * @Description 发送验证码工具类
     * @Author weiloong_zhang
     */
    public Boolean sendCodeEmail(String email, String code) {
        //是否发送成功标记
        Boolean isSend = false;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("zwl1425@163.com");
            // 设置收件人
            helper.setTo(email);
            helper.setSubject("邮箱验证码");

            // 填充 Thymeleaf 模板
            Context context = new Context();

            String[] codeList = code.split("");

            context.setVariable("verifyCode", codeList);
            String htmlContent = templateEngine.process("emailTemplate", context);

            // 发送 HTML 内容
            helper.setText(htmlContent, true);

            mailSender.send(message);
            isSend = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSend = false;
        }

        return isSend;
    }
}
