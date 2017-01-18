package com.lsh.wms.rf.service.back;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.service.back.IInStorageRfRestService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.location.ILocationRpcService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.api.service.task.ITaskRpcService;
import com.lsh.wms.core.constant.BinUsageConstant;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.constant.LocationConstant;
import com.lsh.wms.core.service.csi.CsiSupplierService;
import com.lsh.wms.core.service.item.ItemLocationService;
import com.lsh.wms.core.service.item.ItemService;
import com.lsh.wms.core.service.location.LocationService;
import com.lsh.wms.core.service.so.SoOrderService;
import com.lsh.wms.core.service.stock.StockMoveService;
import com.lsh.wms.core.service.stock.StockQuantService;
import com.lsh.wms.core.service.task.BaseTaskService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemLocation;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.stock.StockMove;
import com.lsh.wms.model.stock.StockQuant;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhao on 2016/10/21.
 */

@Service(protocol = "rest")
@Path("back/in_storage")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class InStorageRfRestService  implements IInStorageRfRestService {
    private static Logger logger = LoggerFactory.getLogger(InStorageRfRestService.class);

    @Autowired
    SoOrderService soOrderService;
    @Autowired
    private ItemService itemService;
    @Reference
    private IItemRpcService itemRpcService;
    @Autowired
    private ItemLocationService itemLocationService;

    @Reference
    private ITaskRpcService iTaskRpcService;

    @Autowired
    private BaseTaskService baseTaskService;
    @Autowired
    private StockQuantService quantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CsiSupplierService supplierService;
    @Autowired
    private StockMoveService moveService;

    @Reference
    private ILocationRpcService locationRpcService;
    /**
     * 获得捡货位信息
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("getPickLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String getPickLocation() throws BizCheckedException {
        List<String>  barcodeList= null;
        Map<String,Object> result = new HashMap<String, Object>();
        Map<String, Object> request = RequestUtils.getRequest();
        try {
 //           barcodeList =(List)request.get("barcodeList");
            JSONArray jsonArray = JSONArray.fromObject(request.get("barcodeList"));
            barcodeList = (List) JSONArray.toCollection(jsonArray,
                    String.class);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtils.TOKEN_ERROR("参数传递格式有误");
        }
        //check 国条信息
        boolean isAllTrue = true;
        BaseinfoLocation backLocation = locationService.getAntiLocation();
        StringBuilder errorStr = new StringBuilder();
        List<Map> locationList = new ArrayList<Map>();
        List<BaseinfoLocation> shelfLocation = new ArrayList<BaseinfoLocation>();
        List<BaseinfoLocation> loftLocation = new ArrayList<BaseinfoLocation>();
        Map<Long,Map> locationMap = new HashMap<Long, Map>();
        if(barcodeList == null || barcodeList.size()==0){
            return JsonUtils.TOKEN_ERROR("请扫国条或箱码");
        }
        for(String barcode:barcodeList) {
            barcode = barcode.trim();
            Long itemId = 0L;
            CsiSku csiSku = itemRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, barcode);
            if (csiSku == null) {
                List<BaseinfoItem> items = itemService.getItemByPackCode(barcode);
                if (items == null || items.size() == 0) {
                    errorStr.append("国条(箱码)"+barcode+",").append("在仓库不存在").append(System.getProperty("line.separator"));
                    isAllTrue = false;
                    continue;
                }
                itemId= items.get(0).getItemId();
            } else {
                //货主默认为链商
                itemId  = itemService.getItemIdBySkuAndOwner(CsiConstan.OWNER_LSH,csiSku.getSkuId());
            }
            //check itemID 在反仓区有没有库存
            Map<String,Object> locationOne = new HashMap<String, Object>();
            List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationList(itemId);
            if(itemLocationList ==null || itemLocationList.size() ==0){
                isAllTrue = false;
                errorStr.append("国条(箱码)"+barcode+",").append("在仓库没有维护捡货位").append(System.getProperty("line.separator"));
                continue;
            }
            BigDecimal backQty = quantService.getQuantQtyByLocationIdAndItemId(backLocation.getLocationId(),itemId);
            if(backQty.compareTo(BigDecimal.ZERO)==0){
                isAllTrue = false;
                errorStr.append("国条(箱码)"+barcode+",").append("在反仓区无库存").append(System.getProperty("line.separator"));
                continue;
            }
            BaseinfoLocation pickLocation = locationService.getLocation(itemLocationList.get(0).getPickLocationid());
            BaseinfoItem item = itemService.getItem(itemId);
            locationOne.put("locationCode",pickLocation.getLocationCode());
            locationOne.put("locationId",pickLocation.getLocationId());
            locationOne.put("skuCode",item.getSkuCode());
            if(csiSku==null) {
                locationOne.put("barcode", item.getCode());
            }else {
                locationOne.put("barcode", barcode);
            }
            locationOne.put("packCode",item.getPackCode());
            locationOne.put("packName",item.getPackName());
            locationOne.put("skuName", item.getSkuName());
            if(pickLocation.getRegionType().compareTo(LocationConstant.LOFTS) == 0) {
                loftLocation.add(pickLocation);
            }else {
                shelfLocation.add(pickLocation);
            }
            locationMap.put(pickLocation.getLocationId(), locationOne);
        }
        if(isAllTrue) {
            shelfLocation  = locationService.calcZwayOrder(shelfLocation,true);
            loftLocation = locationService.calcZwayOrder(loftLocation,true);
            for(BaseinfoLocation baseinfoLocation:shelfLocation){
                locationList.add(locationMap.get(baseinfoLocation.getLocationId()));
            }
            for(BaseinfoLocation baseinfoLocation:loftLocation){
                locationList.add(locationMap.get(baseinfoLocation.getLocationId()));
            }
            result.put("locationList",locationList);
            return JsonUtils.SUCCESS(result);
        }else {
            return JsonUtils.TOKEN_ERROR(errorStr.toString());
        }
    }
    /**
     * 获得so detail信息
     * @return
     * @throws BizCheckedException
     */
    @POST
    @Path("scanLocation")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_JSON})
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public String scanLocation() throws BizCheckedException {
        Map<String, Object> params = RequestUtils.getRequest();
        String locationCode = params.get("locationCode").toString().trim();
        BigDecimal umoQty = BigDecimal.ZERO;
        BigDecimal scatterQty = BigDecimal.ZERO;
        Long uId =  Long.valueOf(RequestUtils.getHeader("uid"));
        if(params.get("umoQty")!=null && !params.get("umoQty").toString().trim().equals("")){
            umoQty = new BigDecimal(params.get("umoQty").toString().trim());
        }
        if(params.get("scatterQty")!=null && !params.get("scatterQty").toString().trim().equals("")){
            scatterQty = new BigDecimal(params.get("scatterQty").toString().trim());
        }
        BaseinfoLocation location = locationRpcService.getLocationByCode(locationCode);
        if(location == null){
            return JsonUtils.TOKEN_ERROR("扫描库位信息有误");
        }
        if(!location.getBinUsage().equals(BinUsageConstant.BIN_UASGE_PICK) && !location.getBinUsage().equals(BinUsageConstant.BIN_PICK_STORE)){
            return JsonUtils.TOKEN_ERROR("请扫描捡货位");
        }
        List<BaseinfoItemLocation> itemLocationList = itemLocationService.getItemLocationByLocationId(location.getLocationId());
        if(itemLocationList ==null){
            return JsonUtils.TOKEN_ERROR("该捡货位未维护商品信息");
        }
        BaseinfoItem item = itemService.getItem(itemLocationList.get(0).getItemId());
        BigDecimal moveQty = umoQty.multiply(item.getPackUnit()).add(scatterQty);
        BaseinfoLocation backLocation = locationService.getAntiLocation();
        //拿到捡货位的托盘码，如果没有，则为0
        Long containerId = null;
        List<Long> containerIdList = quantService.getContainerIdByLocationId(location.getLocationId());
        if(containerIdList !=null && containerIdList.size()!=0){
            containerId = containerIdList.get(0);
        }
        //move,可能有多个托盘移动
        List<StockQuant> quants = quantService.getQuantByLocationIdAndItemId(backLocation.getLocationId(),item.getItemId());
        Map<Long,Integer> movedContainer = new HashMap<Long, Integer>();
        List<StockMove> moves = new ArrayList<StockMove>();
        for(StockQuant quant :quants){
            if(moveQty.compareTo(BigDecimal.ZERO)<=0) {
                break;
            }
            if(movedContainer.containsKey(quant.getContainerId())){
                continue;
            }
            BigDecimal containerQty = quantService.getQuantQtyByContainerId(quant.getContainerId());
            StockMove move = new StockMove();
            move.setItemId(item.getItemId());
            move.setFromContainerId(quant.getContainerId());
            move.setFromLocationId(backLocation.getLocationId());
            move.setToLocationId(location.getLocationId());
            move.setOperator(uId);
            if(containerId!=null) {
                move.setToContainerId(containerId);
            }else {
                move.setToContainerId(quant.getContainerId());
            }
            if(containerQty.compareTo(moveQty)<0){
                move.setQty(containerQty);
            }else {
                move.setQty(moveQty);
            }
            moveQty  = moveQty.subtract(containerQty);
            moves.add(move);
            movedContainer.put(quant.getContainerId(),1);
        }
        if(moveQty.compareTo(BigDecimal.ZERO)>0){
            return JsonUtils.TOKEN_ERROR("反仓区库存不足");
        }
        moveService.move(moves);
        return JsonUtils.SUCCESS(new HashMap<String, Boolean>() {
            {
                put("response", true);
            }
        });
    }
}
