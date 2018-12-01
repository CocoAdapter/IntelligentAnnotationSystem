package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.ui.activity.ReadActivity;
import sjtu.yhapter.ias.ui.adapter.CategoryAdapter;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/29.
 */

public class CategoryFragment extends BaseFragment {
    private ListView lvChapter;
    private CategoryAdapter adapter;
    private List<? extends ChapterData> categories;

    private AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    @SuppressWarnings("all")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.frgm_catalog, null);
        lvChapter = content.findViewById(R.id.lv_chapter);

        adapter = new CategoryAdapter();
        if (categories != null) {
            adapter.setData((List<ChapterData>) categories);
            lvChapter.setAdapter(adapter);
            lvChapter.setOnItemClickListener((parent, view, position, id) -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(parent, view, position, id);
            });
        }

        return content;
    }

    @SuppressWarnings("all")
    public void setCategories(List<? extends ChapterData> categories) {
        this.categories = categories;
        if (adapter != null) {
            adapter.setData((List<ChapterData>) categories);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
