package com.gogoal.app.bean;

/**
 * Created by huangxx on 2017/2/16.
 */

public class FunctionBean {
    private String function_name;
    private int function_map;

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public int getFunction_map() {
        return function_map;
    }

    public void setFunction_map(int function_map) {
        this.function_map = function_map;
    }

    public FunctionBean(String function_name, int function_map) {
        this.function_name = function_name;
        this.function_map = function_map;
    }

    @Override
    public String toString() {
        return "FunctionBean{" +
                "function_name='" + function_name + '\'' +
                ", function_map=" + function_map +
                '}';
    }
}
