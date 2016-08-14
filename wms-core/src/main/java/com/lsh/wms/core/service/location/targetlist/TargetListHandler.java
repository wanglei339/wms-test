package com.lsh.wms.core.service.location.targetlist;

import com.lsh.wms.model.baseinfo.BaseinfoLocation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 获取指定条件的获取位置方法的策略
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 16/8/10 下午5:49
 */
@Component
public interface TargetListHandler {
    List<BaseinfoLocation> getTargetLocaltionModelList();
}
