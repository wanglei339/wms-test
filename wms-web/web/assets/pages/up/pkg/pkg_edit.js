define(function (require, exports, module) {

    var PackageEdit = function () {
        this.el_content_edit = "#content_pkg_edit";
        this.el_div_edit = "#div_pkg_edit";
        this.el_form = "#form_pkg";
        this.el_btn_save = "#btn_pkg_save";
        this.el_btn_back = "#btn_pkg_back";
    }

    PackageEdit.prototype.init = function () {
        this.show();
        this.loadContent();
    }

    PackageEdit.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    PackageEdit.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
    }

    PackageEdit.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/up/pkg/page/edit" + " " + this.el_form;
        var params = {id: this.options.id};
        $(this.el_div_edit).empty();
        Metronic.blockUI({target: ".page-content"});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(".page-content");
            that.loadContentFinish();
        });
    }

    PackageEdit.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    PackageEdit.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }

    PackageEdit.prototype.initValidate = function () {
        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                pkgName: {required: true},
                appId: {required: true},
                osId: {required: true},
                chnId: {required: true},
                modId: {required: true},
                pkgType: {required: true}
            },
            messages: {
                pkgName: {required: "包名称必填！"},
                appId: {required: "应用必填！"},
                osId: {required: "系统必填！"},
                chnId: {required: "渠道必填！"},
                modId: {required: "型号必填！"},
                pkgType: {required: "包类型必填！"}
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

    PackageEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    PackageEdit.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        // 保存请求
        $.ajax({
            url: App.ctx + "/up/pkg/save",
            type: "post",
            data: that.getFormData(),
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(".page-content");
                if (rsObj.rsCode == "1") {
                    toastr["success"]("保存成功！", "提示信息");
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

    module.exports = PackageEdit;

});