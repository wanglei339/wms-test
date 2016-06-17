define(function (require, exports, module) {

    var ItemEdit = require("./item_edit");

    var Item = function () {
        this.page_edit = new ItemEdit();
        this.el_content_edit = "#content_item_edit";
        this.el_tree = "#item_tree";
    }

    Item.prototype.init = function () {
        this.show();
        this.initTree();
        this.initEvents();
    }

    Item.prototype.setOptions = function (options) {
        this.options = $.extend(this.options, options);
    }

    Item.prototype.show = function () {
        $('div[data-sign="content"]').hide();
        $(this.el_content_edit).show();
    }

    Item.prototype.initTree = function () {
        var that = this;
        $(this.el_tree).jstree('destroy');
        $(this.el_tree).jstree({
            "plugins": ["contextmenu", "dnd", "types", "search"],
            "core": {
                "check_callback": true,
                "themes": {"responsive": false},
                "data": {
                    "url": App.ctx + "/pub/dict/item/tree",
                    "type": "POST",
                    "dataType": "json",
                    "data": function (node) {
                        return {"dictId": that.options.dictId};
                    }
                },
                "check_callback" : function (operation, node, parent, position, more) {
                    if(operation === "move_node") {
                        if (parent.id > 0 && parent.original.dictType == 1) {
                            // 禁止移动到列表型节点
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
                    if (node.id == "0" || node.original.dictType == 2) {
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

    Item.prototype.initEvents = function () {
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
        $("#input_item_query").val("");
        $("#input_item_query").off("keypress").on("keypress", function (event) {
            if (event.which == 13) {
                that.nodeQuery();
                event.preventDefault();
            }
        });
        $("#btn_item_query").on("click", function () {
            that.nodeQuery();
        });
    }

    Item.prototype.nodeQuery = function () {
        var val = $("#input_item_query").val();
        $(this.el_tree).jstree(true).search(val);
    }

    Item.prototype.nodeAdd = function (node, obj) {
        var that = this;
        this.page_edit.setOptions({
            id: null,
            did: node.original.dictId,
            pid: node.id,
            callback_btnSave: function () {
                $(that.el_tree).jstree(true).refresh();
            },
            callback_btnBack: function () {
                that.options.callback_btnBack();
            }
        });
        this.page_edit.init();
    }

    Item.prototype.nodeEdit = function (e, data) {
        var that = this;
        this.page_edit.setOptions({
            id: data.node.id,
            did: data.node.original.dictId,
            pid: null,
            callback_btnSave: function () {
                data.instance.refresh();
            },
            callback_btnBack: function () {
                that.options.callback_btnBack();
            }
        });
        this.page_edit.init();
    }

    Item.prototype.nodeMove = function (e, data) {
        var moveParam = {
            itemId: data.node.id,
            pid: data.parent,
            old_pid: data.old_parent,
            position: data.position + 1
        };
        console.log("moveParam=======" + JSON.stringify(moveParam));
        var that = this;
        Metronic.blockUI({message: "正在保存...", target: that.el_tree});
        $.ajax({
            url: App.ctx + "/pub/dict/item/move",
            type: "post",
            data: moveParam,
            success: function (rsObj, textStatus, jqXHR) {
                Metronic.unblockUI(that.el_tree);
                if (rsObj.rsCode == "1") {
                    toastr["success"]("保存成功！", "提示信息");
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

    module.exports = Item;

});