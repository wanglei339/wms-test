<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>渠道厂商管理</title>
    <mymeta>
        <meta content="" name="description"/>
        <meta content="" name="author"/>
    </mymeta>
    <mylink>
        <!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
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
        <!-- BEGIN 列表 -->
        <div id="content_chn_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>渠道厂商管理
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_chn_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_chn_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <input type="text" class="form-control" name="keyword" placeholder="渠道编码或名称">
                                            </div>
                                        </div>
                                        <button id="btn_chn_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_chn_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 列表 -->
        <!-- BEGIN 编辑 -->
        <div id="content_chn_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>渠道厂商信息</div>
                    </div>
                    <div id="div_chn_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 编辑 -->
        <!-- END PAGE CONTENT-->
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/up/chn/chn", function (UpChannel) {
                var upChannel = new UpChannel();
                upChannel.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>