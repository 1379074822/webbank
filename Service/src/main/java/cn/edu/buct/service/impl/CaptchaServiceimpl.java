package cn.edu.buct.service.impl;

import cn.edu.buct.service.CaptchaService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service("captchaService")
public class CaptchaServiceimpl implements CaptchaService {
    public static Logger logger= LoggerFactory.getLogger( CaptchaServiceimpl.class);
    @Autowired
    private Producer captchaProducer;
    @Override
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        System.out.println("生成验证码文本===="+capText);
        //利用生成的字符串构建图片
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getStackTrace().toString());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getStackTrace().toString());
            }
        }
    }
}
