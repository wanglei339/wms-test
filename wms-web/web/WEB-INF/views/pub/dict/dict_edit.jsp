<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>词典编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_dict" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${dict.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">类型</label>
            <div class="col-md-4">
                <c:choose>
                    <c:when test="${isNew}">
                        <select class="form-control" name="dictType">
                            <option value="1"
                                    <c:if test="${dict.dictType == 1}">selected</c:if> >列表
                            </option>
                            <option value="2"
                                    <c:if test="${dict.dictType == 2}">selected</c:if> >树型
                            </option>
                        </select>
                        <span class="help-block">因数据结构不同，保存后不可修改。</span>
                    </c:when>
                    <c:otherwise>
                        <p class="form-control-static">
                            <c:if test="${dict.dictType == 1}">列表</c:if>
                            <c:if test="${dict.dictType == 2}">树型</c:if>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">编码<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="dictCode" value="${dict.dictCode}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="dictName" value="${dict.dictName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">是否启用</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <c:choose>
                        <c:when test="${dict.status==1}">
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
                <button type="button" id="btn_dict_save" class="btn blue">保存</button>
                <button type="button" id="btn_dict_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>