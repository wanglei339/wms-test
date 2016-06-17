define(function (require, exports, module) {

    var modal_fac = require("pages/common/modal_fac");
    var tpl_action = require('tpls/table_op.tpl');

    var VerRule01 = function () {
        this.ruleId = 1;
        this.el_btn_rule_save = "#btn_rule_save_01";
        this.el_btn_con_new = "#btn_con_new_01";
        this.el_table = "#table_con_list_01";
        this.el_form = "#form_con";
    }

    VerRule01.prototype.init = function () {
        this.initEvents();
        this.initModal();
        this.initVerRule();
        this.initTable();
    }

    VerRule01.prototype.initEvents = function () {
        var that = this;
        // 保存
        $(this.el_btn_rule_save).off("click").on("click", function () {
            that.opRuleSave();
        });
        // 新增
        $(this.el_btn_con_new).off("click").on("click", function () {
            that.opConNew();
        });
        // 修改
        $(this.el_table).off("click.op_edit").on("click.op_edit", 'a[data-sign="op_edit"]', function () {
            var $op_row = $(this).closest('span[data-sign="op_row"]');
            that.opConEdit({id: $op_row.data("id")});
        });
        // 删除，见表格初始化完成
    }

    VerRule01.prototype.initVerRule = function () {
        $.ajax({
            url: App.ctx + "/up/rule/verrule",
            type: "post",
            data: {
                ruleId: this.ruleId,
                verId: $("#verId").val()
            },
            success: function (rsObj, textStatus, jqXHR) {
                $("#judgeWay_01").val(rsObj.versionRules.judgeWay);
            },
            error: function (jqXHR, textStatus, errorThrown) {

            }
        });
    }

    VerRule01.prototype.initTable = function () {
        var that = this;
        this.dataTable = $(this.el_table).dataTable({
            "columnDefs": [
                {"targets": 0, "title": "ID", "data": "id"},
                {"targets": 1, "title": "大于版本", "data": "minValueName"},
                {"targets": 2, "title": "小于版本", "data": "maxValueName"},
                {
                    "targets": 3, "title": "操作",
                    "render": function (data, type, full, meta) {
                        return _.template(tpl_action)({
                            id: full.id,
                            name: "",
                            ops: [
                                {color: "blue", sign: "op_edit", btnName: "修改"},
                                {color: "blue", sign: "op_del", btnName: "删除"}
                            ]
                        });
                    }
                }
            ],
            "ajax": {
                "url": App.ctx + "/up/rule/con/list",
                "type": "POST",
                "data": function (d) {
                    return $.extend({}, d, {
                        ruleId: that.ruleId,
                        verId: $("#verId").val()
                    });
                }
            },
            "drawCallback": function (settings) {
                $(that.dataTable).find('a[data-sign="op_del"]').confirmation({
                    placement: "top",
                    title: "确认删除？",
                    singleton: true,
                    popout: true,
                    btnOkLabel: "删除",
                    btnOkClass: "btn btn-sm btn-success",
                    btnCancelLabel: "取消",
                    btnCancelClass: "btn btn-sm btn-danger",
                    onConfirm: function (event, $element) {
                        var $op_row = $element.closest('span[data-sign="op_row"]');
                        that.opConDel({id: $op_row.data("id")});
                    }
                });
            }
        });
    }

    VerRule01.prototype.refreshTable = function () {
        if (this.dataTable) {
            this.dataTable.fnDraw();
        }
    }

    VerRule01.prototype.initModal = function () {
        this.modal = modal_fac.createModal({
            id: "modal_con_edit",
            btnAry: [{id: "btn_con_save", color: "blue", name: "保存"}]
        });
        var that = this;
        $("#btn_con_save").off("click.modal").on("click.modal", function () {
            that.opConSave();
        });
    }

    VerRule01.prototype.opRuleSave = function () {
        $.ajax({
            url: App.ctx + "/up/rule/verrule/save",
            type: "post",
            data: {
                ruleId: this.ruleId,
                verId: $("#verId").val(),
                judgeWay: $("#judgeWay_01").val()
            },
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"]("保存成功！", "提示信息");
                } else {
                    toastr["error"](rsObj.rsMsg || "保存失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    VerRule01.prototype.opConNew = function () {
        var that = this;
        var params = {
            ruleId: this.ruleId,
            verId: $("#verId").val()
        };
        this.modal.find(".modal-title").html("新增条件");
        $('body').modalmanager('loading');
        this.modal.find('.modal-body').load(App.ctx + '/up/rule/page/con/edit #form_con', params, function () {
            $('body').modalmanager('removeLoading');
            that.modal.modal();
            that.initValidate();
        });
    }

    VerRule01.prototype.initValidate = function () {
        $.validator.addMethod("minValueName", function (value, element) {
            var reg = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
            if (reg.test(value)) {
                var group = value.match(reg);
                if (group[1] == 0 && group[2] == 0 && group[3] == 0) {
                    return false;
                }
                return true;
            }
            return false;
        }, "请输入正确的版本号！");
        $.validator.addMethod("maxValueName", function (value, element) {
            var minValue = $("#minValueName_01").val();
            var maxValue = $("#maxValueName_01").val();
            var reg = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
            if (reg.test(minValue) && reg.test(maxValue)) {
                var min_group = minValue.match(reg);
                var max_group = maxValue.match(reg);
                var minValueInt = min_group[1] * 1000000 + min_group[2] * 1000 + min_group[3];
                var maxValueInt = max_group[1] * 1000000 + max_group[2] * 1000 + max_group[3];
                if (minValueInt <= maxValueInt) {
                    return true;
                }
            }
            return false;
        }, "[大于版本]要比[小于版本]大！");
        this.validate = $(this.el_form).validate({
            errorElement: 'span',
            errorClass: 'help-block help-block-error',
            rules: {
                minValueName: {minValueName: true},
                maxValueName: {minValueName: true, maxValueName: true}
            },
            messages: {},
            highlight: function (element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            unhighlight: function (element) {
                $(element).closest('.form-group').removeClass('has-error');
            },
            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
            },
            errorPlacement: function (error, element) { // render error placement for each input type
                if (element.parent(".input-group").size() > 0) {
                    error.insertAfter(element.parent(".input-group"));
                } else if (element.attr("data-error-container")) {
                    error.appendTo(element.attr("data-error-container"));
                } else if (element.parents('.radio-list').size() > 0) {
                    error.appendTo(element.parents('.radio-list').attr("data-error-container"));
                } else if (element.parents('.radio-inline').size() > 0) {
                    error.appendTo(element.parents('.radio-inline').attr("data-error-container"));
                } else if (element.parents('.checkbox-list').size() > 0) {
                    error.appendTo(element.parents('.checkbox-list').attr("data-error-container"));
                } else if (element.parents('.checkbox-inline').size() > 0) {
                    error.appendTo(element.parents('.checkbox-inline').attr("data-error-container"));
                } else {
                    error.insertAfter(element);
                }
            }
        });
    }

    VerRule01.prototype.getFormData = function () {
        var data = {};
        $(this.el_form).find('input[type="hidden"],input[type="text"],input[type="password"],textarea,select').each(function () {
            data[this.name] = $(this).val();
        });
        $(this.el_form).find('input[type="radio"]:checked').each(function () {
            data[this.name] = $(this).val();
        });
        return data;
    }

    VerRule01.prototype.opConSave = function () {
        if (!this.validate.form()) {
            this.validate.focusInvalid();
            return;
        }
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: ".modal-body"});
        $.ajax({
            url: App.ctx + "/up/rule/con/save",
            type: "post",
            data: that.getFormData(),
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(".modal-body");
                that.modal.modal("hide");
                if (rsObj.rsCode == "1") {
                    toastr["success"](rsObj.rsMsg || "保存成功！", "提示信息");
                    that.refreshTable();
                } else {
                    toastr["error"](rsObj.rsMsg || "保存失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Metronic.unblockUI(".modal-body");
                that.modal.modal("hide");
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    VerRule01.prototype.opConEdit = function (data) {
        var that = this;
        var params = {
            conId: data.id,
            ruleId: this.ruleId,
            verId: $("#verId").val()
        };
        this.modal.find(".modal-title").html("修改条件");
        $('body').modalmanager('loading');
        this.modal.find('.modal-body').load(App.ctx + '/up/rule/page/con/edit #form_con', params, function () {
            $('body').modalmanager('removeLoading');
            that.modal.modal();
            that.initValidate();
        });
    }

    VerRule01.prototype.opConDel = function (data) {
        var that = this;
        $.ajax({
            url: App.ctx + "/up/rule/con/del",
            type: "post",
            data: {conId: data.id},
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"](rsObj.rsMsg || "删除成功！", "提示信息");
                    that.refreshTable();
                } else {
                    toastr["error"](rsObj.rsMsg || "删除失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    module.exports = VerRule01;

});