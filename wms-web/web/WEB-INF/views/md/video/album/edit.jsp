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
        <input type="hidden" name="id" id="id" value="${album.id}"/>

        <ul class="nav nav-tabs">
            <li class="active">
                <a href="#tab_1_1" data-toggle="tab">
                    基本信息 </a>
            </li>
            <li>
                <a href="#tab_1_2" data-toggle="tab">
                    专辑图片 </a>
            </li>
            <li>
                <a href="#tab_1_3" data-toggle="tab">
                    专辑标签 </a>
            </li>
            <li>
                <a href="#tab_1_4" data-toggle="tab">
                    明星 </a>
            </li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade active in" id="tab_1_1">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">标题<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="title" value="${album.title}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">副标题</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="subTitle" value="${album.subTitle}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label col-md-2">业务大类<span class="required">*</span></label>
                            <div class="col-md-4">
                                <select name="busType" id="busType"  class="form-control">
                                    <option value="">请选择...</option>
                                    <c:forEach var="item" items="${busTypeList}">
                                        <option value="${item.id}" <c:if test="${album.busType==item.id}">selected</c:if>>${item.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label col-md-2">所属频道<span class="required">*</span></label>
                            <div class="col-md-10">
                                <div class="checkbox-list" style="max-height: 200px;overflow-y: auto;">
                                    <c:forEach items="${album.chnList}" var="chn">
                                        <div class="md-checkbox"  style="width:100px;">
                                            <c:choose>
                                                <c:when test="${chn.selected == true}">
                                                    <input type="checkbox" id="${chn.id}"  name="chn" class="md-check" checked>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="checkbox" id="${chn.id}" name="chn" class="md-check">
                                                </c:otherwise>
                                            </c:choose>

                                            <label for="${chn.id}">
                                                <span></span>
                                                <span class="check"></span>
                                                <span class="box"></span>
                                                    ${chn.chnName} </label>
                                        </div>
                                    </c:forEach>
                                </div>
                                <span class="help-block">至少选择一项</span>
                                <div id="div_error_chn"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label col-md-2">详细描述</label>
                            <div class="col-md-9">
                                <textarea class="form-control" name="detailDesc" rows="3">${album.detailDesc}</textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane fade" id="tab_1_2">
                <c:forEach items="${album.imageTypeList}" var="obj">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-success" id="img_btn_${obj.id}"  sign="upimg_btn">${obj.typeName}上传</button>
                                </div>
                                <div class="col-md-8">
                                    <div class="thumbnail" data-id="${file.id}">
                                        <img id="img${obj.id}" src="${obj.picUrl}" style="height:300px" filePath="" fileName="" sign="star_img"/>
                                        <div class="caption" sign="delimg_div" id="delimg_div_${obj.id}" <c:if test="${obj.picUrl == null || obj.picUrl == ''}">style="display:none;"</c:if>>
                                            <a href="javascript:;" class="btn btn-xs red" type-id="${obj.id}">
                                                删除 </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="tab-pane fade" id="tab_1_3">
                <c:forEach items="${album.tagGroupList}" var="tabType">
                    <div class="row">
                        <div class="col-md-12">
                            <label class="control-label col-md-2">${tabType.typeName}</label>
                            <div class="md-checkbox-inline">
                                <c:forEach items="${tabType.tagList}" var="tag">
                                    <div class="md-checkbox"  style="width:100px;">
                                        <c:choose>
                                            <c:when test="${tag.selected == true}">
                                                <input type="checkbox" id="${tag.id}" tagtype="${tag.tagTypeId}" name="tag" class="md-check" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" id="${tag.id}" tagtype="${tag.tagTypeId}"  name="tag" class="md-check">
                                            </c:otherwise>
                                        </c:choose>

                                        <label for="${tag.id}">
                                            <span></span>
                                            <span class="check"></span>
                                            <span class="box"></span>
                                                ${tag.tagName} </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="tab-pane fade" id="tab_1_4">
                <c:forEach items="${album.roleList}" var="role">
                    <div class="row" style="margin-top:20px;">
                        <div class="col-md-12">
                            <label class="control-label col-md-2">${role.roleName}</label>
                            <c:set var="inputvalue" value=""/>
                            <c:forEach items="${role.starList}" var="star">
                                <c:set var="inputvalue" value="${inputvalue},${star.starId}"/>
                            </c:forEach>
                            <c:set var="inputname" value=""/>
                            <c:forEach items="${role.starList}" var="star">
                                <c:set var="inputname" value="${inputname},${star.starName}"/>
                            </c:forEach>

                            <div class="col-md-9">
                                <input type="text"style="width:400px;" sign="star_input" role="${role.roleId}" names="${inputname}" value="${inputvalue}" class="form-control select2">
                            </div>
                        </div>
                    </div>
                </c:forEach>
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