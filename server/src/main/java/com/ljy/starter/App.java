package com.ljy.starter;

import com.ljy.misc.GlobalQueue;

import java.io.File;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:17:35
 */
public class App {

    public static void main(String[] args) throws Exception {
        System.setProperty("log4jLogpath", new File("").getAbsolutePath()+File.separator+"log.txt");

        GlobalQueue.class.newInstance();
    }


}
