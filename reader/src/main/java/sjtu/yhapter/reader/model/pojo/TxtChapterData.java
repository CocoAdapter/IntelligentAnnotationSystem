package sjtu.yhapter.reader.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class TxtChapterData extends ChapterData {
    // index in byte, included
    protected long startIndex;
    // index in byte, excluded
    protected long endIndex;

    public TxtChapterData() {

    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    @Override
    public int getLength() {
        return (int) (endIndex - startIndex);
    }

    @Override
    public String toString() {
        return "TxtChapterData{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(startIndex);
        dest.writeLong(endIndex);
    }

    public static final Parcelable.Creator<TxtChapterData> CREATOR
            = new Parcelable.Creator<TxtChapterData>() {

        public TxtChapterData createFromParcel(Parcel in) {
            return new TxtChapterData(in);
        }

        public TxtChapterData[] newArray(int size) {
            return new TxtChapterData[size];
        }
    };

    private TxtChapterData(Parcel in) {
        id = in.readLong();
        title = in.readString();
        startIndex = in.readLong();
        endIndex = in.readLong();
    }
}
