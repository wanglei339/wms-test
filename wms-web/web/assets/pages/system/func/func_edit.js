define(function (require, exports, module) {

    var FuncEdit = function () {
        this.el_div_edit = "#div_func_edit";
        this.el_form = "#form_func";
        this.el_btn_save = "#btn_func_save";
        this.el_btn_add = "#btn_func_add";
        this.el_tree = "#func_tree";
    }

    FuncEdit.prototype.init = function () {
        this.loadContent();
    }

    FuncEdit.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    FuncEdit.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/system/func/page/edit" + " " + this.el_form;
        var params = {
            id: this.options.id,
            parentId: this.options.parentId
        };
        //$(this.el_div_edit).empty();
        Metronic.blockUI({target: that.el_form});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(that.el_form);
            that.loadContentFinish();
        });
    }

    FuncEdit.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    FuncEdit.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_add).off("click").on("click", function () {
            that.btnAdd();
        });
    }

    FuncEdit.prototype.initValidate = function () {
        this.validate = $("#form_func").validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                funcName: {required: true}
            },
            messages: {
                funcName: {required: "功能名称必填！"}
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

    FuncEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    FuncEdit.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: that.el_form});
        $.ajax({
            url: App.ctx + "/system/func/save",
            type: "post",
            data: this.getFormData(),
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(that.el_form);
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
                Metronic.unblockUI(that.el_form);
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    FuncEdit.prototype.btnAdd = function () {
        var that = this;
        this.setOptions({
            id: null,
            parentId: this.options.id,
            callback_btnSave: function () {
                $(that.el_tree).jstree(true).refresh();
            }
        });
        this.init();
    }

    module.exports = FuncEdit;

});