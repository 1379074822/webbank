package cn.edu.buct.service.impl;

import cn.edu.buct.dao.AccountListDao;
import cn.edu.buct.entity.*;

import cn.edu.buct.global.Sha;
import cn.edu.buct.service.AccountListService;
import cn.edu.buct.service.global.TransAndSend;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("accountListService")
public class AccountListServiceimpl implements AccountListService {
    @Autowired
    @Qualifier(value = "accountListDao")
    private AccountListDao accountListDao;

    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    @Override
    public boolean update(Account account) {
        int i = accountListDao.update(account);
        return i > 0;
    }

    @Override
    public List<Account> get(String cid) {

        return accountListDao.select(cid);
    }

    @Override
    public List<Account> getByUserName(String un) {
        return accountListDao.selectByUserName(un);
    }

    @Override
    public boolean addNewCard(Account account) {
        int i = accountListDao.addCard(account);
        return i > 0;
    }

    @Override
    public List<Account> getReceieve(String uid, String accNo) {
        return accountListDao.selectReceieve(uid, accNo);
    }

    public Map innerSelfTransfer(HttpSession session, String password, String amount, String acctFrom, String acctTo, String usage, Integer delay, String comments, String code) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) session.getAttribute("user");
        String acctToName = user.getName();
        String securitycode = (String) session.getAttribute("code");
        if (securitycode == null) {
            map.put("msg", "验证码失效");
        } else if (!securitycode.equals(code)) {
            map.put("msg", "验证码错误");
        } else {
            session.setAttribute("code", null);
            final Object[] objs = new Object[1];
            service.schedule(new Runnable() {
                private String password;
                private String amount;
                private String acctFrom;
                private String acctTo;
                private String acctToName;
                private String usage;
                private String comments;

                public Runnable accept(String password, String amount, String acctFrom, String acctTo, String acctToName, String usage, String comments) {
                    System.out.println("赋值");
                    this.password = password;
                    this.amount = amount;
                    this.acctFrom = acctFrom;
                    this.acctTo = acctTo;
                    this.acctToName = acctToName;
                    this.usage = usage;
                    this.comments = comments;
                    return this;
                }

                @Override
                public void run() {
                    System.out.println("主进程");
                    String passWord = Sha.SHA(password + "BUCT", "SHA-256");
                    String json = TransAndSend.createInnerTransferJson(passWord, amount, acctFrom, acctToName, usage, acctTo, comments);
                    System.out.println(json);
                    String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/InnerTransfer.do", json);
                    JSONObject jsonObject = JSONObject.parseObject(returnStr);
                    Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
                    System.out.println(returnJson);
                    Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
                    String success = (String) body.get("INFO");
                    objs[0] = success;
                    System.out.println(success);
                }
            }.accept(password, amount, acctFrom, acctTo, acctToName, usage, comments), delay, TimeUnit.SECONDS);
//            System.out.println(objs);
            map.put("msg", "成功");
        }
        return map;
    }

    public Map innerOtherTransfer(HttpSession session, String password, String amount, String acctFrom, String acctTo,String acctToName, String usage, Integer delay, String comments, String code) {
        Map<String, Object> map = new HashMap<>();

        String securitycode = (String) session.getAttribute("code");
        if (securitycode == null) {
            map.put("msg", "验证码失效");
        } else if (!securitycode.equals(code)) {
            map.put("msg", "验证码错误");
        } else {
            session.setAttribute("code", null);
            final Object[] objs = new Object[1];
            service.schedule(new Runnable() {
                private String password;
                private String amount;
                private String acctFrom;
                private String acctTo;
                private String acctToName;
                private String usage;
                private String comments;

                public Runnable accept(String password, String amount, String acctFrom, String acctTo, String acctToName, String usage, String comments) {
                    System.out.println("赋值");
                    this.password = password;
                    this.amount = amount;
                    this.acctFrom = acctFrom;
                    this.acctTo = acctTo;
                    this.acctToName = acctToName;
                    this.usage = usage;
                    this.comments = comments;
                    return this;
                }

                @Override
                public void run() {
                    System.out.println("主进程");
                    String passWord = Sha.SHA(password + "BUCT", "SHA-256");
                    String json = TransAndSend.createInnerTransferJson(passWord, amount, acctFrom, acctToName, usage, acctTo, comments);
                    System.out.println(json);
                    String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/InnerTransfer.do", json);
                    JSONObject jsonObject = JSONObject.parseObject(returnStr);
                    Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
                    System.out.println(returnJson);
                    Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
                    String success = (String) body.get("INFO");
                    objs[0] = success;
                    System.out.println(success);
                }
            }.accept(password, amount, acctFrom, acctTo, acctToName, usage, comments), delay, TimeUnit.SECONDS);
//            System.out.println(objs);
            map.put("msg", "成功");
        }
        return map;
    }
}
