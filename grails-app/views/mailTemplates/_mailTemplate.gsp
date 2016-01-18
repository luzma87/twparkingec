<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/6/16
  Time: 16:02
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <style type="text/css">
        * {
            font-family: "Courier New", Courier, monospace;
        }

        .hola {
            color: red;
        }

        .mensaje {
            color: blue;
        }

        .table {
            margin-top: 10px;
            border-collapse: collapse;
        }

        .table th {
            background-color: #bbbbbb;
        }
        </style>
    </head>

    <body>
        <p class="hola">Hola a tod@s,</p>

        <div class="mensaje">
            ${raw(mensaje)}
        </div>
    </body>
</html>