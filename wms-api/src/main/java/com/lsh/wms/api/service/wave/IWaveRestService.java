package com.lsh.wms.api.service.wave;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.model.pick.*;
import com.lsh.wms.model.wave.WaveRequest;
import com.lsh.wms.model.wave.WaveTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/15.
 */
public interface IWaveRestService {
    public String getList(Map<String, Object> mapQuery);
    public String  getListCount(Map<String, Object> mapQuery);
    public String getWave(long iWaveId);
    public String getWaveOrders(long iWaveId);
    public String shipWave(long iWaveId, long iUid) throws BizCheckedException;
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

    String getWaveDetailList(HashMap<String, Object> mapQuery);

    String getWaveQcExceptionList(long iWaveId);



    String getWaveTemplateList(Map<String, Object> mapQuery);
    String getWaveTemplateCount(Map<String, Object> mapQuery);
    String getWaveTemplate(long waveTemplateId);
    String createWaveTemplate(WaveTemplate tpl) throws BizCheckedException;
    String updateWaveTemplate(WaveTemplate tpl) throws BizCheckedException;

    //test
    public String runWaveGenerator() throws BizCheckedException;
    public String getWavePreviewList();
    public String createWaveByPreview(Map<String, Object> mapData) throws BizCheckedException;


    public String getWaveDetailListByLocation(String collectLocationCode);
}
