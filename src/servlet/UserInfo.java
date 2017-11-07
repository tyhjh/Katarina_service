package servlet;

import mysql.Mysql;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserInfo extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String userName = req.getParameter("userName");
        String appVersion = req.getParameter("appVersion");
        String model = req.getParameter("model");
        String androidVersion = req.getParameter("androidVersion");
        String position = req.getParameter("position");
        String imei = req.getParameter("imei");
        JSONObject jsonObject = new JSONObject();
        Mysql.userInfo(userName, appVersion, model, androidVersion, position, imei);
        try {
            jsonObject.put("code", 200);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(jsonObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
