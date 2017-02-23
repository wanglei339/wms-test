package com.lsh.wms.service.sync;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.eventbus.Subscribe;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.api.service.inhouse.IProcurementProveiderRpcService;
import com.lsh.wms.api.service.inhouse.IStockTakingProviderRpcService;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.po.PoOrderService;
import com.lsh.wms.core.service.utils.PackUtil;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.ProcurementInfo;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.IbdHeader;
import com.lsh.wms.model.taking.FillTakingPlanParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Project Name: lsh-wms
 * Created by wuhao
 * Date: 16/12/29
 * 北京链商电子商务有限公司
 * Package com.lsh.wms.service.sync.AsyncEventListener
 * desc:异步处理监听类
 */
@Component
public class AsyncEventListener {
    private static Logger logger = LoggerFactory.getLogger(AsyncEventListener.class);

    @Reference
    private IProcurementProveiderRpcService procurementProveiderRpcService;
    @Reference
    private IStockTakingProviderRpcService stockTakingProviderRpcService;
    @Autowired
    private WaveService waveService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private PoOrderService poOrderService;


    /**
     * 生成补货任务
     * @param canMax 是否按最大值补货
     */
    @Subscribe
    public void createProcurement(final Boolean canMax) {
        if(canMax) {
            logger.info("in create max plan ");
            logger.info("begin:"+ DateUtils.getCurrentSeconds());
            procurementProveiderRpcService.createProcurementByMax(canMax);
            logger.info("end:" + DateUtils.getCurrentSeconds());
        }else {
            logger.info("in create wave plan ");
            logger.info("begin:"+ DateUtils.getCurrentSeconds());
            procurementProveiderRpcService.createProcurement(canMax);
            logger.info("end:" + DateUtils.getCurrentSeconds());
        }
    }
    /**
     * 填充盘点任务详情
     * @param  fillTakingPlanParam taskId:盘点任务ID，operator:操作人
     */
    @Subscribe
    public void fillTakingTask(FillTakingPlanParam fillTakingPlanParam) {
        stockTakingProviderRpcService.fillTask(fillTakingPlanParam);
    }
    /**
     * 根据补货type 跑不同类型的补货任务
     * @param  info
     */
    @Subscribe
    public void createProcurementTask(ProcurementInfo info) {
        logger.info("procurement_task_info:"+info);
        //如果locationId和itemId不为0，则单个调整补货任务
        if(info.getLocationId().compareTo(0L)!=0 && info.getItemId().compareTo(0L)!=0){
            BaseinfoLocation pickLocation = locationService.getLocation(info.getLocationId());
            if(pickLocation.getRegionType().compareTo(LocationConstant.SHELFS)!=0){
                //只修改货架区的捡货位补货任务
                return;
            }
            BigDecimal unPickedQty = waveService.getUnPickedQty(info.getItemId());
            procurementProveiderRpcService.adjustTaskQty(unPickedQty,pickLocation,info.getItemId());
            return;
        }
        logger.info("begin_task:"+ DateUtils.getCurrentSeconds());
        if(info.getLocationType()== LocationConstant.LOFTS){
            procurementProveiderRpcService.createLoftProcurement(info.isCanMax());
        }
        if(info.getLocationType() == LocationConstant.SHELFS){
            if(info.getTaskType()==1){
                logger.info("in create wave plan");
                procurementProveiderRpcService.createShelfProcurementBak2(info.isCanMax());
            }else {
                logger.info("in create max plan");
                procurementProveiderRpcService.createShelfProcurement(info.isCanMax());
            }
        }
        logger.info("end_task:" + DateUtils.getCurrentSeconds());
    }

    @Subscribe
    public void closeIbdHeaderStatus(Long orderId){
        logger.info(StrUtils.formatString("order_id : " ,orderId));
        List<IbdDetail> ibdDetails = poOrderService.getInbPoDetailListByOrderId(orderId);
        Boolean doneflag = true;
        List<IbdHeader> updateIbdHeaders = new ArrayList<IbdHeader>();
        for (IbdDetail ibdDetail : ibdDetails) {
            //订货单位转换为基本单位来计算
            BigDecimal orderUnitQty = PackUtil.UomQty2EAQty(ibdDetail.getOrderQty(),ibdDetail.getPackUnit());
            BigDecimal inboundQty = ibdDetail.getInboundQty();
            if(orderUnitQty.compareTo(inboundQty) > 0){
                doneflag = false;
                break;
            }
        }
        if(doneflag){
            IbdHeader updateIbdHeader = new IbdHeader();
            updateIbdHeader.setOrderId(orderId);
            updateIbdHeader.setOrderStatus(PoConstant.ORDER_RECTIPT_ALL);
            updateIbdHeaders.add(updateIbdHeader);
        }

        //批量修改状态
        poOrderService.batchUpdateIbdHeaderByOrderId(updateIbdHeaders);



    }


}
