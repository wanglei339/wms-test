package com.lsh.wms.rpc.service.kanban;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsh.wms.api.service.kanban.IKanBanRpcService;
import com.lsh.wms.core.service.kanban.KanBanService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/26.
 */
@Service(protocol = "dubbo")
public class KanBanRpcService implements IKanBanRpcService {

    @Autowired
    private KanBanService kanBanService;

    public List<Map<String, Object>> getKanbanCount(Long type) {
        return kanBanService.getKanBanCount(type);
    }

    public List<Map<String, Object>> getPoKanbanCount(Long type) {
        return kanBanService.getPoKanBanCount(type);
    }

    public List<Map<String, Object>> getPoDetailKanBanCount(Long orderType) {
        return kanBanService.getPoDetailKanBanCount(orderType);
    }
}
