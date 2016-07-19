package com.lsh.wms.model.pick;


import java.io.Serializable;
import java.util.List;

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
    /** 波次产生目的, COMMON-普通, YG-优供, SUPERMARKET-大卖场 */
    private String waveDest = "COMMON";
    /** 捡货模型模版id */
    private Long pickModelTemplateId;

    public Long getPickModelTemplateId() {
        return pickModelTemplateId;
    }

    public void setPickModelTemplateId(Long pickModelTemplateId) {
        this.pickModelTemplateId = pickModelTemplateId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getWaveDest() {
        return waveDest;
    }

    public void setWaveDest(String waveDest) {
        this.waveDest = waveDest;
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

    private List<Long> orderIds = null;

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public WaveRequest(List<Long> orderIds, Long pickModelTemplateId, Long status, String waveDest, String waveName, String waveSource, Long waveTemplateId, Long waveType) {
        this.orderIds = orderIds;
        this.pickModelTemplateId = pickModelTemplateId;
        this.status = status;
        this.waveDest = waveDest;
        this.waveName = waveName;
        this.waveSource = waveSource;
        this.waveTemplateId = waveTemplateId;
        this.waveType = waveType;
    }

    public WaveRequest() {
    }
}
