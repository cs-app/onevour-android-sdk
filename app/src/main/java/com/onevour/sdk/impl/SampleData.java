package com.onevour.sdk.impl;
//
// Created by Zuliadin on 2019-12-24.
//

public class SampleData {

    private int id;
    private String name;

    private int age;

    public SampleData() {

    }

    public SampleData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SampleData(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }
}
