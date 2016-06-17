<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<!-- BEGIN HEAD -->
<head>
    <title>版本批量修改界面</title>
</head>
<body>
<!-- BEGIN PAGE CONTAINER-->
<!-- BEGIN FORM-->
<form id="form_ver" class="form-horizontal" action="#">
    <div class="form-body">
        <input type="hidden" name="appId" value="${ver.appId}"/>
        <input type="hidden" name="osId" value="${ver.osId}"/>
        <input type="hidden" name="verStr" value="${ver.verStr}"/>
        <div class="form-group">
            <label class="control-label col-md-4">版本个数：</label>
            <div class="col-md-6">
                <p class="form-control-static">
                    ${verCount}
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">版本号</label>
            <div class="col-md-6">
                <p class="form-control-static">
                    ${ver.verStr}（${ver.verInt}）
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">版本名称<span class="required">*</span></label>
            <div class="col-md-6">
                <input type="text" class="form-control" name="verName" value="${ver.verName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">升级标题<span class="required">*</span></label>
            <div class="col-md-6">
                <input type="text" class="form-control" name="verTitle" value="${ver.verTitle}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">升级标题（强制）<span class="required">*</span></label>
            <div class="col-md-6">
                <input type="text" class="form-control" name="verTitleForce" value="${ver.verTitleForce}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">升级描述<span class="required">*</span></label>
            <div class="col-md-6">
                <textarea class="form-control" name="verDesc" rows="3">${ver.verDesc}</textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">升级描述（强制）<span class="required">*</span></label>
            <div class="col-md-6">
                <textarea class="form-control" name="verDescForce" rows="3">${ver.verDescForce}</textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">升级类型</label>
            <div class="col-md-6">
                <div class="md-radio-inline">
                    <div class="md-radio">
                        <input type="radio" id="upType1" name="upType" value="1" class="md-radiobtn" <c:if test="${ver.upType!=2}">checked</c:if>>
                        <label for="upType1">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            普通升级</label>
                    </div>
                    <div class="md-radio">
                        <input type="radio" id="upType2" name="upType" value="2" class="md-radiobtn" <c:if test="${ver.upType==2}">checked</c:if>>
                        <label for="upType2">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            强制升级</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">是否静默下载</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <div class="md-radio-inline">
                        <div class="md-radio">
                            <input type="radio" id="silentDownload1" name="silentDownload" value="0" class="md-radiobtn" checked disabled>
                            <label for="silentDownload1">
                                <span></span>
                                <span class="check"></span>
                                <span class="box"></span>
                                否</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">是否静默安装</label>
            <div class="col-md-6">
                <div class="radio-list">
                    <div class="md-radio-inline">
                        <div class="md-radio">
                            <input type="radio" id="silentInstall1" name="silentInstall" value="0" class="md-radiobtn" checked disabled>
                            <label for="silentInstall1">
                                <span></span>
                                <span class="check"></span>
                                <span class="box"></span>
                                否</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">是否提示升级</label>
            <div class="col-md-6">
                <div class="md-radio-inline">
                    <div class="md-radio">
                        <input type="radio" id="promptUp1" name="promptUp" value="1" class="md-radiobtn" <c:if test="${ver.promptUp!=0}">checked</c:if>>
                        <label for="promptUp1">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            是</label>
                    </div>
                    <div class="md-radio">
                        <input type="radio" id="promptUp2" name="promptUp" value="0" class="md-radiobtn" disabled <c:if test="${ver.promptUp==0}">checked</c:if>>
                        <label for="promptUp2">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            否</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">是否每次打开应用提示升级</label>
            <div class="col-md-6">
                <div class="md-radio-inline">
                    <div class="md-radio">
                        <input type="radio" id="promptAlways1" name="promptAlways" value="1" class="md-radiobtn" <c:if test="${ver.promptAlways!=0}">checked</c:if>>
                        <label for="promptAlways1">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            是</label>
                    </div>
                    <div class="md-radio">
                        <input type="radio" id="promptAlways2" name="promptAlways" value="0" class="md-radiobtn" <c:if test="${ver.promptAlways==0}">checked</c:if>>
                        <label for="promptAlways2">
                            <span></span>
                            <span class="check"></span>
                            <span class="box"></span>
                            否</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-4">提示升级间隔时间（秒）</label>
            <div class="col-md-6">
                <input type="text" class="form-control" name="promptInterval" id="promptInterval" value="${ver.promptInterval}" <c:if test="${ver.promptAlways!=0}">readonly</c:if>/>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <button type="button" id="btn_ver_save" class="btn blue">保存</button>
                <button type="button" id="btn_ver_back" class="btn default">返回</button>
            </div>
        </div>
    </div>
</form>
<!-- END FORM-->
<!-- END PAGE CONTAINER-->
</body>
</html>