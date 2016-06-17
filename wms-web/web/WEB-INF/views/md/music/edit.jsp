<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>编辑界面</title>
    <script type="text/javascript" src="${ctx}/assets/global/plugins/select2/select2_locale_zh-CN.js"></script>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_page" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="id" id="id" value="${music.id}"/>

        <ul class="nav nav-tabs">
            <li class="active">
                <a href="#tab_1_1" data-toggle="tab">
                    基本信息 </a>
            </li>
            <li>
                <a href="#tab_1_2" data-toggle="tab">
                    音乐图片 </a>
            </li>
            <li>
                <a href="#tab_1_3" data-toggle="tab">
                    音乐标签 </a>
            </li>
            <li>
                <a href="#tab_1_4" data-toggle="tab">
                    明星 </a>
            </li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade  active in" id="tab_1_1">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">音乐名<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="title" value="${music.title}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label col-md-2">所属专辑<span class="required">*</span></label>
                            <div class="col-md-4">
                                <input type="text"style="width:200px;" id="albumId" name="albumId" class="form-control select2" value="${music.albumId}">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">专辑中顺序号<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="showOrder" value="${music.showOrder}"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">音乐文件<span class="required">*</span></label>
                            <div class="col-md-8">
                                <button id="music_upload" type="button" class="btn blue">上传</button>
                                <input type="hidden" class="form-control" id="musicFilePath" name="musicFilePath" value=""/>
                                <input type="text" class="form-control" id="musicUrl" name="musicUrl" value="${music.playUrl}" readonly/>

                                <audio src="" id="music1" controls='controls' style="display:none;"></audio>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label col-md-4">时长（秒）<span class="required">*</span></label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" name="duration" value="${music.duration}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label col-md-2">详细描述</label>
                            <div class="col-md-9">
                                <textarea class="form-control" name="detailDesc" rows="3">${music.detailDesc}</textarea>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="tab-pane fade" id="tab_1_2">
                <c:forEach items="${music.imageTypeList}" var="obj">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-success" id="img_btn_${obj.id}"  sign="upimg_btn">${obj.typeName}上传</button>
                                </div>
                                <div class="col-md-8">
                                    <div class="thumbnail" data-id="${file.id}">
                                        <img id="img${obj.id}" src="${obj.picUrl}" style="height:300px" filePath="" fileName="" sign="music_img"/>
                                        <c:if test="${obj.picUrl != null && obj.picUrl != ''}">
                                            <div class="caption">
                                                <a href="javascript:;" class="btn btn-xs red" sign="delimg_btn" type-id="${obj.id}">
                                                    删除 </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="tab-pane fade" id="tab_1_3">
                <c:forEach items="${music.tagGroupList}" var="tabType">
                    <div class="row">
                        <div class="col-md-12">
                            <label class="control-label col-md-2">${tabType.typeName}</label>
                            <div class="md-checkbox-inline">
                                <c:forEach items="${tabType.tagList}" var="tag">
                                    <div class="md-checkbox"  style="width:100px;">
                                        <c:choose>
                                            <c:when test="${tag.selected == true}">
                                                <input type="checkbox" id="${tag.id}" tagtype="${tag.tagTypeId}" name="musicTag" class="md-check" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" id="${tag.id}" tagtype="${tag.tagTypeId}"  name="musicTag" class="md-check">
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
                <c:forEach items="${music.roleList}" var="role">
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
                                <input type="hidden"style="width:400px;" sign="star_input" role="${role.roleId}" names="${inputname}" value="${inputvalue}" class="form-control select2">
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