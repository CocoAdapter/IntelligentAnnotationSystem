package sjtu.yhapter.ias.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import sjtu.yhapter.ias.R;

/**
 * Created by CocoAdapter on 2018/11/30.
 */

public class CatalogAdapter extends BaseAdapter {
    private Context context;

    public CatalogAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_catalog, null);
            holder.tvIndex = convertView.findViewById(R.id.tv_index);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvPageNum = convertView.findViewById(R.id.tv_page);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvIndex.setText("第" + position + "章");
        holder.tvTitle.setText("测试章节标题" + position);
        holder.tvPageNum.setText(position * 10 + System.currentTimeMillis() % 50 + "");
        return convertView;
    }

    private class ViewHolder {
        TextView tvIndex, tvTitle, tvPageNum;
    }
}
