package cn.edu.buct.controller;

import cn.edu.buct.entity.User;
import cn.edu.buct.global.Sha;
import cn.edu.buct.service.LoginService;
import cn.edu.buct.service.global.TransAndSend;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;

@Controller("globalController")
public class GlobalController {

    @Autowired
    private LoginService loginService;
    private static final String SYMBOLS = "0123456789"; // 数字
    private static final Random RANDOM = new SecureRandom();

    public static String getNonce_str() {
        // 如果需要4位，那 new char[4] 即可，其他位数同理可得
        char[] nonceChars = new char[4];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
    private void setNullAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.setAttribute(attrName,null);
                timer.cancel();
            }
        }, 10 * 60 * 1000);
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "loginUI";
    }

    @RequestMapping("/to_register")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/index")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/quit")
    public String quit(HttpSession session) {
        session.setAttribute("user", null);
        return "redirect:to_login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> submit(HttpSession session, @RequestParam String requestData, @RequestParam String encryptKey) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map = loginService.login(session, requestData, encryptKey);
        return map;
    }

    //注册处理
    @RequestMapping("/firstregister")
    @ResponseBody
    public Map register(HttpSession session,@RequestParam String username,@RequestParam String loginPassword,@RequestParam String accountNO,@RequestParam String phoneNo,@RequestParam String password) throws IOException {
//        String code = getNonce_str();
//        session.setAttribute("code",code);
//        this.setNullAttrbute(session,"code");
        Map<String,Object> map = new HashMap<>();
        String passWord = Sha.SHA(password + "BUCT", "SHA-256");
        String str;
        str = TransAndSend.createRegistValidateJson(accountNO,phoneNo,passWord);
        String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/RegistValidate.do", str);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Map<String,Object> returnJson = (Map<String,Object>) jsonObject;
        Map<String,Object> body =(Map<String,Object>) returnJson.get("Body");
        String name = (String) body.get("name");
        String customerID = (String) body.get("customerID");
        User user = new User(customerID,username,loginPassword,3,phoneNo,name);
        session.setAttribute("registerUser",user);
        System.out.println(name+customerID);
        map.put("success",true);
        return map;
    }

    //注册处理
    @RequestMapping("/register")
    @ResponseBody
    public Map register(HttpSession session,@RequestParam String code){
//        String code = getNonce_str();
//        session.setAttribute("code",code);
//        this.setNullAttrbute(session,"code");
        Map<String,Object> map = new HashMap<>();
        String securitycode = (String) session.getAttribute("code");
        if (securitycode == null) {
            map.put("msg", "验证码失效");
        } else if (!securitycode.equals(code)) {
            map.put("msg", "验证码错误");
        } else {
            session.setAttribute("code", null);
            User user = (User) session.getAttribute("registerUser");
            int i = loginService.addUser(user);
            if(i>0){
                map.put("msg", "成功");
            }else{
                map.put("msg", "添加失败");
            }
        }
        return map;
    }

}
