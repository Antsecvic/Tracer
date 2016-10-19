package project.mayikai.tracer;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/9.
 */
public class Item implements Serializable {
    private String name;
    private String number;
    private String location;
    private double distance;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNumber(){
        return this.number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public double getDistance(){
        return this.distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

}
