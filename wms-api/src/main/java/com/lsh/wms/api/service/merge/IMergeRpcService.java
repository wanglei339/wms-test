package com.lsh.wms.api.service.merge;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.wave.WaveDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by fengkun on 2016/10/20.
 */
public interface IMergeRpcService {
    List<Map<String, Object>> getMergeList(Map<String, Object> mapQuery) throws BizCheckedException;
    Integer countMergeList(Map<String, Object> mapQuery) throws BizCheckedException;
    Map<String, BigDecimal> getQcCountsByWaveDetail(WaveDetail waveDetail) throws BizCheckedException;
}
