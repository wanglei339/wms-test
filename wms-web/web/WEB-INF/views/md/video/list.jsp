<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>列表查询页面</title>
    <mymeta>
        <meta content="" name="description"/>
        <meta content="" name="author"/>
    </mymeta>
    <mylink>
        <link href="${ctx}/assets/global/plugins/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/select2/select2.css"/>
    </mylink>
</head>
<body>
<!-- BEGIN CONTENT -->
<div class="page-content-wrapper">
    <div class="page-content">
        <!-- BEGIN 列表区 -->
        <div id="content_page_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>视频管理
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_page_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <form class="form-inline" role="form" id="form_search">
                                <div class="row" style="margin-top:10px;margin-left: 5px;">
                                    <label>关键字:</label>
                                    <div class="form-group">
                                        <div class="input">
                                            <input type="text" class="form-control" id="keywords" name="keywords"
                                                   placeholder="视频标题、ID">
                                        </div>
                                    </div>
                                    <button id="btn_search" type="button" class="btn default">查询</button>
                                    <label>专辑:</label>
                                    <div class="form-group">
                                        <input type="text"style="width:200px;" id="albumId" name="albumId" class="form-control select2" value="${video.albumId}">
                                    </div>
                                    <div class="form-group" style="margin-left: 5px;">
                                        <label class="" >资源类型:</label>
                                        <select name="resType" id="resType" class="form-control">
                                            <option value="">全部</option>
                                            <c:forEach items="${resTypeList}" var="item">
                                                <option value="${item.id}" <c:if test="${item.id==video.resType}">selected</c:if>>${item.itemName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <table class="table table-striped table-bordered table-hover" id="table_list">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <!-- END USER TABLE PORTLET-->
            </div>
        </div>
        <!-- 列表  end-->
        <div id="content_page_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>视频信息</div>
                    </div>
                    <div id="div_page_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>

        <!--编辑  end-->
        <!-- BEGIN COMFIRM MODAL -->
        <div id="modal_confirm" class="modal fade" tabindex="-1" role="dialog"
             aria-labelledby="modal_confirm_title" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h3 id="modal_confirm_title">确认信息</h3>
            </div>
            <div class="modal-body">
                <p id="modal_confirm_info" style="word-break:break-all"></p>
            </div>
            <div class="modal-footer">
                <button id="btn_modal_confirm" data-dismiss="modal" class="btn blue">确定</button>
                <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
            </div>
        </div>
        <!-- END COMFIRM MODAL -->
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <script src="${ctx}/assets/global/plugins/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx}/assets/global/plugins/select2/select2.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/global/plugins/select2/select2_locale_zh-CN.js"></script>


    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/md/video/list", function (Page) {
                var page = new Page();
                page.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>