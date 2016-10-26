package com.lsh.wms.service.tu;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.wms.api.service.tu.ITuRpcService;
import com.lsh.wms.core.constant.TuConstant;
import com.lsh.wms.core.service.tu.TuService;
import com.lsh.wms.model.tu.TuDetail;
import com.lsh.wms.model.tu.TuHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author 马启迪 maqidi@lsh123.com
 * @Date 2016/10/20 下午2:12
 */
@Service(protocol = "dubbo")
public class TuRpcService implements ITuRpcService {

    private static Logger logger = LoggerFactory.getLogger(TuRpcService.class);

    @Autowired
    private TuService tuService;

    public TuHead create(TuHead tuHead) throws BizCheckedException {
        //先查有无,有的话,不能创建
        TuHead preHead = this.getHeadByTuId(tuHead.getTuId());
        if (preHead != null) {
            throw new BizCheckedException("2990020");
        }
        tuService.create(tuHead);
        return tuHead;
    }

    public TuHead update(TuHead tuHead) throws BizCheckedException {
        tuService.update(tuHead);
        return tuHead;
    }

    public TuHead getHeadByTuId(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        return tuHead;
    }

    public List<TuHead> getTuHeadList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuHeadList(mapQuery);
    }

    public Integer countTuHead(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuHead(mapQuery);
    }

    public TuHead removeTuHead(String tuId) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = tuService.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        return tuService.removeTuHead(tuHead);
    }

    public TuDetail create(TuDetail tuDetail) throws BizCheckedException {
        //先查有无,boardId是唯一的key
        TuDetail preDetail = this.getDetailByBoardId(tuDetail.getMergedContainerId());
        if (preDetail != null) {
            throw new BizCheckedException("2990023");
        }
        tuService.create(tuDetail);
        return tuDetail;
    }

    public TuDetail update(TuDetail tuDetail) throws BizCheckedException {
        tuService.update(tuDetail);
        return tuDetail;
    }

    public TuDetail getDetailByBoardId(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        return tuDetail;
    }

    public TuDetail getDetailById(Long id) throws BizCheckedException {
        if (null == id) {
            throw new BizCheckedException("2990025");
        }
        return tuService.getDetailById(id);
    }

    public List<TuDetail> getTuDeailList(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.getTuDeailList(mapQuery);
    }

    public TuDetail removeTuDetail(Long boardId) throws BizCheckedException {
        if (null == boardId) {
            throw new BizCheckedException("2990024");
        }
        TuDetail tuDetail = tuService.getDetailByBoardId(boardId);
        if (null == tuDetail) {
            throw new BizCheckedException("2990026");
        }
        return tuService.removeTuDetail(tuDetail);
    }

    public Integer countTuDetail(Map<String, Object> mapQuery) throws BizCheckedException {
        return tuService.countTuDetail(mapQuery);
    }

    public List<TuDetail> getTuDetailByStoreCode(String tuId, String deliveryCode) throws BizCheckedException {
        if (null == tuId || null == deliveryCode) {
            throw new BizCheckedException("2990027");
        }
        return tuService.getTuDetailByStoreCode(tuId, deliveryCode);
    }

    public TuHead changeTuHeadStatus(String tuId, Integer status) throws BizCheckedException {
        if (null == tuId || tuId.equals("")) {
            throw new BizCheckedException("2990021");
        }
        TuHead tuHead = this.getHeadByTuId(tuId);
        if (null == tuHead) {
            throw new BizCheckedException("2990022");
        }
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }

    public TuHead changeTuHeadStatus(TuHead tuHead, Integer status) throws BizCheckedException {
        tuHead.setStatus(status);
        this.update(tuHead);
        return tuHead;
    }


}
