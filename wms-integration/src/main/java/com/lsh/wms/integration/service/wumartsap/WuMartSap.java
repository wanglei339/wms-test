package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.core.service.system.SysMsgService;
import com.lsh.wms.integration.wumart.ibd.*;
import com.lsh.wms.integration.wumart.ibdaccount.*;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFPROTT;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFVBPOK;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFZDELIVERYEXPORT;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFZDELIVERYIMPORT;
import com.lsh.wms.integration.wumart.ibdaccount.ZDELIVERYIMPORT;
import com.lsh.wms.integration.wumart.obd.*;
import com.lsh.wms.integration.wumart.obdaccount.*;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.system.SysMsg;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysMsgService sysMsgService;

    public String ibd2Sap(CreateIbdHeader createIbdHeader){

//        Calendar calendar = Calendar.getInstance();
//        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
//        date.setYear(calendar.get(Calendar.YEAR));
//        //date.setDay(calendar.get(Calendar.DATE));
//        date.setDay(5);
//        date.setMonth(calendar.get(Calendar.MONTH)+1);
        List<CreateIbdDetail> details = createIbdHeader.getItems();

        com.lsh.wms.integration.wumart.ibd.ObjectFactory factory = new com.lsh.wms.integration.wumart.ibd.ObjectFactory();
        //ibdHeader
        BbpInbdL header = factory.createBbpInbdL();
        //header.setDelivDate(date);

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
        logger.info("传出参数:header :" + JSON.toJSONString(header) + " hItem: " + JSON.toJSONString(hItem) + "  _return + "+ JSON.toJSONString(_return) + " efDelivery: "+JSON.toJSONString(efDelivery));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn.getItem());
        // TODO: 2016/11/1 结果记录到日志表中,将数据保存到redis中。以便失败之后重新下传。
        logger.info("~~~~~~~~~~~~~~~~~~~~~ref:" + ref + "~~~~~~~~~~~~~~~~~~~~~~");

