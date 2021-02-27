package com.fab.rent.Model;

public class Products {

    private String  pname,description,price,image,category,pid,date,time,productState,sid,subcategory,weeklyprice,monthlyprice,sellerName,sellerPhone,sellerAddress;
    private int securityAmt;

    public Products()
    {

    }

    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time, String productState,String sid,String subcategory,int securityAmt,String weeklyprice,String monthlyprice,String sellerName,String sellerPhone,String sellerAddress) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.productState = productState;
        this.sid=sid;
        this.subcategory=subcategory;
        this.securityAmt=securityAmt;
        this.weeklyprice=weeklyprice;
        this.monthlyprice=monthlyprice;
        this.sellerName=sellerName;
        this.sellerPhone=sellerPhone;
        this.sellerAddress=sellerAddress;

    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getSecurityAmt() {
        return securityAmt;
    }

    public void setSecurityAmt(int securityAmt) {
        this.securityAmt = securityAmt;
    }

    public String getWeeklyprice() {
        return weeklyprice;
    }

    public void setWeeklyprice(String weeklyprice) {
        this.weeklyprice = weeklyprice;
    }

    public String getMonthlyprice() {
        return monthlyprice;
    }

    public void setMonthlyprice(String monthlyprice) {
        this.monthlyprice = monthlyprice;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }
}
