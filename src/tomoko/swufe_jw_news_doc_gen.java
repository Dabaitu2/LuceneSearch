package tomoko;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;

import java.sql.ResultSet;

/**
 *  实现SqlDocGenerator 接口 来生成文档
 *  有这么几个field
 *  id              文档的id
 *  url             文档的url
 *  school_name     文档的学校
 *  forum           文档的话题
 *  date            文档的日期
 *  title           文档的标题
 *  content         文档的内容
 */

public class swufe_jw_news_doc_gen implements SqlDocGenerator{
    @Override
    public Document getDocument(ResultSet rs) throws Exception {
        Document doc = new Document();
        Field idField  = getId(rs);
        Field urlField = getUrl(rs);
        Field schoolNameField = getSchoolName(rs);
        Field forumField      = getForum(rs);
        Field dateField       = getDate(rs);
        Field titleField      = getTitle(rs);
        Field indexTitleField = getIndexTitle(rs);
        Field contentField    = getContent(rs);
        Field summaryField    = getSummary(rs);
        doc.add(idField);
        doc.add(urlField);
        doc.add(schoolNameField);
        doc.add(forumField);
        doc.add(dateField);
        doc.add(titleField);
        doc.add(contentField);
        doc.add(summaryField);
        doc.add(indexTitleField);
        return doc;
    }

    private StringField getId(ResultSet rs) throws Exception {
        return new StringField("id", rs.getString(1), Field.Store.YES);
    }

    private StringField getUrl(ResultSet rs) throws Exception {
        return new StringField("url", rs.getString(2), Field.Store.YES);
    }

    private StringField getSchoolName(ResultSet rs) throws Exception {
        return new StringField("school_name", rs.getString(3), Field.Store.YES);
    }

    private StringField getForum(ResultSet rs) throws Exception {
        return new StringField("forum", rs.getString(4), Field.Store.YES);
    }

    private StringField getDate(ResultSet rs) throws Exception {
        return new StringField("date", rs.getString(5), Field.Store.YES);
    }

    private StringField getTitle(ResultSet rs) throws Exception {
        return new StringField("title", rs.getString(6), Field.Store.YES);
    }

    private Field getIndexTitle(ResultSet rs) throws Exception {
        FieldType offsetType = new FieldType(TextField.TYPE_NOT_STORED);
        offsetType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        return new Field("indexTitle", rs.getString(6), offsetType);
    }

    // 给highlighter用的content field
    // 同时设定依据这个字段来进行索引
    private Field getContent(ResultSet rs) throws Exception {
        FieldType offsetType = new FieldType(TextField.TYPE_NOT_STORED);
        offsetType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        return new Field("content", rs.getString(7), offsetType);
    }

    private TextField getSummary(ResultSet rs) throws Exception {
        return new TextField("summary", rs.getString(7), Field.Store.YES);
    }
}
