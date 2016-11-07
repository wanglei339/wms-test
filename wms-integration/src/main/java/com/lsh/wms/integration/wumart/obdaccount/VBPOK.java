
package com.lsh.wms.integration.wumart.obdaccount;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for VBPOK complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VBPOK">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VBELN_VL" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="POSNR_VL" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="VBELN" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="POSNN" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="VBTYP_N" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MATNR" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="CHARG" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="WERKS" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="TAQUI" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KZNTG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KZBRG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KZVOL" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="LIANP" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="LIPS_DEL" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="INSMK" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="BESTQ" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="KZPOD" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="NDIFM" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="ORPOS" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="PIKMG" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="SOBKZ" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SONUM" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="KZBEF" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="PLMIN" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="FORCE_ORPOS_REDUCTION" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="LFIMG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="LGMNG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="LFIMG_FLO" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="LGMNG_FLO" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="UMVKZ" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="UMVKN" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="UMREV" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="AKMNG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="VRKME" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="MEINS" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="KZFME" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="BRGEW" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="NTGEW" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="GEWEI" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="VOLUM" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="VOLEH" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="LGPLA" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="LGTYP" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="BWLVS" type="{urn:sap-com:document:sap:rfc:functions}numeric3"/>
 *         &lt;element name="XWMPP" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="VFDAT" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="KZVFDAT" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="EBUMG_BME" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="WMS_RFBEL" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="WMS_RFPOS" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="HSDAT" type="{urn:sap-com:document:sap:rfc:functions}date"/>
 *         &lt;element name="KZHSDAT" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_DIFF_POST_INDICATOR" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_EXCEPT_CODE" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="SPE_AUTH_NUMBER" type="{urn:sap-com:document:sap:rfc:functions}char20"/>
 *         &lt;element name="SPE_RET_QTY_POST" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="SPE_BASE_UOM" type="{urn:sap-com:document:sap:rfc:functions}unit3"/>
 *         &lt;element name="SPE_AUTH_COMPLET" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_AUTH_COMPLET_F" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_RET_CLOSING_INDICATOR" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_EXP_DATE_EXT_B" type="{urn:sap-com:document:sap:rfc:functions}decimal15.0"/>
 *         &lt;element name="SPE_EXP_DATE_EXT" type="{urn:sap-com:document:sap:rfc:functions}decimal15.0"/>
 *         &lt;element name="SPE_MDIFF_INSMK" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_IMWRK_ITM" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_IMWRK_ITM_F" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_ORIG_SYS" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_CUM_VEND" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="SPE_CUM_ITQS" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="SPE_CUM_SA" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="SPE_LIFEXPOS2" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="SPE_VERSION" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="SPE_DLV_QTY_FROM" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="SPE_INMSK_F" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_LIFEXPOS2_F" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_INB_ITM_UPD" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="ARKTX" type="{urn:sap-com:document:sap:rfc:functions}char40"/>
 *         &lt;element name="LGBZO" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="LGORT" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="KZLGO" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="LIFEXPOS" type="{urn:sap-com:document:sap:rfc:functions}numeric6"/>
 *         &lt;element name="KZLXP" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="BWTAR" type="{urn:sap-com:document:sap:rfc:functions}char10"/>
 *         &lt;element name="KZBWT" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MFRGR" type="{urn:sap-com:document:sap:rfc:functions}char8"/>
 *         &lt;element name="EAN11" type="{urn:sap-com:document:sap:rfc:functions}char18"/>
 *         &lt;element name="IDNLF" type="{urn:sap-com:document:sap:rfc:functions}char35"/>
 *         &lt;element name="LICHN" type="{urn:sap-com:document:sap:rfc:functions}char15"/>
 *         &lt;element name="ABRVW" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="KZTXT" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="TXTOLD_DEL_ALL_LANG" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="MDIFF_BWART" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="MDIFF_GRUND" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="MDIFF_LGORT" type="{urn:sap-com:document:sap:rfc:functions}char4"/>
 *         &lt;element name="MDIFF_AUTO_SPLIT" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_MDNUM_EWM" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="SPE_MDITM_EWM" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="SPE_OMDNUM_EWM" type="{urn:sap-com:document:sap:rfc:functions}char16"/>
 *         &lt;element name="SPE_OMDITM_EWM" type="{urn:sap-com:document:sap:rfc:functions}numeric4"/>
 *         &lt;element name="ORMNG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="SPE_GTS_STOCK_TYPE" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_LIEFFZ" type="{urn:sap-com:document:sap:rfc:functions}quantum15.3"/>
 *         &lt;element name="SPE_APO_QNTYFAC" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="SPE_APO_QNTYDIV" type="{urn:sap-com:document:sap:rfc:functions}decimal5.0"/>
 *         &lt;element name="SPE_PICK_DENIAL" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SERNR_DEL" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="FLAG_DOCUB" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_XNOCON" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *         &lt;element name="SPE_EBUMG" type="{urn:sap-com:document:sap:rfc:functions}quantum13.3"/>
 *         &lt;element name="BWART" type="{urn:sap-com:document:sap:rfc:functions}char3"/>
 *         &lt;element name="SPE_FORCE_ORMNG_UPDATE" type="{urn:sap-com:document:sap:rfc:functions}char1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VBPOK", propOrder = {
    "vbelnvl",
    "posnrvl",
    "vbeln",
    "posnn",
    "vbtypn",
    "matnr",
    "charg",
    "werks",
    "taqui",
    "kzntg",
    "kzbrg",
    "kzvol",
    "lianp",
    "lipsdel",
    "insmk",
    "bestq",
    "kzpod",
    "ndifm",
    "orpos",
    "pikmg",
    "sobkz",
    "sonum",
    "kzbef",
    "plmin",
    "forceorposreduction",
    "lfimg",
    "lgmng",
    "lfimgflo",
    "lgmngflo",
    "umvkz",
    "umvkn",
    "umrev",
    "akmng",
    "vrkme",
    "meins",
    "kzfme",
    "brgew",
    "ntgew",
    "gewei",
    "volum",
    "voleh",
    "lgpla",
    "lgtyp",
    "bwlvs",
    "xwmpp",
    "vfdat",
    "kzvfdat",
    "ebumgbme",
    "wmsrfbel",
    "wmsrfpos",
    "hsdat",
    "kzhsdat",
    "spediffpostindicator",
    "speexceptcode",
    "speauthnumber",
    "speretqtypost",
    "spebaseuom",
    "speauthcomplet",
    "speauthcompletf",
    "speretclosingindicator",
    "speexpdateextb",
    "speexpdateext",
    "spemdiffinsmk",
    "speimwrkitm",
    "speimwrkitmf",
    "speorigsys",
    "specumvend",
    "specumitqs",
    "specumsa",
    "spelifexpos2",
    "speversion",
    "spedlvqtyfrom",
    "speinmskf",
    "spelifexpos2F",
    "speinbitmupd",
    "arktx",
    "lgbzo",
    "lgort",
    "kzlgo",
    "lifexpos",
    "kzlxp",
    "bwtar",
    "kzbwt",
    "mfrgr",
    "ean11",
    "idnlf",
    "lichn",
    "abrvw",
    "kztxt",
    "txtolddelalllang",
    "mdiffbwart",
    "mdiffgrund",
    "mdifflgort",
    "mdiffautosplit",
    "spemdnumewm",
    "spemditmewm",
    "speomdnumewm",
    "speomditmewm",
    "ormng",
    "spegtsstocktype",
    "spelieffz",
    "speapoqntyfac",
    "speapoqntydiv",
    "spepickdenial",
    "sernrdel",
    "flagdocub",
    "spexnocon",
    "speebumg",
    "bwart",
    "speforceormngupdate"
})
public class VBPOK {

