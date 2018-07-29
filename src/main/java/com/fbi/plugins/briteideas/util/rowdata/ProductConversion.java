package com.fbi.plugins.briteideas.util.rowdata;

public class ProductConversion {
    private int id;
    private int productID;
    private int uomFromId;
    private String UpcCode;
    private String UomName;
    private double UomQty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUomFromId() {
        return uomFromId;
    }

    public void setUomFromId(int uomFromId) {
        this.uomFromId = uomFromId;
    }

    public String getUpcCode() {
        return UpcCode;
    }

    public void setUpcCode(String upcCode) {
        UpcCode = upcCode;
    }

    public String getUomName() {
        return UomName;
    }

    public void setUomName(String uomName) {
        UomName = uomName;
    }

    public double getUomQty() {
        return UomQty;
    }

    public void setUomQty(double uomQty) {
        UomQty = uomQty;
    }

}
