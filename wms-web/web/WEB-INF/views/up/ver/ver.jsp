<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>版本管理</title>
    <mymeta>
        <meta content="" name="description"/>
        <meta content="" name="author"/>
    </mymeta>
    <mylink>
        <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
        <link href="${ctx}/assets/global/plugins/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
        <!-- END PAGE LEVEL PLUGIN STYLES -->
        <!-- BEGIN PAGE LEVEL STYLES -->
        <!-- END PAGE LEVEL STYLES -->
    </mylink>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body>
<!-- BEGIN CONTENT -->
<div class="page-content-wrapper">
    <div class="page-content">
        <!-- BEGIN PAGE HEADER-->
        <!-- END PAGE HEADER-->
        <!-- BEGIN PAGE CONTENT-->
        <div class="row">
            <div class="col-md-3">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>版本号
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_ver_no_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_ver_no_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <label>应用:</label>
                                                <select class="form-control" name="appId">
                                                    <c:forEach var="item" items="${appList}">
                                                        <option value="${item.id}">${item.appName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input">
                                                <label>系统:</label>
                                                <select class="form-control" name="osId">
                                                    <c:forEach var="item" items="${osList}">
                                                        <option value="${item.id}">${item.osName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_ver_no_list" class="table table-striped table-bordered table-hover">
                            <thead class="hide">
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-9" id="content_ver_list" data-sign="content">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>版本列表
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <%--
                                    <div class="btn-group">
                                        <button id="btn_ver_all_checked" class="btn ${color_action}">全选</button>
                                    </div>
                                    <div class="btn-group">
                                        <button id="btn_ver_all_unchecked" class="btn ${color_action}">全消</button>
                                    </div>
                                    --%>
                                    <div class="btn-group">
                                        <button id="btn_ver_batch_edit" class="btn ${color_action}">批量修改</button>
                                    </div>
                                    <div class="btn-group">
                                        <button id="btn_ver_batch_enable" class="btn green">批量启用</button>
                                    </div>
                                    <div class="btn-group">
                                        <button id="btn_ver_batch_disable" class="btn red">批量停用</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_ver_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <label>关键字:</label>
                                                <input type="text" class="form-control" name="keyword" placeholder="编码或名称">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input">
                                                <label>渠道:</label>
                                                <select class="form-control" name="chnId">
                                                    <option value="">全部</option>
                                                    <c:forEach var="item" items="${chnList}">
                                                        <option value="${item.id}">${item.chnName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input">
                                                <label>型号:</label>
                                                <select class="form-control" name="modId">
                                                    <option value="">全部</option>
                                                    <c:forEach var="item" items="${modList}">
                                                        <option value="${item.id}">${item.modName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input">
                                                <label>类型:</label>
                                                <select class="form-control" name="pkgType">
                                                    <option value="">全部</option>
                                                    <option value="1">升级包</option>
                                                    <option value="2">资源包</option>
                                                </select>
                                            </div>
                                        </div>
                                        <button id="btn_ver_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_ver_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-8" id="content_ver_edit" data-sign="content">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i><span id="title_ver"></span>
                        </div>
                    </div>
                    <div id="div_ver_edit" class="portlet-body">
                    </div>
                </div>
            </div>
        </div>
        <!-- END PAGE CONTENT-->

        <!-- BEGIN COMFIRM MODAL -->
        <div id="modal_confirm" class="modal fade" tabindex="-1" role="dialog"
             aria-labelledby="modal_confirm_title" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 id="modal_confirm_title">确认信息</h4>
            </div>
            <div class="modal-body">
                <p id="modal_confirm_info" style="word-break:break-all"></p>
            </div>
            <div class="modal-footer">
                <button id="btn_modal_confirm" data-dismiss="modal" class="btn blue">确定</button>
                <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
            </div>
        </div>
    </div>
</div>
<myscript>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/assets/global/plugins/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
    <script src="${ctx}/assets/global/plugins/bootstrap-confirmation/bootstrap-confirmation.min.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/up/ver/ver_no", function (UpVersionNo) {
                var verNo = new UpVersionNo();
                verNo.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>