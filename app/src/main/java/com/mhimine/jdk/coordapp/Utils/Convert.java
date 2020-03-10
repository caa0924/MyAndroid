package com.mhimine.jdk.coordapp.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {

    public static double parseDouble(String input){
        double output = 0;
        try {
            output = Double.parseDouble(input);
            return output;
        }
        catch (Exception e){
            return output;
        }
    }

    /**
     * 保留两位小数，四舍五入的一个老土的方法
     * @param d
     * @return
     */
    public static double formatDouble1(double d) {
        return (double)Math.round(d*1000)/1000;
    }

    public static double parseDouble(double d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        //BigDecimal bg = new BigDecimal(d).setScale(6, RoundingMode.UP);
        BigDecimal bg = new BigDecimal(d).setScale(6, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();
    }

    public static String parseString(double d) {
//        NumberFormat nf = NumberFormat.getNumberInstance();
//        // 保留小数
//        nf.setMaximumFractionDigits(5);
//        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
//        nf.setRoundingMode(RoundingMode.UP);
//        return nf.format(d);
        //return String.format("%.5f", d);
        DecimalFormat df = new DecimalFormat("0.######");
        return df.format(d);
    }
    public static String parseString(double d,String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }

    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日HH时mm分");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

}
