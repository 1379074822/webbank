package cn.edu.buct.entity;

public class Register {
    private String userName;
    private String passWord;
    private String identifyingCode;
    public Register(String userName, String passWord, String identifyingCode){
        this.userName = userName;
        this.passWord = passWord;
        this.identifyingCode = identifyingCode;
    }

    public Register() { }

    public String getIdentifyingCode() {
        return identifyingCode;
    }

    public void setIdentifyingCode(String identifyingCode) {
        this.identifyingCode = identifyingCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserNamem(String userName) {

    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
