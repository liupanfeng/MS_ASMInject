package com.meishe.plugin;

import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;


/**
 * * All rights reserved,Designed by www.meishesdk.com
 *
 * @Author : lpf
 * @CreateDate : 2022/5/1 下午12:13
 * @Description : 插件类
 * @Copyright :www.meishesdk.com Inc.All rights reserved.
 */
public class APMPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        BaseExtension android = project.getExtensions().getByType(BaseExtension.class);
        /**
         * android 插件能够获得所有的class
         * 同时提供一个接口，让我们可以获得在所的class
         * 注册一个Transform
         */
        android.registerTransform(new ASMTransform());
    }
}
