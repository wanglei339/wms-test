define(function (require, exports, module) {

    var ItemEdit = function () {
        this.el_div_edit = "#div_item_edit";
        this.el_form = "#form_item";
        this.el_btn_save = "#btn_item_save";
        this.el_btn_add = "#btn_item_add";
        this.el_btn_back = "#btn_item_back";
        this.el_tree = "#item_tree";
    }

    ItemEdit.prototype.init = function () {
        this.loadContent();
    }

    ItemEdit.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    ItemEdit.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/pub/dict/page/item/edit" + " " + this.el_form;
        var params = {
            id: this.options.id,
            did: this.options.did,
            pid: this.options.pid
        };
        //$(this.el_div_edit).empty();
        Metronic.blockUI({target: that.el_form});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(that.el_form);
            that.loadContentFinish();
        });
    }

    ItemEdit.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化校验组件
        this.initValidate();
        // 初始化表单
        Metronic.initComponents();
    }

    ItemEdit.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_add).off("click").on("click", function () {
            that.btnAdd();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }

    ItemEdit.prototype.initValidate = function () {
        this.validate = $("#form_item").validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                itemCode: {required: true},
                itemName: {required: true},
                itemValue: {required: true}
            },
            messages: {
                itemCode: {required: "编码必填！"},
                itemName: {required: "名称必填！"},
                itemValue: {required: "值必填！"}
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

    ItemEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    ItemEdit.prototype.btnSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: that.el_form});
        $.ajax({
            url: App.ctx + "/pub/dict/item/save",
            type: "post",
            data: that.getFormData(),
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

    ItemEdit.prototype.btnAdd = function () {
        var that = this;
        this.setOptions({
            id: null,
            did: this.options.did,
            pid: this.options.id,
            callback_btnSave: function () {
                $(that.el_tree).jstree(true).refresh();
            }
        });
        this.init();
    }

    module.exports = ItemEdit;

});