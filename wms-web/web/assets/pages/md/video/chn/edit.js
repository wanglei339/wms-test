define(function (require, exports, module) {

    var PageEdit = function () {
        this.el_content_edit = "#content_page_edit";
        this.el_div_edit = "#div_page_edit";
        this.el_form = "#form_page";
        this.el_btn_save = "#btn_page_save";
        this.el_btn_back = "#btn_page_back";
    }

    PageEdit.prototype.init = function () {
        this.show();
        this.loadContent();
    }

    PageEdit.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    PageEdit.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
    }

    PageEdit.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/md/video/chn/page/view" + " " + this.el_form;
        var params = {id: this.options.id};
        $(this.el_div_edit).empty();
        Metronic.blockUI({target: ".page-content"});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(".page-content");
            that.loadContentFinish();
        });
    }

    PageEdit.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    PageEdit.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }
    PageEdit.prototype.initValidate = function () {
        $.validator.addMethod("intMust", function (value, element) {
            return this.optional(element) || /^[0-9]*[1-9][0-9]*$/i.test(value);
        }, "必须填写正整数！");

        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                chnName: {required: true},
                showOrder: {required: false,intMust: true}
            },
            messages: {
                chnName: {required: "频道名称必填！"}
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
            errorPlacement: function (error, element) {
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
    PageEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="radio"]:checked,textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    PageEdit.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        // 保存请求
        $.ajax({
            url: App.ctx + "/md/video/chn/save",
            type: "post",
            data: that.getFormData(),
            success: function (data) {
                Metronic.unblockUI(".page-content");
                if (data.rsCode == "1") {
                    toastr["success"]("保存成功！", "提示信息");
                    if (_.isFunction(that.options.callback_btnSave)) {
                        that.options.callback_btnSave();
                    }
                } else {
                    toastr["error"](data.rsMsg, "提示信息");
                }
            }
        });
    }
    module.exports = PageEdit;

});