package com.lsh.wms.api.service.kanban;

import java.util.Map;

/**
 * Created by lixin-mac on 16/8/26.
 */
public interface IKanBanRestService {

    String getKanbanCount(Long type,Long subType);

    String getPoKanbanCount(Long type);
    String getPoDetailKanBanCount(Long orderType);

    String getKanBanCountByStatus(Long type);

    String getSoKanbanCount(Long type);

    String getWaveKanBanCount();
}
