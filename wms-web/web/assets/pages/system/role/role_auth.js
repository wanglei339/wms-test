define(function (require, exports, module) {

    var RoleAuth = function () {
        this.el_content_edit = "#content_role_auth";
        this.el_div_edit = "#div_role_auth";
        this.el_form = "#form_role_auth";
        this.el_btn_save = "#btn_role_auth_save";
        this.el_btn_back = "#btn_role_auth_back";
    }

    RoleAuth.prototype.init = function () {
        this.show();
        this.initTree();
        this.initEvents();
    }

    RoleAuth.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    RoleAuth.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
    }

    RoleAuth.prototype.initTree = function () {
        var that = this;
        $(this.el_div_edit).jstree('destroy');
        $(this.el_div_edit).jstree({
            "plugins": ["wholerow", "checkbox", "types"],
            "core": {
                "check_callback": true,
                "themes": {"responsive": false},
                "data": {
                    "type": "POST",
                    "url": App.ctx + "/system/role/auth/tree",
                    "data": function (node) {
                        return {"id": that.options.id};
                    }
                }
            },
            "types": {
                "default": {"icon": "fa fa-folder icon-state-warning icon-lg"},
                "file": {"icon": "fa fa-file icon-state-warning icon-lg"}
            }
        });
    }

    RoleAuth.prototype.initEvents = function () {
        var that = this;
        $(this.el_div_edit).on('loaded.jstree', function (e, data) {
            $(that.el_div_edit).jstree().open_all();
        });
        $(this.el_btn_save).off("click").on("click", function () {
            that.btnSave();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }

    RoleAuth.prototype.btnSave = function () {
        var that = this;
        var funcIds = $(this.el_div_edit).jstree().get_checked().join(",");
        var params = {
            roleId: this.options.id,
            funcIds: funcIds
        };
        Metronic.blockUI({message: "正在保存...", target: ".page-content"});
        $.ajax({
            url: App.ctx + "/system/role/auth/save",
            type: "post",
            data: params,
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

    module.exports = RoleAuth;

});