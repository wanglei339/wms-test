package com.lsh.wms.api.service.kanban;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/26.
 */
public interface IKanBanRpcService {
    List<Map<String, Object>> getKanbanCount(Long type,Long subType);

    List<Map<String, Object>> getPoKanbanCount(Long type);

    List<Map<String, Object>> getPoDetailKanBanCount(Long orderType);

    List<Map<String,Object>> getKanBanCountByStatus(Long type);

    List<Map<String, Object>> getSoKanbanCount(Long type);

    List<Map<String, Object>> getWaveKanBanCount();
}
