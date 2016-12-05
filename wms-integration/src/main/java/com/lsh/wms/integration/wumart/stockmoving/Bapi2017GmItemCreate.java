
package com.lsh.wms.integration.wumart.stockmoving;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Bapi2017GmItemCreate complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bapi2017GmItemCreate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Material" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="Plant" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="StgeLoc" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="Batch" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MoveType" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="StckType" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SpecStock" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Vendor" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Customer" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="SalesOrd" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="SOrdItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="SchedLine" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="ValType" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="EntryQnt" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="EntryUom" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="EntryUomIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="PoPrQnt" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="OrderprUn" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="OrderprUnIso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="PoNumber" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="PoItem" type="{urn:sap-com:document:sap:rfc:functions}numeric5"/>
 *         &lt;element name="Shipping" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="CompShip" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="NoMoreGr" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="ItemText" type="{urn:sap-com:document:sap:rfc:functions}char50"/>
 *         &lt;element name="GrRcpt" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="UnloadPt" type="{urn:sap-com:document:sap:rfc:functions}char25"/>
 *         &lt;element name="Costcenter" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="Orderid" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="OrderItno" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="CalcMotive" type="{urn:sap-com:document:sap:rfc:functions}char2"/>
 *         &lt;element name="AssetNo" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="SubNumber" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="ReservNo" type="{urn:sap-com:document:sap:rfc:functions}numeric10"/>
 *         &lt;element name="ResItem" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="ResType" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Withdrawn" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MoveMat" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="MovePlant" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="MoveStloc" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="MoveBatch" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MoveValType" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MvtInd" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MoveReas" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="RlEstKey" type="{urn:sap-com:document:sap:rfc:functions}char8"/>
 *         &lt;element name="RefDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="CostObj" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="ProfitSegmNo" type="{urn:sap-com:document:sap:rfc:functions}numeric10"/>
 *         &lt;element name="ProfitCtr" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="WbsElem" type="{urn:sap-com:document:sap:rfc:functions}char24"/>
 *         &lt;element name="Network" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="Activity" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="PartAcct" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="AmountLc" type="{urn:sap-com:document:sap:rfc:functions}decimal23.4"/>
 *         &lt;element name="AmountSv" type="{urn:sap-com:document:sap:rfc:functions}decimal23.4"/>
 *         &lt;element name="RefDocYr" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="RefDoc" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="RefDocIt" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="Expirydate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="ProdDate" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="Fund" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="FundsCtr" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="CmmtItem" type="{urn:sap-com:document:sap:rfc:functions}char14"/>
 *         &lt;element name="ValSalesOrd" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="ValSOrdItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="ValWbsElem" type="{urn:sap-com:document:sap:rfc:functions}char24"/>
 *         &lt;element name="GlAccount" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="IndProposeQuanx" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Xstob" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="EanUpc" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="DelivNumbToSearch" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DelivItemToSearch" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="SerialnoAutoNumberassignment" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="Vendrbatch" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="StgeType" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="StgeBin" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="SuPlStck1" type="{urn:sap-com:document:sap:rfc:functions}decimal3.0"/>
 *         &lt;element name="StUnQtyy1" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="StUnQtyy1Iso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Unittype1" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="SuPlStck2" type="{urn:sap-com:document:sap:rfc:functions}decimal3.0"/>
 *         &lt;element name="StUnQtyy2" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="StUnQtyy2Iso" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="Unittype2" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="StgeTypePc" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="StgeBinPc" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="NoPstChgnt" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="GrNumber" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="StgeTypeSt" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="StgeBinSt" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MatdocTrCancel" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MatitemTrCancel" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="MatyearTrCancel" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="NoTransferReq" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="CoBusproc" type="{urn:sap-com:document:sap:rfc:functions}char12"/>
 *         &lt;element name="Acttype" type="{urn:sap-com:document:sap:rfc:functions}char6"/>
 *         &lt;element name="SupplVend" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MaterialExternal" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="MaterialGuid" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="MaterialVersion" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="MoveMatExternal" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="MoveMatGuid" type="{urn:sap-com:document:sap:rfc:functions}char32"/>
 *         &lt;element name="MoveMatVersion" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="FuncArea" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="TrPartBa" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="ParCompco" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="DelivNumb" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="DelivItem" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="NbSlips" type="{urn:sap-com:document:sap:rfc:functions}numeric3"/>
 *         &lt;element name="NbSlipsx" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="GrRcptx" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="UnloadPtx" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SpecMvmt" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="GrantNbr" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="CmmtItemLong" type="{urn:sap-com:document:sap:rfc:functions}char24"/>
 *         &lt;element name="FuncAreaLong" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="LineId" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="ParentId" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="LineDepth" type="{urn:sap-com:document:sap:rfc:functions}numeric2"/>
 *         &lt;element name="Quantity" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="BaseUom" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="Longnum" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="BudgetPeriod" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="EarmarkedNumber" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="EarmarkedItem" type="{urn:sap-com:document:sap:rfc:functions}numeric3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bapi2017GmItemCreate", propOrder = {
    "material",
    "plant",
    "stgeLoc",
    "batch",
    "moveType",
    "stckType",
    "specStock",
    "vendor",
    "customer",
    "salesOrd",
    "sOrdItem",
    "schedLine",
    "valType",
    "entryQnt",
    "entryUom",
    "entryUomIso",
    "poPrQnt",
    "orderprUn",
    "orderprUnIso",
    "poNumber",
    "poItem",
    "shipping",
    "compShip",
    "noMoreGr",
    "itemText",
    "grRcpt",
    "unloadPt",
    "costcenter",
    "orderid",
    "orderItno",
    "calcMotive",
    "assetNo",
    "subNumber",
    "reservNo",
    "resItem",
    "resType",
    "withdrawn",
    "moveMat",
    "movePlant",
    "moveStloc",
    "moveBatch",
    "moveValType",
    "mvtInd",
    "moveReas",
    "rlEstKey",
    "refDate",
    "costObj",
    "profitSegmNo",
    "profitCtr",
    "wbsElem",
    "network",
    "activity",
    "partAcct",
    "amountLc",
    "amountSv",
    "refDocYr",
    "refDoc",
    "refDocIt",
    "expirydate",
    "prodDate",
    "fund",
    "fundsCtr",
    "cmmtItem",
    "valSalesOrd",
    "valSOrdItem",
    "valWbsElem",
    "glAccount",
    "indProposeQuanx",
    "xstob",
    "eanUpc",
    "delivNumbToSearch",
    "delivItemToSearch",
    "serialnoAutoNumberassignment",
    "vendrbatch",
    "stgeType",
    "stgeBin",
    "suPlStck1",
    "stUnQtyy1",
    "stUnQtyy1Iso",
    "unittype1",
    "suPlStck2",
    "stUnQtyy2",
    "stUnQtyy2Iso",
    "unittype2",
    "stgeTypePc",
    "stgeBinPc",
    "noPstChgnt",
    "grNumber",
    "stgeTypeSt",
    "stgeBinSt",
    "matdocTrCancel",
    "matitemTrCancel",
    "matyearTrCancel",
    "noTransferReq",
    "coBusproc",
    "acttype",
    "supplVend",
    "materialExternal",
    "materialGuid",
    "materialVersion",
    "moveMatExternal",
    "moveMatGuid",
    "moveMatVersion",
    "funcArea",
    "trPartBa",
    "parCompco",
    "delivNumb",
    "delivItem",
    "nbSlips",
    "nbSlipsx",
    "grRcptx",
    "unloadPtx",
    "specMvmt",
    "grantNbr",
    "cmmtItemLong",
    "funcAreaLong",
    "lineId",
    "parentId",
    "lineDepth",
    "quantity",
    "baseUom",
    "longnum",
    "budgetPeriod",
    "earmarkedNumber",
    "earmarkedItem"
})
public class Bapi2017GmItemCreate {

