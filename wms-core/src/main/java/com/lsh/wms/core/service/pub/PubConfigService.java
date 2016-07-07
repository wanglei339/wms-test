package com.lsh.wms.core.service.pub;

import com.google.common.collect.Maps;
import com.lsh.base.ali.OssClientUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import com.lsh.wms.model.pub.PubConfigData;
import com.lsh.wms.model.pub.PubConfigDataFile;
import com.lsh.wms.model.pub.PubConfigPage;
import com.lsh.wms.model.pub.PubConfigShow;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.UploadConstant;
import com.lsh.wms.core.dao.pub.PubConfigDataDao;
import com.lsh.wms.core.dao.pub.PubConfigDataFileDao;
import com.lsh.wms.core.dao.pub.PubConfigPageDao;
import com.lsh.wms.core.dao.pub.PubConfigShowDao;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisListDao;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 配置管理业务类.
 */
@Component
@Transactional(readOnly = true)
public class PubConfigService {

    private static Logger logger = LoggerFactory.getLogger(PubConfigService.class);

    @Autowired
    private PubConfigPageDao pageDao;
    @Autowired
    private PubConfigDataDao dataDao;
    @Autowired
    private PubConfigDataFileDao fileDao;
    @Autowired
    private PubConfigShowDao showDao;

    @Autowired
    private RedisListDao listDao;
    @Autowired
    private RedisHashDao hashDao;
    @Autowired
    private RedisSortedSetDao sortedSetDao;

