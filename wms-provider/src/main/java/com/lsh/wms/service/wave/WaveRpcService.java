package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.wave.IWaveRpcService;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.so.OutbSoHeader;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveRequest;
import com.lsh.wms.model.wave.WaveTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
@Service(protocol = "dubbo")
public class WaveRpcService implements IWaveRpcService {
    private static final Logger logger = LoggerFactory.getLogger(WaveRpcService.class);
    @Autowired
    WaveTemplateService waveTemplateService;
    @Autowired
    SoOrderService soOrderService;
    @Autowired
    WaveService waveService;
    @Autowired
    private WaveCore core;

    public Long createWave(WaveRequest request) throws BizCheckedException {
        WaveHead pickWaveHead = new WaveHead();
        ObjUtils.bean2bean(request,pickWaveHead);
        //获取波次模版
        WaveTemplate tpl = waveTemplateService.getWaveTemplate(pickWaveHead.getWaveTemplateId());
        if(tpl == null){
            throw new BizCheckedException("2040008");
        }
        pickWaveHead.setPickModelTemplateId(tpl.getPickModelTemplateId());
        pickWaveHead.setWaveDest(tpl.getWaveDest());
        List<Map> orders = request.getOrders();
        for(Map order : orders){
            Long orderId = Long.valueOf(order.get("orderId").toString());
            OutbSoHeader so = soOrderService.getOutbSoHeaderByOrderId(orderId);
            if(so == null){
                throw new BizCheckedException("", String.format("订单[%d]不存在", orderId));
            }
            if(so.getWaveId() > 0){
                throw new BizCheckedException("", String.format("订单[%d]已排好波次", orderId));
            }
            if(order.get("transPlan") == null
                    || order.get("waveIndex") == null
                    || order.get("transTime") == null){
                throw new BizCheckedException("", "参数错误");
            }
        }
        if(orders.size()==0){
            throw new BizCheckedException("订单数为0");
        }
        try{
            waveService.createWave(pickWaveHead,orders);
            return pickWaveHead.getWaveId();
        }catch (Exception e){
            logger.error(e.getCause().getMessage());
            throw new BizCheckedException("创建失败,系统错误");
        }
    }

    public void releaseWave(long iWaveId, long iUid) throws BizCheckedException {
        WaveHead head = waveService.getWave(iWaveId);
        if(head==null){
            throw new BizCheckedException("2040001");
        }
        if(head.getStatus() == WaveConstant.STATUS_NEW
                || head.getStatus() == WaveConstant.STATUS_RELEASE_FAIL
                || (head.getStatus() == WaveConstant.STATUS_RELEASE_START && DateUtils.getCurrentSeconds()-head.getReleaseAt() > 300))
        {

        } else {
            throw new BizCheckedException("2040002");
        }
        head.setReleaseUid(iUid);
        head.setReleaseAt(DateUtils.getCurrentSeconds());
        head.setStatus((long) WaveConstant.STATUS_RELEASE_START);
        try{
            waveService.update(head);
        }catch (Exception e){
            throw  new BizCheckedException("2040003");
        }
        boolean bNeedRollBack = true;
        try {
            int ret = core.release(iWaveId);
            if ( ret == 0 ) {
                bNeedRollBack = false;
            }else{
                logger.error("wave release fail, ret %d", ret);
                throw new BizCheckedException("", "wave release fail");
            }
        }catch (BizCheckedException e){
            logger.error("Wave release fail, wave id %d msg %s", iWaveId, e.getMessage());
            throw e;
        } finally {
            if(bNeedRollBack) {
                waveService.setStatus(iWaveId, WaveConstant.STATUS_RELEASE_FAIL);
            }
        }
    }
}
