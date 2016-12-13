package com.lsh.wms.model.taking;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhao on 16/7/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailRequest implements Serializable {
    /** 详情id*/
    private Long detailId;
    /** 商品id*/
    private Long itemId;
    /** 生产日期*/
    private Long proTime;
    /** */
}
