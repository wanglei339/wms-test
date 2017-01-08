package com.lsh.wms.core.service.item;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemDao;
import com.lsh.wms.core.dao.baseinfo.BaseinfoItemQuantRangeDao;
import com.lsh.wms.core.dao.baseinfo.ItemSkuRelationDao;
import com.lsh.wms.core.dao.csi.CsiSkuDao;
import com.lsh.wms.core.dao.stock.StockQuantDao;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.persistence.PersistenceProxy;
import com.lsh.wms.core.service.utils.IdGenerator;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.baseinfo.BaseinfoItemQuantRange;
import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import com.lsh.wms.model.baseinfo.ItemSkuRelation;
import com.lsh.wms.model.csi.CsiSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zengwenjun on 16/7/6.
 */

@Component
@Transactional(readOnly = true)
public class ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private static final ConcurrentMap<Long, BaseinfoItem> m_ItemCache = new ConcurrentHashMap<Long, BaseinfoItem>();
    @Autowired
    private BaseinfoItemDao itemDao;
    @Autowired
    private BaseinfoItemQuantRangeDao rangeDao;
    @Autowired
    private CsiSkuService csiSkuService;
    @Autowired
    private CsiSkuDao csiSkuDao;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private ItemSkuRelationDao itemSkuRelationDao;
    @Autowired
    private PersistenceProxy persistenceProxy;


    public BaseinfoItem getItem(long iOwnerId, long iSkuId){
        //cache中不存在,穿透查询mysql
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", iOwnerId);
        mapQuery.put("skuId", iSkuId);
        // TODO: 2016/12/13 先查询关系表 从关系表中取出itemid 通过itemId来定位一条item
        List<ItemSkuRelation> itemSkuRelations = itemSkuRelationDao.getItemSkuRelationList(mapQuery);
        if(itemSkuRelations != null && itemSkuRelations.size() > 0){
            ItemSkuRelation itemSkuRelation = itemSkuRelations.get(0);
            Long itemId = itemSkuRelation.getItemId();
            BaseinfoItem item = this.getItem(itemId);
            return item;
        }else{
            return null;
        }
    }



    public BaseinfoItemQuantRange getItemRange(Long itemId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<BaseinfoItemQuantRange> ranges = rangeDao.getBaseinfoItemQuantRangeList(mapQuery);

        return ranges==null ||ranges.isEmpty() ? null :ranges.get(0);
    }
    @Transactional(readOnly = false)
    public void insertItemRange(BaseinfoItemQuantRange range){
        range.setUpdatedAt(DateUtils.getCurrentSeconds());
        range.setCreatedAt(DateUtils.getCurrentSeconds());
        rangeDao.insert(range);

    }
    @Transactional(readOnly = false)
    public void updateItemRange(BaseinfoItemQuantRange range){
        range.setUpdatedAt(DateUtils.getCurrentSeconds());
        rangeDao.update(range);

    }

    public BaseinfoItem getItemsBySkuCode(long iOwnerId, String sSkuCode){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("ownerId", iOwnerId);
        mapQuery.put("skuCode", sSkuCode);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        if(items == null || items.size() <= 0){
            return null;
        }
        return items.get(0);
    }

    public BaseinfoItem getItemByPackCode(long iOwnerId, String packCode){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("packCode", packCode);
        mapQuery.put("ownerId",iOwnerId);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        if(items == null || items.size() <= 0){
            return null;
        }
        return items.get(0);
    }

