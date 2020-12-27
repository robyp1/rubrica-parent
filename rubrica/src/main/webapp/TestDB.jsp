<%@ page import="com.cadit.daotest.*"%>
<html>
<body>
<h2>index</h2>
<%
TestDao t = new TestDao();
t.insertMioNumeroTest();
t.selectFirst();
%>
</body>
</html>
