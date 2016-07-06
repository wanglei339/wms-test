<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>配置页面编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_page" class="form-horizontal" action="#">
    <div class="form-body">
        <h3 class="form-section">页面信息</h3>
        <input type="hidden" name="id" value="${page.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">页面编码<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pageCode" value="${page.pageCode}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">页面名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pageName" value="${page.pageName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">内容类型</label>
            <div class="col-md-4">
                <c:choose>
                    <c:when test="${isNew}">
                        <select class="form-control" name="conTypeCode">
                            <option value="0">无</option>
                            <c:forEach var="dict" items="${dictList}">
                                <option value="${dict.id}" <c:if test="${page.conTypeCode eq dict.id}">selected</c:if>>${dict.dictName}</option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <p class="form-control-static">
                            <c:forEach var="dict" items="${dictList}">
                                <c:if test="${page.conTypeCode eq dict.id}">${dict.dictName}</c:if>
                            </c:forEach>
                        </p>
                    </c:otherwise>
                </c:choose>
                <div class="help-block">
                    数据分类用到的字典，保存后不可修改。
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">缓存类型</label>
            <div class="col-md-4">
                <c:choose>
                    <c:when test="${isNew}">
                        <select class="form-control" name="cacheType">
                            <option value="1" <c:if test="${page.cacheType == 1}">selected</c:if>>按页面</option>
                            <option value="2" <c:if test="${page.cacheType == 2}">selected</c:if>>按内容类型</option>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <p class="form-control-static">
                            <c:if test="${page.cacheType == 1}">按页面</c:if>
                            <c:if test="${page.cacheType == 2}">按内容类型</c:if>
                        </p>
                    </c:otherwise>
                </c:choose>
                <div class="help-block">
                    1、按页面缓存：将该页面下所有数据保存到一个zset结构。</br>
                    2、按内容类型缓存：将该页面下数据按内容分类保存到多个zset结构。</br>
                    3、因数据结构不同，保存后不可修改。
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_page_save" class="btn blue">保存</button>
                <button type="button" id="btn_page_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>