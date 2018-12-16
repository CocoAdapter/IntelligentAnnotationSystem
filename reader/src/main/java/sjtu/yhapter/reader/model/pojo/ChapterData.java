package sjtu.yhapter.reader.model.pojo;

import android.os.Parcelable;

import java.io.BufferedReader;

/**
 * Created by Yhapter on 2018/11/28.
 */
public abstract class ChapterData implements Parcelable {
    protected long id;
    protected String title;

    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract int getLength();
}
