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
        var url = App.ctx + "/md/star/page/view" + " " + this.el_form;
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
        this.initUpload();
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

        $("#form_page").off("click.pic_del").on("click.pic_del", 'div[sign="delimg_div"] a', function () {
            var imageType = $(this).attr("type-id");
            that.deleteImg(imageType);
        });
    }

    PageEdit.prototype.deleteImg = function (imageType) {
        if($("#id").val()){
            //数据库删除
            $.ajax({
                url: App.ctx + "/md/star/deleteImage",
                type: "post",
                data: {starId:$("#id").val(),imageType: imageType}
            }).done(function (data) {
                if (data.rsCode == "1") {
                    toastr["success"]("删除成功！", "提示信息");
                    $("#img" + imageType).attr("src","");
                    $("#img" + imageType).attr("filePath","");
                    $("#delimg_div").css("display","none");
                    $("#delimg_div_"+imageType).css("display","none");
                } else {
                    toastr["error"](data.rsMsg || "删除失败！", "提示信息");
                }
            }).always(function () {
                Metronic.unblockUI(".page-content");
            });
        }else{
            $("#img" + imageType).attr("src","");
            $("#img" + imageType).attr("filePath","");
            $("#delimg_div_"+imageType).css("display","none");
        }
    }
    PageEdit.prototype.initUpload = function () {
        $(":button[sign=upimg_btn]").each(function(){
            var imgType = this.id.split("_")[2];
            $(this).uploadify({
                width: "56",
                height: "34",
                buttonClass: "btn blue",
                buttonText: $(this).text(),
                fileTypeDesc: "请选择图片！",
                fileTypeExts: "*.gif; *.jpg; *.png",
                fileObjName: "file",   //服务器接收文件时的参数名
                removeTimeout: 1,
                swf: App.ctx + "/assets/global/plugins/uploadify/uploadify.swf",
                uploader: App.ctx + "/util/upload/file",
                method: "post",
                formData: {appkey:"",busicode: "md"},
                overrideEvents: ["onUploadSuccess"],
                onUploadSuccess: function (file, data, response) {
                    // 注意此处返回的是string类型的json，需要转换
                    var data = $.parseJSON(data);
                    $("#img"+imgType).attr("src",data.fileUrl);
                    $("#img"+imgType).attr("filePath",data.filePath);
                    $("#img"+imgType).attr("fileName",data.fileName);
                    $("#delimg_div_"+imgType).css("display","block");
                }
            })
        })
        // 去掉原组件的样式
        $(".uploadify-button").removeClass("uploadify-button").removeAttr("style");
    }
    PageEdit.prototype.initValidate = function () {
        $.validator.addMethod("intMust", function (value, element) {
            return this.optional(element) || /^[0-9]*[1-9][0-9]*$/i.test(value);
        }, "必须填写正整数！");

        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                name: {required: true}
            },
            messages: {
                name: {required: "中文艺名必填！"}
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

        //获取图片字符串
        var imagestr = "";
        $("#form_page").find("img[sign=star_img]").each(function(i){
            if($(this).attr("filePath")){
                imagestr += $(this).attr("id").substring("3") + "," + $(this).attr("filePath") + "@";
            }
        })
        if(imagestr){
            imagestr = imagestr.substring(0,imagestr.length-1);
        }
        console.log("imagestr=" + imagestr);

        //获取标签
        var tags = "";
        $("#form_page").find("input[name=tag]").each(function(i){
            if(this.checked){
                tags += $(this).attr("id") + "," + $(this).attr("tagType") + "@";
            }
        })
        if(tags){
            tags = tags.substring(0,tags.length-1);
        }
        console.log("tags=" + tags);

        //获取角色
        var roles = "";
        $("#form_page").find("input[name=role]").each(function(i){
            if(this.checked){
                roles += $(this).attr("id") + "@";
            }
        })
        if(roles){
            roles = roles.substring(0,roles.length-1);
        }
        console.log("roles=" + roles);

        var that = this;
        var data = that.getFormData();
        data.imagestr = imagestr;
        data.tags = tags;
        data.roles = roles;

        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        // 保存请求
        $.ajax({
            url: App.ctx + "/md/star/save",
            type: "post",
            data: data,
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