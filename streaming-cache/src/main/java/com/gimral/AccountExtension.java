package com.gimral;

public class AccountExtension {
    private Integer id;
    private String name;
    private AccountBalance balance;

    public AccountExtension(){}

    public AccountExtension(Integer id, String name, AccountBalance balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountBalance getBalance() {
        return balance;
    }

    public void setBalance(AccountBalance balance) {
        this.balance = balance;
    }
}
