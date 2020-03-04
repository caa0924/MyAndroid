package com.mhimine.jdk.coordapp.Coord;

public final class PointRange {
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    public PointRange(double xMin, double xMax, double yMin, double yMax){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public double getxMin() {
        return xMin;
    }

    public double getxMax() {
        return xMax;
    }

    public double getyMin() {
        return yMin;
    }

    public double getyMax() {
        return yMax;
    }
}
