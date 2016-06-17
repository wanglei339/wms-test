define(function (require, exports, module) {

    var VerRule01 = require("./rule_01");

    var VersionRule = function () {
        this.rule_01 = new VerRule01();
        this.el_content_edit = "#content_ver_edit";
        this.el_div_edit = "#div_ver_edit";
        this.el_div = "#div_rule";
        this.el_btn_cache = "#btn_rule_cache";
        this.el_btn_back = "#btn_rule_back";
    }

    VersionRule.prototype.init = function () {
        this.show();
        this.loadContent();
    }

    VersionRule.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    VersionRule.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
        $("#title_ver").html("版本规则");
    }

    VersionRule.prototype.loadContent = function () {
        var that = this;
        var url = App.ctx + "/up/rule/page/rule" + " " + this.el_div;
        var params = {
            verId: this.options.verId
        };
        $(this.el_div_edit).empty();
        Metronic.blockUI({target: ".page-content"});
        $(this.el_div_edit).load(url, params, function (responseText, textStatus, XMLHttpRequest) {
            Metronic.unblockUI(".page-content");
            that.loadContentFinish();
        });
    }

    VersionRule.prototype.loadContentFinish = function () {
        // 初始化事件
        this.initEvents();
        // 初始化表单
        Metronic.initComponents();
        // 初始化规则
        this.initRules();
    }

    VersionRule.prototype.initEvents = function () {
        var that = this;
        $(this.el_btn_cache).off("click").on("click", function () {
            that.updateRuleCache();
        });
        $(this.el_btn_back).off("click").on("click", function () {
            that.options.callback_btnBack();
        });
    }

    VersionRule.prototype.initRules = function () {
        this.rule_01.init();
    }

    VerRule01.prototype.updateRuleCache = function () {
        $.ajax({
            url: App.ctx + "/up/rule/cache",
            type: "post",
            data: {},
            success: function (rsObj, textStatus, jqXHR) {
                if (rsObj.rsCode == "1") {
                    toastr["success"]("更新成功！", "提示信息");
                } else {
                    toastr["error"](rsObj.rsMsg || "更新失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    module.exports = VersionRule;

});