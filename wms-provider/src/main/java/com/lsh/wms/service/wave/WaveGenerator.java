package com.lsh.wms.service.wave;

import com.aliyun.oss.common.utils.DateUtil;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisStringDao;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.so.ObdHeader;
import com.lsh.wms.model.wave.WaveTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zengwenjun on 16/11/8.
 */
 /*
    本质上来说是通过一组条件查询或者聚合出一组订单集合.
    条件可能性:
        >   订单生成时间
        >   订单计划交货时间
        >   客户编号
        >   运输线路
        >

     补充策略:
        >   订单相似度,这个策略的意义跟订单形态和捡货模型有很大的关系,一般认为只在提总播种模式下生效
        >

     限制因素:
        >   订单个数[订单个数根据不同类型的订单应该有不同的经验值,是一个长期观察值]
        >   订单类型,不同订单类型是否应该放在不同的波次中?


     订单类型:
        >   订单类型是一种自定义的仓库订单的属性,用户区分不同作业形势的订单
                比如: 优供订单[TO B]
                     大门店订单[TO B, 会通过专门的集货道走]
                     多点订单[TO C]
        >   关键问题是:  订单类型如何在创建订单的时候进行生成? 因为原则上上游系统是不知道你仓库要怎么去作业的.
                            货主
                            客户区间
                            行项目数
                            强制上游指定?
                            ?
        >   理论上应该为一个订单类型配置一种波次生成器.
        >   而波次生成器页面的组织方式首先按订单类型进行分割,分别进行计算和数据统计
        >
     */
@Component
public class WaveGenerator {
    private static Logger logger = LoggerFactory.getLogger(WaveGenerator.class);

    @Autowired
    WaveTemplateService waveTemplateService;
    @Autowired
    SoOrderService orderService;
    private Map<Long, List<ObdDetail>> mapOrderId2ObdDetails;
    @Autowired
    private RedisStringDao redisStringDao;

    int sumOrderCount;
    int sumLineCount;
    List<Map> waves;
    int waveIdx;

    public void _init(){
        mapOrderId2ObdDetails = new HashMap<Long, List<ObdDetail>>();
        waves = new LinkedList<Map>();
        sumLineCount = 0;
        sumOrderCount = 0;
        waveIdx = 0;
    }

    //波次类型判定器,取库中未判断波次类型的订单进行判定,可独立运行,由定时器触发.
    public void setWaveOrderType(){
        //获取波次类型配置定义列表
        //获取未判断波次类型的订单
        //执行波次订单类型分类器
    }

    //根据波次订单类型获取全量订单
    private List<ObdHeader> _getUnWaveOrders(String waveOrderType){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("waveId", 0L);
        mapQuery.put("waveOrderType", waveOrderType);
        final List<ObdHeader> orderHeadList = orderService.getOutbSoHeaderList(mapQuery);
        for(ObdHeader header : orderHeadList){
            //List<ObdDetail> details = orderService.getOutbSoDetailListByOrderId(header.getOrderId());
            //mapOrderId2ObdDetails.put(header.getOrderId(), details);
        }
        return orderHeadList;
    }

