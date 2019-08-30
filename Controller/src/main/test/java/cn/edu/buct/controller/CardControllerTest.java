//package cn.edu.buct.controller;
//
//import cn.edu.buct.dao.AccountListDao;
//import cn.edu.buct.entity.Account;
//import cn.edu.buct.entity.Log;
//import cn.edu.buct.global.Sha;
//import cn.edu.buct.service.global.TransAndSend;
//import cn.edu.buct.service.AccountListService;
//import cn.edu.buct.service.LogService;
//import com.alibaba.fastjson.JSONObject;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Timestamp;
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring-controller.xml")
//public class CardControllerTest {
//    @Autowired
//    private LogService logService;
//    @Autowired
//    @Qualifier(value = "accountListDao")
//    private AccountListDao accountListDao;
//    @Autowired
//    private AccountListService accountListService;
//
//    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
//
//    @Test
//    public void addCard() {
//        Map map = new HashMap();
//        String password = Sha.SHA("66661BUCT", "SHA-256");
//        String json = TransAndSend.createAccountPasswordValidateJson("68923574986311018746", password, "6218129487003234");
//        String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/AccountPasswordValidate.do", json);
//        JSONObject jsonObject = JSONObject.parseObject(returnStr);
//        Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
//        System.out.println(returnJson);
//        Map<String,Object> header =(Map<String, Object>) returnJson.get("Header");
//        boolean success = true;
//        if(header.get("RS-CODE").equals("000000")) {
//            Account account = new Account(null, "68923574986311018739", "gaosong", "998001", "6218129084844853",null);
//            success = accountListService.addNewCard(account);
//        }
//        map.put("success",success);
//        System.out.println(success);
//    }
//
//    @Test
//    public void test() {
//        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
//        service.schedule(new Runnable() {
//            private String password;
//            private String amount;
//            private String acctFrom;
//            private String acctTo;
//            private String acctToName;
//            private String usage;
//            private String comments;
//
//            public Runnable accept(String password, String amount, String acctFrom, String acctTo, String acctToName, String usage, String comments) {
//                System.out.println("赋值");
//                this.password = password;
//                this.amount = amount;
//                this.acctFrom = acctFrom;
//                this.acctTo = acctTo;
//                this.acctToName = acctToName;
//                this.usage = usage;
//                this.comments = comments;
//                return this;
//            }
//
//            @Override
//            public void run() {
//                System.out.println("主进程");
//                String passWord = Sha.SHA(password + "BUCT", "SHA-256");
//                System.out.println(passWord);
//                String json = TransAndSend.createInnerTransferJson(passWord, amount, acctFrom, acctToName, usage, acctTo, comments);
//                System.out.println(json);
//                String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/InnerTransfer.do", json);
//                JSONObject jsonObject = JSONObject.parseObject(returnStr);
//                Map<String, Object> returnJson = (Map<String, Object>) jsonObject;
//                System.out.println(returnJson);
//                Map<String, Object> body = (Map<String, Object>) returnJson.get("Body");
//                String success = (String) body.get("INFO");
////                objs[0] = success;
//                System.out.println(success);
//            }
//        }.accept("984434", "12.00", "6218129496832243", "6218129487003234", "李彦宏", "还款", "测试"), 0, TimeUnit.SECONDS);
//        System.out.println("in");
//    }
//
//    @Test
//    public void test1() {
//                System.out.println("主进程");
//                String passWord = Sha.SHA("376881"+"BUCT", "SHA-256");
//                String json = TransAndSend.createInnerTransferJson(passWord,"10.00","6218129087231776","李彦宏","还款","6218129496832243","测试");
//                System.out.println(json);
//                String returnStr = TransAndSend.post("http://118.25.143.204:8080/HostSimulator/InnerTransfer.do",json);
//                JSONObject jsonObject = JSONObject.parseObject(returnStr);
//                Map<String,Object> returnJson = (Map<String,Object>)jsonObject;
//                System.out.println(returnJson);
//                Map<String,Object> body =(Map<String, Object>) returnJson.get("Body");
//                String success =(String) body.get("INFO");
//                System.out.println(success);
//    }
//
//    @Test
//    @Transactional
//    public void testselectcoll(){
//        int page = 1;
//        int limit = 20;
//        String fctp ="error";
//        Timestamp startDate= Timestamp.valueOf("1900-1-1 12:12:12");
//        Timestamp endDate=Timestamp.valueOf("2000-1-1 12:12:12");
//
//
//        Map<String,Object> map = new HashMap<>();
//        List<Log> logs;
//        List logs2;
//        //如果内容为空改为Null,date是必选的
//        if(fctp=="") {
//            fctp=null;
//        }
//        System.out.println(fctp+"controll层");
//        //没有操作类型则返回所选时间内所有数据
//        if(fctp==null){
//            logs = logService.getTime(startDate,endDate);
//            System.out.println("所有数据。controller");
//
//        }
//        else {
//            // System.out.println(studentService.getAllByConditions(sid,sName));
//            logs = logService.getTypeAndTime(fctp,startDate,endDate);
//            System.out.println("controller层接收到"+logs);
//        }
//        //分页操作，直接复制
//        if(page * limit -1<logs.size()-1) {
//            logs2 = logs.subList((page - 1)*limit,page * limit -1);
//        }
//        else {
//            logs2 = logs.subList((page - 1)*limit,logs.size());
//        }
//        //渲染表，直接复制
//        map.put("code",0);
//        map.put("msg","");
//        map.put("count",logs.size());
//        map.put("data",logs2);
//        System.out.println("student2"+logs2);
//        //返回list渲染
//
//        System.out.println(map);//直接打印在控制台
//    }
//}