package sjtu.yhapter.ias.ui.adapter;

import android.widget.TextView;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.reader.model.pojo.ChapterData;

/**
 * Created by CocoAdapter on 2018/11/30.
 */

public class CategoryAdapter extends BaseAdapter<ChapterData> {
    @Override
    protected IViewHolder<ChapterData> onCreateViewHolder(int viewType) {
        return new ViewHolder();
    }

    private static class ViewHolder extends BaseViewHolder<ChapterData> {
        TextView tvIndex, tvTitle, tvPageNum;

        @Override
        protected int getItemLayoutId() {
            return R.layout.item_catalog;
        }

        @Override
        public void initView() {
            tvIndex = findViewById(R.id.tv_index);
            tvTitle = findViewById(R.id.tv_title);
            tvPageNum = findViewById(R.id.tv_page);
        }

        @Override
        public void onBind(ChapterData data, int pos) {
            tvIndex.setText("第" + (pos + 1) + "章");
            tvTitle.setText(data.getTitle());
            tvPageNum.setText(pos * 50 + System.currentTimeMillis() % 50 + "");
        }
    }
}
