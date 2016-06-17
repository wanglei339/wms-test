define(function (require, exports, module) {

    var ItemEdit = require("./mult_item_edit");
    var tpl_action = require('tpls/table_op.tpl');

    var Item = function () {
        this.item_edit = new ItemEdit();
        this.el_content_list = "#content_item_list";
        this.el_query_form = "#form_item_query";
        this.el_btn_batch_edit = "#btn_item_new";
        this.el_btn_back = "#btn_page_back";
        this.el_btn_query = "#btn_item_query";
        this.el_table = "#table_item_list";
    }

    Item.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    Item.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    Item.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
        $("#caption_item_list").html(this.options.pageName);
    }

    Item.prototype.initEvents = function () {
        var that = this;
        // 查询
        this.initQuery();
        // 新增
        $(this.el_btn_batch_edit).off("click").on("click", function () {
            that.opNew();
        });
        // 返回
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
        // 修改
        $(this.el_table).off("click.op_edit").on("click.op_edit", 'a[data-sign="op_edit"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opEdit({id: $op_row.data("id")});
        });
    }

    Item.prototype.initQuery = function () {
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
        $.ajax({
            url: App.ctx + "/pub/conf/mult/item/contype",
            type: "post",
            data: {pageId: this.options.pageId},
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    $("#query_contype").html(rsObj.data);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {

            }
        });
    }

    Item.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    Item.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "类型", "data": "conTypeName"},
                {"targets": 2, "title": "标题", "data": "conTitle"},
                {
                    "targets": 3, "title": "状态",
                    "render": function (data, type, full, meta) {
                        if (full.status == 1) {
                            return '<span class="label label-success">有效</span>';
                        } else {
                            return '<span class="label label-danger">无效</span>';
                        }
                    }
                },
                {
                    "targets": 4, "title": "操作",
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
                "url": App.ctx + "/pub/conf/mult/item/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({pageId: that.options.pageId}, d, that.getQueryData());
                }
            }
        });
    }

    Item.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    Item.prototype.opNew = function () {
        var that = this;
        this.item_edit.setOptions({
            pageId: this.options.pageId,
            dataId: null,
            callback_btnSave: function () {
                that.show();
                that.initTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        this.item_edit.init();
    }

    Item.prototype.opEdit = function (data) {
        var that = this;
        this.item_edit.setOptions({
            pageId: this.options.pageId,
            dataId: data.id,
            callback_btnSave: function () {
                that.show();
                that.refreshTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        this.item_edit.init();
    }

    module.exports = Item;

});