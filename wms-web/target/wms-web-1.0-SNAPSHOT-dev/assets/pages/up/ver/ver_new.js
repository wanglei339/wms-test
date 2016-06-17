define(function (require, exports, module) {

    var tpl_pkg_list = '{{ _.each(pkgList, function(pkg){ }}'
        + '<label>'
        + '<input type="checkbox" name="pkgId" value="{{=pkg.pkgId}}" checked/>{{=pkg.pkgName}}({{=pkg.appKey}})'
        + '</label>'
        + '{{ }); }}';

    var VersionNew = function () {
        this.el_content_edit = "#content_ver_edit";
        this.el_div_edit = "#div_ver_edit";
        this.el_form = "#form_ver";
        this.el_btn_query = "#btn_pkg_query";
        this.el_btn_checked = "#btn_pkg_checked";
        this.el_btn_unchecked = "#btn_pkg_unchecked";
        this.el_btn_save = "#btn_ver_save";
        this.el_btn_back = "#btn_ver_back";
    }

    VersionNew.prototype.init = function () {
        this.show();
        this.loadContent();
    }

    VersionNew.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    VersionNew.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
        $("#title_ver").html("新增版本");
    }

    VersionNew.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/up/ver/page/new" + " " + this.el_form;
        var params = {
            appId: this.options.appId,
            osId: this.options.osId
        };
        $(this.el_div_edit).empty();
        Metronic.blockUI({target: ".page-content"});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(".page-content");
            that.loadContentFinish();
        });
    }

    VersionNew.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    VersionNew.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_query).off("click").on("click", function () {
            that.btnQuery();
        });
        $(this.el_btn_checked).off("click").on("click", function () {
            that.btnChecked();
        });
        $(this.el_btn_unchecked).off("click").on("click", function () {
            that.btnUnChecked();
        });
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });

        //每次打开应用提示
        //点击“是”后，提示间隔禁用，“是否提示”只能选择“是”
        //点击“否”后，提示间隔启用，“是否提示”都可以选
        $("#form_ver").find("input[name=promptAlways]").each(function(i){
            $(this).off("click").on("click", function () {
                if($(this).val() == "1"){
                    $("#promptInterval").attr("readonly",true);
                    $("#form_ver").find("input[name=promptUp]").each(function(i){
                        if($(this).val() == 0){
                            $(this).attr("disabled",true);
                        }
                    })
                }else{
                    $("#promptInterval").attr("readonly",false);
                    $("#form_ver").find("input[name=promptUp]").each(function(i){
                        $(this).attr("disabled",false);
                    })
                }
            });
        })

        //是否提示
        //选择“否”，“每次打开应用提示“只能选择”否“
        //选择”是“，解除对“每次打开应用提示“限制
        $("#form_ver").find("input[name=promptUp]").each(function(i){
            $(this).off("click").on("click", function () {
                if($(this).val() == "0"){
                    $("#form_ver").find("input[name=promptAlways]").each(function(i){
                        if($(this).val() == 1){
                            $(this).attr("disabled",true);
                        }
                    })
                }else{
                    $("#form_ver").find("input[name=promptAlways]").each(function(i){
                        $(this).attr("disabled",false);
                    })
                }
            });
        })

        //升级类型
        //选择强制升级后，”提示升级“只能选择”是“，”每次打开应用提示“只能选择”是“
        //选择普通升级后，解除上面两项限制
        $("#form_ver").find("input[name=upType]").each(function(i){
            $(this).off("click").on("click", function () {
                var upType = $(this).val();
                console.log("upType="+upType);
                that.upTypeClickEvent(upType);
            });
        })
    }

    VersionNew.prototype.upTypeClickEvent = function (upType) {
        if(upType == 1){
            //普通升级
            $("#form_ver").find("input[name=promptUp]").each(function(i){
                $(this).attr("disabled",false);
            })
            $("#form_ver").find("input[name=promptAlways]").each(function(i){
                $(this).attr("disabled",false);
            })
        }else{
            //强制升级
            //提示升级必须选是
            $("#form_ver").find("input[name=promptUp]").each(function(i){
                if($(this).val() == "0"){
                    $(this).attr("disabled",true);
                    $(this).attr("checked",false);
                }
                if($(this).val() == "1"){
                    $(this).attr("checked",true);
                }
            })
            //每次打开应用提示必须选是
            $("#form_ver").find("input[name=promptAlways]").each(function(i){
                if($(this).val() == "0"){
                    $(this).attr("disabled",true);
                    $(this).attr("checked",false);
                }
                if($(this).val() == "1"){
                    $(this).attr("checked",true);
                }
            })
        }
    }

    VersionNew.prototype.initValidate = function () {
        $.validator.addMethod("upVersionStr", function(value, element) {
            var reg = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
            if (reg.test(value)) {
                var group = value.match(reg);
                if (group[1] == 0 && group[2] == 0 && group[3] == 0) {
                    return false;
                }
                return true;
            }
            return false;
        }, "请输入正确的版本号！");
        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                pkgId: {required: true},
                verStr: {upVersionStr: true},
                verName: {required: true},
                verTitle: {required: true},
                verTitleForce: {required: true},
                verDesc: {required: true},
                verDescForce: {required: true},
                promptInterval: {digits: true}
            },
            messages: {
                pkgId: {required: "请至少选择一个应用包！"},
                verName: {required: "版本名称必填！"},
                verTitle: {required: "版本标题必填！"},
                verTitleForce: {required: "版本标题（强制）必填！"},
                verDesc: {required: "版本描述必填！"},
                verDescForce: {required: "版本描述（强制）必填！"},
                promptInterval: {digits: "请输入数字！"}
            },
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            unhighlight: function (element) {
                $(element).closest('.form-group').removeClass('has-error');
            },
            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
            },
            errorPlacement: function (error, element) { // render error placement for each input type
                if (element.parent(".input-group").size() > 0) {
                    error.insertAfter(element.parent(".input-group"));
                } else if (element.attr("data-error-container")) {
                    error.appendTo(element.attr("data-error-container"));
                } else if (element.parents('.radio-list').size() > 0) {
                    error.appendTo(element.parents('.radio-list').attr("data-error-container"));
                } else if (element.parents('.radio-inline').size() > 0) {
                    error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
                } else if (element.parents('.checkbox-list').size() > 0) {
                    error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
                } else if (element.parents('.checkbox-inline').size() > 0) {
                    error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
                } else {
                    error.insertAfter(element);
                }
            }
        });
    }

    VersionNew.prototype.btnQuery = function () {
        $.ajax({
            url: App.ctx + "/up/ver/pkg/query",
            type: "post",
            data: {
                keyword: $('#query_pkg').val(),
                appId: this.options.appId,
                osId: this.options.osId
            },
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    $("#div_pkg_checkbox_list").html(_.template(tpl_pkg_list)(rsObj))
                    Metronic.initUniform($('input[type="checkbox"][name="pkgId"]'));
                } else {
                    toastr["error"](rsObj.rsMsg || "查询失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    VersionNew.prototype.btnChecked = function () {
        var pkgCheckBox = $('input[type="checkbox"][name="pkgId"]');
        pkgCheckBox.each(function () {
            $(this).attr("checked", true);
        });
        $.uniform.update(pkgCheckBox);
    }

    VersionNew.prototype.btnUnChecked = function () {
        var pkgCheckBox = $('input[type="checkbox"][name="pkgId"]');
        pkgCheckBox.each(function () {
            $(this).attr("checked", false);
        });
        $.uniform.update(pkgCheckBox);
    }

    VersionNew.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        var pkgIdAry = [];
        $(this.el_form + " input[name=pkgId]:checked").each(function () {
            pkgIdAry.push($(this).val());
        });
        data["pkgIdAryStr"] = pkgIdAry.join(",");
        return data;
    }

    VersionNew.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        // 保存请求
        $.ajax({
            url: App.ctx + "/up/ver/new/save",
            type: "post",
            data: that.getFormData(),
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(".page-content");
                if (rsObj.rsCode == "1") {
                    toastr["success"](rsObj.rsMsg || "保存成功！", "提示信息");
                    if (_.isFunction(that.options.callback_btnSave)) {
                        that.options.callback_btnSave();
                    }
                } else {
                    toastr["error"](rsObj.rsMsg || "保存失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Metronic.unblockUI(".page-content");
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    module.exports = VersionNew;

});