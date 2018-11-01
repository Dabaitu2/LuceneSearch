package tomoko;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String query = request.getParameter("query");
        String page  = request.getParameter("page");
        String sort  = request.getParameter("sort");
        String school = request.getParameter("school");
        System.out.println("查询值为 "+query+", 页码为" + page+", 排序依据为" + sort+"， 学校为: "+ school);
        testJDBC tjdbc = new testJDBC();
//        String aidSearch = school.equals("全部") ? "" : " AND (school_name:" + school+")";
        List<JSONObject> rst = tjdbc.search(sort, query, true, Integer.parseInt(page), school, "normal");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONArray(rst).toString());
        System.out.println("该次搜索处理完毕");
    }
}
