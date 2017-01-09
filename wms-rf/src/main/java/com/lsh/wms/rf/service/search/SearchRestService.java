package com.lsh.wms.rf.service.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.q.Module.Base;
import com.lsh.wms.api.service.search.ISearchRestService;
import com.lsh.wms.api.service.stock.IStockQuantRpcService;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.service.csi.CsiOwnerService;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiOwner;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockQuant;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by lixin-mac on 2017/1/8.
 */
@Service(protocol = "rest")
@Path("search")
@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class SearchRestService implements ISearchRestService{
    private static Logger logger = LoggerFactory.getLogger(SearchRestService.class);
    @Reference
    private IStockQuantRpcService stockQuantRpcService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CsiSkuService csiSkuService;
    @Autowired
    private CsiOwnerService csiOwnerService;
    @Autowired
    private LocationService locationService;


    @POST
    @Path("searchSomething")
    public String searchSomething(@FormParam("code")String code) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<CsiOwner> owners = csiOwnerService.getOwnerList(map);
        StringBuilder itemSb = new StringBuilder();
        StringBuilder quantSb = new StringBuilder();

        //List<BaseinfoItem> items = new ArrayList<BaseinfoItem>();

        map.put("code",code);
        map.put("codeType",CsiConstan.CSI_CODE_TYPE_BARCODE);
        List<BaseinfoItem> items = itemService.searchItem(map);
        if(null!=items && items.size() > 0){
            return JsonUtils.SUCCESS(this.getDataByItems(items));
        }
        map = new HashMap<String, Object>();
        map.put("packCode",code);
        //barCode值为箱码
        List<BaseinfoItem> baseinfoItemList = itemService.searchItem(map);
        if(baseinfoItemList != null && baseinfoItemList.size() >0){
            return JsonUtils.SUCCESS(this.getDataByItems(baseinfoItemList));
        }

        return null;
    }

    private Map<String,Object>  getDataByItems(List<BaseinfoItem> items){
        StringBuilder itemSb = new StringBuilder();
        StringBuilder quantSb = new StringBuilder();
        for(BaseinfoItem item : items){
            itemSb.append(" 商品名称 : " + item.getSkuName() + " \n ");
            itemSb.append(" 商品编码 : "  + item.getSkuCode() + " \n ");
            itemSb.append(" 货主 : " + item.getOwnerId() + " \n ");
            itemSb.append(" 箱规 : "  + item.getPackUnit() + " \n ");
            itemSb.append(" 国条码 : "  + item.getCode() + " \n ");
            itemSb.append(" 箱码 : "  + item.getPackCode() + " \n ");
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("itemId",item.getItemId());
            List<StockQuant> quants = stockQuantRpcService.getLocationStockList(map);
            if(quants != null && quants.size() > 0){
                //sb.append(" \n ");
                for(StockQuant quant : quants){
                    quantSb.append(" 库位 : " + locationService.getLocation(quant.getLocationId()).getLocationCode() + " \n ");
                    quantSb.append(" 箱规 : " + quant.getPackUnit() + " \n ");
                    quantSb.append(" 数量 : " + quant.getQty() + " \n ");
                    quantSb.append(" 货主 : " + quant.getOwnerId() + " \n ");
                }
            }
        }
        Map<String,Object> rep = new HashMap<String, Object>();
        rep.put("data",itemSb.append( " \n " ).append(quantSb));
        return rep;
    }

//
//    public Map<String,Object>  getDataByItems(List<BaseinfoItem> items,IStockQuantRpcService stockQuantRpcService){
//        StringBuilder itemSb = new StringBuilder();
//        StringBuilder quantSb = new StringBuilder();
//        for(BaseinfoItem item : items){
//            itemSb.append(" 商品名称 : " + item.getSkuName() + " \n ");
//            itemSb.append(" 商品编码 : "  + item.getSkuCode() + " \n ");
//            itemSb.append(" 货主 : " + item.getOwnerId() + " \n ");
//            itemSb.append(" 箱规 : "  + item.getPackUnit() + " \n ");
//            itemSb.append(" 国条码 : "  + item.getCode() + " \n ");
//            itemSb.append(" 箱码 : "  + item.getPackCode() + " \n ");
//            Map<String,Object> map = new HashMap<String, Object>();
//            map.put("itemId",item.getItemId());
//            List<StockQuant> quants = stockQuantRpcService.getLocationStockList(map);
//            if(quants != null && quants.size() > 0){
//                //sb.append(" \n ");
//                for(StockQuant quant : quants){
//                    quantSb.append(" 库位 : " + locationService.getLocation(quant.getLocationId()).getLocationCode() + " \n ");
//                    quantSb.append(" 箱规 : " + quant.getPackUnit() + " \n ");
//                    quantSb.append(" 数量 : " + quant.getQty() + " \n ");
//                    quantSb.append(" 货主 : " + quant.getOwnerId() + " \n ");
//                }
//            }
//        }
//        Map<String,Object> rep = new HashMap<String, Object>();
//        rep.put("data",itemSb.append( " \n " ).append(quantSb));
//        return rep;
//
//    }
}
