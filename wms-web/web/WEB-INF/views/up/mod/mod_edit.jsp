<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>型号编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_mod" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${upMod.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">型号编码<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="modCode" value="${upMod.modCode}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">型号名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="modName" value="${upMod.modName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">是否启用</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <c:choose>
                        <c:when test="${upMod.status==1}">
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
                <button type="button" id="btn_mod_save" class="btn blue">保存</button>
                <button type="button" id="btn_mod_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>