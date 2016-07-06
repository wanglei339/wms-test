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
        <input type="hidden" name="id" value="${tag.id}"/>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">业务类型<span class="required">*</span></label>
                    <div class="col-md-8">
                        <c:if test="${tag.id == null}">
                            <select name="busType" id="busType"  class="form-control">
                                <option value="">请选择...</option>
                                <option value="1">视频标签</option>
                                <option value="2">音乐标签</option>
                                <option value="3">明星标签</option>
                            </select>
                        </c:if>
                        <c:if test="${tag.id != null}">
                            <p class="form-control-static">
                                <c:choose>
                                    <c:when test="${tag.busType==1}">
                                        视频标签
                                    </c:when>
                                    <c:when test="${tag.busType==2}">
                                        音乐标签
                                    </c:when>
                                    <c:when test="${tag.busType==3}">
                                        明星标签
                                    </c:when>
                                    <c:otherwise>

                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">标签分类<span class="required">*</span></label>
                    <div class="col-md-8">
                        <c:if test="${tag.id == null}">
                            <select name="tagTypeId" id="tagTypeId"  class="form-control">
                                <option value="">请选择...</option>
                            </select>
                        </c:if>
                        <c:if test="${tag.id != null}">
                            <p class="form-control-static">
                                    ${tag.tagTypeName}
                            </p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label col-md-4">标签名称<span class="required">*</span></label>
                    <div class="col-md-8">
                        <input type="text" class="form-control" name="tagName" value="${tag.tagName}"/>
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