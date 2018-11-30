package sjtu.yhapter.reader;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import sjtu.yhapter.reader.model.pojo.ChapterData;

public class ChapterDataAdapter extends BaseAdapter {
    protected List<? extends ChapterData> chapters;

    public ChapterDataAdapter(List<? extends ChapterData> chapters) {
        this.chapters = chapters;
    }

    @Override
    public int getCount() {
        return chapters == null ? 0 : chapters.size();
    }

    @Override
    public Object getItem(int position) {
        return chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return chapters.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
