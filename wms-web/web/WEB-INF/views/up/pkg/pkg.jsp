<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <title>升级包管理</title>
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
        <div id="content_pkg_list" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption">
                            <i class="fa fa-list"></i>升级包管理
                        </div>
                    </div>
                    <div class="portlet-body">
                        <div class="table-toolbar">
                            <!-- BEGIN 操作按钮-->
                            <div class="row margin-bottom-10">
                                <div class="col-md-12">
                                    <div class="btn-group">
                                        <button id="btn_pkg_new" class="btn ${color_action}">新增</button>
                                    </div>
                                </div>
                            </div>
                            <!-- END 操作按钮 -->
                            <!-- BEGIN 查询表单 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <form id="form_pkg_query" class="form-inline" role="form">
                                        <div class="form-group">
                                            <div class="input">
                                                <label>关键字:</label>
                                                <input type="text" class="form-control" name="keyword" placeholder="编码或名称">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="input">
                                                <label>应用:</label>
                                                <select class="form-control" name="appId">
                                                    <option value="">全部</option>
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
                                                    <option value="">全部</option>
                                                    <c:forEach var="item" items="${osList}">
                                                        <option value="${item.id}">${item.osName}</option>
                                                    </c:forEach>
                                                </select>
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
                                        <button id="btn_pkg_query" type="button" class="btn default">查询</button>
                                    </form>
                                </div>
                            </div>
                            <!-- END 查询表单 -->
                        </div>
                        <table id="table_pkg_list" class="table table-striped table-bordered table-hover">
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
        <div id="content_pkg_edit" class="row" data-sign="content">
            <div class="col-md-12">
                <div class="portlet box ${color_portlet}">
                    <div class="portlet-title">
                        <div class="caption"><i class="fa fa-edit"></i>升级包信息</div>
                    </div>
                    <div id="div_pkg_edit" class="portlet-body form">
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
            seajs.use("pages/up/pkg/pkg", function (UpPackage) {
                var upPkg = new UpPackage();
                upPkg.init();
            });
        });
    </script>
    <!-- END PAGE LEVEL SCRIPTS -->
</myscript>
<!-- END CONTENT -->
</body>
<!-- END BODY -->
</html>