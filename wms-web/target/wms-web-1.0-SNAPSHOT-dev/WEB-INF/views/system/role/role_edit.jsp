<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>角色编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_role" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${role.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">角色名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="roleName" value="${role.roleName}"/>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_role_save" class="btn blue">保存</button>
                <button type="button" id="btn_role_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>