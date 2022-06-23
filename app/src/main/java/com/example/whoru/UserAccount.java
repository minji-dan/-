package com.example.whoru;

/**
 * 사용자 계정 정보 모델 클래스
 * 이름 등등 여기서 추가
 */
public class UserAccount {
    private String idToken; //Firebase Uid (고유 키값)
    private String name;    //이름
    private String emailId; //이메일 아이디
    private String password;//비밀번호
    private int number; //카운트 번호

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UserAccount() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