//        //存入sys_log
//        //Long sysId = RandomUtils.genId();
//        SysLog sysLog = new SysLog();
//        //sysLog.setLogId(sysId);
//        //记录返回日志
//        sysLog.setLogMessage(newReturn.getItem().get(0).getMessage());
//        sysLog.setTargetSystem(SysLogConstant.LOG_TARGET_WUMART);
//        sysLog.setLogType(SysLogConstant.LOG_TYPE_DIRECT_IBD);
//        //sysLog.setLogCode(newReturn.getItem().get(0).getCode());
//        sysLog.setLogCode(1111l);
//        Long sysId = sysLogService.insertSysLog(sysLog);
//
//        //将返回结果存入缓存,发生错误可以重新下传。
//        SysMsg sysMsg = new SysMsg();
//        sysMsg.setTargetSystem(SysLogConstant.LOG_TARGET_WUMART);
//        sysMsg.setId(sysId);
//        sysMsg.setType(SysLogConstant.LOG_TYPE_DIRECT_IBD);
//
//        sysMsg.setMsgBody(JSON.toJSONString(createIbdHeader));
//        sysMsgService.sendMessage(sysMsg);

        return ref;
    }

    public String obd2Sap(CreateObdHeader createObdHeader){
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        date.setMonth(calendar.get(Calendar.MONTH));
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
                    " dueDate : " + JSON.toJSONString(date) +
                    " _return : " + JSON.toJSONString(_return) +
                    " stockTransItems :"+JSON.toJSONString(stockTransItems));
        TableOfBapiret2 newReturn = zbinding.zBapiOutbCreateObd(createdItems,debugFlg,deliveries,date,extensionIn,extensionOut,noDequeue,_return,serialNumbers,shipPoint,stockTransItems,delivery,numDeliveries);
        logger.info("入口参数: createdItems :" + JSON.toJSONString(createdItems)+
                " debugFlg : " + JSON.toJSONString(debugFlg)+
                " deliveries : " + JSON.toJSONString(deliveries) +
                " dueDate : " + JSON.toJSONString(date) +
                " _return : " + JSON.toJSONString(_return) +
                " stockTransItems :"+JSON.toJSONString(stockTransItems));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn.getItem());



        return ref;
    }

    public String ibd2SapAccount(CreateIbdHeader createIbdHeader) {
        //当前时间转成XMLGregorianCalendar类型
//        Calendar calendar = Calendar.getInstance();
//        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
//        date.setYear(calendar.get(Calendar.YEAR));
//        //date.setDay(calendar.get(Calendar.DATE));
//        date.setDay(5);
//        date.setMonth(calendar.get(Calendar.MONTH));
        //注册工厂
        com.lsh.wms.integration.wumart.ibdaccount.ObjectFactory factory = new com.lsh.wms.integration.wumart.ibdaccount.ObjectFactory();

        TABLEOFZDELIVERYIMPORT pItems = factory.createTABLEOFZDELIVERYIMPORT();

        List<CreateIbdDetail> details = createIbdHeader.getItems();

        for(CreateIbdDetail detail :details){
            ZDELIVERYIMPORT pItem = factory.createZDELIVERYIMPORT();
            pItem.setVBELN(detail.getPoNumber());
            pItem.setPOSNR(detail.getPoItme());
            pItem.setLFIMG(detail.getDeliveQty());
            pItem.setPIKMG(detail.getDeliveQty());
            //pItem.setWADATIST(date);
            // TODO: 2016/11/6  在库OOO1直流0005
            //pItem.setLGORT();
            pItem.setLGORT("0001");
            pItem.setWERKS("DC09");
            pItem.setVRKME(detail.getUnit());
            pItems.getItem().add(pItem);
        }
        //组装参数
        Holder<TABLEOFBAPIIBDLVITEMCTRLCHG> itemCONTROL = new Holder<TABLEOFBAPIIBDLVITEMCTRLCHG>();
        Holder<TABLEOFBAPIIBDLVITEMCHG> itemDATA = new Holder<TABLEOFBAPIIBDLVITEMCHG>();
        Holder<TABLEOFPROTT> prot = new Holder<TABLEOFPROTT>();
        Holder<TABLEOFZDELIVERYEXPORT> pZEXPORT = new Holder<TABLEOFZDELIVERYEXPORT>();
        Holder<TABLEOFZDELIVERYIMPORT> pZIMPORT = new Holder<TABLEOFZDELIVERYIMPORT>(pItems);
        TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();
        Holder<TABLEOFBAPIRET2> return1 = new Holder<TABLEOFBAPIRET2>();
        Holder<TABLEOFVBPOK> vbpokTAB = new Holder<TABLEOFVBPOK>();

        com.lsh.wms.integration.wumart.ibdaccount.ZDELIVERYINBOUNDUPDATE zbinding = new com.lsh.wms.integration.wumart.ibdaccount.Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        logger.info("传入参数: pZIMPORT : " + JSON.toJSONString(pZIMPORT)+"~~~~~~~~~~~~~~");
        TABLEOFBAPIRET2 newReturn = zbinding.zDELIVERYINBOUNDUPDATE(itemCONTROL,itemDATA,prot,pZEXPORT,pZIMPORT,_return,return1,vbpokTAB);
        logger.info("参数 : pZIMPORT : " + JSON.toJSONString(pZIMPORT)+"~~~~~~~~~~~~~~");

        logger.info("返回值 : newReturn : " + JSON.toJSONString(newReturn.getItem()));

        return JSON.toJSONString(newReturn.getItem());
    }

    public String obd2SapAccount(CreateObdHeader createObdHeader) {
        //当前时间转成XMLGregorianCalendar类型
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        date.setMonth(calendar.get(Calendar.MONTH));

        com.lsh.wms.integration.wumart.obdaccount.ObjectFactory factory = new com.lsh.wms.integration.wumart.obdaccount.ObjectFactory();
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT zItmes = factory.createTABLEOFZDELIVERYIMPORT();

        List<CreateObdDetail> details = createObdHeader.getItems();
        for(CreateObdDetail detail : details){
            com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYIMPORT zItem = factory.createZDELIVERYIMPORT();
            zItem.setVBELN(detail.getRefDoc());
            zItem.setPOSNR(detail.getRefItem());
            zItem.setLFIMG(detail.getDlvQty());
            zItem.setPIKMG(detail.getDlvQty());
            zItem.setWADATIST(date);
            zItem.setLGORT("0001");
            zItem.setWERKS("DC09");
            zItem.setVRKME(detail.getSalesUnit());
        }
        //组装参数
        Holder<TABLEOFBAPIOBDLVITEMCTRLCHG> itemCONTROL = new Holder<TABLEOFBAPIOBDLVITEMCTRLCHG>();
        Holder<TABLEOFBAPIOBDLVITEMCHG> itemDATA = new Holder<TABLEOFBAPIOBDLVITEMCHG>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFPROTT> prot = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFPROTT>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYEXPORT> pZEXPORT = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYEXPORT>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT> pZIMPORT = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT>(zItmes);
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2> return1 = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFVBPOK> vbpokTAB = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFVBPOK>();

        com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYOUTBOUNDUPDATE zbinding = new ZDELIVERYOUTBOUNDUPDATE_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2 newReturn = zbinding.zDELIVERYOUTBOUNDUPDATE(itemCONTROL,itemDATA,prot,pZEXPORT,pZIMPORT,_return,return1,vbpokTAB);

        logger.info("返回值 : newReturn : " + JSON.toJSONString(newReturn.getItem()));
        return JSON.toJSONString(newReturn.getItem());
    }

    protected void auth(BindingProvider provider) {
        Map<String, Object> context = provider.getRequestContext();

        logger.info("~~~~~~~~~~~~~~~~~~~ username :"  + PropertyUtils.getString("wumart.sap.username") + "~~~~~~~~~~~~~~~ password :" + PropertyUtils.getString("wumart.sap.password"));
        context.put(BindingProvider.USERNAME_PROPERTY, PropertyUtils.getString("wumart.sap.username"));
        context.put(BindingProvider.PASSWORD_PROPERTY, PropertyUtils.getString("wumart.sap.password"));
    }

}
