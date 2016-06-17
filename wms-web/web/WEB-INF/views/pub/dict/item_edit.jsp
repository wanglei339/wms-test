<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>词典明细编辑</title>
</head>
<body>
<!-- start PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_item" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${item.id}"/>
        <input type="hidden" name="did" value="${item.did}"/>
        <input type="hidden" name="pid" value="${item.pid}"/>
        <c:choose>
            <c:when test="${isRoot}">
                <div class="form-group">
                    <label class="control-label col-md-3">名称：</label>
                    <div class="col-md-6">
                        <p class="form-control-static">根节点</p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label class="control-label col-md-3">父节点</label>
                    <div class="col-md-6">
                        <p class="form-control-static">
                                ${parentItemName}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">值<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="itemValue" value="${item.itemValue}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="itemName" value="${item.itemName}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">排序号</label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="itemOrder" value="${item.itemOrder}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">是否启用</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${item.status==1}">
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
            </c:otherwise>
        </c:choose>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <c:if test="${!isRoot}">
                    <button type="button" id="btn_item_save" class="btn blue">保存</button>
                </c:if>
                <c:if test="${isRoot or (!isNew and dict.dictType==2)}">
                    <button type="button" id="btn_item_add" class="btn blue">增加子节点</button>
                </c:if>
                <button type="button" id="btn_item_back" class="btn blue">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>