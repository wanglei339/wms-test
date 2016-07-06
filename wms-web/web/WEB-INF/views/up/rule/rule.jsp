<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>规则列表界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<div id="div_rule">
    <h4 id="title_rule">${ver.verName} / ${ver.pkgName}（${ver.appKey}）</h4>
    <!-- BEGIN TAB-->
    <div class="tabbable-line">
        <ul class="nav nav-tabs ">
            <li class="active">
                <a href="#tab_rule_01" data-toggle="tab">强制升级</a>
            </li>
            <li>
                <a href="#tab_rule_02" data-toggle="tab">按地区</a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="tab_rule_01" class="tab-pane active">
                <div class="table-toolbar form-inline">
                    <div class="form-group">
                        <div class="input">
                            <label>范围设置:</label>
                            <select id="judgeWay_01" name="judgeWay" class="form-control">
                                <option value="1">在范围内</option>
                                <optvideo_uploadion value="2">不在范围内</optvideo_uploadion>
                            </select>
                        </div>
                    </div>
                    <button id="btn_rule_save_01" type="button" class="btn blue">保存</button>
                    <button id="btn_con_new_01" type="button" class="btn green">新增条件</button>
                </div>
                <table id="table_con_list_01" class="table table-striped table-bordered table-hover">
                    <thead>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <h5>规则说明：限制区间内的版本强制升级，包含最小和最大值。</h5>
            </div>
            <div id="tab_rule_02" class="tab-pane">
                <h5>规则说明：按地区限制升级。</h5>
            </div>
        </div>
    </div>
    <form id="form_rule" class="form-horizontal" action="#">
        <div class="form-body">
            <input type="hidden" id="verId" name="verId" value="${ver.id}"/>
        </div>
        <!-- END TAB-->
        <div class="form-actions">
            <div class="row">
                <div class="col-md-offset-4 col-md-8">
                    <button type="button" id="btn_rule_cache" class="btn default">更新规则缓存</button>
                    <button type="button" id="btn_rule_back" class="btn default">返回</button>
                </div>
            </div>
        </div>
    </form>
</div>
<!-- END PAGE CONTAINER-->
</body>
</html>