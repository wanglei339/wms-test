<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.lsh.wms.web.utils.WebUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.lsh.wms.model.system.SysFunction" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!-- BEGIN SIDEBAR -->
<div class="page-sidebar-wrapper">
    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
    <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
    <div class="page-sidebar navbar-collapse collapse">
        <!-- BEGIN SIDEBAR MENU -->
        <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
        <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
        <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
        <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
        <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
        <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
        <ul class="page-sidebar-menu" data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
            <!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->
            <li class="sidebar-toggler-wrapper">
                <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                <div class="sidebar-toggler">
                </div>
                <!-- END SIDEBAR TOGGLER BUTTON -->
            </li>
            <%
                // 当前功能id
                int fid = 0;
                String fidStr = request.getParameter("fid");
                if (StringUtils.isNumeric(fidStr)) {
                    fid = Integer.valueOf(fidStr);
                }
                boolean isIndexActive = false;
                if (fid == 0) {
                    isIndexActive = true;
                }
                String indexClass = "start";
                if (isIndexActive) {
                    indexClass += " active";
                }
            %>
            <li class="<%=indexClass%>">
                <a href="${ctx}/">
                    <i class="icon-home"></i>
                    <% if (isIndexActive) { %>
                    <span class="selected"></span>
                    <%}%>
                    <span class="title">首页</span>
                </a>
            </li>
            <%
                // 一级菜单
                List<SysFunction> list1 = WebUtils.getUserSysFuncList(request, 0);
                if (list1 == null) {
                    list1 = new ArrayList<SysFunction>();
                }
                for (int i1 = 0, l1 = list1.size(); i1 < l1; i1++) {
                    SysFunction func1 = list1.get(i1);
                    boolean isActive1 = WebUtils.isSysFuncActive(func1, fid);
                    boolean isLeaf1 = false;
                    if (func1.getIsLeaf() != null && func1.getIsLeaf() == 1) {
                        isLeaf1 = true;
                    }
                    String menuClass1 = "";
                    if (i1 == l1 - 1) {
                        menuClass1 = "last";
                    }
                    if (isActive1) {
                        menuClass1 += " active";
                        if (!isLeaf1) {
                            menuClass1 += " open";
                        }
                    }
                    String url1 = "javascript:;";
                    if (isLeaf1) {
                        url1 = request.getContextPath() + WebUtils.addUrlParam(func1.getActionUrl(), "fid", func1.getId().toString());
                    }
                    String icon1 = isLeaf1 ? "icon-doc" : "icon-folder";
                    if (StringUtils.isNoneBlank(func1.getIconUrl())) {
                        icon1 = func1.getIconUrl();
                    }
            %>
            <li class="<%=menuClass1%>">
                <a href="<%=url1%>">
                    <i class="<%=icon1%>"></i>
                    <span class="title"><%=func1.getFuncName()%></span>
                    <% if (isActive1) {%>
                    <span class="selected"></span>
                    <%}%>
                    <%
                        if (!isLeaf1) {
                            String arrowClass = isActive1 ? "arrow open" : "arrow";
                    %>
                    <span class="<%=arrowClass%>"></span>
                    <%}%>
                </a>
                <%
                    // 二级菜单
                    List<SysFunction> list2 = WebUtils.getUserSysFuncList(request, func1.getId());
                    if (list2 != null && !list2.isEmpty()) {
                %>
                <ul class="sub-menu">
                    <%
                        for (int i2 = 0, l2 = list2.size(); i2 < l2; i2++) {
                            SysFunction func2 = list2.get(i2);
                            boolean isActive2 = WebUtils.isSysFuncActive(func2, fid);
                            String menuClass2 = isActive2 ? "active" : "";
                            boolean isLeaf2 = false;
                            if (func2.getIsLeaf() != null && func2.getIsLeaf() == 1) {
                                isLeaf2 = true;
                            }
                            String url2 = "javascript:;";
                            if (isLeaf2) {
                                url2 = request.getContextPath() + WebUtils.addUrlParam(func2.getActionUrl(), "fid", func2.getId().toString());
                            }
                            String icon2 = isLeaf2 ? "icon-doc" : "icon-folder";
                            if (StringUtils.isNoneBlank(func2.getIconUrl())) {
                                icon2 = func2.getIconUrl();
                            }
                    %>
                    <li class="<%=menuClass2%>">
                        <a href="<%=url2%>">
                            <i class="<%=icon2%>"></i> <%=func2.getFuncName()%>
                            <%
                                if (!isLeaf2) {
                                    String arrowClass = isActive2 ? "arrow open" : "arrow";
                            %>
                            <span class="<%=arrowClass%>"></span>
                            <%}%>
                        </a>
                        <%
                            // 三级菜单
                            List<SysFunction> list3 = WebUtils.getUserSysFuncList(request, func2.getId());
                            if (list3 != null && !list3.isEmpty()) {
                        %>
                        <ul class="sub-menu">
                            <%
                                for (int i3 = 0, l3 = list3.size(); i3 < l3; i3++) {
                                    SysFunction func3 = list3.get(i3);
                                    boolean isActive3 = WebUtils.isSysFuncActive(func3, fid);
                                    String menuClass3 = isActive3 ? "active" : "";
                                    boolean isLeaf3 = false;
                                    if (func3.getIsLeaf() != null && func3.getIsLeaf() == 1) {
                                        isLeaf3 = true;
                                    }
                                    String url3 = "javascript:;";
                                    if (isLeaf3) {
                                        url3 = request.getContextPath() + WebUtils.addUrlParam(func3.getActionUrl(), "fid", func3.getId().toString());
                                    }
                                    String icon3 = isLeaf3 ? "icon-doc" : "icon-folder";
                                    if (StringUtils.isNoneBlank(func3.getIconUrl())) {
                                        icon3 = func3.getIconUrl();
                                    }
                            %>
                            <li class="<%=menuClass3%>">
                                <a href="<%=url3%>">
                                    <i class="<%=icon3%>"></i> <%=func3.getFuncName()%>
                                    <%
                                        if (!isLeaf3) {
                                            String arrowClass = isActive3 ? "arrow open" : "arrow";
                                    %>
                                    <span class="<%=arrowClass%>"></span>
                                    <%}%>
                                </a>
                                <%
                                    // 四级菜单
                                    List<SysFunction> list4 = WebUtils.getUserSysFuncList(request, func3.getId());
                                    if (list4 != null && !list4.isEmpty()) {
                                %>
                                <ul class="sub-menu">
                                    <%
                                        for (int i4 = 0, l4 = list4.size(); i4 < l4; i4++) {
                                            SysFunction func4 = list4.get(i4);
                                            boolean isActive4 = WebUtils.isSysFuncActive(func4, fid);
                                            String menuClass4 = isActive4 ? "active" : "";
                                            boolean isLeaf4 = false;
                                            if (func4.getIsLeaf() != null && func4.getIsLeaf() == 1) {
                                                isLeaf4 = true;
                                            }
                                            String url4 = "javascript:;";
                                            if (isLeaf4) {
                                                url4 = request.getContextPath() + WebUtils.addUrlParam(func4.getActionUrl(), "fid", func4.getId().toString());
                                            }
                                            String icon4 = isLeaf4 ? "icon-doc" : "icon-folder";
                                            if (StringUtils.isNoneBlank(func4.getIconUrl())) {
                                                icon4 = func4.getIconUrl();
                                            }
                                    %>
                                    <li class="<%=menuClass4%>">
                                        <a href="<%=url4%>">
                                            <i class="<%=icon4%>"></i> <%=func4.getFuncName()%>
                                        </a>
                                    </li>
                                    <%} // 四级菜单循环%>
                                </ul>
                                <%} // 四级菜单%>
                            </li>
                            <%} // 三级菜单循环%>
                        </ul>
                        <%} // 三级菜单%>
                    </li>
                    <%} // 二级菜单循环%>
                </ul>
                <%} // 二级菜单%>
            </li>
            <%
                } // 一级菜单
            %>
        </ul>
        <!-- END SIDEBAR MENU -->
    </div>
</div>
<!-- END SIDEBAR -->
