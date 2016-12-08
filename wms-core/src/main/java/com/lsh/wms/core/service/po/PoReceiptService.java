package com.lsh.wms.core.service.po;

import com.alibaba.fastjson.JSON;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.model.so.ObdStreamDetail;
import com.lsh.wms.core.dao.po.IbdDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptDetailDao;
import com.lsh.wms.core.dao.po.InbReceiptHeaderDao;
import com.lsh.wms.core.dao.po.ReceiveDetailDao;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockLotService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.po.IbdDetail;
import com.lsh.wms.model.po.InbReceiptDetail;
import com.lsh.wms.model.po.InbReceiptHeader;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.so.ObdDetail;
import com.lsh.wms.model.stock.StockLot;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.wave.WaveDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/12
 * Time: 16/7/12.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.core.service.po.
 * desc:类功能描述
 */
@Component
@Transactional(readOnly = true)
public class PoReceiptService {

    private static Logger logger = LoggerFactory.getLogger(PoReceiptService.class);

    @Autowired
    private InbReceiptDetailDao inbReceiptDetailDao;

    @Autowired
    private InbReceiptHeaderDao inbReceiptHeaderDao;

    @Autowired
    private IbdDetailDao ibdDetailDao;

    @Autowired
    private StockMoveService stockMoveService;

    @Autowired
    private StockLotService stockLotService;

    @Autowired
    private ReceiveDetailDao receiveDetailDao;

    @Autowired
    private WaveService waveService;

    @Autowired
    private SoOrderService soOrderService;

    /**
     * 插入InbReceiptHeader及List<InbReceiptDetail>
     * @param inbReceiptHeader
     * @param inbReceiptDetailList
     */
    @Transactional(readOnly = false)
    public void orderInit(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList){
        inbReceiptHeader.setInserttime(new Date());
        inbReceiptHeaderDao.insert(inbReceiptHeader);
        for (InbReceiptDetail inbReceiptDetail:inbReceiptDetailList) {
            inbReceiptDetail.setReceiptOrderId(inbReceiptHeader.getReceiptOrderId());
        }
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
    }

    /**
     * 插入InbReceiptHeader及List<InbReceiptDetail>
     * @param inbReceiptHeader
     * @param inbReceiptDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList,
                            List<IbdDetail> updateIbdDetailList, List<Map<String, Object>> moveList,
                            List<ReceiveDetail> updateReceiveDetailList, List<ObdStreamDetail> obdStreamDetailList, List<ObdDetail> obdDetails) {

        //插入订单
        inbReceiptHeader.setInserttime(new Date());

        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",inbReceiptHeader.getContainerId());
        //TODO 啥意思?
        InbReceiptHeader oldInbReceiptHeader = this.getInbReceiptHeaderByParams(mapQuery);
        if(oldInbReceiptHeader == null) {
            inbReceiptHeaderDao.insert(inbReceiptHeader);
        }
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
        ibdDetailDao.batchUpdateInboundQtyByOrderIdAndDetailOtherId(updateIbdDetailList);
        if(updateReceiveDetailList != null && updateReceiveDetailList.size() > 0){
            receiveDetailDao.batchUpdateInboundQtyByReceiveIdAndDetailOtherId(updateReceiveDetailList);
        }
        //TODO 这种代码串的太长了,不应该放在这,一个收货的开发人员还管你出库怎么玩????
        //直流生成waveDetail
        if(obdStreamDetailList != null && obdStreamDetailList.size() > 0){
            WaveDetail waveDetail = new WaveDetail();
            ObjUtils.bean2bean(obdStreamDetailList.get(0),waveDetail);
            waveService.insertDetail(waveDetail);
        }

        //TODO 干什么的?
        if(obdDetails != null && obdDetails.size() > 0){
            soOrderService.updateObdDetail(obdDetails.get(0));
        }

        List<StockMove> stockMovesList = new ArrayList<StockMove>();
        for (Map<String, Object> moveInfo : moveList) {
            StockLot lot = (StockLot) moveInfo.get("lot");
            if (! lot.isOld()) {
                stockLotService.insertLot(lot);
            }
            StockMove move = (StockMove) moveInfo.get("move");
            move.setLot(lot);
            stockMovesList.add(move);
            //stockQuantService.move((StockMove) moveInfo.get("move"), lot);
        }
        stockMoveService.move(stockMovesList);
    }

    /**
     * 插入InbReceiptHeader及List<InbReceiptDetail> 不做库存移动
     * @param inbReceiptHeader
     * @param inbReceiptDetailList
     */
    @Transactional(readOnly = false)
    public void insertOrder(InbReceiptHeader inbReceiptHeader, List<InbReceiptDetail> inbReceiptDetailList,
                            List<IbdDetail> updateIbdDetailList,
                            List<ReceiveDetail> updateReceiveDetailList,List<ObdStreamDetail> obdStreamDetailList) {

        //插入订单
        inbReceiptHeader.setInserttime(new Date());

        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("containerId",inbReceiptHeader.getContainerId());
        InbReceiptHeader oldInbReceiptHeader = this.getInbReceiptHeaderByParams(mapQuery);
        if(oldInbReceiptHeader == null) {
            inbReceiptHeaderDao.insert(inbReceiptHeader);
        }
        inbReceiptDetailDao.batchInsert(inbReceiptDetailList);
        ibdDetailDao.batchUpdateInboundQtyByOrderIdAndDetailOtherId(updateIbdDetailList);

        receiveDetailDao.batchUpdateInboundQtyByReceiveIdAndDetailOtherId(updateReceiveDetailList);

        //直流生成waveDetail
        if(obdStreamDetailList.size() > 0){
            WaveDetail waveDetail = new WaveDetail();
            ObjUtils.bean2bean(obdStreamDetailList.get(0),waveDetail);
            waveService.insertDetail(waveDetail);
        }
    }

