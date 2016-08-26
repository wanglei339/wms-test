package com.lsh.wms.api.service.kanban;

import java.util.List;
import java.util.Map;

/**
 * Created by lixin-mac on 16/8/26.
 */
public interface IKanBanRpcService {
    List<Map<String, Object>> getKanbanCount(Long type);
}
