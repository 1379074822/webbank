package cn.edu.buct.controller;

import cn.edu.buct.entity.Account;
import cn.edu.buct.entity.Log;
import cn.edu.buct.entity.Trans;
import cn.edu.buct.entity.User;
import cn.edu.buct.global.Content;
import cn.edu.buct.global.Sha;
import cn.edu.buct.service.LogService;
import cn.edu.buct.service.global.TransAndSend;
import cn.edu.buct.service.AccountListService;
import cn.edu.buct.service.LoginService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;

@Controller("cardController")
public class CardController {

    @Autowired
    private LogService logService;
    @Autowired
    private AccountListService accountListService;
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
    private static final String SYMBOLS = "0123456789"; // 数字
    private static final Random RANDOM = new SecureRandom();


    @RequestMapping("/addcard")
    @ResponseBody
    public Map addCard(HttpSession session, @RequestParam String cardNumber, @RequestParam String password) {
        System.out.println(cardNumber + password);
        Map map = new HashMap();
        User user = (User) session.getAttribute("user");

        Log log = new Log(null, TransAndSend.createSerialNumber(Content.ADD_CARD_SN), user.getUn(), Content.ADD_CARD, "成功", new Timestamp(System.currentTimeMillis()), user.getUn() + "进行加挂");
        logService.add(log);
        int logSn = log.getLid();
        System.out.println("logSn:" + logSn);

        String uid = user.getUid();
        String passWord = Sha.SHA(password + "BUCT", "SHA-256");
        String json = TransAndSend.createAccountPasswordValidateJson(uid, passWord, cardNumber);
        String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/AccountPasswordValidate.do", json);
        System.out.println(returnStr);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
        Map<String, Object> header = (Map<String, Object>) returnJson.get("Header");
        Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
        String info = (String) body.get("INFO");
        boolean success = true;
        if (header.get("RS-CODE").equals("000000")) {
            Account account = new Account(null, uid, user.getName(), password, cardNumber, null);
            success = accountListService.addNewCard(account);
        }

        if (!success) {
            Log log1 = logService.get(logSn);
            log1.setRs("失败");
            boolean successFlag = logService.edit(log1);
        }else{
            map.put("success", "成功");
        }
        return map;
    }

    @RequestMapping("/allowance")
    @ResponseBody
    public Map allowance(@RequestParam String cardNumber) {
        System.out.println(cardNumber);
        Map map = new HashMap();
//        User user =(User) session.getAttribute("user");
//        String uid = user.getUid();
//        String passWord = Sha.SHA(password+"BUCT", "SHA-256");
        String json = TransAndSend.createGetAccountBalanceJson(cardNumber);
        String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/GetAccountBalance.do", json);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
        Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
        boolean success = true;
        String balance = (String) body.get("BALANCE");
        map.put("balance", balance);
        return map;
    }
    @RequestMapping("/register/sendSMS")
    @ResponseBody
    public Map sendRSMS(HttpSession session) {
        String code = getNonce_str();
        session.setAttribute("code",code);
        this.setNullAttrbute(session,"code");
        System.out.println("code:   "+code);
        User user = (User) session.getAttribute("registerUser");
        String uid = user.getUid();


        Map map = new HashMap();
        String json = TransAndSend.createSendSMSJson(uid,code);
        String returnStr = TransAndSend.post("http://118.25.78.207:8080/HostSimulator/SendSMS.do", json);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
        System.out.println(returnStr);
        Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
        boolean success = true;
        String info = (String) body.get("INFO");
        map.put("INFO", info);
        map.put("code",code);
        return map;
    }

    @RequestMapping("/sendSMS")
    @ResponseBody
    public Map sendSMS(HttpSession session) {
        String code = getNonce_str();
        session.setAttribute("code",code);
        this.setNullAttrbute(session,"code");
        System.out.println("code:   "+code);
        User user = (User) session.getAttribute("user");
        String uid = user.getUid();


        Map map = new HashMap();
        String json = TransAndSend.createSendSMSJson(uid,code);
        String returnStr = TransAndSend.post("http://118.25.78.207:8080/HostSimulator/SendSMS.do", json);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
        Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
        boolean success = true;
        String info = (String) body.get("INFO");
        map.put("INFO", info);
        map.put("code",code);
        return map;
    }

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

