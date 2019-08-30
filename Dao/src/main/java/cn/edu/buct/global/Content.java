package cn.edu.buct.global;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Content {
    public final static Integer MAX_LOGIN_AMOUNT = 3;

    public final static String LOGIN = "登陆";
    public final static String LOGOUT = "登出";
    public final static String ADD_CARD = "加挂账户";
    public final static String AccountBalance = "查询余额";
    public final static String TRANSFER = "转账";
    public static List getStatus(){
        List list = new ArrayList();
        list.add(LOGIN);
        list.add(LOGOUT);
        list.add(ADD_CARD);
        list.add(AccountBalance);
        list.add(TRANSFER);
        return list;
    }

    public final static String LOGIN_SN = "001";
    public final static String LOGOUT_SN = "002";
    public final static String ADD_CARD_SN = "003";
    public final static String AccountBalance_SN = "004";
    public final static String TRANSFER_SN = "005";
}
