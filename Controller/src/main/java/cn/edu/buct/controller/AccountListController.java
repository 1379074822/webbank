package cn.edu.buct.controller;

import cn.edu.buct.entity.Account;
import cn.edu.buct.entity.User;
import cn.edu.buct.global.Sha;
import cn.edu.buct.service.AccountListService;
import cn.edu.buct.service.global.TransAndSend;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller("accountListController")

public class AccountListController {
    @Autowired
    private AccountListService accountListService;

    @RequestMapping("/cardList")
    @ResponseBody
    public Map<String,Object> cardList(HttpSession session, @RequestParam Integer page,@RequestParam Integer limit)
    {
        System.out.println("inininininin");
        User user = (User) session.getAttribute("user");
           Map<String, Object> map = new HashMap<>();
        List<Account> accountList = accountListService.get(user.getUid());
        List accountList2;
        if(page * limit -1<accountList.size()-1)
        {
            accountList2 = accountList.subList((page - 1)*limit,page * limit -1);
        }
        else {
            accountList2 = accountList.subList((page - 1)*limit,accountList.size());
        }

//        String str1 = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":[{\"cardId\":10000,\"currencyType\":\"user-0\",\"sex\":\"***\"},{\"cardId\":12000,\"currencyType\":\"user-0\",\"sex\":\"***\"}]}";
//        map.put("list",str1);

        map.put("code",0);
        map.put("msg","");
        map.put("count",10);
        map.put("data",accountList2);
        System.out.println(map);
        return map;
    }

    @RequestMapping("/cardListSelect")
    @ResponseBody
    public Map<String,Object> cardListSelect(HttpSession session)
    {
        System.out.println("inininininin");
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        List<Account> accountList = accountListService.get(user.getUid());

        map.put("code",0);
        map.put("msg","");
        map.put("count",accountList.size());
        map.put("data",accountList);
        System.out.println(map);
        return map;
    }

    @RequestMapping("/cardListReceieveSelect")
    @ResponseBody
    public Map<String,Object> cardListReceiveSelect(HttpSession session,@RequestParam String accNo)
    {
        System.out.println("inininininin");
        User user = (User) session.getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        List<Account> accountList = accountListService.getReceieve(user.getUid(),accNo);

        map.put("code",0);
        map.put("msg","");
        map.put("count",accountList.size());
        map.put("data",accountList);
        System.out.println(map);
        return map;
    }

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
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
                System.out.println(passWord);
                String json = TransAndSend.createInnerTransferJson(passWord, amount, acctFrom, acctToName, usage, acctTo, comments);
                System.out.println(json);
                String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/InnerTransfer.do", json);
                JSONObject jsonObject = JSONObject.parseObject(returnStr);
                Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
                System.out.println(returnJson);
                Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
                String success = (String) body.get("INFO");
//                objs[0] = success;
                System.out.println(success);
            }
        }.accept("984434", "100.00", "6218129496832243", "6218129487003234", "李彦宏", "还款", "测试"), 10, TimeUnit.SECONDS);
    }
}
