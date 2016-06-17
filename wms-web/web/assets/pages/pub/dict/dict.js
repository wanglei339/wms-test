define(function (require, exports, module) {

    var DictEdit = require("./dict_edit");
    var Item = require("./item");
    var tpl_action = require('tpls/table_op.tpl');

    var Dict = function () {
        this.up_version = new DictEdit();
        this.item = new Item();
        this.el_content_list = "#content_dict_list";
        this.el_query_form = "#form_dict_query";
        this.el_btn_batch_edit = "#btn_dict_new";
        this.el_btn_query = "#btn_dict_query";
        this.el_table = "#table_dict_list";
    }

    Dict.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    Dict.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
    }

    Dict.prototype.initEvents = function () {
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
        // 明细
        $(this.el_table).off("click.op_item").on("click.op_item", 'a[data-sign="op_item"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opItem({id: $op_row.data("id")});
        });
        // 上线
        $(this.el_table).off("click.op_online").on("click.op_online", 'a[data-sign="op_online"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opOnline({id: $op_row.data("id")});
        });
    }

    Dict.prototype.initQuery = function () {
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

    Dict.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    Dict.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {
                    "targets": 1, "title": "类型",
                    "render": function (data, type, full, meta) {
                        return full.dictType == 1 ? "列表" : "树型";
                    }
                },
                {"targets": 2, "title": "编码", "data": "dictCode"},
                {"targets": 3, "title": "名称", "data": "dictName"},
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
                        var ops = [
                            {color: "blue", sign: "op_edit", btnName: "修改"},
                            {color: "blue", sign: "op_item", btnName: "明细"}
                        ];
                        if (full.dictType == 1) {
                            ops.push({color: "green", sign: "op_online", btnName: "上线"})
                        }
                        return _.template(tpl_action)({
                            id: full.id,
                            name: "",
                            ops: ops
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/pub/dict/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            }
        });
    }

    Dict.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    Dict.prototype.opNew = function () {
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

    Dict.prototype.opEdit = function (data) {
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

    Dict.prototype.opItem = function (data) {
        var that = this;
        that.item.setOptions({
            dictId: data.id,
            callback_btnSave: function () {
                that.show();
                that.refreshTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        that.item.init();
    }

    Dict.prototype.opOnline = function (row) {
        Metronic.blockUI({message: "正在上线，请稍后...", target: ".page-content"});
        $.ajax({
            url: App.ctx + "/pub/dict/online",
            type: "post",
            data: {id: row.id},
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(".page-content");
                if (rsObj.rsCode == "1") {
                    toastr["success"]("上线成功！", "提示信息");
                } else {
                    toastr["error"](rsObj.rsMsg || "上线失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Metronic.unblockUI(".page-content");
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    module.exports = Dict;

});