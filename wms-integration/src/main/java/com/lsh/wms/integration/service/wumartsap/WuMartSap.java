package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.integration.wumart.ibd.*;
import com.lsh.wms.integration.wumart.ibd.ObjectFactory;
import com.lsh.wms.integration.wumart.obd.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 物美ibd obd
 * Created by lixin-mac on 2016/10/28.
 */
@Service(protocol = "dubbo",async=true)
public class WuMartSap implements IWuMartSap{

    protected final Logger logger = Logger.getLogger(this.getClass());
//
//    @Value("${wumart.sap.username}")
//    private String username;
//
//    @Value("${wumart.sap.password}")
//    private String password;

    public String ibd2Sap(CreateIbdHeader createIbdHeader){
        XMLGregorianCalendar deliveDate = createIbdHeader.getDeliveDate();
        List<CreateIbdDetail> details = createIbdHeader.getItems();

        ObjectFactory factory = new ObjectFactory();
        //ibdHeader
        BbpInbdL header = factory.createBbpInbdL();
        header.setDelivDate(deliveDate);

        //items
        TableOfBbpInbdD items = factory.createTableOfBbpInbdD();
        for (CreateIbdDetail detail : details){
            BbpInbdD item = factory.createBbpInbdD();
            //item.setMaterial(detail.getMaterial());
            item.setUnit(String.valueOf(detail.getUnit()));
            item.setPoNumber(detail.getPoNumber());
            item.setPoItem(detail.getPoItme());
            item.setDelivQty(detail.getDeliveQty());
            items.getItem().add(item);
        }

        ZMMINBIBD zbinding = new ZMMINBIBD_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        TableOfBapireturn _return = factory.createTableOfBapireturn();
        Holder<String> efDelivery  = new Holder<String>();
        Holder<TableOfBbpInbdD> hItem = new Holder<TableOfBbpInbdD>(items);

        logger.info("传入参数:header :" + JSON.toJSONString(header) + " hItem: " + JSON.toJSONString(hItem) + "  _return + "+ JSON.toJSONString(_return) + " efDelivery: "+JSON.toJSONString(efDelivery));
        TableOfBapireturn newReturn = zbinding.zbapiBbpInbIbd(header,hItem,_return,efDelivery);
        logger.info("传入参数:header :" + JSON.toJSONString(header) + " hItem: " + JSON.toJSONString(hItem) + "  _return + "+ JSON.toJSONString(_return) + " efDelivery: "+JSON.toJSONString(efDelivery));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn);
        return ref;
    }

    public String obd2Sap(CreateObdHeader createObdHeader){
        XMLGregorianCalendar dueDate = createObdHeader.getDueDate();
        List<CreateObdDetail> details = createObdHeader.getItems();

        com.lsh.wms.integration.wumart.obd.ObjectFactory factory = new com.lsh.wms.integration.wumart.obd.ObjectFactory();


        //STOCK_TRANS_ITEMS
        TableOfBapidlvreftosto stItems = factory.createTableOfBapidlvreftosto();

        //CREATED_ITEMS
        TableOfBapidlvitemcreated cItems = factory.createTableOfBapidlvitemcreated();

        for(CreateObdDetail detail : details){
            Bapidlvreftosto bItem = factory.createBapidlvreftosto();
            bItem.setRefDoc(detail.getRefDoc());
            bItem.setRefItem(detail.getRefItem());
            bItem.setDlvQty(detail.getDlvQty());
            bItem.setSalesUnit(String.valueOf(detail.getSalesUnit()));
            stItems.getItem().add(bItem);

            Bapidlvitemcreated cItem = factory.createBapidlvitemcreated();
            cItem.setSalesUnit(String.valueOf(detail.getSalesUnit()));
            cItem.setDlvQty(detail.getDlvQty());
            cItem.setRefItem(detail.getRefItem());
            cItem.setRefDoc(detail.getRefDoc());
            cItem.setMaterial(detail.getMaterial());
            cItems.getItem().add(cItem);
        }

        //组装参数

        Holder<TableOfBapidlvitemcreated> createdItems = new Holder<TableOfBapidlvitemcreated>(cItems);
        String debugFlg = "";
        Holder<TableOfBapishpdelivnumb> deliveries = new Holder<TableOfBapishpdelivnumb>();

        Holder<TableOfBapiparex> extensionIn = new Holder<TableOfBapiparex>();
        Holder<TableOfBapiparex> extensionOut = new Holder<TableOfBapiparex>();
        String noDequeue = "";
        TableOfBapiret2 _return = factory.createTableOfBapiret2();
        Holder<TableOfBapidlvserialnumber> serialNumbers = new Holder<TableOfBapidlvserialnumber>();
        String shipPoint = "";
        Holder<TableOfBapidlvreftosto> stockTransItems = new Holder<TableOfBapidlvreftosto>(stItems);
        Holder<String> delivery = new Holder<String>();
        Holder<String> numDeliveries = new Holder<String>();
        ZMMOUTBOBD zbinding = new ZMMOUTBOBD_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);

        logger.info("入口参数: createdItems :" + JSON.toJSONString(createdItems)+
                    " debugFlg : " + JSON.toJSONString(debugFlg)+
                    " deliveries : " + JSON.toJSONString(deliveries) +
                    " dueDate : " + JSON.toJSONString(dueDate) +
                    " _return : " + JSON.toJSONString(_return) +
                    " stockTransItems :"+JSON.toJSONString(stockTransItems));
        TableOfBapiret2 newReturn = zbinding.zBapiOutbCreateObd(createdItems,debugFlg,deliveries,dueDate,extensionIn,extensionOut,noDequeue,_return,serialNumbers,shipPoint,stockTransItems,delivery,numDeliveries);
        logger.info("入口参数: createdItems :" + JSON.toJSONString(createdItems)+
                " debugFlg : " + JSON.toJSONString(debugFlg)+
                " deliveries : " + JSON.toJSONString(deliveries) +
                " dueDate : " + JSON.toJSONString(dueDate) +
                " _return : " + JSON.toJSONString(_return) +
                " stockTransItems :"+JSON.toJSONString(stockTransItems));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn);
        return ref;
    }

    protected void auth(BindingProvider provider) {
        Map<String, Object> context = provider.getRequestContext();
        context.put(BindingProvider.USERNAME_PROPERTY, PropertyUtils.getString("wumart.sap.username"));
        context.put(BindingProvider.PASSWORD_PROPERTY, PropertyUtils.getString("wumart.sap.password"));
    }

}
