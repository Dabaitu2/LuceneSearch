package tomoko;
import org.apache.lucene.document.Document;
import java.sql.ResultSet;


/**
 *  通过 SQL 语句来获取文档
 *  通过 ResultSet 每一次next 后获得的一个元组来生成文档
 */

public interface SqlDocGenerator {
    public Document getDocument(ResultSet rs) throws Exception;
}
