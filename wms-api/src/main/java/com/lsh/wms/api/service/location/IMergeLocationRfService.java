package com.lsh.wms.api.service.location;

import com.lsh.base.common.exception.BizCheckedException;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2017/2/16 下午7:34
 */
public interface IMergeLocationRfService {
    public String mergeBins()throws BizCheckedException;
    public String splitBins()throws BizCheckedException;
    public String checkBin()throws BizCheckedException;
}
