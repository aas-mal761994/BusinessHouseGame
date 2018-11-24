package com.house.game.models;

import java.util.Objects;

public class User {
    private String name;
    private int money=1000;
    private int position = 0;
    private int noOfChances;
    private int sequenceNumber;

    public User(Builder builder) {
        this.name = builder.name;
        this.noOfChances = builder.noOfChances;
        this.sequenceNumber = builder.sequenceNumber;

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getNoOfChances() {
        return noOfChances;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public static class Builder {
        private String name;
        private int noOfChances;
        private int sequenceNumber;

        public Builder() {

        }

        public User build() {
            return new User(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder noOfChances(int noOfChances) {
            this.noOfChances = noOfChances;
            return this;
        }

        public Builder sequenceNumber(int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }
    }
}
