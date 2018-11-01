package tomoko;

import org.apache.lucene.document.Document;

public class WrappedDocument {
    private String id;
    private String title;
    private String content;
    private String href;
    private String school_name;
    private String forum;
    private String date;
    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbstract(String content) {
        this.content = content;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WrappedDocument(Document doc) {
        this.id      = doc.get("id");
        this.title   = doc.get("title");
        this.content = doc.get("summary");
        this.date    = doc.get("date");
        this.school_name = doc.get("school_name");
        this.forum   = doc.get("forum");
        this.href    = doc.get("url");
        this.count   = 0;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstract() {
        return content;
    }
}
