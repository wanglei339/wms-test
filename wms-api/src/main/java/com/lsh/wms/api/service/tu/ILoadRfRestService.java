package com.lsh.wms.api.service.tu;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午7:43
 */
public interface ILoadRfRestService {

    /**
     * 不同装车状态的tuList
     * @return
     * @throws BizCheckedException
     */
    public String getTuHeadListByLoadStatus()throws BizCheckedException;

//    /**
//     * rf获取tu单明细
//     * @return
//     * @throws BizCheckedException
//     */
//    public String getTuHead()throws BizCheckedException;

    /**
     * 下一步,tuhead的状态变更,status变为发车中。。。
     * @return
     * @throws BizCheckedException
     */
    public String changeLoadStatus()throws BizCheckedException;

    /**
     * 板子装车
     * @return
     * @throws BizCheckedException
     */
    public String loadBoard() throws BizCheckedException;

    /**
     * 确认装车完成
     * @return
     * @throws BizCheckedException
     */
    public String confirmLoad() throws BizCheckedException;

    /**
     * 继续装车
     * @return
     * @throws BizCheckedException
     */
    public String reloadByTuId() throws BizCheckedException;



}
