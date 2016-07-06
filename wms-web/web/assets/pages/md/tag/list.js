define(function (require, exports, module) {
    var Formatdate = require("pages/common/date_format");
    var Confirm = require("pages/common/confirm");
    var tpl_action = require('tpls/table_op.tpl');
    var Edit = require("./edit");

    var Page = function () {
        this.formatedate = new Formatdate();
        this.confirm = new Confirm();
        this.edit = new Edit();
        this.el_btn_new = "#btn_page_new";
        this.el_query_form = "#form_search";
    }

    Page.prototype.init = function () {
        this.show();
        this.initEvents();
        this.initTable();
    }

    Page.prototype.show = function () {
        $("div[data-sign=content]").hide();
        $("#content_page_list").show();
    }

    Page.prototype.initEvents = function () {
        var that = this;
        // 搜索
        $("#btn_search").off("click").on("click", function () {
            that.refreshTable();
        });
        $(this.el_query_form).find('input[type="text"]').each(function () {
            $(this).off("keypress").on("keypress", function (event) {
                if (event.which == 13) {
                    that.refreshTable();
                    event.preventDefault();
                }
            });
        });
        // 禁用
        $("#table_list").off("click.status").on("click.status", "a[data-sign=op_status]", function () {
            var $op = $(this).closest("span[data-sign=op_row]");
            var $a = $(this).closest("a[data-sign=op_status]");
            that.editStatus({
                id: $op.data("id"),
                status: $a.data("status")
            });
        });
        // 新增
        $(this.el_btn_new).off("click").on("click", function () {
            that.opNew();
        });
        //编辑
        $("#table_list").off("click.edit").on("click.edit", "a[data-sign=op_edit]", function () {
            var $op = $(this).closest("span[data-sign=op_row]");
            that.opEdit({
                id: $op.data("id")
            });
        });

        $("form#form_search #busType").change(function () {
            that.busTypeChanged();
        });
        $("#tagTypeId").change(function () {
            that.refreshTable();
        });
    }
    Page.prototype.busTypeChanged = function () {
        var that = this;
        $.ajax({
            url: App.ctx + "/md/tag/getTagTypeList",
            type: "post",
            data: {busType:$("form#form_search #busType").val()}
        }).done(function (data) {
            var selector=$("form#form_search #tagTypeId");
            selector.empty();
            if(data.list){
                console.log("data.list="+data.list.length);
                selector.append('<option value="">'+'全部'+'</option>');
                $.each(data.list, function(i, item) {
                    selector.append('<option value="'+item.id+'">'+item.itemName+'</option>');
                });
            }
            that.refreshTable();
        }).always(function () {
            Metronic.unblockUI(".page-content");
        });
    }

    Page.prototype.initTable = function () {
        var that = this;
        this.dataTable = $('#table_list').dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {
                    "targets": 1, "title": "业务类型",
                    "render": function (data, type, full, meta) {
                        if(full.busType == 1) {
                            return '视频标签';
                        }else if(full.busType == 2) {
                            return '音乐标签';
                        }else if(full.busType == 3) {
                            return '明星标签';
                        }else{
                            return '';
                        }
                    }
                },
                {"targets": 2, "title": "标签分类", "data": "tagTypeName"},
                {"targets": 3, "title": "标签名称", "data": "tagName"},
                {
                    "targets": 4, "title": "状态",
                    "render": function (data, type, full, meta) {
                        if(full.status) {
                            return '<span class="label label-success">正常</span>';
                        }else{
                            return '<span class="label label-danger">无效</span>';
                        }
                    }
                },
                {
                    "targets": 5, "title": "操作",
                    "render": function (data, type, full, meta) {
                        var ops = [];
                        ops.push({color: "blue", sign: "op_edit", id: "", name: "", btnName: "编辑"});
                        if(full.status != 0) {
                            ops.push({color: "blue", sign: "op_status", id: "", name: "",status:"0", btnName: "禁用"});
                        }else{
                            ops.push({color: "blue", sign: "op_status", id: "", name: "",status:"1", btnName: "启用"});
                        }
                        return _.template(tpl_action)({
                            id: full.id,
                            ops: ops
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/md/tag/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryParam());
                }
            }
        });
    }

    Page.prototype.getQueryParam = function () {
        var data = {};
        $("#form_search").find("input[type=hidden],input[type=text],input[type=checkbox]:selected,select").each(
            function () {
                data[this.name] = $(this).val();
            }
        );
        return data;
    }

    //刷新表格
    Page.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }
    Page.prototype.editStatus = function (data) {
        var that = this;
        $.post(App.ctx + "/md/tag/editStatus",data,function(result){
            if (result.rsCode == 1) {
                toastr["success"](result.rsMsg, "操作成功！");
                that.refreshTable();
            } else {
                toastr["error"](result.rsMsg, "操作失败！");
            }
        });
    }

    Page.prototype.opNew = function () {
        var that = this;
        that.edit.setOptions({
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
        that.edit.init();
    }

    //查看
    Page.prototype.opEdit = function (data) {
        var that = this;
        that.edit.setOptions({
            id: data.id,
            callback_btnSave: function () {
                that.show();
                that.initTable();
            },
            callback_btnBack: function () {
                that.show();
                that.refreshTable();
            }
        });
        that.edit.init();
    }
    module.exports = Page;

});