<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html>
<html>
<head>
    <title>Library Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>

<div class="left-side"></div>

<div class="right-side">
    <div class="login-box">
        <h2>Login</h2>

        <%
            String error = request.getParameter("error");
            String success = request.getParameter("success");
        %>

        <% if (error != null) { %>
        <div class="message error"><%= URLDecoder.decode(error, "UTF-8") %></div>
        <% } %>

        <% if (success != null) { %>
        <div class="message success"><%= URLDecoder.decode(success, "UTF-8") %></div>
        <% } %>

        <form action="auth" method="post">
            <input type="text" name="username" placeholder="Enter Username" required>
            <input type="password" name="password" placeholder="Enter Password" required>
            <button type="submit">Login</button>
        </form>

        <div class="signup">
            Don't have an account?
            <a href="signup.jsp">Sign Up</a>
        </div>
    </div>
</div>

</body>
</html>
