package com.mhimine.jdk.coordapp.Coord;

public class TransParaSeven {


    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double rotateX;
    private double rotateY;
    private double rotateZ;
    private double scale;

    public TransParaSeven(double offsetX, double offsetY, double offsetZ, double rotateX, double rotateY, double rotateZ, double scale) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
        this.scale = scale;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getRotateX() {
        return rotateX;
    }

    public double getRotateY() {
        return rotateY;
    }

    public double getRotateZ() {
        return rotateZ;
    }

    public double getScale() {
        return scale;
    }
}
