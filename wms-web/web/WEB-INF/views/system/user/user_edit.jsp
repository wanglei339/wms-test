<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>用户编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_user" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${user.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">登录名<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="loginName" value="${user.loginName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">用户名<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="userName" value="${user.userName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">密码</label>
            <div class="col-md-4">
                <input type="password" class="form-control" name="pazzword" value=""/>
                <p class="help-block">默认密码:123456</p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">角色<span class="required">*</span></label>
            <div class="col-md-4">
                <div class="checkbox-list" style="max-height: 200px;overflow-y: auto;" data-error-container="#div_error_role">
                    <c:forEach var="item" items="${roleList}">
                        <c:set var="isChecked" value="0"/>
                        <c:forEach var="tmp" items="${user.sysUserRoleRelationList}">
                            <c:if test="${tmp.roleId==item.id}">
                                <c:set var="isChecked" value="1"/>
                            </c:if>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${isChecked == 1}">
                                <label>
                                    <input type="checkbox" name="roles" value="${item.id}" checked/>${item.roleName}
                                </label>
                            </c:when>
                            <c:otherwise>
                                <label>
                                    <input type="checkbox" name="roles" value="${item.id}"/>${item.roleName}
                                </label>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <span class="help-block">至少选择一项</span>
                <div id="div_error_role"></div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_user_save" class="btn blue">保存</button>
                <button type="button" id="btn_user_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>