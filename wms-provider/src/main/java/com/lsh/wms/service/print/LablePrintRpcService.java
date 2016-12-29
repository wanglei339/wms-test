package com.lsh.wms.service.print;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.api.service.container.IContainerRpcService;
import com.lsh.wms.api.service.print.ILablePrintRpcService;
import com.lsh.wms.core.service.container.ContainerService;
import com.lsh.wms.model.baseinfo.BaseinfoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongling on 16/11/8.
 */
@Service(protocol = "dubbo")
public class LablePrintRpcService implements ILablePrintRpcService {
    @Reference
    private IContainerRpcService iContainerRpcService;
    @Autowired
    private ContainerService containerService;
    private static Logger logger = LoggerFactory.getLogger(LablePrintRpcService.class);

    //根据数量,批量生成托盘码
    public List<String> getContainerCode(Integer number){
        logger.info("getContainerCode start [number]" + number + "[data]" + DateUtils.getCurrentSeconds());
        List<String> containerIdList =  iContainerRpcService.batchcreateContainer(number);
        logger.info("getContainerCode end"  + "[data]" + DateUtils.getCurrentSeconds());
        return containerIdList;
    }
    //根据数量,批量生成托盘码
   /* public List<String> getContainerCode(Integer number){
        List<String> containerList = new ArrayList<String>();
        for(int i = 0;i < number;i++){
            BaseinfoContainer container = iContainerRpcService.createTray();
            containerList.add(container.getContainerCode());
        }
        return containerList;
    }*/
    //验证输入托盘码在系统中是否存在
    public boolean checkContainerCode(String containerCode) throws BizCheckedException{
        BaseinfoContainer container = containerService.getContainerByCode(containerCode);
        if(container == null){
            throw  new BizCheckedException("2000002");
        }
        return true;
    }

}
