define(function (require, exports, module) {

    var RoleEdit = require("./role_edit");
    var RoleAuth = require("./role_auth");
    var tpl_action = require('tpls/table_op.tpl');

    var Role = function () {
        this.role_edit = new RoleEdit();
        this.role_auth = new RoleAuth();
        this.el_content_list = "#content_role_list";
        this.el_query_form = "#form_role_query";
        this.el_btn_batch_edit = "#btn_role_new";
        this.el_btn_query = "#btn_role_query";
        this.el_table = "#table_role_list";
    }

    Role.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    Role.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
    }

    Role.prototype.initEvents = function () {
        var that = this;
        // 查询
        this.initQuery();
        // 新增
        $(this.el_btn_batch_edit).off("click").on("click", function () {
            that.opNew();
        });
        // 修改
        $(this.el_table).off("click.op_edit").on("click.op_edit", 'a[data-sign="op_edit"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opEdit({id: $op_row.data("id")});
        });
        // 分配权限
        $(this.el_table).off("click.op_auth").on("click.op_auth", 'a[data-sign="op_auth"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opAuth({id: $op_row.data("id")});
        });
    }

    Role.prototype.initQuery = function () {
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

    Role.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    Role.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "角色名", "data": "roleName"},
                {
                    "targets": 2, "title": "操作",
                    "render": function (data, type, full, meta) {
                        return _.template(tpl_action)({
                            id: full.id,
                            name: "",
                            ops: [
                                {color: "blue", sign: "op_edit", btnName: "修改"},
                                {color: "blue", sign: "op_auth", btnName: "授权"}
                            ]
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/system/role/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            }
        });
    }

    Role.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    Role.prototype.opNew = function () {
        var that = this;
        this.role_edit.setOptions({
            id: '',
            callback_btnSave: function () {
                that.show();
                that.initTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        this.role_edit.init();
    }

    Role.prototype.opEdit = function (data) {
        var that = this;
        this.role_edit.setOptions({
            id: data.id,
            callback_btnSave: function () {
                that.show();
                that.refreshTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        this.role_edit.init();
    }

    Role.prototype.opAuth = function (data) {
        var that = this;
        this.role_auth.setOptions({
            id: data.id,
            callback_btnSave: function () {
                that.show();
            },
            callback_btnBack: function () {
                that.show();
            }
        });
        this.role_auth.init();
    }

    module.exports = Role;

});