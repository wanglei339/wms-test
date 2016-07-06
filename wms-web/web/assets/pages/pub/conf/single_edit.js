define(function (require, exports, module) {

    var tpl_pic = require('tpls/pub_conf_pic.tpl');
    var pic_id_count = 1000;

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
        var url = App.ctx + "/pub/conf/single/page/edit" + " " + this.el_form;
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
        // 初始化上传组件
        this.initImgAdd();
        this.initImgEdit();
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
        $("#div_pic_list").off("click.pic_del").on("click.pic_del", 'a[data-sign="del"]', function () {
            var pid = $(this).data("id");
            $("#pic_div_" + pid).remove();
        });
    }

    PageEdit.prototype.initImgAdd = function () {
        var that = this;
        $("#btn_pic_add").uploadify({
                width: "56",
                height: "34",
                buttonClass: "btn blue",
                buttonText: "增加",
                fileTypeDesc: "请选择图片！",
                fileTypeExts: "*.gif; *.jpg; *.png",
                fileObjName: "imgUpload",   //服务器接收文件时的参数名
                removeTimeout: 1,
                swf: App.ctx + "/assets/global/plugins/uploadify/uploadify.swf",
                uploader: App.ctx + "/pub/conf/single/img/upload",
                method: "post",
                //formData: {id: 100},
                overrideEvents: ["onUploadSuccess"],
                onUploadSuccess: function (file, data, response) {
                    // 注意此处返回的是string类型的json，需要转换
                    var data = $.parseJSON(data);
                    var pic_id = pic_id_count++;
                    // var pic_url_default = App.ctx + "/assets/admin/layout/img/no_image.png";
                    $("#div_pic_list").append(_.template(tpl_pic)({
                        id: "new_" + pic_id,
                        pic_url: data.fileUrl,
                        pic_path: data.filePath,
                        pic_name: data.fileName
                    }));
                    that.initImgEdit();
                }
            }
        );
        // 去掉原组件的样式
        $(".uploadify-button").removeClass("uploadify-button").removeAttr("style");
    }

    PageEdit.prototype.initImgEdit = function () {
        var that = this;
        $('#div_pic_list a[data-sign="edit"]').uploadify({
                width: "36",
                height: "21",
                buttonClass: "btn btn-xs blue",
                buttonText: "修改",
                fileTypeDesc: "请选择图片！",
                fileTypeExts: "*.gif; *.jpg; *.png",
                fileObjName: "imgUpload",//服务器接收文件时的参数名
                multi: false,
                removeTimeout: 1,
                swf: App.ctx + "/assets/global/plugins/uploadify/uploadify.swf",
                uploader: App.ctx + "/pub/conf/single/img/upload",
                method: "post",
                //formData: {id: 100},
                overrideEvents: ["onUploadSuccess"],
                onUploadSuccess: function (file, data, response) {
                    // 注意此处返回的是string类型的json，需要转换
                    var data = $.parseJSON(data);
                    var pic_id = this.settings.id.replace("pic_edit_", "");
                    $("#pic_path_" + pic_id).val(data.filePath);
                    $("#pic_name_" + pic_id).val(data.fileName);
                    $("#pic_img_" + pic_id).attr("src", data.fileUrl);
                }
            }
        );
        // 去掉原组件的样式
        $(".uploadify-button").removeClass("uploadify-button").removeAttr("style");
    }

    PageEdit.prototype.initValidate = function () {
        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                pageCode: {required: true},
                page: {required: true},
                conTitle: {required: $("#conTitle").data("required") == "1"},
                conUrl: {required: $("#conUrl").data("required") == "1"},
                conId: {required: $("#conId").data("required") == "1"},
                conP1: {required: $("#conP1").data("required") == "1"},
                conP2: {required: $("#conP2").data("required") == "1"},
                conP3: {required: $("#conP3").data("required") == "1"},
                conP4: {required: $("#conP4").data("required") == "1"},
                conP5: {required: $("#conP5").data("required") == "1"},
                conDesc: {required: $("#conDesc").data("required") == "1"}
            },
            messages: {
                pageCode: {required: "页面编码必填！"},
                page: {required: "页面名称必填！"},
                conTitle: {required: "该项必填！"},
                conUrl: {required: "该项必填！"},
                conId: {required: "该项必填！"},
                conP1: {required: "该项必填！"},
                conP2: {required: "该项必填！"},
                conP3: {required: "该项必填！"},
                conP4: {required: "该项必填！"},
                conP5: {required: "该项必填！"},
                conDesc: {required: "该项必填！"}
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

    PageEdit.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        var pic_ary = [];
        $(".thumbnail").each(function (index, value) {
            pic_ary.push($(value).data("id"));
        });
        data["pic_ary"] = pic_ary.join(",");
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
            url: App.ctx + "/pub/conf/single/save",
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

    module.exports = PageEdit;

});