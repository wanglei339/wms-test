package com.lsh.wms.rf.service.outbound;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.wms.api.service.csi.ICsiRpcService;
import com.lsh.wms.api.service.item.IItemRpcService;
import com.lsh.wms.api.service.pick.IRFQCRestService;
import com.lsh.wms.api.service.request.RequestUtils;
import com.lsh.wms.core.constant.CsiConstan;
import com.lsh.wms.core.service.csi.CsiSkuService;
import com.lsh.wms.core.service.wave.WaveService;
import com.lsh.wms.model.baseinfo.BaseinfoItem;
import com.lsh.wms.model.csi.CsiSku;
import com.lsh.wms.model.wave.WaveDetail;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengwenjun on 16/7/30.
 */

@Service(protocol = "rest")
@Path("outbound/qc")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class QCRestService implements IRFQCRestService{
    @Reference
    private ICsiRpcService csiRpcService;
    @Autowired
    private WaveService waveService;
    @Reference
    private IItemRpcService itemRpcService;


    @POST
    @Path("setResult")
    public String setResult() throws BizCheckedException{
        Map<String,Object> request = RequestUtils.getRequest();
        long containerId = (Long) request.get("containerId");
        String code = (String) request.get("code");
        CsiSku skuInfo = csiRpcService.getSkuByCode(CsiConstan.CSI_CODE_TYPE_BARCODE, code);
        long skuId = skuInfo.getSkuId();
        BigDecimal qty = new BigDecimal((String)request.get("qty"));
        long exceptionType = (Long) request.get("exceptionType");
        //获取当前的有效待QC container 任务列表
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        //遍历
        int seekNum = 0;
        WaveDetail detail = null;
        for(WaveDetail d : details){
            if(d.getSkuId()!=skuId){
                continue;
            }
            seekNum++;
            detail = d;
        }
        if(seekNum == 0){
            throw new BizCheckedException("");
        }
        if(seekNum > 1){
            throw new BizCheckedException("");
        }
        detail.setQcQty(qty);
        waveService.updateDetail(detail);
        return JsonUtils.SUCCESS();
    }

    @GET
    @Path("getUndoDetails")
    public String getUndoDetails(@QueryParam("containerId") long containerId) {
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        List<Map<String, Object>> undoDetails = new LinkedList<Map<String, Object>>();
        for (WaveDetail d : details){
            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("skuId", d.getSkuId());
            BaseinfoItem item = itemRpcService.getItem(d.getOwnerId(), d.getSkuId());
            detail.put("code", item.getCode());
            detail.put("codeType", item.getCodeType());
            detail.put("pickQty", d.getPickQty());
            detail.put("skuName", item.getSkuName());
            undoDetails.add(detail);
        }
        return JsonUtils.SUCCESS(undoDetails);
    }

    public String confirm(long containerId) {
        List<WaveDetail> details = waveService.getDetailsByContainerId(containerId);
        for (WaveDetail d : details){
            //未qc行项目
            //已QC但异常未处理完成行项目
        }
        return JsonUtils.SUCCESS();
    }
}
