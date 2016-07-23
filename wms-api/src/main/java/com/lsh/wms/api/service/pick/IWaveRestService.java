package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.pick.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IWaveRestService {
    public String getList(Map<String, Object> mapQuery);
    public String  getListCount(Map<String, Object> mapQuery);
    public String getWave(long iWaveId);
    public String getWaveOrders(long iWaveId);
    public String releaseWave(long iWaveId, long iUid) throws BizCheckedException;
    String createWave(WaveRequest request);
    String setStatus(long iWaveId, int iStatus);

    String getPickzoneList(Map<String, Object> mapQuery);
    String getPickzoneCount(Map<String, Object> mapQuery);
    String getPickzone(long iPickZoneId);
    String createPickzone(PickZone zone);
    String updatePickzone(PickZone zone);

    String getPickModelTplList(Map<String, Object> mapQuery);
    String getPickModelTplCount(Map<String, Object> mapQuery);
    String getPickModelTpl(long iPickModelTplId);
    String createPickModelTpl(PickModelTemplate tpl);
    String updatePickModelTpl(PickModelTemplate tpl);

    String getPickModelList(long iPickModelTplId);
    String getPickModel(long iPickModelId);
    String createPickModel(PickModel model) throws BizCheckedException;
    String updatePickModel(PickModel model);
    String deletePickModel(long iPickModelId);
}
