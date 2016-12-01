package com.lsh.wms.integration.service.wumartsap;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.wms.api.model.wumart.CreateIbdDetail;
import com.lsh.wms.api.model.wumart.CreateIbdHeader;
import com.lsh.wms.api.model.wumart.CreateObdDetail;
import com.lsh.wms.api.model.wumart.CreateObdHeader;
import com.lsh.wms.api.service.wumart.IWuMartSap;
import com.lsh.wms.core.constant.PoConstant;
import com.lsh.wms.core.constant.SoConstant;
import com.lsh.wms.core.constant.SysLogConstant;
import com.lsh.wms.core.service.po.ReceiveService;
import com.lsh.wms.core.service.system.SysLogService;
import com.lsh.wms.core.service.system.SysMsgService;
import com.lsh.wms.integration.wumart.ibd.*;
import com.lsh.wms.integration.wumart.ibdaccount.*;
import com.lsh.wms.integration.wumart.ibdaccount.BAPIRET2;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFPROTT;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFVBPOK;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFZDELIVERYEXPORT;
import com.lsh.wms.integration.wumart.ibdaccount.TABLEOFZDELIVERYIMPORT;
import com.lsh.wms.integration.wumart.ibdaccount.ZDELIVERYEXPORT;
import com.lsh.wms.integration.wumart.ibdaccount.ZDELIVERYIMPORT;
import com.lsh.wms.integration.wumart.ibdback.*;
import com.lsh.wms.integration.wumart.ibdback.ObjectFactory;
import com.lsh.wms.integration.wumart.obd.*;
import com.lsh.wms.integration.wumart.obdaccount.*;
import com.lsh.wms.integration.wumart.soobd.*;
import com.lsh.wms.model.po.ReceiveDetail;
import com.lsh.wms.model.system.SysLog;
import com.lsh.wms.model.system.SysMsg;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.math.BigDecimal;
import java.util.*;

/**
 * 物美ibd obd
 * Created by lixin-mac on 2016/10/28.
 */
@Service(protocol = "dubbo")
public class WuMartSap implements IWuMartSap{

    protected final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysMsgService sysMsgService;

    @Autowired
    private ReceiveService receiveService;



    public CreateIbdHeader ibd2Sap(CreateIbdHeader createIbdHeader){

        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        //date.setDay(5);
        date.setMonth(calendar.get(Calendar.MONTH)+1);
        List<CreateIbdDetail> details = createIbdHeader.getItems();

        com.lsh.wms.integration.wumart.ibd.ObjectFactory factory = new com.lsh.wms.integration.wumart.ibd.ObjectFactory();
        //ibdHeader
        BbpInbdL header = factory.createBbpInbdL();
        header.setDelivDate(date);

        String warehouseCode =  createIbdHeader.getWarehouseCode();
        Long receiveId = 0l;
        if(PropertyUtils.getString("wumart.werks").equals(warehouseCode)){
            receiveId = Long.valueOf(details.get(0).getVendMat());
        }

        //items
        TableOfBbpInbdD items = factory.createTableOfBbpInbdD();
        Integer orderType = 0;
        for (CreateIbdDetail detail : details){
            BbpInbdD item = factory.createBbpInbdD();
            //item.setMaterial(detail.getMaterial());
            item.setUnit(String.valueOf(detail.getUnit()));
            item.setPoNumber(detail.getPoNumber());
            item.setPoItem(detail.getPoItme());
            item.setDelivQty(detail.getDeliveQty());
            item.setVendMat(detail.getVendMat());
            items.getItem().add(item);
            orderType = detail.getOrderType();
        }

        ZMMINBIBD zbinding = new ZMMINBIBD_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        TableOfBapireturn _return = factory.createTableOfBapireturn();
        Holder<String> efDelivery  = new Holder<String>();
        Holder<TableOfBbpInbdD> hItem = new Holder<TableOfBbpInbdD>(items);
        logger.info("ibd创建传入参数:header :" + JSON.toJSONString(header) + " hItem: " + JSON.toJSONString(hItem) + "  _return + "+ JSON.toJSONString(_return) + " efDelivery: "+JSON.toJSONString(efDelivery));
        TableOfBapireturn newReturn = zbinding.zbapiBbpInbIbd(header,hItem,_return,efDelivery);
        logger.info("ibd创建传出参数:header :" + JSON.toJSONString(header) + " hItem: " + JSON.toJSONString(hItem) + "  _return + "+ JSON.toJSONString(_return) + " efDelivery: "+JSON.toJSONString(efDelivery));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn.getItem());
        logger.info("~~~~~~~~~~~~~~~~~~~~~ibd创建返回值ref:" + ref + "~~~~~~~~~~~~~~~~~~~~~~");