    public Integer countPubConfigPage(String keyword, int pageType) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("pageType", pageType);
        return pageDao.countPubConfigPage(params);
    }

    public List<PubConfigPage> getPubConfigPageList(String keyword, int pageType, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("pageType", pageType);
        params.put("start", start);
        params.put("limit", limit);
        return pageDao.getPubConfigPageList(params);
    }

    public PubConfigPage getPubConfigPageById(Integer id) {
        if (id == null) {
            return null;
        }
        return pageDao.getPubConfigPageById(id);
    }

    @Transactional(readOnly = false)
    public void insertPubConfigPage(PubConfigPage pubConfigPage) {
        if (pubConfigPage == null) {
            logger.warn("要新增的页面为空！");
            return;
        }
        // 新增
        pubConfigPage.setCreatedTime(new Date());
        pageDao.insert(pubConfigPage);
    }

    @Transactional(readOnly = false)
    public void updatePubConfigPage(PubConfigPage pubConfigPage) {
        if (pubConfigPage == null || pubConfigPage.getId() == null) {
            logger.warn("要修改的页面为空！");
            return;
        }
        // 修改
        pubConfigPage.setUpdatedTime(new Date());
        pageDao.update(pubConfigPage);
    }

    @Transactional(readOnly = false)
    public void savePubConfigPage(PubConfigPage pubConfigPage,
                                  PubConfigData pubConfigData,
                                  List<PubConfigDataFile> dataFileList) {

        if (pubConfigPage == null || pubConfigData == null) {
            return;
        }
        // 保存页面
        if (pubConfigPage.getId() == null) {
            pubConfigPage.setCreatedTime(new Date());
            pubConfigPage.setPageType(BusiConstant.CONFIG_PAGE_TYPE_SINGLE);
            pubConfigPage.setStatus(BusiConstant.EFFECTIVE_YES);
            pageDao.insert(pubConfigPage);
        } else {
            pubConfigPage.setUpdatedTime(new Date());
            pageDao.update(pubConfigPage);
        }
        pubConfigData.setPageId(pubConfigPage.getId());
        savePubConfigData(pubConfigData, dataFileList);
    }

    @Transactional(readOnly = false)
    public void savePubConfigData(PubConfigData pubConfigData,
                                  List<PubConfigDataFile> dataFileList) {

        // 保存数据
        if (pubConfigData.getId() == null) {
            if (pubConfigData.getStatus() == null) {
                pubConfigData.setStatus(BusiConstant.EFFECTIVE_YES);
            }
            pubConfigData.setCreatedTime(new Date());
            dataDao.insert(pubConfigData);
        } else {
            pubConfigData.setUpdatedTime(new Date());
            dataDao.update(pubConfigData);
        }
        // 保存文件
        if (dataFileList == null || dataFileList.isEmpty()) {
            fileDao.deleteByDataId(pubConfigData.getId());
            return;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("dataId", pubConfigData.getId());
        List<PubConfigDataFile> dataFileList_db = fileDao.getPubConfigDataFileList(params);
        // 数据库中文件列表为空，则只新增
        if (dataFileList_db == null || dataFileList_db.isEmpty()) {
            for (PubConfigDataFile file : dataFileList) {
                if (file.getId() == null) {
                    //新增文件
                    file.setFileType(1);//图片
                    file.setFileCode(RandomUtils.uuid2());
                    file.setFileStatus(BusiConstant.EFFECTIVE_YES);
                    file.setStatus(BusiConstant.EFFECTIVE_YES);
                    file.setDataId(pubConfigData.getId());
                    file.setCreateTime(new Date());
                    // 上传到阿里云oss
                    String url = OssClientUtils.uploadImage(file.getFilePath(), new File(UploadConstant.UPLOAD_PATH_ROOT + file.getFilePath()));
                    if (StringUtils.isNotEmpty(url)) {
                        file.setFileUrl(url);
                    } else {
                        file.setFileUrl(UploadConstant.UPLOAD_SERVER_HOST + file.getFilePath());
                    }
                    fileDao.insert(file);
                }
            }
            return;
        }
        // 对比数据库和页面上的图片
        for (PubConfigDataFile file_db : dataFileList_db) {
            boolean del = true;
            for (PubConfigDataFile file : dataFileList) {
                if (file.getId() == null) {
                    file.setFileType(1);//图片
                    file.setFileStatus(BusiConstant.EFFECTIVE_YES);
                    file.setStatus(BusiConstant.EFFECTIVE_YES);
                    file.setDataId(pubConfigData.getId());
                    file.setCreateTime(new Date());
                    String url = OssClientUtils.uploadImage(file.getFilePath(), new File(UploadConstant.UPLOAD_PATH_ROOT + file.getFilePath()));
                    if (StringUtils.isNotEmpty(url)) {
                        file.setFileUrl(url);
                    } else {
                        file.setFileUrl(UploadConstant.UPLOAD_SERVER_HOST + file.getFilePath());
                    }
                    fileDao.insert(file);
                    continue;
                }
                if (file.getId().equals(file_db.getId())) {
                    del = false;
                }
                // 修改文件
                file.setDataId(pubConfigData.getId()); // 保存dataId
                file.setUpdateTime(new Date());
                if (StringUtils.isNotEmpty(file.getFilePath())) {
                    String url = OssClientUtils.uploadImage(file.getFilePath(), new File(UploadConstant.UPLOAD_PATH_ROOT + file.getFilePath()));
                    if (StringUtils.isNotEmpty(url)) {
                        file.setFileUrl(url);
                    } else {
                        file.setFileUrl(UploadConstant.UPLOAD_SERVER_HOST + file.getFilePath());
                    }
                }
                fileDao.update(file);
            }
            if (del) {
                // 删除文件
                fileDao.delete(file_db.getId());
            }
        }
    }


    public PubConfigData getPubConfigDataByPageId(Integer pageId) {
        if (pageId == null) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageId", pageId);
        List<PubConfigData> dataList = dataDao.getPubConfigDataList(params);
        if (dataList == null || dataList.isEmpty()) {
            return null;
        }
        return dataList.get(0);
    }

    public Integer countPubConfigData(int pageId, String keyword, String queryContype) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        Integer conType = ObjUtils.toInteger(queryContype);
        if (conType != null) {
            params.put("conType", queryContype);
        }
        params.put("pageId", pageId);
        return dataDao.countPubConfigData(params);
    }

    public List<PubConfigData> getPubConfigDataList(int pageId, String keyword, String queryContype, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        Integer conType = ObjUtils.toInteger(queryContype);
        if (conType != null) {
            params.put("conType", queryContype);
        }
        params.put("pageId", pageId);
        params.put("start", start);
        params.put("limit", limit);
        return dataDao.getPubConfigDataList(params);
    }

    public List<PubConfigData> getValidPubConfigDataList(Integer pageId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pageId", pageId);
        params.put("status", BusiConstant.EFFECTIVE_YES);
        return dataDao.getPubConfigDataList(params);
    }

    public PubConfigData getPubConfigDataById(Integer id) {
        if (id == null) {
            return null;
        }
        return dataDao.getPubConfigDataById(id);
    }

    public List<PubConfigDataFile> getPubConfigDataFileListByDataId(Integer dataId) {
        if (dataId == null) {
            return null;
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("dataId", dataId);
        return fileDao.getPubConfigDataFileList(params);
    }

    public void singlePageOnline(PubConfigPage pubConfigPage, PubConfigData pubConfigData) {
        if (pubConfigPage == null || pubConfigData == null) {
            logger.warn("配置页面上线数据为空，忽略。");
            return;
        }
        String pageKey = pubConfigPage.getPageCode();
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("conTitle", ObjUtils.toString(pubConfigData.getConTitle(), ""));
        dataMap.put("conUrl", ObjUtils.toString(pubConfigData.getConUrl(), ""));
        dataMap.put("conDesc", ObjUtils.toString(pubConfigData.getConDesc(), ""));
        dataMap.put("conId", ObjUtils.toString(pubConfigData.getConId(), "0"));
        dataMap.put("conP1", ObjUtils.toString(pubConfigData.getConP1(), ""));
        dataMap.put("conP2", ObjUtils.toString(pubConfigData.getConP2(), ""));
        dataMap.put("conP3", ObjUtils.toString(pubConfigData.getConP3(), ""));
        dataMap.put("conP4", ObjUtils.toString(pubConfigData.getConP4(), ""));
        dataMap.put("conP5", ObjUtils.toString(pubConfigData.getConP5(), ""));
        dataMap.put("picUrl", "");
        hashDao.delete(pageKey);
        hashDao.putAll(pageKey, dataMap);
        List<PubConfigDataFile> fileList = pubConfigData.getFileList();
        if (fileList == null || fileList.isEmpty()) {
            return;
        }
        // 把第1个图片放到hash结构中
        hashDao.put(pageKey, "picUrl", ObjUtils.toString(fileList.get(0).getFileUrl(), ""));
        String fileKey = pageKey + ":imglist";
        listDao.delete(fileKey);
        if (fileList.size() > 1) {
            for (PubConfigDataFile file : fileList) {
                listDao.rightPush(fileKey, ObjUtils.toString(file.getFileUrl(), ""));
            }
        }
    }

    public void multPageOnline(PubConfigPage pubConfigPage, List<PubConfigData> configDataList) {
        if (pubConfigPage == null || configDataList == null || configDataList.isEmpty()) {
            logger.warn("配置页面上线数据为空，忽略。");
            return;
        }
        String pageKey = pubConfigPage.getPageCode();
        Integer cacheType = pubConfigPage.getCacheType();
        Map<String, Boolean> keyClearMap = Maps.newHashMap();
        for (PubConfigData pubConfigData : configDataList) {
            if (cacheType == 2 && pubConfigData.getConType() != null) {
                // 按类型缓存
                pageKey = pubConfigPage.getPageCode() + ":" + pubConfigData.getConType();
            }
            if (!keyClearMap.containsKey(pageKey)) {
                sortedSetDao.delete(pageKey);
                keyClearMap.put(pageKey, true);
            }
            double score = 0;
            if (pubConfigData.getConOrder() != null) {
                score = pubConfigData.getConOrder().doubleValue();
            } else {
                score = pubConfigData.getId().doubleValue();
            }
            sortedSetDao.add(pageKey, ObjUtils.toString(pubConfigData.getId()), score);
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("conTitle", ObjUtils.toString(pubConfigData.getConTitle(), ""));
            dataMap.put("conUrl", ObjUtils.toString(pubConfigData.getConUrl(), ""));
            dataMap.put("conDesc", ObjUtils.toString(pubConfigData.getConDesc(), ""));
            dataMap.put("conId", ObjUtils.toString(pubConfigData.getConId(), "0"));
            dataMap.put("conP1", ObjUtils.toString(pubConfigData.getConP1(), ""));
            dataMap.put("conP2", ObjUtils.toString(pubConfigData.getConP2(), ""));
            dataMap.put("conP3", ObjUtils.toString(pubConfigData.getConP3(), ""));
            dataMap.put("conP4", ObjUtils.toString(pubConfigData.getConP4(), ""));
            dataMap.put("conP5", ObjUtils.toString(pubConfigData.getConP5(), ""));
            dataMap.put("picUrl", "");
            String dataKey = pageKey + ":" + pubConfigData.getId();
            hashDao.delete(dataKey);
            hashDao.putAll(dataKey, dataMap);
            List<PubConfigDataFile> fileList = pubConfigData.getFileList();
            if (fileList == null || fileList.isEmpty()) {
                continue;
            }
            // 把第1个图片放到hash结构中
            hashDao.put(dataKey, "picUrl", ObjUtils.toString(fileList.get(0).getFileUrl(), ""));
            String fileKey = dataKey + ":imglist";
            listDao.delete(fileKey);
            if (fileList.size() > 1) {
                for (PubConfigDataFile file : fileList) {
                    listDao.rightPush(fileKey, ObjUtils.toString(file.getFileUrl(), ""));
                }
            }
        }
    }

    @Transactional(readOnly = false)
    public void savePubConfigShow(PubConfigShow pubConfigShow) {
        if (pubConfigShow == null
                || StringUtils.isEmpty(pubConfigShow.getPageCode())) {
            logger.warn("要保存的页面显示参数为空！");
            return;
        }
        if (pubConfigShow.getId() == null) {
            pubConfigShow.setCreatedTime(new Date());
            showDao.insert(pubConfigShow);
        } else {
            pubConfigShow.setUpdatedTime(new Date());
            showDao.update(pubConfigShow);
        }
    }

    public PubConfigShow getSinglePubConfigShow(Integer pageId) {
        if (pageId == null) {
            PubConfigShow pubConfigShow = showDao.getPubConfigShowByCode("default_single");
            pubConfigShow.setId(null);
            pubConfigShow.setPageCode(null);
            return pubConfigShow;
        }
        PubConfigShow pubConfigShow = showDao.getPubConfigShowByCode(pageId.toString());
        if (pubConfigShow == null) {
            pubConfigShow = showDao.getPubConfigShowByCode("default_single");
            pubConfigShow.setId(null);
            pubConfigShow.setPageCode(pageId.toString());
        }
        return pubConfigShow;
    }

    public PubConfigShow getMultPubConfigShow(Integer pageId) {
        if (pageId == null) {
            PubConfigShow pubConfigShow = showDao.getPubConfigShowByCode("default_mult");
            pubConfigShow.setId(null);
            pubConfigShow.setPageCode(null);
            return pubConfigShow;
        }
        PubConfigShow pubConfigShow = showDao.getPubConfigShowByCode(pageId.toString());
        if (pubConfigShow == null) {
            pubConfigShow = showDao.getPubConfigShowByCode("default_mult");
            pubConfigShow.setId(null);
            pubConfigShow.setPageCode(pageId.toString());
        }
        return pubConfigShow;
    }

}