    @XmlElement(name = "VBELN_VL", required = true)
    protected String vbelnvl;
    @XmlElement(name = "POSNR_VL", required = true)
    protected String posnrvl;
    @XmlElement(name = "VBELN", required = true)
    protected String vbeln;
    @XmlElement(name = "POSNN", required = true)
    protected String posnn;
    @XmlElement(name = "VBTYP_N", required = true)
    protected String vbtypn;
    @XmlElement(name = "MATNR", required = true)
    protected String matnr;
    @XmlElement(name = "CHARG", required = true)
    protected String charg;
    @XmlElement(name = "WERKS", required = true)
    protected String werks;
    @XmlElement(name = "TAQUI", required = true)
    protected String taqui;
    @XmlElement(name = "KZNTG", required = true)
    protected String kzntg;
    @XmlElement(name = "KZBRG", required = true)
    protected String kzbrg;
    @XmlElement(name = "KZVOL", required = true)
    protected String kzvol;
    @XmlElement(name = "LIANP", required = true)
    protected String lianp;
    @XmlElement(name = "LIPS_DEL", required = true)
    protected String lipsdel;
    @XmlElement(name = "INSMK", required = true)
    protected String insmk;
    @XmlElement(name = "BESTQ", required = true)
    protected String bestq;
    @XmlElement(name = "KZPOD", required = true)
    protected String kzpod;
    @XmlElement(name = "NDIFM", required = true)
    protected BigDecimal ndifm;
    @XmlElement(name = "ORPOS", required = true)
    protected String orpos;
    @XmlElement(name = "PIKMG", required = true)
    protected BigDecimal pikmg;
    @XmlElement(name = "SOBKZ", required = true)
    protected String sobkz;
    @XmlElement(name = "SONUM", required = true)
    protected String sonum;
    @XmlElement(name = "KZBEF", required = true)
    protected String kzbef;
    @XmlElement(name = "PLMIN", required = true)
    protected String plmin;
    @XmlElement(name = "FORCE_ORPOS_REDUCTION", required = true)
    protected String forceorposreduction;
    @XmlElement(name = "LFIMG", required = true)
    protected BigDecimal lfimg;
    @XmlElement(name = "LGMNG", required = true)
    protected BigDecimal lgmng;
    @XmlElement(name = "LFIMG_FLO")
    protected float lfimgflo;
    @XmlElement(name = "LGMNG_FLO")
    protected float lgmngflo;
    @XmlElement(name = "UMVKZ", required = true)
    protected BigDecimal umvkz;
    @XmlElement(name = "UMVKN", required = true)
    protected BigDecimal umvkn;
    @XmlElement(name = "UMREV")
    protected float umrev;
    @XmlElement(name = "AKMNG", required = true)
    protected String akmng;
    @XmlElement(name = "VRKME", required = true)
    protected String vrkme;
    @XmlElement(name = "MEINS", required = true)
    protected String meins;
    @XmlElement(name = "KZFME", required = true)
    protected String kzfme;
    @XmlElement(name = "BRGEW", required = true)
    protected BigDecimal brgew;
    @XmlElement(name = "NTGEW", required = true)
    protected BigDecimal ntgew;
    @XmlElement(name = "GEWEI", required = true)
    protected String gewei;
    @XmlElement(name = "VOLUM", required = true)
    protected BigDecimal volum;
    @XmlElement(name = "VOLEH", required = true)
    protected String voleh;
    @XmlElement(name = "LGPLA", required = true)
    protected String lgpla;
    @XmlElement(name = "LGTYP", required = true)
    protected String lgtyp;
    @XmlElement(name = "BWLVS", required = true)
    protected String bwlvs;
    @XmlElement(name = "XWMPP", required = true)
    protected String xwmpp;
    @XmlElement(name = "VFDAT", required = true)
    protected XMLGregorianCalendar vfdat;
    @XmlElement(name = "KZVFDAT", required = true)
    protected String kzvfdat;
    @XmlElement(name = "EBUMG_BME", required = true)
    protected BigDecimal ebumgbme;
    @XmlElement(name = "WMS_RFBEL", required = true)
    protected String wmsrfbel;
    @XmlElement(name = "WMS_RFPOS", required = true)
    protected String wmsrfpos;
    @XmlElement(name = "HSDAT", required = true)
    protected XMLGregorianCalendar hsdat;
    @XmlElement(name = "KZHSDAT", required = true)
    protected String kzhsdat;
    @XmlElement(name = "SPE_DIFF_POST_INDICATOR", required = true)
    protected String spediffpostindicator;
    @XmlElement(name = "SPE_EXCEPT_CODE", required = true)
    protected String speexceptcode;
    @XmlElement(name = "SPE_AUTH_NUMBER", required = true)
    protected String speauthnumber;
    @XmlElement(name = "SPE_RET_QTY_POST", required = true)
    protected BigDecimal speretqtypost;
    @XmlElement(name = "SPE_BASE_UOM", required = true)
    protected String spebaseuom;
    @XmlElement(name = "SPE_AUTH_COMPLET", required = true)
    protected String speauthcomplet;
    @XmlElement(name = "SPE_AUTH_COMPLET_F", required = true)
    protected String speauthcompletf;
    @XmlElement(name = "SPE_RET_CLOSING_INDICATOR", required = true)
    protected String speretclosingindicator;
    @XmlElement(name = "SPE_EXP_DATE_EXT_B", required = true)
    protected BigDecimal speexpdateextb;
    @XmlElement(name = "SPE_EXP_DATE_EXT", required = true)
    protected BigDecimal speexpdateext;
    @XmlElement(name = "SPE_MDIFF_INSMK", required = true)
    protected String spemdiffinsmk;
    @XmlElement(name = "SPE_IMWRK_ITM", required = true)
    protected String speimwrkitm;
    @XmlElement(name = "SPE_IMWRK_ITM_F", required = true)
    protected String speimwrkitmf;
    @XmlElement(name = "SPE_ORIG_SYS", required = true)
    protected String speorigsys;
    @XmlElement(name = "SPE_CUM_VEND", required = true)
    protected BigDecimal specumvend;
    @XmlElement(name = "SPE_CUM_ITQS", required = true)
    protected BigDecimal specumitqs;
    @XmlElement(name = "SPE_CUM_SA", required = true)
    protected BigDecimal specumsa;
    @XmlElement(name = "SPE_LIFEXPOS2", required = true)
    protected String spelifexpos2;
    @XmlElement(name = "SPE_VERSION", required = true)
    protected String speversion;
    @XmlElement(name = "SPE_DLV_QTY_FROM", required = true)
    protected BigDecimal spedlvqtyfrom;
    @XmlElement(name = "SPE_INMSK_F", required = true)
    protected String speinmskf;
    @XmlElement(name = "SPE_LIFEXPOS2_F", required = true)
    protected String spelifexpos2F;
    @XmlElement(name = "SPE_INB_ITM_UPD", required = true)
    protected String speinbitmupd;
    @XmlElement(name = "ARKTX", required = true)
    protected String arktx;
    @XmlElement(name = "LGBZO", required = true)
    protected String lgbzo;
    @XmlElement(name = "LGORT", required = true)
    protected String lgort;
    @XmlElement(name = "KZLGO", required = true)
    protected String kzlgo;
    @XmlElement(name = "LIFEXPOS", required = true)
    protected String lifexpos;
    @XmlElement(name = "KZLXP", required = true)
    protected String kzlxp;
    @XmlElement(name = "BWTAR", required = true)
    protected String bwtar;
    @XmlElement(name = "KZBWT", required = true)
    protected String kzbwt;
    @XmlElement(name = "MFRGR", required = true)
    protected String mfrgr;
    @XmlElement(name = "EAN11", required = true)
    protected String ean11;
    @XmlElement(name = "IDNLF", required = true)
    protected String idnlf;
    @XmlElement(name = "LICHN", required = true)
    protected String lichn;
    @XmlElement(name = "ABRVW", required = true)
    protected String abrvw;
    @XmlElement(name = "KZTXT", required = true)
    protected String kztxt;
    @XmlElement(name = "TXTOLD_DEL_ALL_LANG", required = true)
    protected String txtolddelalllang;
    @XmlElement(name = "MDIFF_BWART", required = true)
    protected String mdiffbwart;
    @XmlElement(name = "MDIFF_GRUND", required = true)
    protected String mdiffgrund;
    @XmlElement(name = "MDIFF_LGORT", required = true)
    protected String mdifflgort;
    @XmlElement(name = "MDIFF_AUTO_SPLIT", required = true)
    protected String mdiffautosplit;
    @XmlElement(name = "SPE_MDNUM_EWM", required = true)
    protected String spemdnumewm;
    @XmlElement(name = "SPE_MDITM_EWM", required = true)
    protected String spemditmewm;
    @XmlElement(name = "SPE_OMDNUM_EWM", required = true)
    protected String speomdnumewm;
    @XmlElement(name = "SPE_OMDITM_EWM", required = true)
    protected String speomditmewm;
    @XmlElement(name = "ORMNG", required = true)
    protected BigDecimal ormng;
    @XmlElement(name = "SPE_GTS_STOCK_TYPE", required = true)
    protected String spegtsstocktype;
    @XmlElement(name = "SPE_LIEFFZ", required = true)
    protected BigDecimal spelieffz;
    @XmlElement(name = "SPE_APO_QNTYFAC", required = true)
    protected BigDecimal speapoqntyfac;
    @XmlElement(name = "SPE_APO_QNTYDIV", required = true)
    protected BigDecimal speapoqntydiv;
    @XmlElement(name = "SPE_PICK_DENIAL", required = true)
    protected String spepickdenial;
    @XmlElement(name = "SERNR_DEL", required = true)
    protected String sernrdel;
    @XmlElement(name = "FLAG_DOCUB", required = true)
    protected String flagdocub;
    @XmlElement(name = "SPE_XNOCON", required = true)
    protected String spexnocon;
    @XmlElement(name = "SPE_EBUMG", required = true)
    protected BigDecimal speebumg;
    @XmlElement(name = "BWART", required = true)
    protected String bwart;
    @XmlElement(name = "SPE_FORCE_ORMNG_UPDATE", required = true)
    protected String speforceormngupdate;

