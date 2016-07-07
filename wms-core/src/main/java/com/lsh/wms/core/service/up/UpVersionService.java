package com.lsh.wms.core.service.up;

import com.google.common.collect.Maps;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.wms.core.model.up.UpVersion;
import com.lsh.wms.core.constant.BusiConstant;
import com.lsh.wms.core.constant.RedisKeyConstant;
import com.lsh.wms.core.dao.redis.RedisHashDao;
import com.lsh.wms.core.dao.redis.RedisSortedSetDao;
import com.lsh.wms.core.dao.up.UpVersionDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 版本
 */
@Component
@Transactional(readOnly = true)
public class UpVersionService {

    private static Logger logger = LoggerFactory.getLogger(UpVersionService.class);

    @Autowired
    private UpVersionDao versionDao;

    @Autowired
    private RedisSortedSetDao sortedSetDao;
    @Autowired
    private RedisHashDao hashDao;

    public Integer countVersion(String keyword, Integer appId, Integer osId, Integer chnId, Integer modId, Integer pkgType, String verStr) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("chnId", chnId);
        params.put("modId", modId);
        params.put("pkgType", pkgType);
        params.put("verStr", verStr);
        return versionDao.countVersion(params);
    }

    public List<UpVersion> getVersionList(String keyword, Integer appId, Integer osId, Integer chnId, Integer modId, Integer pkgType, String verStr, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("chnId", chnId);
        params.put("modId", modId);
        params.put("pkgType", pkgType);
        params.put("verStr", verStr);
        params.put("start", start);
        params.put("limit", limit);
        return versionDao.getVersionList(params);
    }

    public List<UpVersion> getVersionList(Integer appId, Integer osId, String verStr) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("verStr", verStr);
        return versionDao.getVersionList(params);
    }

    public Integer countVersionNo(Integer appId, Integer osId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("osId", osId);
        return versionDao.countVersionNo(params);
    }

    public List<UpVersion> getVersionNoList(Integer appId, Integer osId, Integer start, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("osId", osId);
        params.put("start", start);
        params.put("limit", limit);
        return versionDao.getVersionNoList(params);
    }

    public UpVersion getVersionById(Long id) {
        if (id == null) {
            return null;
        }
        return versionDao.getVersionById(id);
    }

    public boolean existVersion(Long pkgId, String verStr) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("pkgId", pkgId);
        params.put("verStr", verStr);
        return versionDao.countVersion(params) > 0;
    }

    @Transactional(readOnly = false)
    public void insert(UpVersion version) {
        if (version == null) {
            return;
        }
        version.setCreatedTime(new Date());
        versionDao.insert(version);
    }

    @Transactional(readOnly = false)
    public void update(UpVersion version) {
        if (version == null) {
            return;
        }
        version.setUpdatedTime(new Date());
        versionDao.update(version);
        cacheVersion(version.getId());
    }

    public void cacheVersion(Long id) {
        UpVersion version = getVersionById(id);
        if (version == null) {
            return;
        }
        if (version.getStatus() != null && version.getStatus() == BusiConstant.EFFECTIVE_YES) {
            String key_pkg_ver = StrUtils.formatString(RedisKeyConstant.UP_PKG_VER, version.getPkgId());
            sortedSetDao.add(key_pkg_ver, version.getId().toString(), version.getVerInt());
            Map<String, String> hash = Maps.newLinkedHashMap();
            hash.put("pkg_id", ObjUtils.toString(version.getPkgId(), "-1"));
            hash.put("ver_str", ObjUtils.toString(version.getVerStr(), ""));
            hash.put("ver_int", ObjUtils.toString(version.getVerInt(), "-1"));
            hash.put("ver_name", ObjUtils.toString(version.getVerName(), ""));
            hash.put("ver_title", ObjUtils.toString(version.getVerTitle(), ""));
            hash.put("ver_title_force", ObjUtils.toString(version.getVerTitleForce(), ""));
            hash.put("ver_desc", ObjUtils.toString(version.getVerDesc(), ""));
            hash.put("ver_desc_force", ObjUtils.toString(version.getVerDescForce(), ""));
            hash.put("up_type", ObjUtils.toString(version.getUpType(), "-1"));
            hash.put("silent_download", ObjUtils.toString(version.getSilentDownload(), "-1"));
            hash.put("silent_install", ObjUtils.toString(version.getSilentInstall(), "-1"));
            hash.put("prompt_up", ObjUtils.toString(version.getPromptUp(), "-1"));
            hash.put("prompt_always", ObjUtils.toString(version.getPromptAlways(), "-1"));
            hash.put("prompt_interval", ObjUtils.toString(version.getPromptInterval(), "-1"));
            hash.put("file_md5", ObjUtils.toString(version.getFileMd5(), ""));
            hash.put("file_url", ObjUtils.toString(version.getFileUrl(), ""));
            String key_ver_info = StrUtils.formatString(RedisKeyConstant.UP_VER_INFO, version.getId());
            hashDao.putAll(key_ver_info, hash);
        } else {
            // 删除版本信息
            String key_pkg_ver = StrUtils.formatString(RedisKeyConstant.UP_PKG_VER, version.getPkgId());
            sortedSetDao.remove(key_pkg_ver, version.getId().toString());
            // 删除版本详细信息
            String key_ver_info = StrUtils.formatString(RedisKeyConstant.UP_VER_INFO, version.getId());
            hashDao.delete(key_ver_info);
        }
    }

}
