package com.lsh.wms.integration.service.back;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.stock.StockItem;
import com.lsh.wms.api.model.stock.StockRequest;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.service.back.IBuildDataRpcService;
import com.lsh.wms.core.constant.IntegrationConstan;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.taking.StockTakingService;
import com.lsh.wms.integration.service.wumartsap.WuMart;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.po.ReceiveHeader;
import com.lsh.wms.model.taking.StockTakingDetail;
import com.lsh.wms.model.taking.StockTakingHead;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixin-mac on 2016/11/17.
 */
@Service(protocol = "dubbo")
public class BuildDataRpcService implements IBuildDataRpcService {

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private WuMart wuMart;

    @Autowired
    private DataBackService dataBackService;

    @Autowired
    private StockTakingService stockTakingService;

    @Autowired
    private ItemService itemService;



    public void BuildIbdData(Long receiveId) {
        List<ReceiveDetail> receiveDetails = receiveService.getReceiveDetailListByReceiveId(receiveId);
        ReceiveHeader receiveHeader = receiveService.getReceiveHeaderByReceiveId(receiveId);
        // TODO: 2016/11/3 回传WMSAP 组装信息
        CreateIbdHeader createIbdHeader = new CreateIbdHeader();
        List<CreateIbdDetail> details = new ArrayList<CreateIbdDetail>();
        for(ReceiveDetail receiveDetail : receiveDetails){
            CreateIbdDetail detail = new CreateIbdDetail();
            detail.setPoNumber(receiveHeader.getOrderOtherId());
            detail.setPoItme(receiveDetail.getDetailOtherId());
            BigDecimal inboudQty =  receiveDetail.getInboundQty();

            BigDecimal orderQty = receiveDetail.getOrderQty();
            BigDecimal deliveQty = receiveHeader.getOrderType().equals(3) ? orderQty : inboudQty;
            if(deliveQty.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            detail.setDeliveQty(deliveQty.setScale(2,BigDecimal.ROUND_HALF_UP));
            detail.setUnit(receiveDetail.getUnitName());
            detail.setMaterial(receiveDetail.getSkuCode());
            detail.setOrderType(receiveHeader.getOrderType());
            detail.setVendMat(receiveHeader.getReceiveId().toString());

            details.add(detail);
        }
        createIbdHeader.setItems(details);

        if(receiveHeader.getOwnerUid() == 1){
            wuMart.sendIbd(createIbdHeader);

        }else{
            dataBackService.erpDataBack(JSON.toJSONString(createIbdHeader));
        }
    }

    public void BuildInventoryData(Long taskingId) {
        StockTakingHead stockTakingHead = stockTakingService.getHeadById(taskingId);
        List<StockTakingDetail> stockTakingDetails = stockTakingService.getDetailByTakingId(taskingId);
        //盘亏 盘盈的分成两个list itemsLoss为盘亏 itemsWin盘盈
        StockRequest request = new StockRequest();
        List<StockItem> itemsLoss = new ArrayList<StockItem>();
        List<StockItem> itemsWin = new ArrayList<StockItem>();


        for(StockTakingDetail stockTakingDetail : stockTakingDetails){
            StockItem item = new StockItem();
            BaseinfoItem baseinfoItem = itemService.getItem(stockTakingDetail.getItemId());
            if(baseinfoItem.getOwnerId() == 1){

                item.setEntryUom("EA");
                item.setMaterialNo(baseinfoItem.getSkuCode());
                //实际值大于理论值 报溢
                if(stockTakingDetail.getRealQty().compareTo(stockTakingDetail.getTheoreticalQty()) > 0){
                    item.setEntryQnt(stockTakingDetail.getTheoreticalQty().subtract(stockTakingDetail.getRealQty()).toString());
                    itemsWin.add(item);
                }
                //实际值小于理论值 报损
                else if (stockTakingDetail.getRealQty().compareTo(stockTakingDetail.getTheoreticalQty()) < 0){
                    item.setEntryQnt(stockTakingDetail.getTheoreticalQty().subtract(stockTakingDetail.getRealQty()).toString());
                    itemsLoss.add(item);
                }
            }
        }
        request.setPlant(PropertyUtils.getString("wumart.werks"));
        if( itemsLoss != null || itemsLoss.size() >0 ){
            request.setMoveType(String.valueOf(IntegrationConstan.LOSS));
            request.setItems(itemsLoss);
            dataBackService.wmDataBackByPost(JSON.toJSONString(request),IntegrationConstan.URL_STOCKCHANGE, SysLogConstant.LOG_TYPE_LOSS);
        }

        if(itemsWin != null || itemsWin.size() > 0 ){
            request.setMoveType(String.valueOf(IntegrationConstan.WIN));
            request.setItems(itemsWin);
            dataBackService.wmDataBackByPost(JSON.toJSONString(request),IntegrationConstan.URL_STOCKCHANGE,SysLogConstant.LOG_TYPE_WIN);
        }
    }
}
