package tomoko;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONObject;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class testJDBC {

    String Dest_Index_Path = "./src/swufe_jw_news_index";
    String normal_Dest_Index_Path = "./src/swufe_news_index";

    public static void main(String[] args) {

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
            String uri = "jdbc:mysql://127.0.0.1:3306/news";
            // String uri = "jdbc:mysql://127.0.0.1:3306/jwnews";
            String user = "root";
            String password = "123456";

            con = DriverManager.getConnection(uri+"?user="+user+"&password="+password+"&useSSL=false&serverTimezone=UTC");
            sql = con.createStatement();
            rs = sql.executeQuery("SELECT * FROM news");


            //TODO 数据库更新时此处记得重新运行一次
//            tjdbc.buildIndex(rs);
//            tjdbc.buildnormalIndex(rs);

            // TODO 这只是测试代码, 可以不用管
//            tjdbc.search("indexTitle", "上海财经大学", true, 1, "", "jw");




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void buildnormalIndex(ResultSet rs) {
        SQLIndexBuilder ib=new SQLIndexBuilder();
        Analyzer textAnalyzer=new IKAnalyzer();
        SqlDocGenerator docGen=new swufe_jw_news_doc_gen();
        ib.buildIndex(normal_Dest_Index_Path, rs, textAnalyzer, docGen);
    }

    public void buildIndex(ResultSet rs, String path) {
        SQLIndexBuilder ib=new SQLIndexBuilder();
        Analyzer textAnalyzer=new IKAnalyzer();
        SqlDocGenerator docGen=new swufe_jw_news_doc_gen();
        ib.buildIndex(path, rs, textAnalyzer, docGen);
    }


    /**
     * 用来获取分页所需的上一个scoreDoc 内部方法 可以不管
     * @param page 当前页码
     * @param size 每一页的文档数量
     */
    private ScoreDoc getLastDoc(int page, int size, Query query, IndexSearcher indexSearcher) throws IOException{
        if (page == 1) return null;
        int num = (page - 1) * size;
        TopDocs tds = indexSearcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }

    /** 搜索文档，field是搜索对应的位置，实际上content最有用，一般就考虑它，query就是搜索的对应文本
     *  在处理用户输入的时候，可能需要考虑一下用户输入过长要把它拆开成"XX OR/AND XX"的形式，原因讲义上有
     *  具体怎么拆可能需要再使用IK分词分一下
     *  sortTime 的布尔值决定了它是不是要按照时间分类。
     *  @param type 取值为normal 或 jw 指明搜索的是哪个数据库的索引
     * */
    public List<JSONObject> search(String field, String queryVal, Boolean sortTime, int page, String school, String type) {
        IndexSearcher is;
        Analyzer analyzer;
        List<JSONObject> rstList = new ArrayList<JSONObject>();

        try{
            String path = type.equals("jw") ? "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\webapps\\web_war_exploded\\swufe_jw_news_index" : "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\webapps\\web_war_exploded\\swufe_news_index";
            // TODO 修改为实际的索引路径
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
            is        = new IndexSearcher(reader);
            analyzer  = new IKAnalyzer();

            String[] Fields = {field, "school_name"};
            String[] querys = {queryVal, school};
            MultiFieldQueryParser qp = new MultiFieldQueryParser(Fields, analyzer);
            Query query3 = qp.parse(querys, Fields, analyzer);

            ScoreDoc lastDoc = getLastDoc(page, 10, query3, is);
            TopDocs topDocs3 = is.searchAfter(lastDoc, query3, 10);
            ScoreDoc[] sd = topDocs3.scoreDocs;

            for (int i = 0; i < sd.length; ++i)
            {
                Document targetDoc = is.doc(sd[i].doc);
                WrappedDocument wrappedDocument = new WrappedDocument(targetDoc);
                wrappedDocument.setCount(topDocs3.totalHits);
                wrappedDocument.setAbstract(displayHtmlHighlight(query3, analyzer,"content", wrappedDocument.getAbstract(), 200));
                if(displayHtmlHighlight(query3, analyzer,"content", wrappedDocument.getTitle(), 200)!=null)
                {
                    wrappedDocument.setTitle(displayHtmlHighlight(query3, analyzer,"content", wrappedDocument.getTitle(), 200));
                }
                JSONObject jsonObject = new JSONObject(wrappedDocument);
                rstList.add(jsonObject);
            }


        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return rstList;
    }

    /**
     * 内部方法，用于给返回的文本加上高亮，可以不管
     * */
    private static String displayHtmlHighlight(Query query, Analyzer analyzer,String fieldName, String fieldContent, int fragmentSize) throws IOException, InvalidTokenOffsetsException
    {
        //创建一个高亮器
        Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span style='color: rgb(73, 133, 255)'>", "</span>"), new QueryScorer(query));
        Fragmenter fragmenter = new SimpleFragmenter(fragmentSize);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter.getBestFragment(analyzer, fieldName, fieldContent);
    }

}
