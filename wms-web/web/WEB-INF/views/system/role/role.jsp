<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>角色管理</title>
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
        <!-- BEGIN 角色列表 -->
        <div id="content_role_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>角色管理
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_role_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_role_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <input type="text" class="form-control" name="query_rolename" placeholder="角色名">
                                            </div>
                                        </div>
                                        <button id="btn_role_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_role_list" class="table table-striped table-bordered table-hover">
                            <thead>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 角色列表 -->
        <!-- BEGIN 角色编辑 -->
        <div id="content_role_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>角色信息</div>
                    </div>
                    <div id="div_role_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
        <!-- END 角色编辑 -->
        <!-- BEGIN 角色授权 -->
        <div id="content_role_auth" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>角色授权</div>
                    </div>
                    <div class="portlet-body form">
                        <form id="form_role_auth" class="form-horizontal" action="#">
                            <div class="form-body" style="max-height: 360px;overflow-y: auto;">
                                <div id="div_role_auth"></div>
                            </div>
                            <div class="form-actions">
                                <div class="row">
                                    <div class="col-md-offset-4 col-md-8">
                                        <button id="btn_role_auth_save" type="button" class="btn blue">保存</button>
                                        <button id="btn_role_auth_back" type="button" class="btn default">返回</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- END 角色授权 -->
        <!-- END PAGE CONTENT-->
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="${ctx}/assets/global/plugins/jstree/dist/jstree.min.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL PLUGINS -->
    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/system/role/role", function (Role) {
                var role = new Role();
                role.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>