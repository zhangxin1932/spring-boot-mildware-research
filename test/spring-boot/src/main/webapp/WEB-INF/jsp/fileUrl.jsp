<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<HTML>
<HEAD>
    <TITLE> ZTREE DEMO - Async</TITLE>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/css/demo.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ztree.core.js"></script>
    <SCRIPT type="text/javascript">
        <!--
        let setting = {
            async: {
                enable: true,
                url:"/files/getSonFileUrls", // 这里是controller层RequestingMapping对应的值
                autoParam:["id"], // 这里的id是传递到后台的参数,controller中可以接收到
                //otherParam:{"otherParam":"zTreeAsyncTest"}, // 本案例中,这里可以不写
                dataFilter: filter
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };

        function filter(treeId, parentNode, childNodes) {
            if (!childNodes) return null;
            for (var i=0, l=childNodes.length; i<l; i++) {
                childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            }
            return childNodes;
        }

        $(document).ready(function(){
            $.fn.zTree.init($("#treeDemo"), setting, ${fatherFileUrls}); // 此处的$中的值,是初始化顶级父节点
        });
        //-->
    </SCRIPT>
</HEAD>

<BODY>
<h1>异步加载节点数据的树</h1>
<h6>[ 文件路径: core/async.html ]</h6>
<div class="content_wrap">
    <div class="zTreeDemoBackground left">
        <ul id="treeDemo" class="ztree"></ul>
    </div>
</div>
</BODY>
</HTML>
