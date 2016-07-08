package com.lsh.wms.core.service.pub;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lsh.base.common.utils.DateUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.model.pub.PubDict;
import com.lsh.wms.model.pub.PubDictItem;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.dao.pub.PubDictDao;
import com.lsh.wms.core.dao.pub.PubDictItemDao;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisListDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Transactional(readOnly = true)
public class PubDictService {

    private static Logger logger = LoggerFactory.getLogger(PubDictService.class);

    @Autowired
    private PubDictDao pubDictDao;
    @Autowired
    private PubDictItemDao pubDictItemDao;

    @Autowired
    private RedisListDao listDao;
    @Autowired
    private RedisHashDao hashDao;

    @Transactional(readOnly = false)
    public void insertPubDict(PubDict pubDict) {
        if (pubDict == null) {
            logger.warn("insert PubDict is null.");
            return;
        }
        pubDict.setCreatedTime(new Date());
        pubDictDao.insert(pubDict);
    }

    @Transactional(readOnly = false)
    public void updatePubDict(PubDict pubDict) {
        if (pubDict == null) {
            logger.warn("update PubDict is null.");
            return;
        }
        pubDict.setUpdatedTime(new Date());
        pubDictDao.update(pubDict);
    }

    public Integer countPubDict(String keyword) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        return pubDictDao.countPubDict(params);
    }

    public List<PubDict> getPubDictList(String keyword, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("start", start);
        params.put("limit", limit);
        return pubDictDao.getPubDictList(params);
    }

    public PubDict getPubDictById(Integer id) {
        if (id == null) {
            return null;
        }
        return pubDictDao.getPubDictById(id);
    }

    public boolean existPubDict(String dictCode) {
        if (StringUtils.isBlank(dictCode)) {
            return false;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("dictCode", dictCode);
        return pubDictDao.countPubDict(params) > 0;
    }

    @Transactional(readOnly = false)
    public void insertPubDictItem(PubDictItem pubDictItem) {
        if (pubDictItem == null) {
            logger.warn("insert PubDictItem is null.");
            return;
        }
        pubDictItem.setCreatedTime(new Date());
        pubDictItemDao.insert(pubDictItem);
    }

    @Transactional(readOnly = false)
    public void updatePubDictItem(PubDictItem pubDictItem) {
        if (pubDictItem == null) {
            logger.warn("update PubDictItem is null.");
            return;
        }
        pubDictItem.setUpdatedTime(new Date());
        pubDictItemDao.update(pubDictItem);
    }

    public List<PubDict> getValidDictList(Integer dictType) {
        Map<String, Object> params = Maps.newHashMap();
        if (dictType != null) {
            params.put("dictType", dictType);
        }
        params.put("status", BusiConstant.EFFECTIVE_YES);
        return pubDictDao.getPubDictList(params);
    }

    public List<PubDictItem> getValidDictItemListByDid(Integer did) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("did", did);
        params.put("status", BusiConstant.EFFECTIVE_YES);
        return pubDictItemDao.getPubDictItemList(params);
    }

    public List<PubDictItem> getValidDictItemListByDictCode(String dictCode) {
        if (StringUtils.isEmpty(dictCode)) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("dictCode", dictCode);
        params.put("status", BusiConstant.EFFECTIVE_YES);
        return pubDictItemDao.getPubDictItemList(params);
    }

    public Map<String, PubDictItem> getValidDictItemMapByDid(Integer did) {
        List<PubDictItem> dictItemList = getValidDictItemListByDid(did);
        if (dictItemList == null || dictItemList.isEmpty()) {
            return null;
        }
        Map<String, PubDictItem> itemMap = Maps.newHashMap();
        for (PubDictItem dictItem : dictItemList) {
            itemMap.put(dictItem.getItemValue(), dictItem);
        }
        return itemMap;
    }

    public Map<String, PubDictItem> getValidDictItemMapByDictCode(String dictCode) {
        List<PubDictItem> dictItemList = getValidDictItemListByDictCode(dictCode);
        if (dictItemList == null || dictItemList.isEmpty()) {
            return null;
        }
        Map<String, PubDictItem> itemMap = Maps.newHashMap();
        for (PubDictItem dictItem : dictItemList) {
            itemMap.put(dictItem.getItemValue(), dictItem);
        }
        return itemMap;
    }

    public PubDictItem getPubDictItemById(Integer id) {
        if (id == null) {
            return null;
        }
        return pubDictItemDao.getPubDictItemById(id);
    }

    /**
     * 获取词典树
     *
     * @return
     */
    public Map<String, Object> getPubDictTreeMap(Integer dictId) {
        // 组织成树状结构
        Map<String, Object> treeMap = Maps.newHashMap();
        // 获取词典信息
        PubDict pubDict = pubDictDao.getPubDictById(dictId);
        if (pubDict == null) {
            treeMap.put("id", "0");
            treeMap.put("text", "词典不存在 0");
            return treeMap;
        }
        treeMap.put("id", "0");
        treeMap.put("text", pubDict.getDictName() + "(根节点)");
        treeMap.put("dictId", pubDict.getId());
        treeMap.put("dictType", pubDict.getDictType());
        treeMap.put("isEffective", 1);
        // 获取子节点
        Map<String, Object> params = Maps.newHashMap();
        params.put("did", dictId);
        List<PubDictItem> pubDictItemList = pubDictItemDao.getPubDictItemList(params);
        if (pubDictItemList == null || pubDictItemList.isEmpty()) {
            treeMap.put("icon", "fa fa-file icon-state-success");
        } else {
            treeMap.put("icon", "fa fa-folder icon-state-success");
            Map<String, Object> stateMap = Maps.newHashMap();
            stateMap.put("opened", true);
            treeMap.put("state", stateMap);
            treeMap.put("children", getPubDictItemChildList(pubDict, pubDictItemList, 0));
        }
        return treeMap;
    }

    /**
     * 设置子节点
     *
     * @param pubDictItemList
     * @param pid
     * @return
     */
    private List<Map<String, Object>> getPubDictItemChildList(PubDict pubDict, List<PubDictItem> pubDictItemList, Integer pid) {

        List<Map<String, Object>> childList = Lists.newArrayList();
        for (PubDictItem pubDictItem : pubDictItemList) {
            if (pubDictItem.getPid().equals(pid)) {
                Map<String, Object> nodeMap = Maps.newHashMap();
                nodeMap.put("id", pubDictItem.getId());
                nodeMap.put("text", pubDictItem.getItemValue() + " " + pubDictItem.getItemName());
                String icon = "fa";
                boolean isLeaf = isLeaf(pubDictItemList, pubDictItem.getId());
                if (isLeaf) {
                    icon = icon + " fa-file";
                } else {
                    icon = icon + " fa-folder";
                }
                if (pubDictItem.getStatus() == BusiConstant.EFFECTIVE_YES) {
                    icon = icon + " icon-state-success";
                } else {
                    icon = icon + " icon-state-danger";
                }
                nodeMap.put("icon", icon);
                nodeMap.put("dictId", pubDict.getId());
                nodeMap.put("dictType", pubDict.getDictType());
                nodeMap.put("isEffective", pubDictItem.getStatus());
                nodeMap.put("children", getPubDictItemChildList(pubDict, pubDictItemList, pubDictItem.getId()));
                childList.add(nodeMap);
            }
        }
        return childList;
    }

    /**
     * 是否为子节点
     *
     * @param pubDictItemList
     * @param pid
     * @return
     */
    private boolean isLeaf(List<PubDictItem> pubDictItemList, Integer pid) {
        for (PubDictItem pubDictItem : pubDictItemList) {
            if (pubDictItem.getPid().equals(pid)) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void moveItem(Integer itemId, Integer pid, Integer old_pid, Integer position) {
        // 1、更新当前节点的父节点
        PubDictItem item = new PubDictItem();
        item.setId(itemId);
        item.setPid(pid);
        item.setItemOrder(position);
        item.setUpdatedTime(new Date());
        pubDictItemDao.update(item);
        // 2、对父节点的子节点重新排序
        item = pubDictItemDao.getPubDictItemById(itemId);
        Map<String, Object> param = Maps.newHashMap();
        param.put("did", item.getDid());
        param.put("pid", pid);
        List<PubDictItem> childList = pubDictItemDao.getPubDictItemList(param);
        int i = 1;
        for (PubDictItem dictItem : childList) {
            PubDictItem upItem = new PubDictItem();
            if (dictItem.getId() == itemId) {
                continue;
            }
            upItem.setId(dictItem.getId());
            if (i == position) {
                i = i + 1; // 位置预留出来
            }
            upItem.setItemOrder(i);
            upItem.setUpdatedTime(new Date());
            pubDictItemDao.update(upItem);
            i = i + 1;
        }
        // 3、对原父节点的子节点重新排序，可忽略
    }

    public void online(PubDict dict, List<PubDictItem> itemList) {
        if (dict == null || itemList == null || itemList.isEmpty()) {
            logger.warn("词典上线数据为空，忽略。");
            return;
        }
        String listKey = dict.getDictCode();
        listDao.delete(listKey);
        for (PubDictItem item : itemList) {
            Map<String, Object> itemMap = Maps.newHashMap();
            itemMap.put("name", item.getItemName());
            itemMap.put("status", ObjUtils.toString(item.getStatus()));
            itemMap.put("created_time", DateUtils.FORMAT_TIME_NO_BAR.format(item.getCreatedTime()));
            itemMap.put("updated_time", DateUtils.FORMAT_TIME_NO_BAR.format(item.getUpdatedTime()));
            String itemKey = listKey + ":" + item.getItemValue();
            hashDao.delete(itemKey);
            hashDao.putAll(itemKey, itemMap);
            listDao.rightPush(listKey, item.getItemValue());
        }
    }

}
