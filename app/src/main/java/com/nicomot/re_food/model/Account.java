package com.nicomot.re_food.model;

public class Account {
    private String usernameAccount;
    private String passwordAccount;
    private String typeAccount;

    public Account(){

    }
    public Account(String usernameAccount, String passwordAccount, String typeAccount) {
        this.usernameAccount = usernameAccount;
        this.passwordAccount = passwordAccount;
        this.typeAccount = typeAccount;
    }

    public String getUsernameAccount() {
        return usernameAccount;
    }

    public void setUsernameAccount(String usernameAccount) {
        this.usernameAccount = usernameAccount;
    }

    public String getPasswordAccount() {
        return passwordAccount;
    }

    public void setPasswordAccount(String passwordAccount) {
        this.passwordAccount = passwordAccount;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }
}
