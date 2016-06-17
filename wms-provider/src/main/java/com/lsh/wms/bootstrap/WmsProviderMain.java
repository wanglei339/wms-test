package com.lsh.wms.bootstrap;

public class WmsProviderMain {

    public static void main(String[] args) {
        // 第一个参数用来标记程序名称
        String[] argAry = null;
        if (args != null && args.length > 1) {
            argAry = new String[args.length - 1];
            System.arraycopy(args, 1, argAry, 0, args.length - 1);
        }
        // http://127.0.0.1:8888/api/wms/java/v1/sms/sendMsg
        com.alibaba.dubbo.container.Main.main(argAry);
    }

}
