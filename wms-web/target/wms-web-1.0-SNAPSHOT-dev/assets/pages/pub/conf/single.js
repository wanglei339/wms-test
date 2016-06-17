define(function (require, exports, module) {

    var PageEdit = require("./single_edit");
    var ShowEdit = require("./single_show_edit");
    var tpl_action = require('tpls/table_op.tpl');

    var Page = function () {
        this.page_edit = new PageEdit();
        this.show_edit = new ShowEdit();
        this.el_content_list = "#content_page_list";
        this.el_query_form = "#form_page_query";
        this.el_btn_batch_edit = "#btn_page_new";
        this.el_btn_query = "#btn_page_query";
        this.el_table = "#table_page_list";
    }

    Page.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    Page.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_list).show();
    }

    Page.prototype.initEvents = function () {
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
        // 显示设置
        $(this.el_table).off("click.op_show").on("click.op_show", 'a[data-sign="op_show"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opShow({id: $op_row.data("id")});
        });
        // 上线
        $(this.el_table).off("click.op_online").on("click.op_online", 'a[data-sign="op_online"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opOnline({id: $op_row.data("id")});
        });
    }

    Page.prototype.initQuery = function () {
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

    Page.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    Page.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "页面编码", "data": "pageCode"},
                {"targets": 2, "title": "页面名称", "data": "pageName"},
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
                                {color: "blue", sign: "op_edit", btnName: "修改"},
                                {color: "blue", sign: "op_show", btnName: "设置"},
                                {color: "green", sign: "op_online", btnName: "上线"}
                            ]
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/pub/conf/single/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            }
        });
    }

    Page.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    Page.prototype.opNew = function () {
        var that = this;
        that.page_edit.setOptions({
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
        that.page_edit.init();
    }

    Page.prototype.opEdit = function (data) {
        var that = this;
        that.page_edit.setOptions({
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
        that.page_edit.init();
    }

    Page.prototype.opShow = function (data) {
        var that = this;
        that.show_edit.setOptions({
            pageId: data.id,
            callback_btnSave: function () {
                that.show();
            },
            callback_btnBack: function () {
                that.show();
            }
        });
        that.show_edit.init();
    }

    Page.prototype.opOnline = function (row) {
        Metronic.blockUI({message: "正在上线，请稍后...", target: ".page-content"});
        $.ajax({
            url: App.ctx + "/pub/conf/single/online",
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

    module.exports = Page;

});