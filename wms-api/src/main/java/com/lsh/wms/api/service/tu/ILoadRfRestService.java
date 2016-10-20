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
    public String getTuHeadListByLoadStatus(Integer status)throws BizCheckedException;

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



}