    @RequestMapping("/cards")
    @ResponseBody
    public Map cards(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        List<Account> accountList = accountListService.getByUserName(user.getUn());
        map.put("list", accountList);
        System.out.println(accountList);
        return map;
    }

    @RequestMapping("/transList")
    @ResponseBody
    public Map transfor(String dateFrom, String dateTo, String acctNo, @RequestParam String page, @RequestParam String limit) {
        Map<String, Object> map = new HashMap<>();
        String json = TransAndSend.createTransListJson(dateTo, dateFrom, acctNo, page, limit);
        String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/TransList.do", json);
        System.out.println("returnStr:"+returnStr);
        JSONObject jsonObject = JSONObject.parseObject(returnStr);
        System.out.println("json:"+jsonObject);
        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
        System.out.println("jsonMap:"+returnJson);
        Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
        System.out.println("jsonBody:"+body.getClass().getName()+body);
        List<Trans> transList = (List<Trans>) body.get("TransList");
        Integer total = (Integer) body.get("total");
        System.out.println(total);
        System.out.println(transList);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", total);
        map.put("data", transList);
        return map;
    }

    @RequestMapping("/firstupload")
    @ResponseBody
    public Map firstUpload(HttpSession session,@RequestParam String password,
                                @RequestParam String amount,@RequestParam String acctFrom,
                                @RequestParam String acctTo,@RequestParam String acctToName,
                                @RequestParam String usage,@RequestParam Integer delay,
                                @RequestParam String comments){
        Map<String,Object> map = new HashMap<>();
        map.put("password",password);
        map.put("amount",amount);
        map.put("acctFrom",acctFrom);
        map.put("acctTo",acctTo);
        map.put("acctToName",acctToName);
        map.put("usage",usage);
        map.put("delay",delay);
        map.put("comments",comments);
        System.out.println(map);
        String sighEle = password+amount+acctFrom+acctTo+acctToName+usage+delay+comments;
        String firstSign = Sha.SHA(sighEle, "SHA-256");
        System.out.println(firstSign);
        session.setAttribute("firstUpload",map);
        session.setAttribute("firstSign",firstSign);
        session.setAttribute("sighEle",sighEle);
        return map;
    }

    @RequestMapping("/innertransfer")
    @ResponseBody
    public Map transfer(HttpSession session,@RequestParam String hashsummary,@RequestParam String code,@RequestParam String summary) {
        String firstSign = (String)session.getAttribute("firstSign");
        String sighEle = (String)session.getAttribute("sighEle");
        System.out.println(sighEle);
        System.out.println(summary);

        System.out.println(firstSign);
        System.out.println(hashsummary);
        Map<String, Object> map = new HashMap<>();
        Map<String,Object> firstUpload = (Map<String, Object>)session.getAttribute("firstUpload");
        if (firstSign!=null&&firstSign.equals(hashsummary)){
            DecimalFormat df = new DecimalFormat("0.00");
            double d = Double.parseDouble((String) firstUpload.get("amount"));
            String amounts = df.format(d);

            map = accountListService.innerOtherTransfer(session,(String) firstUpload.get("password"),amounts,(String) firstUpload.get("acctFrom"),
                    (String) firstUpload.get("acctTo"),(String) firstUpload.get("acctToName"),(String) firstUpload.get("usage"),(Integer) firstUpload.get("delay"),
                    (String) firstUpload.get("comments"),code);
        }else {
            map.put("msg","信息不匹配");
        }

        return map;
    }

    @RequestMapping("/innertransferself")
    @ResponseBody
    public Map transferSelf(HttpSession session,@RequestParam String password,
                            @RequestParam String amount,@RequestParam String acctFrom,
                            @RequestParam String acctTo,@RequestParam String usage,
                            @RequestParam Integer delay,@RequestParam String comments,
                            @RequestParam String code) {
        DecimalFormat df = new DecimalFormat("0.00");
        double d = Double.parseDouble(amount);
        String amounts = df.format(d);
        System.out.println("password:"+password+" amount:"+amounts+" acctFrom:"+acctFrom+" acctTo:"+acctTo+" usage:"+usage+" delay:"+delay+" comments:"+comments+" code:"+code);
        Map<String, Object> map = new HashMap<>();
        map = accountListService.innerSelfTransfer(session,password,amounts,acctFrom,acctTo,usage,delay,comments,code);
        return map;
    }
}