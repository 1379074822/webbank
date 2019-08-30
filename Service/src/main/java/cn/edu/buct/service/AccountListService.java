package cn.edu.buct.service;

import cn.edu.buct.entity.Account;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface AccountListService {
    //       Account Decode(String enData, String enKey);
    boolean update(Account account);

    List<Account> get(String cid);

    List<Account> getByUserName(String un);

    boolean addNewCard(Account account);

    List<Account> getReceieve(String uid, String accNo);
    Map innerSelfTransfer(HttpSession session, String password, String amount, String acctFrom, String acctTo, String usage, Integer delay, String comments, String code);

    Map<String, Object> innerOtherTransfer(HttpSession session, String password, String amount, String acctFrom, String acctTo, String acctToName, String usage, Integer delay, String comments, String code);
}