        if(newReturn.getItem() == null || newReturn.getItem().size() <= 0){
            return null;

        }

        CreateIbdHeader backDate = new CreateIbdHeader();
        List<CreateIbdDetail> backDetails = new ArrayList<CreateIbdDetail>();

        for(Bapireturn bapireturn1 : newReturn.getItem()){
            if(bapireturn1.getType().equals("E")){
                return null;
            }
            if("03".equals(bapireturn1.getCode())){
                CreateIbdDetail backDetail = new CreateIbdDetail();
                backDetail.setOrderType(orderType);
                backDetail.setDeliveQty(new BigDecimal(bapireturn1.getMessage().trim()).setScale(2,BigDecimal.ROUND_HALF_UP));
                backDetail.setPoNumber(bapireturn1.getMessageV1());
                backDetail.setPoItme(bapireturn1.getMessageV2());
                backDetail.setVendMat(String.valueOf(receiveId));
                backDetails.add(backDetail);
            }
        }
        backDate.setItems(backDetails);
        backDate.setWarehouseCode(createIbdHeader.getWarehouseCode());
        return backDate;
    }

    public CreateObdHeader obd2Sap(CreateObdHeader createObdHeader){
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        date.setMonth(calendar.get(Calendar.MONTH) + 1);
        List<CreateObdDetail> details = createObdHeader.getItems();
        com.lsh.wms.integration.wumart.obd.ObjectFactory factory = new com.lsh.wms.integration.wumart.obd.ObjectFactory();
        //STOCK_TRANS_ITEMS
        TableOfBapidlvreftosto stItems = factory.createTableOfBapidlvreftosto();
        //CREATED_ITEMS
        TableOfBapidlvitemcreated cItems = factory.createTableOfBapidlvitemcreated();
        Integer orderType = 0;
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
            orderType = detail.getOrderType();
        }
        //组装参数
        Holder<TableOfBapidlvitemcreated> createdItems = new Holder<TableOfBapidlvitemcreated>(cItems);
        String debugFlg = "";
        Holder<TableOfBapishpdelivnumb> deliveries = new Holder<TableOfBapishpdelivnumb>();
        Holder<TableOfBapiparex> extensionIn = new Holder<TableOfBapiparex>();
        Holder<TableOfBapiparex> extensionOut = new Holder<TableOfBapiparex>();
        String noDequeue = "";
        TableOfBapiret2 _return = factory.createTableOfBapiret2();
        Bapiret2 inBapiret2 = factory.createBapiret2();
        inBapiret2.setMessage(createObdHeader.getTuId());
        _return.getItem().add(inBapiret2);

        Holder<TableOfBapidlvserialnumber> serialNumbers = new Holder<TableOfBapidlvserialnumber>();
        String shipPoint = "";
        logger.info("");
        if(SoConstant.ORDER_TYPE_DIRECT == orderType){
            shipPoint = PropertyUtils.getString("shipPoint");
        }
        Holder<TableOfBapidlvreftosto> stockTransItems = new Holder<TableOfBapidlvreftosto>(stItems);
        Holder<String> delivery = new Holder<String>();
        Holder<String> numDeliveries = new Holder<String>();
        ZMMOUTBOBD zbinding = new ZMMOUTBOBD_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        logger.info("obd创建入口参数: createdItems :" + JSON.toJSONString(createdItems)+
                    " debugFlg : " + JSON.toJSONString(debugFlg)+
                    " deliveries : " + JSON.toJSONString(deliveries) +
                    " dueDate : " + JSON.toJSONString(date) +
                    " _return : " + JSON.toJSONString(_return) +
                    " stockTransItems :"+JSON.toJSONString(stockTransItems) +
                    " shipPoint : " + shipPoint);
        TableOfBapiret2 newReturn = zbinding.zBapiOutbCreateObd(createdItems,debugFlg,deliveries,date,extensionIn,extensionOut,noDequeue,_return,serialNumbers,shipPoint,stockTransItems,delivery,numDeliveries);
        logger.info("obd创建传出参数: createdItems :" + JSON.toJSONString(createdItems)+
                " debugFlg : " + JSON.toJSONString(debugFlg)+
                " deliveries : " + JSON.toJSONString(deliveries) +
                " dueDate : " + JSON.toJSONString(date) +
                " _return : " + JSON.toJSONString(_return) +
                " stockTransItems :"+JSON.toJSONString(stockTransItems));
        String ref = com.alibaba.fastjson.JSON.toJSONString(newReturn.getItem());
        logger.info("obd创建传出参数: createdItems :" + JSON.toJSONString(createdItems));
        logger.info("obd创建返回值 ref : " + ref);
        //循环返回值
        CreateObdHeader backDate = new CreateObdHeader();
        List<CreateObdDetail> list = new ArrayList<CreateObdDetail>();

        // TODO: 2016/11/21 依据返回值判断是否创建成功


        //Map<String,Object> map = new HashMap<String, Object>();

        if(newReturn.getItem() == null && newReturn.getItem().size() <=0){
            return null;
        }else{
            for(Bapiret2 bapiret2 : newReturn.getItem()){
                String type = bapiret2.getType();
                if("E".equals(type)){
                    return null;
                }
            }
            //组装返回的数据
            for(Bapidlvitemcreated item : createdItems.value.getItem()){
                CreateObdDetail backDetail = new CreateObdDetail();
                backDetail.setSalesUnit(item.getSalesUnit());
                backDetail.setRefItem(item.getDelivItem());
                backDetail.setRefDoc(item.getDelivNumb());
                backDetail.setMaterial(item.getMaterial());
                backDetail.setDlvQty(item.getDlvQty());
                list.add(backDetail);
            }
            backDate.setItems(list);
            backDate.setDeliveryId(createObdHeader.getDeliveryId());
        }
        return backDate;
    }

    public Map ibd2SapAccount(CreateIbdHeader createIbdHeader) {
        //当前时间转成XMLGregorianCalendar类型
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        //date.setDay(5);
        date.setMonth(calendar.get(Calendar.MONTH)+1);
        //注册工厂
        com.lsh.wms.integration.wumart.ibdaccount.ObjectFactory factory = new com.lsh.wms.integration.wumart.ibdaccount.ObjectFactory();
        TABLEOFZDELIVERYIMPORT pItems = factory.createTABLEOFZDELIVERYIMPORT();
        List<CreateIbdDetail> details = createIbdHeader.getItems();
        Long receiveId = 0l;
        if(PropertyUtils.getString("wumart.werks").equals(createIbdHeader.getWarehouseCode())){
            receiveId = Long.valueOf(details.get(0).getVendMat());
        }
        Integer orderType = 0;
        for(CreateIbdDetail detail :details){
            ZDELIVERYIMPORT pItem = factory.createZDELIVERYIMPORT();
            pItem.setVBELN(detail.getPoNumber());
            //pItem.setVBELN(ibdId);
            pItem.setPOSNR(detail.getPoItme());
            pItem.setLFIMG(detail.getDeliveQty());
            pItem.setPIKMG(detail.getDeliveQty());
            pItem.setWADATIST(date);
            if(detail.getOrderType() == PoConstant.ORDER_TYPE_CPO){
                pItem.setLGORT("0005");
            }else{
                pItem.setLGORT("0001");
            }
            pItem.setWERKS(createIbdHeader.getWarehouseCode());
            pItem.setVRKME(detail.getUnit());
            pItems.getItem().add(pItem);
            //receiveId = Long.valueOf(detail.getVendMat());
            orderType = detail.getOrderType();
        }
        TABLEOFZDELIVERYEXPORT eItems = factory.createTABLEOFZDELIVERYEXPORT();
        ZDELIVERYEXPORT eItem = factory.createZDELIVERYEXPORT();
        eItem.setVBELN("180011153");
        eItems.getItem().add(eItem);
        //组装参数
        Holder<TABLEOFBAPIIBDLVITEMCTRLCHG> itemCONTROL = new Holder<TABLEOFBAPIIBDLVITEMCTRLCHG>();
        Holder<TABLEOFBAPIIBDLVITEMCHG> itemDATA = new Holder<TABLEOFBAPIIBDLVITEMCHG>();
        Holder<TABLEOFPROTT> prot = new Holder<TABLEOFPROTT>();
        Holder<TABLEOFZDELIVERYEXPORT> pZEXPORT = new Holder<TABLEOFZDELIVERYEXPORT>(eItems);
        Holder<TABLEOFZDELIVERYIMPORT> pZIMPORT = new Holder<TABLEOFZDELIVERYIMPORT>(pItems);
        com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();
        Holder<com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2> return1 = new Holder<com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2>();
        Holder<TABLEOFVBPOK> vbpokTAB = new Holder<TABLEOFVBPOK>();

        com.lsh.wms.integration.wumart.ibdaccount.ZDELIVERYINBOUNDUPDATE zbinding = new com.lsh.wms.integration.wumart.ibdaccount.Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        logger.info("ibd过账传入参数: pZIMPORT : " + JSON.toJSONString(pZIMPORT)+"~~~~~~~~~~~~~~");
        com.lsh.wms.integration.wumart.ibdaccount.TABLEOFBAPIRET2 newReturn = zbinding.zDELIVERYINBOUNDUPDATE(itemCONTROL,itemDATA,prot,pZEXPORT,pZIMPORT,_return,return1,vbpokTAB);
        logger.info("参数 : pZIMPORT : " + JSON.toJSONString(pZIMPORT)
                + " itemCONTROL : "+itemCONTROL
                + "itemDATA : " + itemDATA
                + "prot : " + prot
                + " pZEXPORT : " + JSON.toJSONString(pZEXPORT)
                + " pZIMPORT : " + JSON.toJSONString(pZIMPORT)
                + " _return  : " +JSON.toJSONString(_return)
                + " return1 : " +JSON.toJSONString(return1)
                + " vbpokTAB" + JSON.toJSONString(vbpokTAB));
        logger.info("ibd过账返回值 : newReturn : " + JSON.toJSONString(newReturn.getItem()));
        // TODO: 2016/11/10 将返回的数据对应到相应的验收单中。
        Map<String,Object> map = new HashMap<String, Object>();


        //E表示返回值为空 S表示成功 P表示有返回值。
        if(newReturn == null){
            map.put("type","E");
            return map;
        }
        StringBuffer sb = new StringBuffer();
        for(BAPIRET2 bapiret2 : newReturn.getItem()){
            if("E".equals(bapiret2.getTYPE())){
                //将错误记录下来
                sb.append(bapiret2.getMESSAGEV2()).append(";");

            }

            logger.info("~~~~ibd过账1111 id :"+bapiret2.getID() + "   DC :" + createIbdHeader.getWarehouseCode());
            if("02".equals(bapiret2.getID()) && createIbdHeader.getWarehouseCode().equals("wumart.werks")){
                if(orderType != PoConstant.ORDER_TYPE_CPO){
                    logger.info("~~~~~2222 ordertype : " + orderType);
                    ReceiveDetail receiveDetail = new ReceiveDetail();
                    receiveDetail.setReceiveId(receiveId);
                    String detailOtherId = bapiret2.getMESSAGEV2().replaceAll("^(0+)", "");
                    receiveDetail.setDetailOtherId(detailOtherId);
                    receiveDetail.setBackStatus(PoConstant.RECEIVE_DETAIL_STATUS_SUCCESS);
                    receiveDetail.setAccountId(bapiret2.getMESSAGEV3());
                    receiveDetail.setAccountDetailId(bapiret2.getMESSAGEV4());
                    receiveService.updateByReceiveIdAndDetailOtherId(receiveDetail);
                }
            }

        }

        if(sb != null && sb.length() > 0){
            map.put("type","P");
            String message = sb.toString();
            map.put("message",message.substring(0,message.length()-1));
        }else{
            map.put("type","S");
        }
        logger.info("~~~~~~~~~~~~~~~Map :" + JSON.toJSONString(map));

        return map;
    }

    public String obd2SapAccount(CreateObdHeader createObdHeader) {
        //当前时间转成XMLGregorianCalendar类型
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        date.setMonth(calendar.get(Calendar.MONTH) + 1);

        com.lsh.wms.integration.wumart.obdaccount.ObjectFactory factory = new com.lsh.wms.integration.wumart.obdaccount.ObjectFactory();
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT zItmes = factory.createTABLEOFZDELIVERYIMPORT();

        List<CreateObdDetail> details = createObdHeader.getItems();
        for(CreateObdDetail detail : details){
            com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYIMPORT zItem = factory.createZDELIVERYIMPORT();
            zItem.setVBELN(detail.getRefDoc());
            //zItem.setVBELN(obdId);
            zItem.setPOSNR(detail.getRefItem());
            zItem.setLFIMG(detail.getDlvQty());
            zItem.setPIKMG(detail.getDlvQty());
            zItem.setWADATIST(date);
            zItem.setLGORT("0001");
            zItem.setWERKS(PropertyUtils.getString("wumart.werks"));
            zItem.setVRKME(detail.getSalesUnit());
            zItmes.getItem().add(zItem);
        }
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYEXPORT eItems = factory.createTABLEOFZDELIVERYEXPORT();
        com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYEXPORT eItem = factory.createZDELIVERYEXPORT();
        eItem.setVBELN("11111");
        eItems.getItem().add(eItem);


        //组装参数
        Holder<TABLEOFBAPIOBDLVITEMCTRLCHG> itemCONTROL = new Holder<TABLEOFBAPIOBDLVITEMCTRLCHG>();
        Holder<TABLEOFBAPIOBDLVITEMCHG> itemDATA = new Holder<TABLEOFBAPIOBDLVITEMCHG>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFPROTT> prot = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFPROTT>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYEXPORT> pZEXPORT = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYEXPORT>(eItems);
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT> pZIMPORT = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFZDELIVERYIMPORT>(zItmes);
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2> return1 = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2>();
        Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFVBPOK> vbpokTAB = new Holder<com.lsh.wms.integration.wumart.obdaccount.TABLEOFVBPOK>();

        com.lsh.wms.integration.wumart.obdaccount.ZDELIVERYOUTBOUNDUPDATE zbinding = new ZDELIVERYOUTBOUNDUPDATE_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);
        logger.info("obd过账入参: pZEXPORT : " + JSON.toJSONString(pZEXPORT) + " pZIMPORT : " + JSON.toJSONString(pZIMPORT));
        com.lsh.wms.integration.wumart.obdaccount.TABLEOFBAPIRET2 newReturn = zbinding.zDELIVERYOUTBOUNDUPDATE(itemCONTROL,itemDATA,prot,pZEXPORT,pZIMPORT,_return,return1,vbpokTAB);

        logger.info("obd过账返回值 : newReturn : " + JSON.toJSONString(newReturn.getItem())
                + " itemCONTROL : " + JSON.toJSONString(itemCONTROL)
                + " itemDATA : " + JSON.toJSONString(itemDATA)
                + " prot : " + JSON.toJSONString(prot)
                + " pZEXPORT: " + JSON.toJSONString(pZEXPORT)
                + " pZIMPORT : " + JSON.toJSONString(pZIMPORT));
        for(com.lsh.wms.integration.wumart.obdaccount.BAPIRET2 bapiret2 : newReturn.getItem()){
            if(bapiret2.getTYPE().equals("E")){
                return "E";
            }
        }

        return "S";
    }

    public String ibd2SapBack(String accountId,String accountDetailId) {
        Calendar calendar = Calendar.getInstance();
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl();
        date.setYear(calendar.get(Calendar.YEAR));
        date.setDay(calendar.get(Calendar.DATE));
        date.setMonth(calendar.get(Calendar.MONTH) + 1);

        String pDOCYEAR =String.valueOf(calendar.get(Calendar.YEAR));
        String pDOCUMENT = accountId;
        String pUNAME = "";
        Holder<BAPI2017GMHEADRET> pHEADRET = new Holder<BAPI2017GMHEADRET>();

        com.lsh.wms.integration.wumart.ibdback.ObjectFactory factory = new com.lsh.wms.integration.wumart.ibdback.ObjectFactory();
        com.lsh.wms.integration.wumart.ibdback.TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();


        TABLEOFBAPI2017GMITEM04 gmitem04s = factory.createTABLEOFBAPI2017GMITEM04();
        BAPI2017GMITEM04 gmitem04 = factory.createBAPI2017GMITEM04();
        gmitem04.setMATDOCITEM(accountDetailId);
        gmitem04s.getItem().add(gmitem04);

        Holder<TABLEOFBAPI2017GMITEM04> pDOCITEM = new Holder<TABLEOFBAPI2017GMITEM04>(gmitem04s);
        ZBAPIGOODSMVTCANCEL zbinding = new ZBAPIGOODSMVTCANCEL_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);

        logger.info("ibd冲销入参: pDOCITEM : " + JSON.toJSONString(pDOCITEM) + " pDOCUMENT : " + pDOCUMENT);
        com.lsh.wms.integration.wumart.ibdback.TABLEOFBAPIRET2 newReturn = zbinding.zbapiGOODSMVTCANCEL(date,pDOCITEM,pDOCUMENT,pDOCYEAR,pUNAME,_return,pHEADRET);
        logger.info("ibd冲销返回值: newReturn : " + JSON.toJSONString(newReturn));

        return JSON.toJSONString(newReturn);
    }

    public String soObd2Sap(CreateObdHeader createObdHeader) {
        com.lsh.wms.integration.wumart.soobd.ObjectFactory factory = new com.lsh.wms.integration.wumart.soobd.ObjectFactory();
        //拼装header信息
        TABLEOFZBAPIR2DELIVERYHEAD deliveryheads = factory.createTABLEOFZBAPIR2DELIVERYHEAD();
        ZBAPIR2DELIVERYHEAD deliveryhead = factory.createZBAPIR2DELIVERYHEAD();
        deliveryhead.setORDERSTYLE("1");
        deliveryhead.setORDERNO(createObdHeader.getOrderOtherId());//so单号。
        deliveryheads.getItem().add(deliveryhead);
        //拼装detail信息
        List<CreateObdDetail> details = createObdHeader.getItems();
        TABLEOFZBAPIR2DELIVERYITEM deliveryitems = factory.createTABLEOFZBAPIR2DELIVERYITEM();
        for (CreateObdDetail detail : details ){
            ZBAPIR2DELIVERYITEM  item = factory.createZBAPIR2DELIVERYITEM();
            item.setLFIMG(detail.getDlvQty());
            item.setPOSNN(detail.getRefItem());
            item.setMATNR(detail.getMaterial());//skuCode
            deliveryitems.getItem().add(item);
        }
        Holder<TABLEOFZBAPIR2DELIVERYHEAD> obdheader = new Holder<TABLEOFZBAPIR2DELIVERYHEAD>(deliveryheads);
        Holder<TABLEOFZBAPIR2DELIVERYITEM> obditem = new Holder<TABLEOFZBAPIR2DELIVERYITEM>(deliveryitems);
        com.lsh.wms.integration.wumart.soobd.TABLEOFBAPIRET2 _return = factory.createTABLEOFBAPIRET2();
        com.lsh.wms.integration.wumart.soobd.BAPIRET2 bapiret2 = factory.createBAPIRET2();
        bapiret2.setMESSAGE(createObdHeader.getTuId());
        _return.getItem().add(bapiret2);
        ZBAPIR2DELIVERYSO zbinding = new ZBAPIR2DELIVERYSO_Service().getBindingSOAP12();
        this.auth((BindingProvider) zbinding);

        logger.info("so obd创建 入口参数: obdheader : " + JSON.toJSONString(obdheader.value) + " obditem : " + JSON.toJSONString(obditem.value));
        com.lsh.wms.integration.wumart.soobd.TABLEOFBAPIRET2 newReturn = zbinding.zBAPIR2DELIVERYSO(obdheader,obditem,_return);
        logger.info("so obd创建 出口参数: obdheader : " + JSON.toJSONString(obdheader.value) + " obditem : " + JSON.toJSONString(obditem.value));
        logger.info("返回值 newReturn : " + JSON.toJSONString(newReturn));

        if(newReturn == null){
            return "S";
        }else{
            return "E";
        }
    }

    protected void auth(BindingProvider provider) {
        Map<String, Object> context = provider.getRequestContext();

        logger.info("~~~~~~~~~~~~~~~~~~~ username :"  + PropertyUtils.getString("wumart.sap.username") + "~~~~~~~~~~~~~~~ password :" + PropertyUtils.getString("wumart.sap.password"));
        context.put(BindingProvider.USERNAME_PROPERTY, PropertyUtils.getString("wumart.sap.username"));
        context.put(BindingProvider.PASSWORD_PROPERTY, PropertyUtils.getString("wumart.sap.password"));
    }

}
