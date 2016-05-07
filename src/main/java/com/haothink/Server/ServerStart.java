package com.haothink.Server;

import org.eclipse.jetty.server.Server;

/**
 * Created by wanghao on 16-4-26.
 */
public class ServerStart {
    //http://kielczewski.eu/2013/11/using-embedded-jetty-spring-mvc/
    //在jetty这注入springmvc的controller
    public static void main(String[] args) {

        Server server = new Server(8888);
        // 设置在JVM退出时关闭Jetty的钩子。
        server.setStopAtShutdown(true);

    }
}
