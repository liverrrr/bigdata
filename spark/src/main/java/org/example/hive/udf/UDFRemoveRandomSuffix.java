package org.example.hive.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Random;

@Description(
        name = "removeRandomSuffix",
        value = "_FUNC_(str) - str is random$str,random belong (1,length+1], returns str",
        extended = "If input is empty return null"
)
public class UDFRemoveRandomSuffix extends UDF {

    public String evaluate(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String[] split = input.split("\\$");
        return split[split.length - 1];
    }

}
