package sjtu.yhapter.ias.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.ui.adapter.CatalogAdapter;

/**
 * Created by CocoAdapter on 2018/11/29.
 */

public class CatalogFragment extends BaseFragment {
    private ListView lvChapter;

    private CatalogAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CatalogAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.frgm_catalog, null);
        lvChapter = content.findViewById(R.id.lv_chapter);
        lvChapter.setAdapter(adapter);
        return content;
    }
}
