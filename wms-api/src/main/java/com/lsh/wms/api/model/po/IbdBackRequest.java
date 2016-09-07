package com.lsh.wms.api.model.po;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixin-mac on 16/9/6.
 */
public class IbdBackRequest implements Serializable {
    //header信息
    private Header header;

    //detail信息
    private List<IbdItem> items;

    public IbdBackRequest(){}

    public IbdBackRequest(List<IbdItem> items, Header header) {
        this.items = items;
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<IbdItem> getItems() {
        return items;
    }

    public void setItems(List<IbdItem> items) {
        this.items = items;
    }

}