    /**
     * 根据ReceiptId更新InbReceiptHeader
     * @param inbReceiptHeader
     */
    @Transactional(readOnly = false)
    public void updateInbReceiptHeaderByReceiptId(InbReceiptHeader inbReceiptHeader) {
        inbReceiptHeader.setUpdatetime(new Date());

        inbReceiptHeaderDao.updateByReceiptId(inbReceiptHeader);
    }

    /**
     * 通过ID获取InbReceiptHeader
     * @param id
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderById(Long id) {
        return inbReceiptHeaderDao.getInbReceiptHeaderById(id);
    }

    /**
     * 根据参数获取InbReceiptHeader数量
     * @param params
     * @return
     */
    public Integer countInbReceiptHeader(Map<String, Object> params) {
        return inbReceiptHeaderDao.countInbReceiptHeader(params);
    }

    /**
     * 自定义参数获取List<InbReceiptHeader>
     * @param params
     * @return
     */
    public List<InbReceiptHeader> getInbReceiptHeaderList(Map<String, Object> params) {
        return inbReceiptHeaderDao.getInbReceiptHeaderList(params);
    }

    /**
     * 通过ID获取InbReceiptDetail
     * @param id
     * @return
     */
    public InbReceiptDetail getInbReceiptDetailById(Long id) {
        return inbReceiptDetailDao.getInbReceiptDetailById(id);
    }

    /**
     * 根据参数获取InbReceiptDetail数量
     * @param params
     * @return
     */
    public Integer countInbReceiptDetail(Map<String, Object> params) {
        return inbReceiptDetailDao.countInbReceiptDetail(params);
    }

    /**
     * 自定义参数获取List<InbReceiptDetail>
     * @param params
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailList(Map<String, Object> params) {
        return inbReceiptDetailDao.getInbReceiptDetailList(params);
    }

    /**
     * 自定义参数获取InbReceiptHeader
     * @param params
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderByParams(Map<String, Object> params) {
        List<InbReceiptHeader> inbReceiptHeaderList = getInbReceiptHeaderList(params);

        if(inbReceiptHeaderList.size() <= 0 || inbReceiptHeaderList.size() > 1) {
            return  null;
        }

        return inbReceiptHeaderList.get(0);
    }

    /**
     * 根据ReceiptId获取InbReceiptHeader
     * @param receiptId
     * @return
     */
    public InbReceiptHeader getInbReceiptHeaderByReceiptId(Long receiptId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receiptOrderId", receiptId);

        return getInbReceiptHeaderByParams(params);
    }

    /**
     * 根据ReceiptId获取List<InbReceiptDetail>
     * @param receiptId
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailListByReceiptId(Long receiptId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receiptOrderId", receiptId);

        return getInbReceiptDetailList(params);
    }

    /**
     * 根据OrderId获取List<InbReceiptDetail>
     * @param orderId
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailListByOrderId(Long orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);

        return getInbReceiptDetailList(params);
    }

    /**
     * List<InbReceiptHeader>填充InbReceiptDetail
     * @param inbReceiptHeaderList
     */
    public void fillDetailToHeaderList(List<InbReceiptHeader> inbReceiptHeaderList) {
        for(InbReceiptHeader inbReceiptHeader : inbReceiptHeaderList) {
            fillDetailToHeader(inbReceiptHeader);
        }
    }

    /**
     * InbReceiptHeader填充InbReceiptDetail
     * @param inbReceiptHeader
     */
    public void fillDetailToHeader(InbReceiptHeader inbReceiptHeader) {
        if(inbReceiptHeader == null) {
            return;
        }

        List<InbReceiptDetail> inbReceiptDetailList = getInbReceiptDetailListByReceiptId(inbReceiptHeader.getReceiptOrderId());

        inbReceiptHeader.setReceiptDetails(inbReceiptDetailList);
    }

    /**
     * 根据OrderId获取List<InbReceiptDetail>
     * @param orderId
     * @return
     */
    public List<InbReceiptDetail> getInbReceiptDetailListByOrderIdAndCode(Long orderId,String barCode,Integer isValid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("barCode",barCode);
        params.put("isValid",isValid);

        return getInbReceiptDetailList(params);
    }

    /**
     * 根据OrderId获取List<InbReceiptDetail>
     * @param receiptId
     * @return
     */
    public InbReceiptDetail getInbReceiptDetailListByReceiptIdAndCode(Long receiptId,String barCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("receiptOrderId", receiptId);
        params.put("barCode",barCode);
        List<InbReceiptDetail> list = getInbReceiptDetailList(params);
        if(list.size() <= 0){
            return null;
        }

        return list.get(0);
    }
}
