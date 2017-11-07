package mysql;

import org.json.JSONArray;
import org.json.JSONObject;
import util.TimeUtil;

import java.sql.*;


public class Mysql {
    static String url3 = "jdbc:mysql://118.190.172.230/kotarina?useUnicode=true&characterEncoding=utf8";
    static Connection conn;
    static Statement statement;

    //初始化
    public static synchronized void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // System.out.println("成功加载MySQL驱动！");
        } catch (Exception e) {
            System.out.println("找不到MySQL驱动!");
            e.printStackTrace();
        }
        try {
            conn = (Connection) DriverManager.getConnection(url3, "tyhj", "4444");
            // System.out.println("成功加载conn！");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("找不到conn!");
        }
        try {
            if (conn != null) {
                statement = (Statement) conn.createStatement();
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //获取gif数据
    public static synchronized String getGifs() {
        String wallpaper = null;
        String sql = "select * from gif";
        JSONObject jsonObject = new JSONObject();
        try {
            if (statement == null || conn.isClosed()) {
                init();
            }
            ResultSet rs = null;
            rs = (ResultSet) statement.executeQuery(sql);
            jsonObject.put("code", 200);
            JSONArray array = new JSONArray();
            while (rs.next()) {
                JSONObject object = new JSONObject();
                object.put("id", rs.getInt("id"));
                object.put("userName", rs.getString("userName"));
                object.put("imageUrl", rs.getString("imageUrl"));
                object.put("imei", rs.getString("imei"));
                array.put(object);
            }
            jsonObject.put("gifs", array);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jsonObject.put("code", 202);
                return jsonObject.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        log("gif图片" + wallpaper);
        return jsonObject.toString();
    }


    //保存用户信息
    public static synchronized void userInfo(String userName, String appVersion, String phoneName, String androidVersion, String position, String imei) {
        String time = TimeUtil.getNowTime();
        String sql = "delete from userInfo where imei = '" + imei + "'";
        action(sql);
        sql = "insert into userInfo values('" + userName + "','" + appVersion + "','" + phoneName + "','" + androidVersion + "','" + position + "','" + imei + "','" + time + "')";
        action(sql);
    }

    //保存信息
    private static void action(String sql) {
        try {
            if (statement == null || conn.isClosed())
                init();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void pushGif(String userName, String imageUrl, String imei) {
        String sql = "delete from gif where imageUrl = '" + imageUrl + "'";
        action(sql);
        sql = "insert into gif (userName,imageUrl,imei) values('" + userName + "','" + imageUrl + "','" + imei + "')";
        action(sql);
    }


    //检查更新
    public static synchronized String checkUpdate() {
        String sql = "select * from app";
        JSONObject object = new JSONObject();
        try {
            if (statement == null || conn.isClosed())
                init();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.last()) {
                object.put("code", 200);
                object.put("version", resultSet.getString("version"));
                object.put("apkUrl", resultSet.getString("appUrl"));
                object.put("imageUrl", resultSet.getString("imageUrl"));
                object.put("info", resultSet.getString("info"));
                object.put("must", resultSet.getBoolean("must"));
                return object.toString();
            }
            object.put("code", 201);
        } catch (Exception e) {
            try {
                object.put("code", 202);
                return object.toString();
            } catch (org.json.JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return object.toString();
    }


    public static void log(String msg) {
        System.out.println(msg);
    }


}
