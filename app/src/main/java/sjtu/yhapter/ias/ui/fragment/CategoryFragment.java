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
import sjtu.yhapter.ias.presenter.CategoryPresenter;
import sjtu.yhapter.ias.presenter.contract.CategoryContract;
import sjtu.yhapter.ias.ui.activity.ReadActivity;
import sjtu.yhapter.ias.ui.adapter.CategoryAdapter;
import sjtu.yhapter.ias.ui.base.BaseContract;
import sjtu.yhapter.ias.ui.base.BaseMVPFragment;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by CocoAdapter on 2018/11/29.
 */

public class CategoryFragment extends BaseMVPFragment<CategoryContract.Presenter> implements CategoryContract.View {
    private ListView lvChapter;
    private CategoryAdapter adapter;
    private List<? extends ChapterData> categories;

    private AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.frgm_catalog;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        lvChapter = findViewById(R.id.lv_chapter);
        adapter = new CategoryAdapter();
        if (categories != null) {
            adapter.setData((List<ChapterData>) categories);
            lvChapter.setAdapter(adapter);
            lvChapter.setOnItemClickListener((parent, view, position, id) -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(parent, view, position, id);
            });
        }
    }

    @Override
    protected void initListener() {

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

    @Override
    protected CategoryContract.Presenter bindPresenter() {
        return new CategoryPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }
}
