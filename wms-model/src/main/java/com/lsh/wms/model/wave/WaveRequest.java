package com.lsh.wms.model.wave;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/7/18.
 */
public class WaveRequest implements Serializable {
    private String waveName;
    /** 波次状态，10-新建，20-确定释放，30-释放完成，40-释放失败，50-已完成[完全出库], 100－取消 */
    private Long status = 10L;
    /** 波次类型 */
    private Long waveType = 1L;
    /** 波次模版id */
    private Long waveTemplateId = 0L;
    /** 波次产生来源，SYS-系统，TMS-运输系统 */
    private String waveSource = "SYS";


    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getWaveSource() {
        return waveSource;
    }

    public void setWaveSource(String waveSource) {
        this.waveSource = waveSource;
    }

    public Long getWaveTemplateId() {
        return waveTemplateId;
    }

    public void setWaveTemplateId(Long waveTemplateId) {
        this.waveTemplateId = waveTemplateId;
    }

    public Long getWaveType() {
        return waveType;
    }

    public void setWaveType(Long waveType) {
        this.waveType = waveType;
    }

    public String getWaveName() {
        return waveName;
    }

    public void setWaveName(String waveName) {
        this.waveName = waveName;
    }

    private List<Map> orders = null;

    public List<Map> getOrders() {
        return orders;
    }

    public void setOrders(List<Map> orders) {
        this.orders = orders;
    }

    public WaveRequest(List<Map> orders, Long status, String waveName, String waveSource, Long waveTemplateId, Long waveType) {
        this.orders = orders;
        this.status = status;
        this.waveName = waveName;
        this.waveSource = waveSource;
        this.waveTemplateId = waveTemplateId;
        this.waveType = waveType;
    }

    public WaveRequest() {
    }
}
