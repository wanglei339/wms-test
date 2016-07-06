<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>页面显示设置界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_show" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${show.id}"/>
        <input type="hidden" name="pageCode" value="${show.pageCode}"/>
        <h3 class="form-section">页面名称：${page.pageName}</h3>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">标题名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conTitleName" value="${show.conTitleName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conTitleShow==1}">
                                    <label class="radio-inline"><input type="radio" name="conTitleShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTitleShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conTitleShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTitleShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conTitleRequired==1}">
                                    <label class="radio-inline"><input type="radio" name="conTitleRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTitleRequired" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conTitleRequired" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTitleRequired" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">链接名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conUrlName" value="${show.conUrlName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conUrlShow==1}">
                                    <label class="radio-inline"><input type="radio" name="conUrlShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conUrlShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conUrlShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conUrlShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conUrlRequired==1}">
                                    <label class="radio-inline"><input type="radio" name="conUrlRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conUrlRequired" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conUrlRequired" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conUrlRequired" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">内容类型名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conTypeName" value="${show.conTypeName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${page.cacheType==2}">
                                    <label class="radio-inline"><input type="radio" name="conTypeShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTypeShow" value="0" disabled/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${show.conTypeShow==1}">
                                            <label class="radio-inline"><input type="radio" name="conTypeShow" value="1" checked/>是</label>
                                            <label class="radio-inline"><input type="radio" name="conTypeShow" value="0"/>否</label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="radio-inline"><input type="radio" name="conTypeShow" value="1"/>是</label>
                                            <label class="radio-inline"><input type="radio" name="conTypeShow" value="0" checked/>否</label>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${page.cacheType==2}">
                                    <label class="radio-inline"><input type="radio" name="conTypeRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conTypeRequired" value="0" disabled/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${show.conTypeRequired==1}">
                                            <label class="radio-inline"><input type="radio" name="conTypeRequired" value="1" checked/>是</label>
                                            <label class="radio-inline"><input type="radio" name="conTypeRequired" value="0"/>否</label>
                                        </c:when>
                                        <c:otherwise>
                                            <label class="radio-inline"><input type="radio" name="conTypeRequired" value="1"/>是</label>
                                            <label class="radio-inline"><input type="radio" name="conTypeRequired" value="0" checked/>否</label>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">内容ID名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conIdName" value="${show.conIdName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conIdShow==1}">
                                    <label class="radio-inline"><input type="radio" name="conIdShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conIdShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conIdShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conIdShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conIdRequired==1}">
                                    <label class="radio-inline"><input type="radio" name="conIdRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conIdRequired" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conIdRequired" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conIdRequired" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">参数1名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conP1Name" value="${show.conP1Name}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP1Show==1}">
                                    <label class="radio-inline"><input type="radio" name="conP1Show" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP1Show" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP1Show" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP1Show" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP1Required==1}">
                                    <label class="radio-inline"><input type="radio" name="conP1Required" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP1Required" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP1Required" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP1Required" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">参数2名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conP2Name" value="${show.conP2Name}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP2Show==1}">
                                    <label class="radio-inline"><input type="radio" name="conP2Show" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP2Show" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP2Show" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP2Show" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP2Required==1}">
                                    <label class="radio-inline"><input type="radio" name="conP2Required" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP2Required" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP2Required" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP2Required" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">参数3名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conP3Name" value="${show.conP3Name}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP3Show==1}">
                                    <label class="radio-inline"><input type="radio" name="conP3Show" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP3Show" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP3Show" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP3Show" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP3Required==1}">
                                    <label class="radio-inline"><input type="radio" name="conP3Required" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP3Required" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP3Required" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP3Required" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">参数4名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conP4Name" value="${show.conP4Name}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP4Show==1}">
                                    <label class="radio-inline"><input type="radio" name="conP4Show" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP4Show" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP4Show" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP4Show" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP4Required==1}">
                                    <label class="radio-inline"><input type="radio" name="conP4Required" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP4Required" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP4Required" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP4Required" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">参数5名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conP5Name" value="${show.conP5Name}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP5Show==1}">
                                    <label class="radio-inline"><input type="radio" name="conP5Show" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP5Show" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP5Show" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP5Show" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conP5Required==1}">
                                    <label class="radio-inline"><input type="radio" name="conP5Required" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP5Required" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conP5Required" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conP5Required" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">排序名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conOrderName" value="${show.conOrderName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conOrderShow==1}">
                                    <label class="radio-inline"><input type="radio" name="conOrderShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conOrderShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conOrderShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conOrderShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conOrderRequired==1}">
                                    <label class="radio-inline"><input type="radio" name="conOrderRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conOrderRequired" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conOrderRequired" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conOrderRequired" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">描述名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="conDescName" value="${show.conDescName}"/>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conDescShow==1}">
                                    <label class="radio-inline"><input type="radio" name="conDescShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conDescShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conDescShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conDescShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">是否必填</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.conDescRequired==1}">
                                    <label class="radio-inline"><input type="radio" name="conDescRequired" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conDescRequired" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="conDescRequired" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="conDescRequired" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label col-md-6">图片是否显示</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${show.picShow==1}">
                                    <label class="radio-inline"><input type="radio" name="picShow" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="picShow" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="picShow" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="picShow" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_show_save" class="btn blue">保存</button>
                <button type="button" id="btn_show_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>