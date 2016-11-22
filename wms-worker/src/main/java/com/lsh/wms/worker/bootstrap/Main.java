package com.lsh.wms.worker.bootstrap;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.core.service.location.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengkun on 2016/11/18.
 */

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Map<String, Object> mapQuery = RequestUtils.getRequest(); // 参数,暂时先建立满树
        // 判断表中是否为空,必须为空表时才能构建
        Map<String, Object> params = new HashMap<String, Object>();
        LocationService locationService = new LocationService();
        Integer count = locationService.countLocation(params);
        if (count > 0) {
            logger.info("123321", "库位表不为空,不能进行初始化构建");
        }
        Map<String, Object> config = JsonUtils.json2Obj("{\"type\":1,\"containerVol\":999999999,\"locationCode\":\"DC40\",\"regionNo\":0,\"passageNo\":0,\"shelfLevelNo\":0,\"binPositionNo\":0,\"children\":[{\"type\":2,\"containerVol\":999999999,\"locationCode\":\"DC40-Q1\",\"regionNo\":1,\"children\":[{\"type\":4,\"containerVol\":999999999,\"locationCode\":\"PKPY\"},{\"type\":5,\"containerVol\":999999999,\"locationCode\":\"A1\",\"isPassage\":true,\"children\":[{\"levels\":[{\"type\":3,\"containerVol\":0,\"locationCode\":\"-P%d\",\"canStore\":0,\"counts\":8,\"children\":[{\"type\":13,\"containerVol\":0,\"locationCode\":\"-%03d\",\"canStore\":0,\"counts\":16,\"children\":[{\"type\":27,\"containerVol\":0,\"locationCode\":\"-%03d\",\"canStore\":0,\"isLevel\":true,\"counts\":1,\"children\":[{\"type\":36,\"containerVol\":0,\"locationCode\":\"-%03d\",\"canStore\":0,\"isLevel\":true,\"counts\":10,\"children\":[{\"type\":16,\"containerVol\":999999999,\"locationCode\":\"-%03d\",\"counts\":10},{\"type\":27,\"containerVol\":0,\"locationCode\":\"-%03d\",\"canStore\":0,\"isLevel\":true,\"counts\":5}]}]}]}]}]}]}]}]}", Map.class);
        //Map<String, Object> config = JsonUtils.json2Obj(mapQuery.get("config").toString(), Map.class);
        locationService.initLocationTree(config, -1L);
    }
}
