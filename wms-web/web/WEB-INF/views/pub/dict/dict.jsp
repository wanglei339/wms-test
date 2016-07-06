<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>词典管理</title>
    <mymeta>
        <meta content="" name="description"/>
        <meta content="" name="author"/>
    </mymeta>
    <mylink>
        <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
        <link href="${ctx}/assets/global/plugins/jstree/dist/themes/default/style.min.css" rel="stylesheet" type="text/css"/>
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
        <!-- BEGIN 词典列表 -->
        <div id="content_dict_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>词典管理
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_dict_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_dict_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <input type="text" class="form-control" name="query_dictName" placeholder="词典编码或名称">
                                            </div>
                                        </div>
                                        <button id="btn_dict_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_dict_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 词典列表 -->
        <!-- BEGIN 词典编辑 -->
        <div id="content_dict_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>词典信息</div>
                    </div>
                    <div id="div_dict_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 词典编辑 -->
        <!-- BEGIN 词典明细编辑 -->
        <div id="content_item_edit" class="row" data-sign="content">
            <div class="col-md-4">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>词典明细
                        </div>
                    </div>
                    <div class="portlet-body" style="max-height: 400px;overflow: auto;">
                        <p>拖拽可调整位置，树型词典右键可增加子节点。</p>
                        <div class="form-group">
                            <div class="input-group">
                                <input id="input_item_query" type="text" class="form-control" placeholder="关键字">
							<span class="input-group-btn">
							<button id="btn_item_query" class="btn blue" type="button">查询</button>
							</span>
                            </div>
                        </div>
                        <div id="item_tree"></div>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>词典明细维护
                        </div>
                    </div>
                    <div id="div_item_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- BEGIN 词典明细编辑 -->
        <!-- END PAGE CONTENT-->
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/assets/global/plugins/jstree/dist/jstree.min.js"></script>
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/pub/dict/dict", function (Dict) {
                var dict = new Dict();
                dict.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>