package tomoko;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "JwIndexServlet")
public class JwIndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con;
        Statement sql;
        ResultSet rs;

        testJDBC tjdbc = new testJDBC();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("没有加载到JDBC!");
        }

        try {

            //TODO  每一次数据更新后，交替注释uri变量，然后分别运行下面todo后面来重建索引
            String uri = "jdbc:mysql://127.0.0.1:3306/mydata";
            String user = "root";
            String password = "123456";

            con = DriverManager.getConnection(uri+"?user="+user+"&password="+password+"&useSSL=false&serverTimezone=UTC");
            sql = con.createStatement();
            rs = sql.executeQuery("SELECT * FROM jwnews");


            //TODO 数据库更新时此处记得重新运行一次
            tjdbc.buildIndex(rs, this.getServletContext().getRealPath("/")+"/swufe_jw_news_index");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
