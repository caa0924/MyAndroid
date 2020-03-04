package com.mhimine.jdk.coordapp.Coord;

public class Point {



    private String pointName = "";
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(String pointName, double x, double y, double z) {
        this.pointName = pointName;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public String getPointName() {
        return pointName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
