package com.lsh.wms.service.wave;

import com.lsh.wms.core.service.wave.WaveTemplateService;
import com.lsh.wms.model.wave.WaveTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zengwenjun on 16/11/8.
 */

@Component
public class WaveGenerator {

    @Autowired
    WaveTemplateService waveTemplateService;
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

    //波次类型判定器,取库中未判断波次类型的订单进行判定,可独立运行,由定时器触发.
    void setWaveOrderType(){
        //获取波次类型配置定义列表
        //获取未判断波次类型的订单
        //执行波次订单类型分类器
    }

    //根据波次订单类型获取全量订单
    private void _getUnWaveOrders(String waveOrderType){

    }

    //执行波次规划器,进行波次聚合
    private void _clusterWave(WaveTemplate tpl){
        //获取订单列表
        this._getUnWaveOrders(tpl.getWaveOrderType());
        //执行波次聚合引擎
        //保存redis
        /*结构:
        {
            'orderCount' : 1 //订单数
            'lineCount' : 1 //行项目总数
            'waveCount' : 1 //波次数
            'waves' : [
                {
                    'orderCount' : 1 //订单数
                    'lineCount' : 1 //行项目总数
                    'orderCount' : 1 //订单数
                    'orders' : [ 'Oa', 'Ob', 'Oc']
                }
               ]
        }
        */
    }

    //批量执行波次规划期,可以独立运行,由定时器触发,结果将保存在redis中,前端页面直接从redis中读取结果
    private void _autoCluster(){
        this.setWaveOrderType();
        //获取配置好的订单类型列表
        List<WaveTemplate> waveTemplateList = waveTemplateService.getWaveTemplateList(new HashMap<String, Object>());
        //遍历执行
        for(WaveTemplate tpl : waveTemplateList){
            this._clusterWave(tpl);
        }
        //redis中存储本次计算的日志,用于展示.
    }

}
