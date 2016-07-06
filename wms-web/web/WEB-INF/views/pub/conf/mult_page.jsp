<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>配置管理（多内容）</title>
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
        <!-- BEGIN 列表区 -->
        <div id="content_page_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>配置管理（多内容）
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_page_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_page_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <input type="text" class="form-control" name="query_name" placeholder="页面名称">
                                        </div>
                                        <button id="btn_page_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_page_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 列表区 -->
        <!-- BEGIN 编辑区 -->
        <div id="content_page_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>配置信息</div>
                    </div>
                    <div id="div_page_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 编辑区 -->
        <!-- BEGIN 列表区 -->
        <div id="content_item_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>配置管理：<span id="caption_item_list"></span>
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_item_new" class="btn ${color_action}">新增</button>
                                    </div>
                                    <div class="btn-group">
                                        <button id="btn_page_back" class="btn ${color_action}">返回</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_item_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <input type="text" class="form-control" name="query_name" placeholder="内容标题">
                                        </div>
                                        <div class="form-group">
                                            <label class="sr-only" for="query_contype">类型：</label>
                                            <select class="form-control" id="query_contype" name="query_contype">
                                            </select>
                                        </div>
                                        <button id="btn_item_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_item_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 列表区 -->
        <!-- BEGIN 编辑区 -->
        <div id="content_item_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>配置信息</div>
                    </div>
                    <div id="div_item_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 编辑区 -->
        <!-- BEGIN 设置显示 -->
        <div id="content_show_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>显示信息</div>
                    </div>
                    <div id="div_show_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 设置显示 -->
        <!-- END PAGE CONTENT-->
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/assets/global/plugins/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/pub/conf/mult_page", function (Page) {
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