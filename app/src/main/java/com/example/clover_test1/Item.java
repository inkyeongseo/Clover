package com.example.clover_test1;

public class Item {
    int image_id;
    String name;
    String msg;

    public Item(){
    }

    public Item(int image_id,String name,String msg){
        this.image_id = image_id;
        this.name = name;
        this.msg = msg;
    }

    public int getImage_id(){
        return image_id;
    }
    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getMsg(){return msg;}
    public void setMsg(String msg){this.msg = msg;}
}
