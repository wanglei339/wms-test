package com.lsh.wms.service.wave;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.wave.IWaveRpcService;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.WaveConstant;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.wave.WaveHead;
import com.lsh.wms.model.wave.WaveRequest;
import com.lsh.wms.model.wave.WaveTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private WaveGenerator waveGenerator;



    public Long decorateCreateWave(WaveRequest request) throws BizCheckedException {
        List<Map> oldOrders = request.getOrders();
        List<Map> newOrders = new ArrayList<Map>();
        for(Map order : oldOrders){
            String orderOtherId = order.get("orderId").toString();
            ObdHeader so = soOrderService.getOutbSoHeaderByOrderOtherIdAndType(orderOtherId, SoConstant.ORDER_TYPE_SO);
            if(so == null){
                throw new BizCheckedException("2041000", orderOtherId,"");
            }
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("orderId",so.getOrderId());
            map.put("transPlan",order.get("transPlan"));
            map.put("waveIndex",order.get("waveIndex"));
            map.put("transTime",order.get("transTime"));
            newOrders.add(map);
        }
        request.setOrders(newOrders);

        return this.createWave(request);
    }

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
        String waveName = "";
        List<Map> orders = request.getOrders();
        for(Map order : orders){
            Long orderId = Long.valueOf(order.get("orderId").toString());
            //String orderOtherId = order.get("orderId").toString();
            ObdHeader so = soOrderService.getOutbSoHeaderByOrderId(orderId);
            //ObdHeader so = soOrderService.getOutbSoHeaderByOrderOtherId(orderOtherId);
            if(so == null){
                throw new BizCheckedException("2041000", orderId,"");
            }
            if(so.getWaveId() > 0){
                throw new BizCheckedException("2041001", orderId,"");
            }
            /*if(order.get("transPlan") == null
                    || order.get("waveIndex") == null
                    || order.get("transTime") == null){
                throw new BizCheckedException("2041002");
            }
            */
            waveName = so.getDeliveryCode()+"..";
        }
        waveName = String.format("%s[%s]", tpl.getWaveTemplateName(), waveName);
        pickWaveHead.setWaveName(waveName);
        if(orders.size()==0){
            throw new BizCheckedException("2041003");
        }
        try{
            waveService.createWave(pickWaveHead,orders);
            return pickWaveHead.getWaveId();
        }catch (Exception e){
            logger.error(e.getCause()!=null ? e.getCause().getMessage():e.getMessage());
            throw new BizCheckedException("2041004");
        }
    }

    public void releaseWave(long iWaveId, long iUid) throws BizCheckedException {
        WaveHead head = waveService.getWave(iWaveId);
        if(head==null){
            throw new BizCheckedException("2040001");
        }
        if(
                head.getStatus() == WaveConstant.STATUS_RELEASE_SUCC
                || head.getStatus() == WaveConstant.STATUS_NEW
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
            //加锁
            int ret = core.release(iWaveId);
            if ( ret == 0 ) {
                bNeedRollBack = false;
            }else{
                logger.error("wave release fail, ret %d", ret);
                throw new BizCheckedException("2041004");
            }
        } catch (BizCheckedException e){
            logger.error(String.format("Wave release fail, wave id %d msg %s", iWaveId, e.getMessage()));
            logger.error(e.getCause()!=null ? e.getMessage():e.getMessage());
            throw e;
        } /*catch (Exception e){
            logger.error(String.format("Wave release fail, wave id god %d msg %s", iWaveId, e.getMessage()));
            logger.error(e.getCause()!=null ? e.getMessage():e.getMessage());
            throw  e;
            //throw new BizCheckedException("2041004");
        }*/  finally {
            if(bNeedRollBack) {
                waveService.setStatus(iWaveId, WaveConstant.STATUS_RELEASE_FAIL);
            }
            //释放锁
        }
    }

    public void runWaveGenerator() throws BizCheckedException{
        waveGenerator.autoCluster();
    }

}
