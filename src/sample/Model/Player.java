package sample.Model;

import java.io.Serializable;

public class Player implements Serializable{

    private String name;
    private String country;
    private int age;
    private double height;
    private String clubName;
    private String position;
    private int number;
    private double salary;
    private boolean onSell;

    public Player() {
    }

    public Player(String name, String country, int age, double height, String clubName, String position, int number, double salary,boolean onSell) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.clubName = clubName;
        this.position = position;
        this.number = number;
        this.salary = salary;
        this.onSell = onSell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setOnSell(Boolean onSell){
        this.onSell = onSell;
    }
    public Boolean getOnSell(){
        return this.onSell;
    }

    @Override
    public String toString() {

        return  name ;//+ country +"      "+ age+"      " + height +"      " +clubName +"      "+ position+"      " + number +"      "+ salary;
    }
}
