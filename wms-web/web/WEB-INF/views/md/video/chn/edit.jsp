<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_page" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${chn.id}"/>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">频道名称<span class="required">*</span></label>
                    <div class="col-md-8">
                        <input type="text" class="form-control" name="chnName" value="${chn.chnName}"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">显示顺序</label>
                    <div class="col-md-8">
                        <input type="text" class="form-control" name="showOrder" value="${chn.showOrder}"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">频道编码</label>
                    <div class="col-md-8">
                        <input type="text" class="form-control" name="chnCode" value="${chn.chnCode}"/>
                    </div>
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
<!-- END PAGE CONTAINER-->
</body>
</html>