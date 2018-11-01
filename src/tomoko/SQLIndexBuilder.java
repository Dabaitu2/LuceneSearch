package tomoko;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLIndexBuilder {
    private SqlDocGenerator docGen;
    private int count;
    /**
     * 为数据库集合建立索引
     * @param Dest_Index_Path 索引被创建的路径
     * */
    public void buildIndex(String Dest_Index_Path, ResultSet rs,
                           Analyzer textAnalyzer, SqlDocGenerator docGen){
        this.docGen=docGen;
        try{
            IndexWriterConfig conf = new IndexWriterConfig(textAnalyzer);
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            Directory dir = FSDirectory.open(Paths.get(Dest_Index_Path));
            IndexWriter writer=new IndexWriter(dir,conf);
            processSQL(writer, rs);
            writer.close();
            System.out.println("End! Successful process "+count+" sql records.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 处理SQL的所有查询记录,并使用IndexWriter记录索引
     * */
    private void processSQL(IndexWriter writer, ResultSet rs) throws SQLException {
        while(rs.next()) {
            Document doc;
            System.out.println("adding news id" + rs.getString(1));
            count++;
            try{
                doc=docGen.getDocument(rs);
                writer.addDocument(doc);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}

