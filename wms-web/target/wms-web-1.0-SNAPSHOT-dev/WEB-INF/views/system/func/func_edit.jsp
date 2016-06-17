<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>功能编辑</title>
</head>
<body>
<!-- start PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_func" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" value="${func.id}"/>
        <input type="hidden" name="parentFuncId" value="${func.parentFuncId}"/>
        <c:choose>
            <c:when test="${isRoot}">
                <div class="form-group">
                    <label class="control-label col-md-3">功能名称：</label>
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
                                ${parentFuncName}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">功能名称<span class="required">*</span></label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="funcName" value="${func.funcName}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">资源链接</label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="actionUrl" value="${func.actionUrl}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">图标标识</label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="iconUrl" value="${func.iconUrl}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">排序号</label>
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="funcOrder" value="${func.funcOrder}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">是否叶子节点</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${func.isLeaf==1}">
                                    <label class="radio-inline"><input type="radio" name="isLeaf" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="isLeaf" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="isLeaf" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="isLeaf" value="0" checked/>否</label>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-md-3">是否启用</label>
                    <div class="col-md-6">
                        <div class="radio-list">
                            <c:choose>
                                <c:when test="${func.isEffective==1}">
                                    <label class="radio-inline"><input type="radio" name="isEffective" value="1" checked/>是</label>
                                    <label class="radio-inline"><input type="radio" name="isEffective" value="0"/>否</label>
                                </c:when>
                                <c:otherwise>
                                    <label class="radio-inline"><input type="radio" name="isEffective" value="1"/>是</label>
                                    <label class="radio-inline"><input type="radio" name="isEffective" value="0" checked/>否</label>
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
                    <button type="button" id="btn_func_save" class="btn blue">保存</button>
                </c:if>
                <c:if test="${isRoot or (!isNew and func.isLeaf==0)}">
                    <button type="button" id="btn_func_add" class="btn blue">增加子节点</button>
                </c:if>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>