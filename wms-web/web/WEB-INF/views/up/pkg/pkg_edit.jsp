<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>升级包编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_pkg" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${pkg.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">包编码</label>
            <div class="col-md-4">
                <p class="form-control-static">${pkg.appKey}</p>
                <div class="help-block">
                    保存后系统自动生成，不可修改。
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">包名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pkgName" value="${pkg.pkgName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">包路径</label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pkgPath" value="${pkg.pkgPath}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">应用</label>
            <div class="col-md-4">
                <select class="form-control" name="appId">
                    <c:forEach var="item" items="${appList}">
                        <option value="${item.id}" <c:if test="${pkg.appId==item.id}">selected</c:if>>${item.appName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">系统</label>
            <div class="col-md-4">
                <select class="form-control" name="osId">
                    <c:forEach var="item" items="${osList}">
                        <option value="${item.id}" <c:if test="${pkg.osId==item.id}">selected</c:if>>${item.osName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">渠道</label>
            <div class="col-md-4">
                <select class="form-control" name="chnId">
                    <c:forEach var="item" items="${chnList}">
                        <option value="${item.id}" <c:if test="${pkg.chnId==item.id}">selected</c:if>>${item.chnName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">型号</label>
            <div class="col-md-4">
                <select class="form-control" name="modId">
                    <c:forEach var="item" items="${modList}">
                        <option value="${item.id}" <c:if test="${pkg.modId==item.id}">selected</c:if>>${item.modName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">类型</label>
            <div class="col-md-4">
                <select class="form-control" name="pkgType">
                    <option value="1" <c:if test="${pkg.pkgType==1}">selected</c:if>>基线版</option>
                    <option value="2" <c:if test="${pkg.pkgType==2}">selected</c:if>>预制版</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">是否启用</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <c:choose>
                        <c:when test="${pkg.status==1}">
                            <label class="radio-inline"><input type="radio" name="status" value="1" checked/>是</label>
                            <label class="radio-inline"><input type="radio" name="status" value="0"/>否</label>
                        </c:when>
                        <c:otherwise>
                            <label class="radio-inline"><input type="radio" name="status" value="1"/>是</label>
                            <label class="radio-inline"><input type="radio" name="status" value="0" checked/>否</label>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_pkg_save" class="btn blue">保存</button>
                <button type="button" id="btn_pkg_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>