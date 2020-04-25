package org.example.hive.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

@Description(
        name = "addRandomSuffix",
        value = "_FUNC_(str, length) - returns random$str,random belong (1,length+1]",
        extended = "If length<0 or input is empty return null"
)
public class UDFAddRandomSuffix extends UDF {

    public String evaluate(String input, int length) {
        if (length<=0 || StringUtils.isEmpty(input)){
            return null;
        }
        Random random = new Random();
        int num = random.nextInt(length)+1;
        return num + "$" + input;
    }

}
