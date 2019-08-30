package cn.edu.buct.entity;

public class User {
    private String uid;
    private String un;
    private String pw;
    private Integer tm;//times
    private String phone;
    private String name;

    public User(String uid, String un, String pw, Integer tm, String phone, String name) {
        this.uid = uid;
        this.un = un;
        this.pw = pw;
        this.tm = tm;
        this.phone = phone;
        this.name = name;
    }

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Integer getTm() {
        return tm;
    }

    public void setTm(Integer tm) {
        this.tm = tm;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", un='" + un + '\'' +
                ", pw='" + pw + '\'' +
                ", tm=" + tm +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}