    @XmlElement(name = "Material", required = true)
    protected String material;
    @XmlElement(name = "Plant", required = true)
    protected String plant;
    @XmlElement(name = "StgeLoc", required = true)
    protected String stgeLoc;
    @XmlElement(name = "Batch", required = true)
    protected String batch;
    @XmlElement(name = "MoveType", required = true)
    protected String moveType;
    @XmlElement(name = "StckType", required = true)
    protected String stckType;
    @XmlElement(name = "SpecStock", required = true)
    protected String specStock;
    @XmlElement(name = "Vendor", required = true)
    protected String vendor;
    @XmlElement(name = "Customer", required = true)
    protected String customer;
    @XmlElement(name = "SalesOrd", required = true)
    protected String salesOrd;
    @XmlElement(name = "SOrdItem", required = true)
    protected String sOrdItem;
    @XmlElement(name = "SchedLine", required = true)
    protected String schedLine;
    @XmlElement(name = "ValType", required = true)
    protected String valType;
    @XmlElement(name = "EntryQnt", required = true)
    protected BigDecimal entryQnt;
    @XmlElement(name = "EntryUom", required = true)
    protected String entryUom;
    @XmlElement(name = "EntryUomIso", required = true)
    protected String entryUomIso;
    @XmlElement(name = "PoPrQnt", required = true)
    protected BigDecimal poPrQnt;
    @XmlElement(name = "OrderprUn", required = true)
    protected String orderprUn;
    @XmlElement(name = "OrderprUnIso", required = true)
    protected String orderprUnIso;
    @XmlElement(name = "PoNumber", required = true)
    protected String poNumber;
    @XmlElement(name = "PoItem", required = true)
    protected String poItem;
    @XmlElement(name = "Shipping", required = true)
    protected String shipping;
    @XmlElement(name = "CompShip", required = true)
    protected String compShip;
    @XmlElement(name = "NoMoreGr", required = true)
    protected String noMoreGr;
    @XmlElement(name = "ItemText", required = true)
    protected String itemText;
    @XmlElement(name = "GrRcpt", required = true)
    protected String grRcpt;
    @XmlElement(name = "UnloadPt", required = true)
    protected String unloadPt;
    @XmlElement(name = "Costcenter", required = true)
    protected String costcenter;
    @XmlElement(name = "Orderid", required = true)
    protected String orderid;
    @XmlElement(name = "OrderItno", required = true)
    protected String orderItno;
    @XmlElement(name = "CalcMotive", required = true)
    protected String calcMotive;
    @XmlElement(name = "AssetNo", required = true)
    protected String assetNo;
    @XmlElement(name = "SubNumber", required = true)
    protected String subNumber;
    @XmlElement(name = "ReservNo", required = true)
    protected String reservNo;
    @XmlElement(name = "ResItem", required = true)
    protected String resItem;
    @XmlElement(name = "ResType", required = true)
    protected String resType;
    @XmlElement(name = "Withdrawn", required = true)
    protected String withdrawn;
    @XmlElement(name = "MoveMat", required = true)
    protected String moveMat;
    @XmlElement(name = "MovePlant", required = true)
    protected String movePlant;
    @XmlElement(name = "MoveStloc", required = true)
    protected String moveStloc;
    @XmlElement(name = "MoveBatch", required = true)
    protected String moveBatch;
    @XmlElement(name = "MoveValType", required = true)
    protected String moveValType;
    @XmlElement(name = "MvtInd", required = true)
    protected String mvtInd;
    @XmlElement(name = "MoveReas", required = true)
    protected String moveReas;
    @XmlElement(name = "RlEstKey", required = true)
    protected String rlEstKey;
    @XmlElement(name = "RefDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar refDate;
    @XmlElement(name = "CostObj", required = true)
    protected String costObj;
    @XmlElement(name = "ProfitSegmNo", required = true)
    protected String profitSegmNo;
    @XmlElement(name = "ProfitCtr", required = true)
    protected String profitCtr;
    @XmlElement(name = "WbsElem", required = true)
    protected String wbsElem;
    @XmlElement(name = "Network", required = true)
    protected String network;
    @XmlElement(name = "Activity", required = true)
    protected String activity;
    @XmlElement(name = "PartAcct", required = true)
    protected String partAcct;
    @XmlElement(name = "AmountLc", required = true)
    protected BigDecimal amountLc;
    @XmlElement(name = "AmountSv", required = true)
    protected BigDecimal amountSv;
    @XmlElement(name = "RefDocYr", required = true)
    protected String refDocYr;
    @XmlElement(name = "RefDoc", required = true)
    protected String refDoc;
    @XmlElement(name = "RefDocIt", required = true)
    protected String refDocIt;
    @XmlElement(name = "Expirydate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar expirydate;
    @XmlElement(name = "ProdDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar prodDate;
    @XmlElement(name = "Fund", required = true)
    protected String fund;
    @XmlElement(name = "FundsCtr", required = true)
    protected String fundsCtr;
    @XmlElement(name = "CmmtItem", required = true)
    protected String cmmtItem;
    @XmlElement(name = "ValSalesOrd", required = true)
    protected String valSalesOrd;
    @XmlElement(name = "ValSOrdItem", required = true)
    protected String valSOrdItem;
    @XmlElement(name = "ValWbsElem", required = true)
    protected String valWbsElem;
    @XmlElement(name = "GlAccount", required = true)
    protected String glAccount;
    @XmlElement(name = "IndProposeQuanx", required = true)
    protected String indProposeQuanx;
    @XmlElement(name = "Xstob", required = true)
    protected String xstob;
    @XmlElement(name = "EanUpc", required = true)
    protected String eanUpc;
    @XmlElement(name = "DelivNumbToSearch", required = true)
    protected String delivNumbToSearch;
    @XmlElement(name = "DelivItemToSearch", required = true)
    protected String delivItemToSearch;
    @XmlElement(name = "SerialnoAutoNumberassignment", required = true)
    protected String serialnoAutoNumberassignment;
    @XmlElement(name = "Vendrbatch", required = true)
    protected String vendrbatch;
    @XmlElement(name = "StgeType", required = true)
    protected String stgeType;
    @XmlElement(name = "StgeBin", required = true)
    protected String stgeBin;
    @XmlElement(name = "SuPlStck1", required = true)
    protected BigDecimal suPlStck1;
    @XmlElement(name = "StUnQtyy1", required = true)
    protected BigDecimal stUnQtyy1;
    @XmlElement(name = "StUnQtyy1Iso", required = true)
    protected String stUnQtyy1Iso;
    @XmlElement(name = "Unittype1", required = true)
    protected String unittype1;
    @XmlElement(name = "SuPlStck2", required = true)
    protected BigDecimal suPlStck2;
    @XmlElement(name = "StUnQtyy2", required = true)
    protected BigDecimal stUnQtyy2;
    @XmlElement(name = "StUnQtyy2Iso", required = true)
    protected String stUnQtyy2Iso;
    @XmlElement(name = "Unittype2", required = true)
    protected String unittype2;
    @XmlElement(name = "StgeTypePc", required = true)
    protected String stgeTypePc;
    @XmlElement(name = "StgeBinPc", required = true)
    protected String stgeBinPc;
    @XmlElement(name = "NoPstChgnt", required = true)
    protected String noPstChgnt;
    @XmlElement(name = "GrNumber", required = true)
    protected String grNumber;
    @XmlElement(name = "StgeTypeSt", required = true)
    protected String stgeTypeSt;
    @XmlElement(name = "StgeBinSt", required = true)
    protected String stgeBinSt;
    @XmlElement(name = "MatdocTrCancel", required = true)
    protected String matdocTrCancel;
    @XmlElement(name = "MatitemTrCancel", required = true)
    protected String matitemTrCancel;
    @XmlElement(name = "MatyearTrCancel", required = true)
    protected String matyearTrCancel;
    @XmlElement(name = "NoTransferReq", required = true)
    protected String noTransferReq;
    @XmlElement(name = "CoBusproc", required = true)
    protected String coBusproc;
    @XmlElement(name = "Acttype", required = true)
    protected String acttype;
    @XmlElement(name = "SupplVend", required = true)
    protected String supplVend;
    @XmlElement(name = "MaterialExternal", required = true)
    protected String materialExternal;
    @XmlElement(name = "MaterialGuid", required = true)
    protected String materialGuid;
    @XmlElement(name = "MaterialVersion", required = true)
    protected String materialVersion;
    @XmlElement(name = "MoveMatExternal", required = true)
    protected String moveMatExternal;
    @XmlElement(name = "MoveMatGuid", required = true)
    protected String moveMatGuid;
    @XmlElement(name = "MoveMatVersion", required = true)
    protected String moveMatVersion;
    @XmlElement(name = "FuncArea", required = true)
    protected String funcArea;
    @XmlElement(name = "TrPartBa", required = true)
    protected String trPartBa;
    @XmlElement(name = "ParCompco", required = true)
    protected String parCompco;
    @XmlElement(name = "DelivNumb", required = true)
    protected String delivNumb;
    @XmlElement(name = "DelivItem", required = true)
    protected String delivItem;
    @XmlElement(name = "NbSlips", required = true)
    protected String nbSlips;
    @XmlElement(name = "NbSlipsx", required = true)
    protected String nbSlipsx;
    @XmlElement(name = "GrRcptx", required = true)
    protected String grRcptx;
    @XmlElement(name = "UnloadPtx", required = true)
    protected String unloadPtx;
    @XmlElement(name = "SpecMvmt", required = true)
    protected String specMvmt;
    @XmlElement(name = "GrantNbr", required = true)
    protected String grantNbr;
    @XmlElement(name = "CmmtItemLong", required = true)
    protected String cmmtItemLong;
    @XmlElement(name = "FuncAreaLong", required = true)
    protected String funcAreaLong;
    @XmlElement(name = "LineId", required = true)
    protected String lineId;
    @XmlElement(name = "ParentId", required = true)
    protected String parentId;
    @XmlElement(name = "LineDepth", required = true)
    protected String lineDepth;
    @XmlElement(name = "Quantity", required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "BaseUom", required = true)
    protected String baseUom;
    @XmlElement(name = "Longnum", required = true)
    protected String longnum;
    @XmlElement(name = "BudgetPeriod", required = true)
    protected String budgetPeriod;
    @XmlElement(name = "EarmarkedNumber", required = true)
    protected String earmarkedNumber;
    @XmlElement(name = "EarmarkedItem", required = true)
    protected String earmarkedItem;

    /**
     * 获取material属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterial() {
        return material;
    }

    /**
     * 设置material属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterial(String value) {
        this.material = value;
    }

    /**
     * 获取plant属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlant() {
        return plant;
    }

    /**
     * 设置plant属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlant(String value) {
        this.plant = value;
    }

    /**
     * 获取stgeLoc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeLoc() {
        return stgeLoc;
    }

    /**
     * 设置stgeLoc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeLoc(String value) {
        this.stgeLoc = value;
    }

    /**
     * 获取batch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatch() {
        return batch;
    }

    /**
     * 设置batch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatch(String value) {
        this.batch = value;
    }

    /**
     * 获取moveType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveType() {
        return moveType;
    }

    /**
     * 设置moveType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveType(String value) {
        this.moveType = value;
    }

    /**
     * 获取stckType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStckType() {
        return stckType;
    }

    /**
     * 设置stckType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStckType(String value) {
        this.stckType = value;
    }

    /**
     * 获取specStock属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecStock() {
        return specStock;
    }

    /**
     * 设置specStock属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecStock(String value) {
        this.specStock = value;
    }

    /**
     * 获取vendor属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * 设置vendor属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * 获取customer属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * 设置customer属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomer(String value) {
        this.customer = value;
    }

    /**
     * 获取salesOrd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesOrd() {
        return salesOrd;
    }

    /**
     * 设置salesOrd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesOrd(String value) {
        this.salesOrd = value;
    }

    /**
     * 获取sOrdItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSOrdItem() {
        return sOrdItem;
    }

    /**
     * 设置sOrdItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSOrdItem(String value) {
        this.sOrdItem = value;
    }

    /**
     * 获取schedLine属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchedLine() {
        return schedLine;
    }

    /**
     * 设置schedLine属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchedLine(String value) {
        this.schedLine = value;
    }

    /**
     * 获取valType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValType() {
        return valType;
    }

    /**
     * 设置valType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValType(String value) {
        this.valType = value;
    }

    /**
     * 获取entryQnt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEntryQnt() {
        return entryQnt;
    }

    /**
     * 设置entryQnt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEntryQnt(BigDecimal value) {
        this.entryQnt = value;
    }

    /**
     * 获取entryUom属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryUom() {
        return entryUom;
    }

    /**
     * 设置entryUom属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryUom(String value) {
        this.entryUom = value;
    }

    /**
     * 获取entryUomIso属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryUomIso() {
        return entryUomIso;
    }

    /**
     * 设置entryUomIso属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryUomIso(String value) {
        this.entryUomIso = value;
    }

    /**
     * 获取poPrQnt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPoPrQnt() {
        return poPrQnt;
    }

    /**
     * 设置poPrQnt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPoPrQnt(BigDecimal value) {
        this.poPrQnt = value;
    }

    /**
     * 获取orderprUn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderprUn() {
        return orderprUn;
    }

    /**
     * 设置orderprUn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderprUn(String value) {
        this.orderprUn = value;
    }

    /**
     * 获取orderprUnIso属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderprUnIso() {
        return orderprUnIso;
    }

    /**
     * 设置orderprUnIso属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderprUnIso(String value) {
        this.orderprUnIso = value;
    }

    /**
     * 获取poNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * 设置poNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoNumber(String value) {
        this.poNumber = value;
    }

    /**
     * 获取poItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoItem() {
        return poItem;
    }

    /**
     * 设置poItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoItem(String value) {
        this.poItem = value;
    }

    /**
     * 获取shipping属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipping() {
        return shipping;
    }

    /**
     * 设置shipping属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipping(String value) {
        this.shipping = value;
    }

    /**
     * 获取compShip属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompShip() {
        return compShip;
    }

    /**
     * 设置compShip属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompShip(String value) {
        this.compShip = value;
    }

    /**
     * 获取noMoreGr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoMoreGr() {
        return noMoreGr;
    }

    /**
     * 设置noMoreGr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoMoreGr(String value) {
        this.noMoreGr = value;
    }

    /**
     * 获取itemText属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemText() {
        return itemText;
    }

    /**
     * 设置itemText属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemText(String value) {
        this.itemText = value;
    }

    /**
     * 获取grRcpt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrRcpt() {
        return grRcpt;
    }

    /**
     * 设置grRcpt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrRcpt(String value) {
        this.grRcpt = value;
    }

    /**
     * 获取unloadPt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnloadPt() {
        return unloadPt;
    }

    /**
     * 设置unloadPt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnloadPt(String value) {
        this.unloadPt = value;
    }

    /**
     * 获取costcenter属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostcenter() {
        return costcenter;
    }

    /**
     * 设置costcenter属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostcenter(String value) {
        this.costcenter = value;
    }

    /**
     * 获取orderid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置orderid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderid(String value) {
        this.orderid = value;
    }

    /**
     * 获取orderItno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderItno() {
        return orderItno;
    }

    /**
     * 设置orderItno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderItno(String value) {
        this.orderItno = value;
    }

    /**
     * 获取calcMotive属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalcMotive() {
        return calcMotive;
    }

    /**
     * 设置calcMotive属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalcMotive(String value) {
        this.calcMotive = value;
    }

    /**
     * 获取assetNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetNo() {
        return assetNo;
    }

    /**
     * 设置assetNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetNo(String value) {
        this.assetNo = value;
    }

    /**
     * 获取subNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubNumber() {
        return subNumber;
    }

    /**
     * 设置subNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubNumber(String value) {
        this.subNumber = value;
    }

    /**
     * 获取reservNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReservNo() {
        return reservNo;
    }

    /**
     * 设置reservNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReservNo(String value) {
        this.reservNo = value;
    }

    /**
     * 获取resItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResItem() {
        return resItem;
    }

    /**
     * 设置resItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResItem(String value) {
        this.resItem = value;
    }

    /**
     * 获取resType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResType() {
        return resType;
    }

    /**
     * 设置resType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResType(String value) {
        this.resType = value;
    }

    /**
     * 获取withdrawn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWithdrawn() {
        return withdrawn;
    }

    /**
     * 设置withdrawn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWithdrawn(String value) {
        this.withdrawn = value;
    }

    /**
     * 获取moveMat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveMat() {
        return moveMat;
    }

    /**
     * 设置moveMat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveMat(String value) {
        this.moveMat = value;
    }

    /**
     * 获取movePlant属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovePlant() {
        return movePlant;
    }

    /**
     * 设置movePlant属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovePlant(String value) {
        this.movePlant = value;
    }

    /**
     * 获取moveStloc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveStloc() {
        return moveStloc;
    }

    /**
     * 设置moveStloc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveStloc(String value) {
        this.moveStloc = value;
    }

    /**
     * 获取moveBatch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveBatch() {
        return moveBatch;
    }

    /**
     * 设置moveBatch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveBatch(String value) {
        this.moveBatch = value;
    }

    /**
     * 获取moveValType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveValType() {
        return moveValType;
    }

    /**
     * 设置moveValType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveValType(String value) {
        this.moveValType = value;
    }

    /**
     * 获取mvtInd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMvtInd() {
        return mvtInd;
    }

    /**
     * 设置mvtInd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMvtInd(String value) {
        this.mvtInd = value;
    }

    /**
     * 获取moveReas属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveReas() {
        return moveReas;
    }

    /**
     * 设置moveReas属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveReas(String value) {
        this.moveReas = value;
    }

    /**
     * 获取rlEstKey属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRlEstKey() {
        return rlEstKey;
    }

    /**
     * 设置rlEstKey属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRlEstKey(String value) {
        this.rlEstKey = value;
    }

    /**
     * 获取refDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRefDate() {
        return refDate;
    }

    /**
     * 设置refDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRefDate(XMLGregorianCalendar value) {
        this.refDate = value;
    }

    /**
     * 获取costObj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostObj() {
        return costObj;
    }

    /**
     * 设置costObj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostObj(String value) {
        this.costObj = value;
    }

    /**
     * 获取profitSegmNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfitSegmNo() {
        return profitSegmNo;
    }

    /**
     * 设置profitSegmNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfitSegmNo(String value) {
        this.profitSegmNo = value;
    }

    /**
     * 获取profitCtr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfitCtr() {
        return profitCtr;
    }

    /**
     * 设置profitCtr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfitCtr(String value) {
        this.profitCtr = value;
    }

    /**
     * 获取wbsElem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWbsElem() {
        return wbsElem;
    }

    /**
     * 设置wbsElem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWbsElem(String value) {
        this.wbsElem = value;
    }

    /**
     * 获取network属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetwork() {
        return network;
    }

    /**
     * 设置network属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetwork(String value) {
        this.network = value;
    }

    /**
     * 获取activity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivity() {
        return activity;
    }

    /**
     * 设置activity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivity(String value) {
        this.activity = value;
    }

    /**
     * 获取partAcct属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartAcct() {
        return partAcct;
    }

    /**
     * 设置partAcct属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartAcct(String value) {
        this.partAcct = value;
    }

    /**
     * 获取amountLc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountLc() {
        return amountLc;
    }

    /**
     * 设置amountLc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountLc(BigDecimal value) {
        this.amountLc = value;
    }

    /**
     * 获取amountSv属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountSv() {
        return amountSv;
    }

    /**
     * 设置amountSv属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountSv(BigDecimal value) {
        this.amountSv = value;
    }

    /**
     * 获取refDocYr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDocYr() {
        return refDocYr;
    }

    /**
     * 设置refDocYr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDocYr(String value) {
        this.refDocYr = value;
    }

    /**
     * 获取refDoc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDoc() {
        return refDoc;
    }

    /**
     * 设置refDoc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDoc(String value) {
        this.refDoc = value;
    }

    /**
     * 获取refDocIt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefDocIt() {
        return refDocIt;
    }

    /**
     * 设置refDocIt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefDocIt(String value) {
        this.refDocIt = value;
    }

    /**
     * 获取expirydate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpirydate() {
        return expirydate;
    }

    /**
     * 设置expirydate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpirydate(XMLGregorianCalendar value) {
        this.expirydate = value;
    }

    /**
     * 获取prodDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProdDate() {
        return prodDate;
    }

    /**
     * 设置prodDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setProdDate(XMLGregorianCalendar value) {
        this.prodDate = value;
    }

    /**
     * 获取fund属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFund() {
        return fund;
    }

    /**
     * 设置fund属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFund(String value) {
        this.fund = value;
    }

    /**
     * 获取fundsCtr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundsCtr() {
        return fundsCtr;
    }

    /**
     * 设置fundsCtr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundsCtr(String value) {
        this.fundsCtr = value;
    }

    /**
     * 获取cmmtItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmmtItem() {
        return cmmtItem;
    }

    /**
     * 设置cmmtItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmmtItem(String value) {
        this.cmmtItem = value;
    }

    /**
     * 获取valSalesOrd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValSalesOrd() {
        return valSalesOrd;
    }

    /**
     * 设置valSalesOrd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValSalesOrd(String value) {
        this.valSalesOrd = value;
    }

    /**
     * 获取valSOrdItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValSOrdItem() {
        return valSOrdItem;
    }

    /**
     * 设置valSOrdItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValSOrdItem(String value) {
        this.valSOrdItem = value;
    }

    /**
     * 获取valWbsElem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValWbsElem() {
        return valWbsElem;
    }

    /**
     * 设置valWbsElem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValWbsElem(String value) {
        this.valWbsElem = value;
    }

    /**
     * 获取glAccount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlAccount() {
        return glAccount;
    }

    /**
     * 设置glAccount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlAccount(String value) {
        this.glAccount = value;
    }

    /**
     * 获取indProposeQuanx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndProposeQuanx() {
        return indProposeQuanx;
    }

    /**
     * 设置indProposeQuanx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndProposeQuanx(String value) {
        this.indProposeQuanx = value;
    }

    /**
     * 获取xstob属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXstob() {
        return xstob;
    }

    /**
     * 设置xstob属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXstob(String value) {
        this.xstob = value;
    }

    /**
     * 获取eanUpc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEanUpc() {
        return eanUpc;
    }

    /**
     * 设置eanUpc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEanUpc(String value) {
        this.eanUpc = value;
    }

    /**
     * 获取delivNumbToSearch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivNumbToSearch() {
        return delivNumbToSearch;
    }

    /**
     * 设置delivNumbToSearch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivNumbToSearch(String value) {
        this.delivNumbToSearch = value;
    }

    /**
     * 获取delivItemToSearch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivItemToSearch() {
        return delivItemToSearch;
    }

    /**
     * 设置delivItemToSearch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivItemToSearch(String value) {
        this.delivItemToSearch = value;
    }

    /**
     * 获取serialnoAutoNumberassignment属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialnoAutoNumberassignment() {
        return serialnoAutoNumberassignment;
    }

    /**
     * 设置serialnoAutoNumberassignment属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialnoAutoNumberassignment(String value) {
        this.serialnoAutoNumberassignment = value;
    }

    /**
     * 获取vendrbatch属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendrbatch() {
        return vendrbatch;
    }

    /**
     * 设置vendrbatch属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendrbatch(String value) {
        this.vendrbatch = value;
    }

    /**
     * 获取stgeType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeType() {
        return stgeType;
    }

    /**
     * 设置stgeType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeType(String value) {
        this.stgeType = value;
    }

    /**
     * 获取stgeBin属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeBin() {
        return stgeBin;
    }

    /**
     * 设置stgeBin属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeBin(String value) {
        this.stgeBin = value;
    }

    /**
     * 获取suPlStck1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSuPlStck1() {
        return suPlStck1;
    }

    /**
     * 设置suPlStck1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSuPlStck1(BigDecimal value) {
        this.suPlStck1 = value;
    }

    /**
     * 获取stUnQtyy1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStUnQtyy1() {
        return stUnQtyy1;
    }

    /**
     * 设置stUnQtyy1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStUnQtyy1(BigDecimal value) {
        this.stUnQtyy1 = value;
    }

    /**
     * 获取stUnQtyy1Iso属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStUnQtyy1Iso() {
        return stUnQtyy1Iso;
    }

    /**
     * 设置stUnQtyy1Iso属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStUnQtyy1Iso(String value) {
        this.stUnQtyy1Iso = value;
    }

    /**
     * 获取unittype1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnittype1() {
        return unittype1;
    }

    /**
     * 设置unittype1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnittype1(String value) {
        this.unittype1 = value;
    }

    /**
     * 获取suPlStck2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSuPlStck2() {
        return suPlStck2;
    }

    /**
     * 设置suPlStck2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSuPlStck2(BigDecimal value) {
        this.suPlStck2 = value;
    }

    /**
     * 获取stUnQtyy2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStUnQtyy2() {
        return stUnQtyy2;
    }

    /**
     * 设置stUnQtyy2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStUnQtyy2(BigDecimal value) {
        this.stUnQtyy2 = value;
    }

    /**
     * 获取stUnQtyy2Iso属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStUnQtyy2Iso() {
        return stUnQtyy2Iso;
    }

    /**
     * 设置stUnQtyy2Iso属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStUnQtyy2Iso(String value) {
        this.stUnQtyy2Iso = value;
    }

    /**
     * 获取unittype2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnittype2() {
        return unittype2;
    }

    /**
     * 设置unittype2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnittype2(String value) {
        this.unittype2 = value;
    }

    /**
     * 获取stgeTypePc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeTypePc() {
        return stgeTypePc;
    }

    /**
     * 设置stgeTypePc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeTypePc(String value) {
        this.stgeTypePc = value;
    }

    /**
     * 获取stgeBinPc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeBinPc() {
        return stgeBinPc;
    }

    /**
     * 设置stgeBinPc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeBinPc(String value) {
        this.stgeBinPc = value;
    }

    /**
     * 获取noPstChgnt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoPstChgnt() {
        return noPstChgnt;
    }

    /**
     * 设置noPstChgnt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoPstChgnt(String value) {
        this.noPstChgnt = value;
    }

    /**
     * 获取grNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrNumber() {
        return grNumber;
    }

    /**
     * 设置grNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrNumber(String value) {
        this.grNumber = value;
    }

    /**
     * 获取stgeTypeSt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeTypeSt() {
        return stgeTypeSt;
    }

    /**
     * 设置stgeTypeSt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeTypeSt(String value) {
        this.stgeTypeSt = value;
    }

    /**
     * 获取stgeBinSt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStgeBinSt() {
        return stgeBinSt;
    }

    /**
     * 设置stgeBinSt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStgeBinSt(String value) {
        this.stgeBinSt = value;
    }

    /**
     * 获取matdocTrCancel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatdocTrCancel() {
        return matdocTrCancel;
    }

    /**
     * 设置matdocTrCancel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatdocTrCancel(String value) {
        this.matdocTrCancel = value;
    }

    /**
     * 获取matitemTrCancel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatitemTrCancel() {
        return matitemTrCancel;
    }

    /**
     * 设置matitemTrCancel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatitemTrCancel(String value) {
        this.matitemTrCancel = value;
    }

    /**
     * 获取matyearTrCancel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatyearTrCancel() {
        return matyearTrCancel;
    }

    /**
     * 设置matyearTrCancel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatyearTrCancel(String value) {
        this.matyearTrCancel = value;
    }

    /**
     * 获取noTransferReq属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoTransferReq() {
        return noTransferReq;
    }

    /**
     * 设置noTransferReq属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoTransferReq(String value) {
        this.noTransferReq = value;
    }

    /**
     * 获取coBusproc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoBusproc() {
        return coBusproc;
    }

    /**
     * 设置coBusproc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoBusproc(String value) {
        this.coBusproc = value;
    }

    /**
     * 获取acttype属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActtype() {
        return acttype;
    }

    /**
     * 设置acttype属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActtype(String value) {
        this.acttype = value;
    }

    /**
     * 获取supplVend属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplVend() {
        return supplVend;
    }

    /**
     * 设置supplVend属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplVend(String value) {
        this.supplVend = value;
    }

    /**
     * 获取materialExternal属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialExternal() {
        return materialExternal;
    }

    /**
     * 设置materialExternal属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialExternal(String value) {
        this.materialExternal = value;
    }

    /**
     * 获取materialGuid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialGuid() {
        return materialGuid;
    }

    /**
     * 设置materialGuid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialGuid(String value) {
        this.materialGuid = value;
    }

    /**
     * 获取materialVersion属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    /**
     * 设置materialVersion属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialVersion(String value) {
        this.materialVersion = value;
    }

    /**
     * 获取moveMatExternal属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveMatExternal() {
        return moveMatExternal;
    }

    /**
     * 设置moveMatExternal属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveMatExternal(String value) {
        this.moveMatExternal = value;
    }

    /**
     * 获取moveMatGuid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveMatGuid() {
        return moveMatGuid;
    }

    /**
     * 设置moveMatGuid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveMatGuid(String value) {
        this.moveMatGuid = value;
    }

    /**
     * 获取moveMatVersion属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveMatVersion() {
        return moveMatVersion;
    }

    /**
     * 设置moveMatVersion属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveMatVersion(String value) {
        this.moveMatVersion = value;
    }

    /**
     * 获取funcArea属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncArea() {
        return funcArea;
    }

    /**
     * 设置funcArea属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncArea(String value) {
        this.funcArea = value;
    }

    /**
     * 获取trPartBa属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrPartBa() {
        return trPartBa;
    }

    /**
     * 设置trPartBa属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrPartBa(String value) {
        this.trPartBa = value;
    }

    /**
     * 获取parCompco属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParCompco() {
        return parCompco;
    }

    /**
     * 设置parCompco属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParCompco(String value) {
        this.parCompco = value;
    }

    /**
     * 获取delivNumb属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivNumb() {
        return delivNumb;
    }

    /**
     * 设置delivNumb属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivNumb(String value) {
        this.delivNumb = value;
    }

    /**
     * 获取delivItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelivItem() {
        return delivItem;
    }

    /**
     * 设置delivItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelivItem(String value) {
        this.delivItem = value;
    }

    /**
     * 获取nbSlips属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbSlips() {
        return nbSlips;
    }

    /**
     * 设置nbSlips属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbSlips(String value) {
        this.nbSlips = value;
    }

    /**
     * 获取nbSlipsx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbSlipsx() {
        return nbSlipsx;
    }

    /**
     * 设置nbSlipsx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbSlipsx(String value) {
        this.nbSlipsx = value;
    }

    /**
     * 获取grRcptx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrRcptx() {
        return grRcptx;
    }

    /**
     * 设置grRcptx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrRcptx(String value) {
        this.grRcptx = value;
    }

    /**
     * 获取unloadPtx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnloadPtx() {
        return unloadPtx;
    }

    /**
     * 设置unloadPtx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnloadPtx(String value) {
        this.unloadPtx = value;
    }

    /**
     * 获取specMvmt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecMvmt() {
        return specMvmt;
    }

    /**
     * 设置specMvmt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecMvmt(String value) {
        this.specMvmt = value;
    }

    /**
     * 获取grantNbr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrantNbr() {
        return grantNbr;
    }

    /**
     * 设置grantNbr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrantNbr(String value) {
        this.grantNbr = value;
    }

    /**
     * 获取cmmtItemLong属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmmtItemLong() {
        return cmmtItemLong;
    }

    /**
     * 设置cmmtItemLong属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmmtItemLong(String value) {
        this.cmmtItemLong = value;
    }

    /**
     * 获取funcAreaLong属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncAreaLong() {
        return funcAreaLong;
    }

    /**
     * 设置funcAreaLong属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncAreaLong(String value) {
        this.funcAreaLong = value;
    }

    /**
     * 获取lineId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * 设置lineId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineId(String value) {
        this.lineId = value;
    }

    /**
     * 获取parentId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置parentId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * 获取lineDepth属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineDepth() {
        return lineDepth;
    }

    /**
     * 设置lineDepth属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineDepth(String value) {
        this.lineDepth = value;
    }

    /**
     * 获取quantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * 设置quantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * 获取baseUom属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUom() {
        return baseUom;
    }

    /**
     * 设置baseUom属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUom(String value) {
        this.baseUom = value;
    }

    /**
     * 获取longnum属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongnum() {
        return longnum;
    }

    /**
     * 设置longnum属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongnum(String value) {
        this.longnum = value;
    }

    /**
     * 获取budgetPeriod属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    /**
     * 设置budgetPeriod属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBudgetPeriod(String value) {
        this.budgetPeriod = value;
    }

    /**
     * 获取earmarkedNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEarmarkedNumber() {
        return earmarkedNumber;
    }

    /**
     * 设置earmarkedNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEarmarkedNumber(String value) {
        this.earmarkedNumber = value;
    }

    /**
     * 获取earmarkedItem属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEarmarkedItem() {
        return earmarkedItem;
    }

    /**
     * 设置earmarkedItem属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEarmarkedItem(String value) {
        this.earmarkedItem = value;
    }

}
