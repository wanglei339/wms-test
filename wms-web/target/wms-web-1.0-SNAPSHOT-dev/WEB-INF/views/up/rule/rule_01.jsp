<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>规则条件编辑</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_con" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="verId" value="${verId}"/>
        <input type="hidden" name="ruleId" value="${ruleId}"/>
        <input type="hidden" name="id" value="${con.id}"/>
        <div class="form-group">
            <label class="control-label col-md-4">大于版本<span class="required">*</span></label>
            <div class="col-md-6">
                <input type="text" class="form-control" id="minValueName_01" name="minValueName" value="${con.minValueName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">小于版本<span class="required">*</span></label>
            <div class="col-md-6">
                <input type="text" class="form-control" id="maxValueName_01" name="maxValueName" value="${con.maxValueName}"/>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>