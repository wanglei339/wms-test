define(function (require, exports, module) {

    var FuncEdit = require("./func_edit");

    var Func = function () {
        this.func_edit = new FuncEdit();
        this.el_tree = "#func_tree";
    }

    Func.prototype.init = function () {
        this.initTree();
        this.initEvents();
    }

    Func.prototype.initTree = function () {
        var that = this;
        $(this.el_tree).jstree({
            "plugins": ["contextmenu", "dnd", "types"],
            "core": {
                "check_callback": true,
                "themes": {"responsive": false},
                "data": {
                    "url": App.ctx + "/system/func/tree",
                    "type": "POST",
                    "dataType": "json",
                    "data": function (node) {
                        return {"id": node.id};
                    }
                },
                "check_callback" : function (operation, node, parent, position, more) {
                    if(operation === "move_node") {
                        if(parent.original.isLeaf == 1) {
                            // 禁止移动到叶子节点
                            return false;
                        }
                    }
                    return true;
                }
            },
            "contextmenu": {
                "select_node": false,
                "items": function (node) {
                    var menus = {}
                    if (node.original.isLeaf == 0) {
                        menus["create"] = {
                            "label": "增加子节点",
                            "action": function (obj) {
                                that.nodeAdd(node, obj);
                            }
                        };
                    }
                    return menus;
                }
            },
            "types": {
                "default": {"icon": "fa fa-folder icon-state-warning icon-lg"},
                "file": {"icon": "fa fa-file icon-state-warning icon-lg"}
            }
        });
    }

    Func.prototype.initEvents = function () {
        var that = this;
        $(this.el_tree).on("select_node.jstree", function (e, data) {
            that.nodeEdit(e, data);
        });
        $(this.el_tree).on("move_node.jstree", function (e, data) {
            that.nodeMove(e, data);
        });
        $(this.el_tree).on("loaded.jstree", function (e, data) {
            data.instance.select_node("0");
        });
    }

    Func.prototype.nodeAdd = function (node, obj) {
        var that = this;
        this.func_edit.setOptions({
            id: null,
            parentId: node.id,
            callback_btnSave: function () {
                $(that.el_tree).jstree(true).refresh();
            }
        });
        this.func_edit.init();
    }

    Func.prototype.nodeEdit = function (e, data) {
        var that = this;
        this.func_edit.setOptions({
            id: data.node.id,
            parentId: null,
            callback_btnSave: function () {
                data.instance.refresh();
            }
        });
        this.func_edit.init();
    }

    Func.prototype.nodeMove = function (e, data) {
        var moveParam = {
            funcId: data.node.id,
            parentId: data.parent,
            old_parent: data.old_parent,
            position: data.position + 1
        };
        console.log("moveParam=======" + JSON.stringify(moveParam));
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: that.el_tree});
        $.ajax({
            url: App.ctx + "/system/func/move",
            type: "post",
            data: moveParam,
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(that.el_tree);
                if (rsObj.rsCode == "1") {
                    toastr["success"]("保存成功！", "提示信息");
                    if (_.isFunction(that.options.callback_btnSave)) {
                        that.options.callback_btnSave();
                    }
                } else {
                    toastr["error"](rsObj.rsMsg || "保存失败！", "提示信息");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Metronic.unblockUI(that.el_tree);
                toastr["error"](errorThrown, "提示信息");
            }
        });
    }

    module.exports = Func;

});