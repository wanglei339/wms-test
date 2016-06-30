define(function (require, exports, module) {

    var OsEdit = require("./os_edit");
    var tpl_action = require('tpls/table_op.tpl');

    var UpOs = function () {
        this.up_version = new OsEdit();
        this.el_content_list = "#content_os_list";
        this.el_query_form = "#form_os_query";
        this.el_btn_batch_edit = "#btn_os_new";
        this.el_btn_query = "#btn_os_query";
        this.el_table = "#table_os_list";
    }

    UpOs.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    UpOs.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
    }

    UpOs.prototype.initEvents = function () {
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
    }

    UpOs.prototype.initQuery = function () {
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

    UpOs.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    UpOs.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "操作系统编码", "data": "osCode"},
                {"targets": 2, "title": "操作系统名称", "data": "osName"},
                {
                    "targets": 3, "title": "操作系统类型",
                    "render": function (data, type, full, meta) {
                        if (full.osType == 1) {
                            return 'PC端';
                        } else if (full.osType == 2) {
                            return '移动端';
                        } else if (full.osType == 3) {
                            return 'TV端';
                        } else {
                            return '未知';
                        }
                    }
                },
                {
                    "targets": 4, "title": "状态",
                    "render": function (data, type, full, meta) {
                        if (full.status == 1) {
                            return '<span class="label label-success">有效</span>';
                        } else {
                            return '<span class="label label-danger">无效</span>';
                        }
                    }
                },
                {
                    "targets": 5, "title": "操作",
                    "render": function (data, type, full, meta) {
                        return _.template(tpl_action)({
                            id: full.id,
                            name: "",
                            ops: [
                                {color: "blue", sign: "op_edit", btnName: "修改"}
                            ]
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/up/os/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            }
        });
    }

    UpOs.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    UpOs.prototype.opNew = function () {
        var that = this;
        that.up_version.setOptions({
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
        that.up_version.init();
    }

    UpOs.prototype.opEdit = function (data) {
        var that = this;
        that.up_version.setOptions({
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
        that.up_version.init();
    }

    module.exports = UpOs;

});