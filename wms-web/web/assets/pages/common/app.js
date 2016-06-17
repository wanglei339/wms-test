var App = function () {

    var handleTemplate = function () {
        // 因jsp冲突，将模板匹配字符改为{{}}
        _.templateSettings = {
            evaluate: /\{\{([\s\S]+?)\}\}/g,
            interpolate: /\{\{=([\s\S]+?)\}\}/g,
            escape: /\{\{-([\s\S]+?)\}\}/g
        };
    }

    var handleSeajs = function (ctx) {
        seajs.config({
            // Sea.js 的基础路径
            base: ctx + "/assets/",
            // 文件编码
            charset: 'utf-8'
        });
    }

    var handleToastr = function () {
        // 提示消息插件
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "1000",
            "hideDuration": "1000",
            "timeOut": "2000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
    }

    return {
        ctx: $("body").data("ctx") || "",
        init: function () {
            handleTemplate();
            handleSeajs(this.ctx);
            handleToastr();
        }
    }

}();