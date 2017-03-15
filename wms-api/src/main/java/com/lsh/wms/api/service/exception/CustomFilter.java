package com.lsh.wms.api.service.exception;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import org.apache.log4j.Logger;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 17/3/13
 * Time: 17/3/13.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.exception.
 * desc:类功能描述
 */
public class CustomFilter implements Filter {

    private final Logger logger = Logger.getLogger(this.getClass());

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        try {
            SessionId.reset();
            Result result = invoker.invoke(invocation);
            long end = System.currentTimeMillis();
            logger.info("RPC日志==> 类名：" + invoker.getInterface().getName() + " 方法名：" + invocation.getMethodName() + " 耗时：" + (end - start) + "ms");
            return result;
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            logger.error("RPC日志==> 类名：" + invoker.getInterface().getName() + " 方法名：" + invocation.getMethodName() + " 耗时：" + (end - start) + "ms", t);
            throw t;
        } finally {
            SessionId.clear();
        }
    }
}