package com.phantomorion.moneymanager;

public class UserInfo {
    String name,age,savings,gender;
    public UserInfo(){}
    public UserInfo(String name, String age, String savings, String gender) {
        this.name = name;
        this.age = age;
        this.savings = savings;

        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }





    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
