<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <form action="SearchServlet" method="post">
    <label for="query">
        <span>字段</span>
      <input type="text" name="query" id="query">
    </label>
      <label for="page">
          <span>页数</span>
          <input type="text" name="page" id="page">
      </label>
      <input type="submit" value="搜索">
  </form>
  </body>
</html>