    //执行波次规划器,进行波次聚合
    private void _clusterWave(WaveTemplate tpl){
        logger.info("start to run wave cluster "+tpl.getWaveTemplateName());
        //获取订单列表
        List<ObdHeader> orderHeaders = this._getUnWaveOrders(tpl.getWaveOrderType());
        //执行波次聚合引擎
        //首先根据聚合规则简单做聚合
        //CLUSTER_KEY "路线__客户"
        Map<String, List<ObdHeader>> clusters = new HashMap<String, List<ObdHeader>>();
        for(ObdHeader header : orderHeaders){
            String key = "";
            if(tpl.getClusterRoute().equals(1L) ){
                key += header.getTransPlan();
            }
            key += "_";
            if(tpl.getClusterCustomer().equals(1L)){
                key += header.getDeliveryCode();
                logger.info("cao a ");
            }
            if(clusters.get(key) == null){
                clusters.put(key, new LinkedList<ObdHeader>());
            }
            clusters.get(key).add(header);
            logger.info(String.format("cluster key %s t[%d] %s c[%d] %s size %d",
                    key,
                    tpl.getClusterRoute(), header.getTransPlan(),
                    tpl.getClusterCustomer(), header.getDeliveryCode(),
                    clusters.get(key).size()));
        }
        //跑策略
        //后期复杂策略,先不实现
        //根据订单限制做切割.
        List<List<ObdHeader>> finalClusters = new LinkedList<List<ObdHeader>>();
        for(String key : clusters.keySet()){
            List<ObdHeader> headers = clusters.get(key);
            if(tpl.getOrderLimit() <= 0){
                finalClusters.add(headers);
            }else{
                int idx = 0;
                while(idx*tpl.getOrderLimit().intValue()<headers.size()){
                    int end = (idx+1)*tpl.getOrderLimit().intValue();
                    end = end > headers.size() ? headers.size() : end;
                    finalClusters.add(headers.subList(idx*tpl.getOrderLimit().intValue(), end));
                    idx++;
                }
            }
        }
        //生成json结果

        long waveTag = DateUtils.getCurrentSeconds();
        for(List<ObdHeader> cluster : finalClusters){
            waveIdx++;
            int orderCount = cluster.size();
            int lineCount = 0;
            List<Long> orderIdList = new LinkedList<Long>();
            for(ObdHeader header : cluster){
                //lineCount = mapOrderId2ObdDetails.get(header.getOrderId()).size();
                orderIdList.add(header.getOrderId());
            }
            sumOrderCount += orderCount;
            sumLineCount += lineCount;
            Map<String, Object> wave = new HashMap<String, Object>();
            wave.put("orderCount", orderCount);
            wave.put("lineCount", lineCount);
            wave.put("orders", orderIdList);
            wave.put("wavePreviewId", String.format("%d%05d", waveTag, waveIdx));
            wave.put("waveTemplateId", tpl.getWaveTemplateId());
            wave.put("waveTemplateName", tpl.getWaveTemplateName());
            wave.put("waveOrderType", tpl.getWaveOrderType());
            waves.add(wave);
        }
        logger.info("end to run wave cluster "+tpl.getWaveTemplateName()+" wave count :"+finalClusters.size());
    }

    //批量执行波次规划期,可以独立运行,由定时器触发,结果将保存在redis中,前端页面直接从redis中读取结果
    public void autoCluster(){
        this._init();

        this.setWaveOrderType();
        //获取配置好的订单类型列表
        List<WaveTemplate> waveTemplateList = waveTemplateService.getWaveTemplateList(new HashMap<String, Object>());
        //遍历执行
        for(WaveTemplate tpl : waveTemplateList){
            this._clusterWave(tpl);
        }
        //redis中存储本次计算的日志,用于展示.
        /*结构:
        {
            'orderCount' : 1 //订单数
            'lineCount' : 1 //行项目总数
            'waveCount' : 1 //波次数
            'waves' : [
                {
                    'previewWaveId' : 1//预览waveid
                    'orderCount' : 1 //订单数
                    'lineCount' : 1 //行项目总数
                    'orders' : [ 'Oa', 'Ob', 'Oc']
                }
               ]
        }
        */
        Map<String, Object> waveJson = new HashMap<String, Object>();
        waveJson.put("orderCount", sumOrderCount);
        waveJson.put("lineCount", sumLineCount);
        waveJson.put("waveCount", waves.size());
        waveJson.put("waves", waves);
        String rst = JsonUtils.obj2Json(waveJson);
        try{
            redisStringDao.set(RedisKeyConstant.WAVE_PREVIEW_KEY, rst,24, TimeUnit.HOURS);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("write wave preview to redis fail");
        }
    }

    public String getWavePreviewList(){
        String info = redisStringDao.get(RedisKeyConstant.WAVE_PREVIEW_KEY);
        return info;
    }

    public List<Long> getOrderIdsByWavePreviewId(String wavePreviewId){
        String info = redisStringDao.get(RedisKeyConstant.WAVE_PREVIEW_KEY);
        Map<String, Object> previewInfo = JsonUtils.json2Obj(info, Map.class);
        if(previewInfo == null || previewInfo.get("waves") == null){
            return null;
        }
        List<Map> previewWaves = (List<Map>)previewInfo.get("waves");
        for(Map<String, Object> wave : previewWaves){
            if(wave.get("wavePreviewId").equals(wavePreviewId)){
                return (List<Long>)wave.get("orders");
            }
        }
        return null;
    }

    public Long getWaveTemplateIdByWavePreviewId(String wavePreviewId){
        String info = redisStringDao.get(RedisKeyConstant.WAVE_PREVIEW_KEY);
        Map<String, Object> previewInfo = JsonUtils.json2Obj(info, Map.class);
        if(previewInfo == null || previewInfo.get("waves") == null){
            return null;
        }
        List<Map> previewWaves = (List<Map>)previewInfo.get("waves");
        for(Map<String, Object> wave : previewWaves){
            if(wave.get("wavePreviewId").equals(wavePreviewId)){
                return Long.valueOf(wave.get("waveTemplateId").toString());
            }
        }
        return null;
    }

}
