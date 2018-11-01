<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: tomokokawase
  Date: 2018/9/23
  Time: 17:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>搜索结果</title>
</head>
<body>
    <%
        List<JSONObject> rst = (List<JSONObject>) request.getAttribute("rst");
        for (int i = 0; i < rst.size(); i++) {
    %>
        <h2><%=rst.get(i).getString("title")%></h2>
        <p>
            <%=rst.get(i).get("content")%>
        </p>
    <%
        }
    %>
</body>
</html>
