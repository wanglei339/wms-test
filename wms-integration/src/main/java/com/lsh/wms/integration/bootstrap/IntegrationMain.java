package com.lsh.wms.integration.bootstrap;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/8/19
 * Time: 16/8/19.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.integration.bootstrap.
 * desc:类功能描述
 */
public class IntegrationMain {
    public static void main(String[] args) {
        // 第一个参数用来标记程序名称
        String[] argAry = null;
        if (args != null && args.length > 1) {
            argAry = new String[args.length - 1];
            System.arraycopy(args, 1, argAry, 0, args.length - 1);
        }
        com.alibaba.dubbo.container.Main.main(argAry);

    }
}
