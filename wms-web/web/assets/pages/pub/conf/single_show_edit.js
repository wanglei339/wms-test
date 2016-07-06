define(function (require, exports, module) {

    var ShowEdit = function () {
        this.el_content_edit = "#content_show_edit";
        this.el_div_edit = "#div_show_edit";
        this.el_form = "#form_show";
        this.el_btn_save = "#btn_show_save";
        this.el_btn_back = "#btn_show_back";
    }

    ShowEdit.prototype.init = function () {
        this.show();
        this.loadContent();
    }

    ShowEdit.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    ShowEdit.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
    }

    ShowEdit.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/pub/conf/single/page/show/edit" + " " + this.el_form;
        var params = {pageId: this.options.pageId};
        $(this.el_div_edit).empty();
        Metronic.blockUI({target: ".page-content"});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(".page-content");
            that.loadContentFinish();
        });
    }

    ShowEdit.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    ShowEdit.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }

    ShowEdit.prototype.initValidate = function () {
        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                conTitleName: {required: true},
                conUrlName: {required: true},
                conIdName: {required: true},
                conP1Name: {required: true},
                conP2Name: {required: true},
                conP3Name: {required: true},
                conP4Name: {required: true},
                conP5Name: {required: true},
                conDescName: {required: true}
            },
            messages: {
                conTitleName: {required: "标题名称必填！"},
                conUrlName: {required: "链接名称必填！"},
                conIdName: {required: "内容ID名称必填！"},
                conP1Name: {required: "参数1名称必填！"},
                conP2Name: {required: "参数2名称必填！"},
                conP3Name: {required: "参数3名称必填！"},
                conP4Name: {required: "参数4名称必填！"},
                conP5Name: {required: "参数5名称必填！"},
                conDescName: {required: "描述名称必填！"}
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

    ShowEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    ShowEdit.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        // 保存请求
        $.ajax({
            url: App.ctx + "/pub/conf/single/show/save",
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

    module.exports = ShowEdit;

});