    /**
     * Gets the value of the vbelnvl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBELNVL() {
        return vbelnvl;
    }

    /**
     * Sets the value of the vbelnvl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBELNVL(String value) {
        this.vbelnvl = value;
    }

    /**
     * Gets the value of the posnrvl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSNRVL() {
        return posnrvl;
    }

    /**
     * Sets the value of the posnrvl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSNRVL(String value) {
        this.posnrvl = value;
    }

    /**
     * Gets the value of the vbeln property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBELN() {
        return vbeln;
    }

    /**
     * Sets the value of the vbeln property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBELN(String value) {
        this.vbeln = value;
    }

    /**
     * Gets the value of the posnn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSNN() {
        return posnn;
    }

    /**
     * Sets the value of the posnn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSNN(String value) {
        this.posnn = value;
    }

    /**
     * Gets the value of the vbtypn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVBTYPN() {
        return vbtypn;
    }

    /**
     * Sets the value of the vbtypn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVBTYPN(String value) {
        this.vbtypn = value;
    }

    /**
     * Gets the value of the matnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * Sets the value of the matnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

    /**
     * Gets the value of the charg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCHARG() {
        return charg;
    }

    /**
     * Sets the value of the charg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCHARG(String value) {
        this.charg = value;
    }

    /**
     * Gets the value of the werks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKS() {
        return werks;
    }

    /**
     * Sets the value of the werks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKS(String value) {
        this.werks = value;
    }

    /**
     * Gets the value of the taqui property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTAQUI() {
        return taqui;
    }

    /**
     * Sets the value of the taqui property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTAQUI(String value) {
        this.taqui = value;
    }

    /**
     * Gets the value of the kzntg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZNTG() {
        return kzntg;
    }

    /**
     * Sets the value of the kzntg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZNTG(String value) {
        this.kzntg = value;
    }

    /**
     * Gets the value of the kzbrg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZBRG() {
        return kzbrg;
    }

    /**
     * Sets the value of the kzbrg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZBRG(String value) {
        this.kzbrg = value;
    }

    /**
     * Gets the value of the kzvol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZVOL() {
        return kzvol;
    }

    /**
     * Sets the value of the kzvol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZVOL(String value) {
        this.kzvol = value;
    }

    /**
     * Gets the value of the lianp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIANP() {
        return lianp;
    }

    /**
     * Sets the value of the lianp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIANP(String value) {
        this.lianp = value;
    }

    /**
     * Gets the value of the lipsdel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIPSDEL() {
        return lipsdel;
    }

    /**
     * Sets the value of the lipsdel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIPSDEL(String value) {
        this.lipsdel = value;
    }

    /**
     * Gets the value of the insmk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSMK() {
        return insmk;
    }

    /**
     * Sets the value of the insmk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSMK(String value) {
        this.insmk = value;
    }

    /**
     * Gets the value of the bestq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBESTQ() {
        return bestq;
    }

    /**
     * Sets the value of the bestq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBESTQ(String value) {
        this.bestq = value;
    }

    /**
     * Gets the value of the kzpod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZPOD() {
        return kzpod;
    }

    /**
     * Sets the value of the kzpod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZPOD(String value) {
        this.kzpod = value;
    }

    /**
     * Gets the value of the ndifm property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNDIFM() {
        return ndifm;
    }

    /**
     * Sets the value of the ndifm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNDIFM(BigDecimal value) {
        this.ndifm = value;
    }

    /**
     * Gets the value of the orpos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORPOS() {
        return orpos;
    }

    /**
     * Sets the value of the orpos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORPOS(String value) {
        this.orpos = value;
    }

    /**
     * Gets the value of the pikmg property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPIKMG() {
        return pikmg;
    }

    /**
     * Sets the value of the pikmg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPIKMG(BigDecimal value) {
        this.pikmg = value;
    }

    /**
     * Gets the value of the sobkz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSOBKZ() {
        return sobkz;
    }

    /**
     * Sets the value of the sobkz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSOBKZ(String value) {
        this.sobkz = value;
    }

    /**
     * Gets the value of the sonum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSONUM() {
        return sonum;
    }

    /**
     * Sets the value of the sonum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSONUM(String value) {
        this.sonum = value;
    }

    /**
     * Gets the value of the kzbef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZBEF() {
        return kzbef;
    }

    /**
     * Sets the value of the kzbef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZBEF(String value) {
        this.kzbef = value;
    }

    /**
     * Gets the value of the plmin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLMIN() {
        return plmin;
    }

    /**
     * Sets the value of the plmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLMIN(String value) {
        this.plmin = value;
    }

    /**
     * Gets the value of the forceorposreduction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFORCEORPOSREDUCTION() {
        return forceorposreduction;
    }

    /**
     * Sets the value of the forceorposreduction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFORCEORPOSREDUCTION(String value) {
        this.forceorposreduction = value;
    }

    /**
     * Gets the value of the lfimg property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLFIMG() {
        return lfimg;
    }

    /**
     * Sets the value of the lfimg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLFIMG(BigDecimal value) {
        this.lfimg = value;
    }

    /**
     * Gets the value of the lgmng property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLGMNG() {
        return lgmng;
    }

    /**
     * Sets the value of the lgmng property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLGMNG(BigDecimal value) {
        this.lgmng = value;
    }

    /**
     * Gets the value of the lfimgflo property.
     * 
     */
    public float getLFIMGFLO() {
        return lfimgflo;
    }

