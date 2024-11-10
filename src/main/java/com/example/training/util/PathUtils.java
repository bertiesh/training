package com.example.training.util;

import cn.hutool.core.io.FileUtil;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * @Author: Deng Yunhu
 * @Date: 2019/11/26 17:07
 */
public class PathUtils {
    /**
     * 获取jar包文件的根目录
     *
     * @return jar包根目录
     */
    public static String getApplicationJarHome() {
        ApplicationHome applicationHome = new ApplicationHome(PathUtils.class);
        File source = applicationHome.getSource();
        if (FileUtil.isEmpty(source)) {
            return applicationHome.getDir().getAbsolutePath();
        }
        return source.getParentFile().toString();
    }
}
