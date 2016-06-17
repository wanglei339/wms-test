<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>配置页面编辑界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_page" class="form-horizontal" action="#">
    <div class="form-body">
        <h3 class="form-section">页面信息</h3>
        <input type="hidden" name="id" value="${page.id}"/>
        <div class="form-group">
            <label class="control-label col-md-3">页面编码<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pageCode" value="${page.pageCode}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3">页面名称<span class="required">*</span></label>
            <div class="col-md-4">
                <input type="text" class="form-control" name="pageName" value="${page.pageName}"/>
            </div>
        </div>
        <h3 class="form-section">页面数据</h3>
        <input type="hidden" name="dataId" value="${data.id}"/>
        <input type="hidden" name="pageId" value="${page.id}"/>
        <c:if test="${show.conTitleShow==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conTitleName}<c:if
                        test="${show.conTitleRequired==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conTitle" name="conTitle" value="${data.conTitle}" data-required="${show.conTitleRequired}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conUrlShow==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conUrlName}<c:if
                        test="${show.conUrlRequired==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conUrl" name="conUrl" value="${data.conUrl}" data-required="${show.conUrlRequired}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conIdShow==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conIdName}<c:if
                        test="${show.conIdRequired==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <div class="input-group">
                        <input type="text" class="form-control" id="conId" name="conId" value="${data.conId}" data-required="${show.conIdRequired}"/>
                        <span class="input-group-btn"><button class="btn blue" type="button">选择</button></span>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conP1Show==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conP1Name}<c:if
                        test="${show.conP1Required==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conP1" name="conP1" value="${data.conP1}" data-required="${show.conP1Required}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conP2Show==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conP2Name}<c:if
                        test="${show.conP2Required==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conP2" name="conP2" value="${data.conP2}" data-required="${show.conP2Required}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conP3Show==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conP3Name}<c:if
                        test="${show.conP3Required==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conP3" name="conP3" value="${data.conP3}" data-required="${show.conP3Required}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conP4Show==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conP4Name}<c:if
                        test="${show.conP4Required==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conP4" name="conP4" value="${data.conP4}" data-required="${show.conP4Required}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conP5Show==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conP5Name}<c:if
                        test="${show.conP5Required==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="conP5" name="conP5" value="${data.conP5}" data-required="${show.conP5Required}"/>
                </div>
            </div>
        </c:if>
        <c:if test="${show.conDescShow==1}">
            <div class="form-group">
                <label class="control-label col-md-3">${show.conDescName}<c:if
                        test="${show.conDescRequired==1}"><span class="required">*</span></c:if></label>
                <div class="col-md-4">
                    <textarea class="form-control" id="conDesc" name="conDesc" rows="3" data-required="${show.conDescRequired}">${data.conDesc}</textarea>
                </div>
            </div>
        </c:if>
        <c:if test="${show.picShow==1}">
            <h3 class="form-section">页面图片</h3>
            <div class="form-group">
                <label class="control-label col-md-3">上传图片</label>
                <div class="col-md-9">
                    <div class="margin-bottom-10">
                        <button id="btn_pic_add" type="button" class="btn blue">添加</button>
                    </div>
                    <div id="div_pic_list" class="row">
                        <c:forEach var="file" items="${fileList}">
                            <div id="pic_div_${file.id}" class="col-sm-6 col-md-4">
                                <div class="thumbnail" data-id="${file.id}">
                                    <input type="hidden" id="pic_path_${file.id}" name="pic_path_${file.id}" value="">
                                    <img id="pic_img_${file.id}" src="${file.fileUrl}" style="width: 100%; height: 150px; display: block;">
                                    <div class="caption">
                                        <a id="pic_edit_${file.id}" href="javascript:;" class="btn btn-xs blue" data-sign="edit" data-id="${file.id}">
                                            修改 </a>
                                        <a href="javascript:;" class="btn btn-xs red" data-sign="del" data-id="${file.id}">
                                            删除 </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>
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
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>