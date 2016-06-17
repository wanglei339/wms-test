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
                            <i class="fa fa-list"></i>频道管理
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
                                                   placeholder="频道名称">
                                        </div>
                                    </div>
                                    <button id="btn_search" type="button" class="btn default">查询</button>
                                    <div class="form-group" style="margin-left: 5px;">
                                        <label class="" >状态:</label>
                                        <select name="status" id="status" class="form-control">
                                            <option value="">全部</option>
                                            <option value="1">正常</option>
                                            <option value="0">禁用</option>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" id="beginDate" name="beginDate" value="" />
                                <input type="hidden" id="endDate" name="endDate" value="" />
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
                        <div class="caption"><i class="fa fa-edit"></i>频道信息查看</div>
                    </div>
                    <div id="div_page_edit" class="portlet-body form">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END PAGE CONTENT-->
<myscript>
    <script type="text/javascript"
            src="${ctx}/assets/global/plugins/bootstrap-daterangepicker/moment.min.js"></script>
    <script>
        jQuery(document).ready(function () {
            Metronic.init(); // init metronic core components
            Layout.init(); // init current layout
            App.init(); // init app
            seajs.use("pages/md/video/chn/list", function (Page) {
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