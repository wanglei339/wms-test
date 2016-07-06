define(function (require, exports, module) {

    var VersionEdit = require("./ver_edit");
    var VersionEditBatch = require("./ver_edit_batch");
    var VersionRule = require("../rule/rule");
    var tpl_action = require('tpls/table_op.tpl');
    var Confirm = require("pages/common/confirm");

    var UpVersion = function () {
        this.ver_edit = new VersionEdit();
        this.ver_edit_batch = new VersionEditBatch();
        this.ver_rule = new VersionRule();
        this.el_content_list = "#content_ver_list";
        this.el_query_form = "#form_ver_query";
        this.el_btn_batch_edit = "#btn_ver_batch_edit";
        this.el_btn_batch_enable = "#btn_ver_batch_enable";
        this.el_btn_batch_disable = "#btn_ver_batch_disable";
        this.el_btn_query = "#btn_ver_query";
        this.el_table = "#table_ver_list";
        this.confirm = new Confirm();
    }

    UpVersion.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    UpVersion.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    UpVersion.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
    }

    UpVersion.prototype.initEvents = function () {
        var that = this;
        // 查询
        this.initQuery();
        // 批量修改
        $(this.el_btn_batch_edit).off("click").on("click", function () {
            that.opBatchEdit();
        });
        // 批量启用
        $(this.el_btn_batch_enable).off("click").on("click", function () {
            that.confirm.show({info: "批量启用"}, function () {
                that.opBatchEnable();
            });
        });
        // 批量停用
        $(this.el_btn_batch_disable).off("click").on("click", function () {
            that.confirm.show({info: "批量停用"}, function () {
                that.opBatchDisable();
            });
        });
        // 修改
        $(this.el_table).off("click.op_edit").on("click.op_edit", 'a[data-sign="op_edit"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opEdit({id: $op_row.data("id")});
        });
        // 启用停用
        $(this.el_table).off("click.op_status").on("click.op_status", 'a[data-sign="op_status"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opStatus({id: $op_row.data("id")});
        });
        // 规则
        $(this.el_table).off("click.op_rule").on("click.op_rule", 'a[data-sign="op_rule"]', function () {
            alert("该功能暂时停用!");
            return;
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opRule({id: $op_row.data("id")});
        });

    }

    UpVersion.prototype.initQuery = function () {
        var that = this;
        // 查询
        $(this.el_btn_query).off("click").on("click", function () {
            that.initTable();
        });
        // 如果查询表单中只有一个input[type=text]，按回车会自动提交，变成触发查询事件
        $(this.el_query_form).find('input[type="text"]').each(function () {
            $(this).off("keypress").on("keypress", function (event) {
                if (event.which == 13) {
                    that.initTable();
                    event.preventDefault();
                }
            });
        });
        $(this.el_query_form).find('select').each(function () {
            $(this).off("change").on("change", function (event) {
                that.initTable();
            });
        });
    }

    UpVersion.prototype.getQueryData = function () {
        var data = {
            appId: this.options.appId,
            osId: this.options.osId,
            verStr: this.options.verStr
        };
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    UpVersion.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "包编码", "data": "appKey"},
                {"targets": 2, "title": "包名称", "data": "pkgName"},
                {"targets": 3, "title": "版本名称", "data": "verName"},
                {
                    "targets": 4, "title": "文件状态",
                    "render": function (data, type, full, meta) {
                        if (full.fileStatus == 1) {
                            return '<span class="label label-success">有效</span>';
                        } else {
                            return '<span class="label label-danger">无效</span>';
                        }
                    }
                },
                {
                    "targets": 5, "title": "状态",
                    "render": function (data, type, full, meta) {
                        if (full.status == 1) {
                            return '<span class="label label-success">有效</span>';
                        } else {
                            return '<span class="label label-danger">无效</span>';
                        }
                    }
                },
                {
                    "targets": 6, "title": "操作",
                    "render": function (data, type, full, meta) {
                        var op_status = "启用";
                        var op_status_color = "green";
                        if (full.status == 1) {
                            op_status = "停用";
                            op_status_color = "red";
                        }
                        return _.template(tpl_action)({
                            id: full.id,
                            name: "",
                            ops: [
                                {color: "blue", sign: "op_edit", btnName: "修改"},
                                {color: op_status_color, sign: "op_status", btnName: op_status},
                                {color: "default", sign: "op_rule", btnName: "规则"}
                            ]
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/up/ver/ver/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            }
        });
    }

    UpVersion.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    UpVersion.prototype.opBatchEdit = function (data) {
        if(_.isEmpty(this.options.verStr)){
            toastr["error"]("无版本信息，不能批量操作！", "提示信息");
            return;
        }
        var that = this;
        that.ver_edit_batch.setOptions({
            appId: this.options.appId,
            osId: this.options.osId,
            verStr: this.options.verStr,
            callback_btnSave: function () {
                that.show();
                that.refreshTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        that.ver_edit_batch.init();
    }

    UpVersion.prototype.opBatchEnable = function () {
        if(_.isEmpty(this.options.verStr)){
            toastr["error"]("无版本信息，不能批量操作！", "提示信息");
            return;
        }
        var that = this;
        $.ajax({
            url: App.ctx + "/up/ver/status/enable/batch",
            type: "post",
            data: {
                appId: this.options.appId,
                osId: this.options.osId,
                verStr: this.options.verStr
            },
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"](rsObj.rsMsg, "提示信息");
                    that.refreshTable();
                } else {
                    toastr["error"](rsObj.rsMsg || "批量启用失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    UpVersion.prototype.opBatchDisable = function () {
        if(_.isEmpty(this.options.verStr)){
            toastr["error"]("无版本信息，不能批量操作！", "提示信息");
            return;
        }
        var that = this;
        $.ajax({
            url: App.ctx + "/up/ver/status/disable/batch",
            type: "post",
            data: {
                appId: this.options.appId,
                osId: this.options.osId,
                verStr: this.options.verStr
            },
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"]("批量停用成功！", "提示信息");
                    that.refreshTable();
                } else {
                    toastr["error"](rsObj.rsMsg || "批量停用失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    UpVersion.prototype.opEdit = function (data) {
        var that = this;
        that.ver_edit.setOptions({
            verId: data.id,
            callback_btnSave: function () {
                that.show();
                that.refreshTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        that.ver_edit.init();
    }

    UpVersion.prototype.opStatus = function (data) {
        var that = this;
        $.ajax({
            url: App.ctx + "/up/ver/status/edit",
            type: "post",
            data: {verId: data.id},
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"]("操作成功！", "提示信息");
                    that.refreshTable();
                } else {
                    toastr["error"](rsObj.rsMsg || "操作失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Metronic.unblockUI(".page-content");
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    UpVersion.prototype.opRule = function (data) {
        var that = this;
        that.ver_rule.setOptions({
            verId: data.id,
            callback_btnSave: function () {
                that.show();
            },
            callback_btnBack: function () {
                that.show();
            }
        });
        that.ver_rule.init();
    }

    module.exports = UpVersion;

});