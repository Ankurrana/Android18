package com.example.ankurrana.helloandroid;

import java.util.Date;

/**
 * Created by Ankur.Rana on 11/16/2016.
 */

public class Task {
    private String description,status,owner,key;
    private Date completedAt;
    private String scheduleString;

    Task(String _key,String _description){
        this.key = _key;
        this.description = _description;
    }
    Task(String _description,String _status, String _owner, String _key){
        this.description = _description;
        this.status = _status;
        this.owner = _owner;
        this.key = _key;
    }
    Task(String _description,String schedule,int k){
        this.description = _description;
        this.scheduleString = schedule;
    }

    String scheduleString(){
            return this.scheduleString;
    }

    String Key(){
        if(this.key == null || this.key == ""){
            return "Not Available";
        }else{
            return this.key;
        }
    }
    String Description(){
        if(this.description == null || this.description == ""){
            return "Not Available";
        }else{
            return this.description;
        }
    }
    String Owner(){
        if(this.owner == null || this.owner == ""){
            return "Not Available";
        }else{
            return this.owner;
        }
    }
    String Status(){
        if(this.status  == null || this.status == ""){
            return "Not Available";
        }else{
            return this.status;
        }
    }




}
