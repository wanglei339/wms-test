<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>操作系统编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_os" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${os.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">操作系统编码<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="osCode" value="${os.osCode}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">操作系统名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="osName" value="${os.osName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">操作系统类型</label>
            <div class="col-md-4">
                <select class="form-control" name="osType">
                    <option value="1" <c:if test="${os.osType==1}">selected</c:if>>PC端</option>
                    <option value="2" <c:if test="${os.osType==2}">selected</c:if>>移动端</option>
                    <option value="3" <c:if test="${os.osType==3}">selected</c:if>>TV端</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">是否启用</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <c:choose>
                        <c:when test="${os.status==1}">
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
                <button type="button" id="btn_os_save" class="btn blue">保存</button>
                <button type="button" id="btn_os_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>