define(function (require, exports, module) {

    var UpVersion = require("./ver.js");
    var VersionNew = require("./ver_new.js");
    //var tpl_action = require('tpls/table_op.tpl');

    var UpVersionNo = function () {
        this.ver = new UpVersion();
        this.ver_new = new VersionNew();
        this.el_query_form = "#form_ver_no_query";
        this.el_btn_batch_edit = "#btn_ver_no_new";
        this.el_table = "#table_ver_no_list";
    }

    UpVersionNo.prototype.init = function () {
        this.initEvents();
        this.initTable();
    }

    UpVersionNo.prototype.initEvents = function () {
        var that = this;
        // 查询
        this.initQuery();
        // 新增
        $(this.el_btn_batch_edit).off("click").on("click", function () {
            that.opNew();
        });
        // 选中版本
        $(this.el_table).off("click.ver_no").on("click.ver_no", 'a[data-sign="ver_no"]', function () {
            var $a = $(this);
            $('a[data-sign="ver_no"].red').removeClass("red");
            $a.addClass("red");
            that.opSelected({verStr: $a.data("ver")});
        });
    }

    UpVersionNo.prototype.initQuery = function () {
        var that = this;
        $(this.el_query_form).find('select').each(function () {
            $(this).off("change").on("change", function (event) {
                that.initTable();
            });
        });
    }

    UpVersionNo.prototype.getQueryData = function () {
        var data = {};
        $(this.el_query_form).find('input[type="text"],select').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    UpVersionNo.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "dom": "<'row'r><'table-scrollable't><'row'<'col-md-12 col-sm-12'p>>",
            "columnDefs": [
                {
                    "targets": 0, "title": "版本号",
                    "render": function (data, type, full, meta) {
                        var tpl = '<a href="javascript:void(0);" class="btn btn-xs" data-sign="ver_no" data-ver="{{=verStr}}">{{=verStr}}</a>';
                        return _.template(tpl)({verStr: full.verStr});
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/up/ver/no/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, that.getQueryData());
                }
            },
            drawCallback: function () {
                var alist = $('a[data-sign="ver_no"]');
                if (alist.length > 0) {
                    // 如果有版本，选中第一个
                    alist.eq(0).trigger("click.ver_no");
                } else {
                    that.opSelected({verStr: null});
                }
            }
        });
    }

    UpVersionNo.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    UpVersionNo.prototype.opNew = function () {
        var that = this;
        var data = this.getQueryData();
        if (_.isEmpty(data.appId) || _.isEmpty(data.osId)) {
            toastr["error"]("请选择应用和系统后新增！", "提示信息");
            return;
        }
        that.ver_new.setOptions({
            appId: data.appId,
            osId: data.osId,
            callback_btnSave: function () {
                that.initTable();
            },
            callback_btnBack: function () {
                that.refreshTable();
            }
        });
        that.ver_new.init();
    }

    UpVersionNo.prototype.opSelected = function (data) {
        var that = this;
        var appData = this.getQueryData();
        that.ver.setOptions({
            appId: appData.appId,
            osId: appData.osId,
            verStr: data.verStr
        });
        that.ver.init();
    }

    module.exports = UpVersionNo;

});