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
        albumSelectInit($(this.el_query_form+" #albumId"));
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
        // 禁用
        $("#table_list").off("click.status").on("click.status", "a[data-sign=op_status]", function () {
            var $op = $(this).closest("span[data-sign=op_row]");
            that.editStatus({
                id: $op.data("id")
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

        //状态
        $("#status").change(function () {
            that.refreshTable();
        });

        //状态
        $("#albumId").change(function () {
            that.refreshTable();
        });

    }

    Page.prototype.initTable = function () {
        var that = this;
        this.dataTable = $('#table_list').dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "音乐名称", "data": "title"},
                {"targets": 2, "title": "所在专辑", "data": "albumName"},
                {"targets": 3, "title": "时长（秒）", "data": "duration"},
                {
                    "targets": 4, "title": "创建时间",
                    "render": function (data, type, full, meta) {
                        if(full.createdTime)
                            return that.formatedate.format(new Date(full.createdTime), '%Y-%M-%d %H:%m:%s');
                        else
                            return '';
                    }
                },
                {
                    "targets": 5, "title": "操作",
                    "render": function (data, type, full, meta) {
                        var ops = [];
                        ops.push({color: "blue", sign: "op_edit", id: "", name: "", btnName: "编辑"});
                        /*
                        if(full.status != 0) {
                            ops.push({color: "blue", sign: "op_status", id: "", name: "", btnName: "禁用"});
                        }else{
                            ops.push({color: "blue", sign: "op_status", id: "", name: "", btnName: "启用"});
                        }
                        */
                        return _.template(tpl_action)({
                            id: full.id,
                            ops: ops
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/md/music/list",
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
        $.post(App.ctx + "/md/music/editStatus",{id:data.id},function(result){
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
    //专辑下拉框初始化
    function albumSelectInit(el){
        el.select2({
            placeholder: "请输入专辑名称",
            minimumInputLength: 1,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: App.ctx + "/md/music/album/list",
                type: "post",
                data: function (keywords, page) {
                    return {
                        keywords: keywords,
                        page_limit: 1
                    };
                },
                results: function (data, page) {
                    return {
                        results: data.data
                    };
                }
            },
            initSelection: function (element, callback) {
                var id = $(element).val();
                if (id !== "") {
                    var url = App.ctx + "/md/music/album/getInfo";
                    $.ajax(url, {
                        data: {
                            id: id
                        },
                        dataType: "json"
                    }).done(function (obj) {
                        callback(obj);
                    });
                }
            },
            formatResult: function(obj){
                var markup = "<table class='movie-result'><tr>";
                markup += "<td valign='top'><h5>" + obj.title + "</h5>";
                markup += "</td></tr></table>"
                return markup;
            },
            formatSelection: function(obj){
                return obj.title;
            },
            dropdownCssClass: "bigdrop"
        });
    }
    module.exports = Page;

});