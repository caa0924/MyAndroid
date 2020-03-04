package com.mhimine.jdk.coordapp.Coord;

import android.content.Context;
import android.content.res.Resources;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2020.
 * 坐标转换相关方法
 * @author 子凡
 * @Date    2019/6/2
 * @version 1.00
 */
public class CoordTransform {

    //储存所有（6种）坐标转换方式的7参数
    public static Map<String,TransParaSeven> SevenParames = new HashMap<String,TransParaSeven>();

    private static final PointRange pointRange;// = new PointRange(xMin,xMax,yMin,yMax);
    static {
        Context context = Utils.getContext();
        Resources res =context.getResources();
        String xMinRange = res.getString(R.string.xMinRange);
        String xMaxRange = res.getString(R.string.xMaxRange);
        String yMinRange = res.getString(R.string.yMinRange);
        String yMaxRange = res.getString(R.string.yMaxRange);
        double xMin = Convert.parseDouble(xMinRange);
        double xMax = Convert.parseDouble(xMaxRange);
        double yMin = Convert.parseDouble(yMinRange);
        double yMax = Convert.parseDouble(yMaxRange);
        pointRange = new PointRange(xMin,xMax,yMin,yMax);
    }

    /**
    * @Description: 布尔萨七参数转换（需要传入7参数化和源坐标点）
    * @Param: [transParaSeven, point] 
    * @return: Point 
    * @Author: 子凡
    * @Date: 2019/6/2 
    */
    public static Point BolsaSevenParameters(TransParaSeven transParaSeven,Point point){
        //第二步转换，空间直角坐标系里七参数转换
        double Ex = transParaSeven.getRotateX() / 3600 / 180 * Math.PI;
        double Ey = transParaSeven.getRotateY() / 3600 / 180 * Math.PI;
        double Ez = transParaSeven.getRotateZ() / 3600 / 180 * Math.PI;

        double targetX = transParaSeven.getOffsetX() + transParaSeven.getScale() * point.getX() + point.getY() * Ez - point.getZ() * Ey + point.getX();
        double targetY = transParaSeven.getOffsetY() + transParaSeven.getScale() * point.getY() - point.getX() * Ez + point.getZ() * Ex + point.getY();
        double targetZ = transParaSeven.getOffsetZ() + transParaSeven.getScale() * point.getZ() + point.getX() * Ey - point.getY() * Ex + point.getZ();

        double outX = Convert.parseDouble(targetX);
        double outY = Convert.parseDouble(targetY);
        double outZ = Convert.parseDouble(targetZ);

        return new Point(point.getPointName(),outX,outY,outZ);
    }

    public static boolean isRange(Point point){
        boolean isRange = true;
        if (pointRange.getxMin() > point.getX() || point.getX() > pointRange.getxMax() &&
                pointRange.getyMin() > point.getY() && point.getY() > pointRange.getyMax()) {
            isRange = false;
        }
        return isRange;
    }

}
