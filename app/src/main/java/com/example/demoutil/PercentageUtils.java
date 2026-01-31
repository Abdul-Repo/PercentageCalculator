package com.example.demoutil;

import java.text.DecimalFormat;

public class PercentageUtils {
    public static float handleStringToFloat(String input){
        float parsedFloat = 0;
        if(input.contains("%")){
            input = input.replace("%","");
        }
        if(input == null ||input.isEmpty()){
            return 0;
        }
        try{
            parsedFloat = Float.parseFloat(input);
        } catch (Exception e) {
            parsedFloat = 0;
        }
        return parsedFloat;
    }

    public static String handleFloatToString(float input) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(input);
    }

}
