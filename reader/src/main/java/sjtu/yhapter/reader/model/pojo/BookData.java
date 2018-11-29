package sjtu.yhapter.reader.model.pojo;

import java.util.List;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class BookData {
    private long id;
    private String path; // 针对本地文件的形式, 在这里
    private String name;

    private List<ChapterData> chapters;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChapterData> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterData> chapters) {
        this.chapters = chapters;
    }
}