    /**
     * Sets the value of the lfimgflo property.
     * 
     */
    public void setLFIMGFLO(float value) {
        this.lfimgflo = value;
    }

    /**
     * Gets the value of the lgmngflo property.
     * 
     */
    public float getLGMNGFLO() {
        return lgmngflo;
    }

    /**
     * Sets the value of the lgmngflo property.
     * 
     */
    public void setLGMNGFLO(float value) {
        this.lgmngflo = value;
    }

    /**
     * Gets the value of the umvkz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUMVKZ() {
        return umvkz;
    }

    /**
     * Sets the value of the umvkz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUMVKZ(BigDecimal value) {
        this.umvkz = value;
    }

    /**
     * Gets the value of the umvkn property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUMVKN() {
        return umvkn;
    }

    /**
     * Sets the value of the umvkn property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUMVKN(BigDecimal value) {
        this.umvkn = value;
    }

    /**
     * Gets the value of the umrev property.
     * 
     */
    public float getUMREV() {
        return umrev;
    }

    /**
     * Sets the value of the umrev property.
     * 
     */
    public void setUMREV(float value) {
        this.umrev = value;
    }

    /**
     * Gets the value of the akmng property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAKMNG() {
        return akmng;
    }

    /**
     * Sets the value of the akmng property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAKMNG(String value) {
        this.akmng = value;
    }

    /**
     * Gets the value of the vrkme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVRKME() {
        return vrkme;
    }

    /**
     * Sets the value of the vrkme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVRKME(String value) {
        this.vrkme = value;
    }

    /**
     * Gets the value of the meins property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEINS() {
        return meins;
    }

    /**
     * Sets the value of the meins property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEINS(String value) {
        this.meins = value;
    }

    /**
     * Gets the value of the kzfme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZFME() {
        return kzfme;
    }

    /**
     * Sets the value of the kzfme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZFME(String value) {
        this.kzfme = value;
    }

    /**
     * Gets the value of the brgew property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBRGEW() {
        return brgew;
    }

    /**
     * Sets the value of the brgew property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBRGEW(BigDecimal value) {
        this.brgew = value;
    }

    /**
     * Gets the value of the ntgew property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNTGEW() {
        return ntgew;
    }

    /**
     * Sets the value of the ntgew property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNTGEW(BigDecimal value) {
        this.ntgew = value;
    }

    /**
     * Gets the value of the gewei property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGEWEI() {
        return gewei;
    }

    /**
     * Sets the value of the gewei property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGEWEI(String value) {
        this.gewei = value;
    }

    /**
     * Gets the value of the volum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVOLUM() {
        return volum;
    }

    /**
     * Sets the value of the volum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVOLUM(BigDecimal value) {
        this.volum = value;
    }

    /**
     * Gets the value of the voleh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVOLEH() {
        return voleh;
    }

    /**
     * Sets the value of the voleh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVOLEH(String value) {
        this.voleh = value;
    }

    /**
     * Gets the value of the lgpla property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLGPLA() {
        return lgpla;
    }

    /**
     * Sets the value of the lgpla property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLGPLA(String value) {
        this.lgpla = value;
    }

    /**
     * Gets the value of the lgtyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLGTYP() {
        return lgtyp;
    }

    /**
     * Sets the value of the lgtyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLGTYP(String value) {
        this.lgtyp = value;
    }

    /**
     * Gets the value of the bwlvs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBWLVS() {
        return bwlvs;
    }

    /**
     * Sets the value of the bwlvs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBWLVS(String value) {
        this.bwlvs = value;
    }

    /**
     * Gets the value of the xwmpp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXWMPP() {
        return xwmpp;
    }

    /**
     * Sets the value of the xwmpp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXWMPP(String value) {
        this.xwmpp = value;
    }

    /**
     * Gets the value of the vfdat property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVFDAT() {
        return vfdat;
    }

    /**
     * Sets the value of the vfdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVFDAT(XMLGregorianCalendar value) {
        this.vfdat = value;
    }

    /**
     * Gets the value of the kzvfdat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZVFDAT() {
        return kzvfdat;
    }

    /**
     * Sets the value of the kzvfdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZVFDAT(String value) {
        this.kzvfdat = value;
    }

    /**
     * Gets the value of the ebumgbme property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEBUMGBME() {
        return ebumgbme;
    }

    /**
     * Sets the value of the ebumgbme property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEBUMGBME(BigDecimal value) {
        this.ebumgbme = value;
    }

    /**
     * Gets the value of the wmsrfbel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWMSRFBEL() {
        return wmsrfbel;
    }

    /**
     * Sets the value of the wmsrfbel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWMSRFBEL(String value) {
        this.wmsrfbel = value;
    }

    /**
     * Gets the value of the wmsrfpos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWMSRFPOS() {
        return wmsrfpos;
    }

    /**
     * Sets the value of the wmsrfpos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWMSRFPOS(String value) {
        this.wmsrfpos = value;
    }

    /**
     * Gets the value of the hsdat property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHSDAT() {
        return hsdat;
    }

    /**
     * Sets the value of the hsdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHSDAT(XMLGregorianCalendar value) {
        this.hsdat = value;
    }

    /**
     * Gets the value of the kzhsdat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZHSDAT() {
        return kzhsdat;
    }

    /**
     * Sets the value of the kzhsdat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZHSDAT(String value) {
        this.kzhsdat = value;
    }

    /**
     * Gets the value of the spediffpostindicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEDIFFPOSTINDICATOR() {
        return spediffpostindicator;
    }

    /**
     * Sets the value of the spediffpostindicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEDIFFPOSTINDICATOR(String value) {
        this.spediffpostindicator = value;
    }

    /**
     * Gets the value of the speexceptcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEEXCEPTCODE() {
        return speexceptcode;
    }

    /**
     * Sets the value of the speexceptcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEEXCEPTCODE(String value) {
        this.speexceptcode = value;
    }

    /**
     * Gets the value of the speauthnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEAUTHNUMBER() {
        return speauthnumber;
    }

    /**
     * Sets the value of the speauthnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEAUTHNUMBER(String value) {
        this.speauthnumber = value;
    }

    /**
     * Gets the value of the speretqtypost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPERETQTYPOST() {
        return speretqtypost;
    }

    /**
     * Sets the value of the speretqtypost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPERETQTYPOST(BigDecimal value) {
        this.speretqtypost = value;
    }

    /**
     * Gets the value of the spebaseuom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEBASEUOM() {
        return spebaseuom;
    }

    /**
     * Sets the value of the spebaseuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEBASEUOM(String value) {
        this.spebaseuom = value;
    }

    /**
     * Gets the value of the speauthcomplet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEAUTHCOMPLET() {
        return speauthcomplet;
    }

    /**
     * Sets the value of the speauthcomplet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEAUTHCOMPLET(String value) {
        this.speauthcomplet = value;
    }

    /**
     * Gets the value of the speauthcompletf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEAUTHCOMPLETF() {
        return speauthcompletf;
    }

    /**
     * Sets the value of the speauthcompletf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEAUTHCOMPLETF(String value) {
        this.speauthcompletf = value;
    }

    /**
     * Gets the value of the speretclosingindicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPERETCLOSINGINDICATOR() {
        return speretclosingindicator;
    }

    /**
     * Sets the value of the speretclosingindicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPERETCLOSINGINDICATOR(String value) {
        this.speretclosingindicator = value;
    }

    /**
     * Gets the value of the speexpdateextb property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEEXPDATEEXTB() {
        return speexpdateextb;
    }

    /**
     * Sets the value of the speexpdateextb property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEEXPDATEEXTB(BigDecimal value) {
        this.speexpdateextb = value;
    }

    /**
     * Gets the value of the speexpdateext property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEEXPDATEEXT() {
        return speexpdateext;
    }

    /**
     * Sets the value of the speexpdateext property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEEXPDATEEXT(BigDecimal value) {
        this.speexpdateext = value;
    }

    /**
     * Gets the value of the spemdiffinsmk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEMDIFFINSMK() {
        return spemdiffinsmk;
    }

    /**
     * Sets the value of the spemdiffinsmk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEMDIFFINSMK(String value) {
        this.spemdiffinsmk = value;
    }

    /**
     * Gets the value of the speimwrkitm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEIMWRKITM() {
        return speimwrkitm;
    }

    /**
     * Sets the value of the speimwrkitm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEIMWRKITM(String value) {
        this.speimwrkitm = value;
    }

    /**
     * Gets the value of the speimwrkitmf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEIMWRKITMF() {
        return speimwrkitmf;
    }

    /**
     * Sets the value of the speimwrkitmf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEIMWRKITMF(String value) {
        this.speimwrkitmf = value;
    }

    /**
     * Gets the value of the speorigsys property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEORIGSYS() {
        return speorigsys;
    }

    /**
     * Sets the value of the speorigsys property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEORIGSYS(String value) {
        this.speorigsys = value;
    }

    /**
     * Gets the value of the specumvend property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPECUMVEND() {
        return specumvend;
    }

    /**
     * Sets the value of the specumvend property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPECUMVEND(BigDecimal value) {
        this.specumvend = value;
    }

    /**
     * Gets the value of the specumitqs property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPECUMITQS() {
        return specumitqs;
    }

    /**
     * Sets the value of the specumitqs property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPECUMITQS(BigDecimal value) {
        this.specumitqs = value;
    }

    /**
     * Gets the value of the specumsa property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPECUMSA() {
        return specumsa;
    }

    /**
     * Sets the value of the specumsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPECUMSA(BigDecimal value) {
        this.specumsa = value;
    }

    /**
     * Gets the value of the spelifexpos2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPELIFEXPOS2() {
        return spelifexpos2;
    }

    /**
     * Sets the value of the spelifexpos2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPELIFEXPOS2(String value) {
        this.spelifexpos2 = value;
    }

    /**
     * Gets the value of the speversion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEVERSION() {
        return speversion;
    }

    /**
     * Sets the value of the speversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEVERSION(String value) {
        this.speversion = value;
    }

    /**
     * Gets the value of the spedlvqtyfrom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEDLVQTYFROM() {
        return spedlvqtyfrom;
    }

    /**
     * Sets the value of the spedlvqtyfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEDLVQTYFROM(BigDecimal value) {
        this.spedlvqtyfrom = value;
    }

    /**
     * Gets the value of the speinmskf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEINMSKF() {
        return speinmskf;
    }

    /**
     * Sets the value of the speinmskf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEINMSKF(String value) {
        this.speinmskf = value;
    }

    /**
     * Gets the value of the spelifexpos2F property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPELIFEXPOS2F() {
        return spelifexpos2F;
    }

    /**
     * Sets the value of the spelifexpos2F property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPELIFEXPOS2F(String value) {
        this.spelifexpos2F = value;
    }

    /**
     * Gets the value of the speinbitmupd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEINBITMUPD() {
        return speinbitmupd;
    }

    /**
     * Sets the value of the speinbitmupd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEINBITMUPD(String value) {
        this.speinbitmupd = value;
    }

    /**
     * Gets the value of the arktx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getARKTX() {
        return arktx;
    }

    /**
     * Sets the value of the arktx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setARKTX(String value) {
        this.arktx = value;
    }

    /**
     * Gets the value of the lgbzo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLGBZO() {
        return lgbzo;
    }

    /**
     * Sets the value of the lgbzo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLGBZO(String value) {
        this.lgbzo = value;
    }

    /**
     * Gets the value of the lgort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLGORT() {
        return lgort;
    }

    /**
     * Sets the value of the lgort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLGORT(String value) {
        this.lgort = value;
    }

    /**
     * Gets the value of the kzlgo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZLGO() {
        return kzlgo;
    }

    /**
     * Sets the value of the kzlgo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZLGO(String value) {
        this.kzlgo = value;
    }

    /**
     * Gets the value of the lifexpos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLIFEXPOS() {
        return lifexpos;
    }

    /**
     * Sets the value of the lifexpos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLIFEXPOS(String value) {
        this.lifexpos = value;
    }

    /**
     * Gets the value of the kzlxp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZLXP() {
        return kzlxp;
    }

    /**
     * Sets the value of the kzlxp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZLXP(String value) {
        this.kzlxp = value;
    }

    /**
     * Gets the value of the bwtar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBWTAR() {
        return bwtar;
    }

    /**
     * Sets the value of the bwtar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBWTAR(String value) {
        this.bwtar = value;
    }

    /**
     * Gets the value of the kzbwt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZBWT() {
        return kzbwt;
    }

    /**
     * Sets the value of the kzbwt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZBWT(String value) {
        this.kzbwt = value;
    }

    /**
     * Gets the value of the mfrgr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMFRGR() {
        return mfrgr;
    }

    /**
     * Sets the value of the mfrgr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMFRGR(String value) {
        this.mfrgr = value;
    }

    /**
     * Gets the value of the ean11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAN11() {
        return ean11;
    }

    /**
     * Sets the value of the ean11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAN11(String value) {
        this.ean11 = value;
    }

    /**
     * Gets the value of the idnlf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDNLF() {
        return idnlf;
    }

    /**
     * Sets the value of the idnlf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDNLF(String value) {
        this.idnlf = value;
    }

    /**
     * Gets the value of the lichn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLICHN() {
        return lichn;
    }

    /**
     * Sets the value of the lichn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLICHN(String value) {
        this.lichn = value;
    }

    /**
     * Gets the value of the abrvw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getABRVW() {
        return abrvw;
    }

    /**
     * Sets the value of the abrvw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setABRVW(String value) {
        this.abrvw = value;
    }

    /**
     * Gets the value of the kztxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKZTXT() {
        return kztxt;
    }

    /**
     * Sets the value of the kztxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKZTXT(String value) {
        this.kztxt = value;
    }

    /**
     * Gets the value of the txtolddelalllang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTXTOLDDELALLLANG() {
        return txtolddelalllang;
    }

    /**
     * Sets the value of the txtolddelalllang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTXTOLDDELALLLANG(String value) {
        this.txtolddelalllang = value;
    }

    /**
     * Gets the value of the mdiffbwart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDIFFBWART() {
        return mdiffbwart;
    }

    /**
     * Sets the value of the mdiffbwart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDIFFBWART(String value) {
        this.mdiffbwart = value;
    }

    /**
     * Gets the value of the mdiffgrund property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDIFFGRUND() {
        return mdiffgrund;
    }

    /**
     * Sets the value of the mdiffgrund property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDIFFGRUND(String value) {
        this.mdiffgrund = value;
    }

    /**
     * Gets the value of the mdifflgort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDIFFLGORT() {
        return mdifflgort;
    }

    /**
     * Sets the value of the mdifflgort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDIFFLGORT(String value) {
        this.mdifflgort = value;
    }

    /**
     * Gets the value of the mdiffautosplit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDIFFAUTOSPLIT() {
        return mdiffautosplit;
    }

    /**
     * Sets the value of the mdiffautosplit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDIFFAUTOSPLIT(String value) {
        this.mdiffautosplit = value;
    }

    /**
     * Gets the value of the spemdnumewm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEMDNUMEWM() {
        return spemdnumewm;
    }

    /**
     * Sets the value of the spemdnumewm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEMDNUMEWM(String value) {
        this.spemdnumewm = value;
    }

    /**
     * Gets the value of the spemditmewm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEMDITMEWM() {
        return spemditmewm;
    }

    /**
     * Sets the value of the spemditmewm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEMDITMEWM(String value) {
        this.spemditmewm = value;
    }

    /**
     * Gets the value of the speomdnumewm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEOMDNUMEWM() {
        return speomdnumewm;
    }

    /**
     * Sets the value of the speomdnumewm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEOMDNUMEWM(String value) {
        this.speomdnumewm = value;
    }

    /**
     * Gets the value of the speomditmewm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEOMDITMEWM() {
        return speomditmewm;
    }

    /**
     * Sets the value of the speomditmewm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEOMDITMEWM(String value) {
        this.speomditmewm = value;
    }

    /**
     * Gets the value of the ormng property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getORMNG() {
        return ormng;
    }

    /**
     * Sets the value of the ormng property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setORMNG(BigDecimal value) {
        this.ormng = value;
    }

    /**
     * Gets the value of the spegtsstocktype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEGTSSTOCKTYPE() {
        return spegtsstocktype;
    }

    /**
     * Sets the value of the spegtsstocktype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEGTSSTOCKTYPE(String value) {
        this.spegtsstocktype = value;
    }

    /**
     * Gets the value of the spelieffz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPELIEFFZ() {
        return spelieffz;
    }

    /**
     * Sets the value of the spelieffz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPELIEFFZ(BigDecimal value) {
        this.spelieffz = value;
    }

    /**
     * Gets the value of the speapoqntyfac property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEAPOQNTYFAC() {
        return speapoqntyfac;
    }

    /**
     * Sets the value of the speapoqntyfac property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEAPOQNTYFAC(BigDecimal value) {
        this.speapoqntyfac = value;
    }

    /**
     * Gets the value of the speapoqntydiv property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEAPOQNTYDIV() {
        return speapoqntydiv;
    }

    /**
     * Sets the value of the speapoqntydiv property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEAPOQNTYDIV(BigDecimal value) {
        this.speapoqntydiv = value;
    }

    /**
     * Gets the value of the spepickdenial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEPICKDENIAL() {
        return spepickdenial;
    }

    /**
     * Sets the value of the spepickdenial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEPICKDENIAL(String value) {
        this.spepickdenial = value;
    }

    /**
     * Gets the value of the sernrdel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSERNRDEL() {
        return sernrdel;
    }

    /**
     * Sets the value of the sernrdel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSERNRDEL(String value) {
        this.sernrdel = value;
    }

    /**
     * Gets the value of the flagdocub property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAGDOCUB() {
        return flagdocub;
    }

    /**
     * Sets the value of the flagdocub property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAGDOCUB(String value) {
        this.flagdocub = value;
    }

    /**
     * Gets the value of the spexnocon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEXNOCON() {
        return spexnocon;
    }

    /**
     * Sets the value of the spexnocon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEXNOCON(String value) {
        this.spexnocon = value;
    }

    /**
     * Gets the value of the speebumg property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSPEEBUMG() {
        return speebumg;
    }

    /**
     * Sets the value of the speebumg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSPEEBUMG(BigDecimal value) {
        this.speebumg = value;
    }

    /**
     * Gets the value of the bwart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBWART() {
        return bwart;
    }

    /**
     * Sets the value of the bwart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBWART(String value) {
        this.bwart = value;
    }

    /**
     * Gets the value of the speforceormngupdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPEFORCEORMNGUPDATE() {
        return speforceormngupdate;
    }

    /**
     * Sets the value of the speforceormngupdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPEFORCEORMNGUPDATE(String value) {
        this.speforceormngupdate = value;
    }

}
