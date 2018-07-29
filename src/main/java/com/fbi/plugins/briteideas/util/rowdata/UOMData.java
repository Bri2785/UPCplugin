package com.fbi.plugins.briteideas.util.rowdata;

public class UOMData {

    private String uomCode;
    private double multiply;
    private int id;

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double multiply) {
        this.multiply = multiply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UOMData(String code, double multiply, int id){
        this.setId(id);
        this.setMultiply(multiply);
        this.setUomCode(code);
    }
}
