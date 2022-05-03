package com.meishe.ms_asminject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : lpf
 * @FileName: MSTimeAnalysis
 * @Date: 2022/5/2 21:26
 * @Description: 耗时分析
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MSTimeAnalysis {
}