//    /**
//     * 查找有效的item
//     * @param skuCode
//     * @param ownerId
//     * @return
//     */
//    public BaseinfoItem getItemBySkuCodeAndOwnerId(String skuCode,Long ownerId){
//        Map<String, Object> mapQuery = new HashMap<String, Object>();
//        mapQuery.put("ownerId", ownerId);
//        mapQuery.put("skuCode", skuCode);
//        mapQuery.put("isValid",1);
//        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
//        if(items ==  null || items.size() <= 0){
//            return null;
//        }
//        return items.get(0);
//    }

    @Transactional(readOnly = false)
    public BaseinfoItem insertItem(BaseinfoItem item){
        //如果sku表中不存在,更新sku表
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("code",item.getCode());
        mapQuery.put("codeType",item.getCodeType());
        List<CsiSku> skus = csiSkuDao.getCsiSkuList(mapQuery);
        if(skus.size() > 0){
            item.setSkuId(skus.get(0).getSkuId());
        }else{
            CsiSku sku = new CsiSku();
            String code = item.getCode();
            sku.setCode(code);
            sku.setCodeType(item.getCodeType().toString());
            sku.setShelfLife(item.getShelfLife());
            sku.setSkuName(item.getSkuName());
            sku.setHeight(item.getHeight());
            sku.setLength(item.getLength());
            sku.setWidth(item.getWidth());
            sku.setWeight(item.getWeight());
            sku.setCreatedAt(DateUtils.getCurrentSeconds());
            //生成csi_sku表
            csiSkuService.insertSku(sku);
            item.setSkuId(sku.getSkuId());
        }
        //gen itemId
        item.setItemId(idGenerator.genId("item_main", false, false));
        item.setCreatedAt(DateUtils.getCurrentSeconds());
        //创建商品
        itemDao.insert(item);
        //更新关系表数据
        ItemSkuRelation itemSkuRelation = new ItemSkuRelation();
        itemSkuRelation.setItemId(item.getItemId());
        itemSkuRelation.setSkuId(item.getSkuId());
        itemSkuRelation.setOwnerId(item.getOwnerId());
        itemSkuRelation.setIsValid(1l);
        itemSkuRelation.setUpdatedAt(DateUtils.getCurrentSeconds());
        itemSkuRelation.setCreatedAt(DateUtils.getCurrentSeconds());
        itemSkuRelationDao.insert(itemSkuRelation);
        return item;
    }

    @Transactional(readOnly = false)
    public void updateBarcode(Long itemId,String barcode){
        BaseinfoItem item = this.getItem(itemId);
        //如果sku表中不存在,更新sku表
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("code", barcode);
        mapQuery.put("codeType", item.getCodeType());
        List<CsiSku> skus = csiSkuDao.getCsiSkuList(mapQuery);
        if(skus != null && skus.size() > 0){
            item.setSkuId(skus.get(0).getSkuId());
            item.setCode(barcode);
        }else{
            CsiSku sku = new CsiSku();
            //String code = item.getCode();
            sku.setCode(barcode);
            sku.setCodeType(item.getCodeType().toString());
            sku.setShelfLife(item.getShelfLife());
            sku.setSkuName(item.getSkuName());
            sku.setHeight(item.getHeight());
            sku.setLength(item.getLength());
            sku.setWidth(item.getWidth());
            sku.setWeight(item.getWeight());
            sku.setCreatedAt(DateUtils.getCurrentSeconds());
            //生成csi_sku表
            csiSkuService.insertSku(sku);
            item.setSkuId(sku.getSkuId());
            item.setCode(barcode);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("itemId", item.getItemId());
        map.put("skuId", item.getSkuId());
        map.put("ownerId", item.getOwnerId());

        List<ItemSkuRelation> list = itemSkuRelationDao.getItemSkuRelationList(map);
        if(list == null || list.size() <= 0){
            //新增关系表数据
            ItemSkuRelation itemSkuRelation = new ItemSkuRelation();
            itemSkuRelation.setOwnerId(item.getOwnerId());
            itemSkuRelation.setIsValid(1L);
            itemSkuRelation.setItemId(item.getItemId());
            itemSkuRelation.setSkuId(item.getSkuId());
            itemSkuRelation.setCreatedAt(DateUtils.getCurrentSeconds());
            itemSkuRelation.setUpdatedAt(DateUtils.getCurrentSeconds());
            itemSkuRelationDao.insert(itemSkuRelation);
        }
        //更新item数据
        this.updateItem(item);
    }

    @Transactional(readOnly = false)
    public void updateItem(BaseinfoItem item){
        item.setUpdatedAt(DateUtils.getCurrentSeconds());
        //更新商品
        itemDao.update(item);

        //回传商品信息
        if(item.getOwnerId()==null) {
            BaseinfoItem oldItem = this.getItem(item.getItemId());
            if (oldItem.getOwnerId().compareTo(2L) == 0) {
                
                persistenceProxy.doOne(SysLogConstant.LOG_TYPE_BACK_COMMODITY, item.getItemId(), 0);
            }
        }else {
            if (item.getOwnerId().compareTo(2L) == 0) {
                persistenceProxy.doOne(SysLogConstant.LOG_TYPE_BACK_COMMODITY, item.getItemId(), 0);
            }
        }
    }

    public int deleteItem(BaseinfoItem item){
        return -1;
    }

    //按品类,sku_id,owner等查询
    public List<BaseinfoItem> searchItem(Map<String, Object> mapQuery){
        //增加skuName模糊查询
        String skuName = (String) mapQuery.get("skuName");
        if(skuName != null){
            skuName = "%"+skuName+"%";
            mapQuery.put("skuName",skuName);
        }
        //增加skuCode模糊查询
        String skuCode = (String) mapQuery.get("skuCode");
        if(skuCode != null){
            skuCode = "%"+skuCode+"%";
            mapQuery.remove("skuCode");
            mapQuery.put("skuCodeSearch",skuCode);
        }
        return itemDao.getBaseinfoItemList(mapQuery);
    }

    //按获取count
    public int countItem(Map<String, Object> mapQuery){
        return itemDao.countBaseinfoItem(mapQuery);
    }

    public BaseinfoItem getItem(long itemId){
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId", itemId);
        List<BaseinfoItem> items = itemDao.getBaseinfoItemList(mapQuery);
        return items.size() == 0 ? null : items.get(0);
    }

    @Transactional(readOnly = false)
    public void setStatus(long iItemId,long iStatus){
        BaseinfoItem item = new BaseinfoItem();
        item.setItemId(iItemId);
        item.setStatus(iStatus);
        this.updateItem(item);
    }


    /**
     * 查找货主为物美的所有商品 返回商品编码集合
     */
    public List<String> getSkuCodeList(Long ownerId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ownerId", ownerId);
        List<BaseinfoItem> itemList = itemDao.getBaseinfoItemList(map);
        List<String> skuCodes = new ArrayList<String>();
        for (BaseinfoItem item : itemList) {
            skuCodes.add(item.getSkuCode());
        }
        return skuCodes;
    }

    /**
     * 根据itemId查询历史国条
     */
    public List<CsiSku> getCsiSkuListByItemId(Long itemId){
        List<CsiSku> list = new ArrayList<CsiSku>();
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<ItemSkuRelation> itemSkuRelations = itemSkuRelationDao.getItemSkuRelationList(mapQuery);
        for(ItemSkuRelation relation : itemSkuRelations){
            CsiSku sku = csiSkuService.getSku(relation.getSkuId());
            list.add(sku);
        }
        return list;
    }
    /**
     * 判断该商品是否存在该国条
     */
    public boolean checkSkuItem(Long itemId,Long skuId){
        Map<String,Object> mapQuery = new HashMap<String, Object>();
        mapQuery.put("itemId",itemId);
        List<ItemSkuRelation> itemSkuRelations = itemSkuRelationDao.getItemSkuRelationList(mapQuery);
        for(ItemSkuRelation relation : itemSkuRelations){
           if(relation.getSkuId().compareTo(skuId)==0){
               return true;
           }
        }
       return false;
    }


}
