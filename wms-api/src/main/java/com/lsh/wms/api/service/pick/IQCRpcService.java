package com.lsh.wms.api.service.pick;

import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.model.task.TaskInfo;
import com.lsh.wms.model.wave.WaveDetail;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/30.
 */
public interface IQCRpcService {
    void skipException(long id) throws BizCheckedException; //忽略qc异常,照常发货
    void repairException(long id) throws BizCheckedException; //修复异常,会设置pick_qty=qc_qty,同时保留qc遗迹
    void fallbackException(long id) throws BizCheckedException; //回退异常,qc自身错误,会设置pick_qty=qc_qty,同时保留qc遗迹

    //前端组盘页面的显示
    List<Map<String, Object>> getGroupList(Map<String, Object> mapQuery) throws BizCheckedException;
    Integer countGroupList(Map<String, Object> mapQuery) throws BizCheckedException;
    public Map<Long, Map<String, Object>> getGroupDetailByStoreNo(String storeNo) throws BizCheckedException;
    public List<TaskInfo> getQcDoneTaskInfoByWaveDetails(List<WaveDetail> waveDetails)throws BizCheckedException;
    public List<WaveDetail> getQcWaveDetailsByStoreNo(String storeNo)throws BizCheckedException;

    public boolean repairExceptionRf(Map<String, Object> request) throws BizCheckedException;
    public boolean fallbackExceptionRf(Map<String, Object> request) throws BizCheckedException;
    public boolean skipExceptionRf(Map<String, Object> request) throws BizCheckedException;

    public Map<Long,Map<String,Object>> getQcDoneExpensiveMapByCustmerCode(String custmerCode) throws BizCheckedException;




}
