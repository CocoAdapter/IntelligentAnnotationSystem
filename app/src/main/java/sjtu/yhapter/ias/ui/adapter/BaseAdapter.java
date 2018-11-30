package sjtu.yhapter.ias.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private List<T> data = new ArrayList<>();

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(T value){
        data.add(value);
        notifyDataSetChanged();
    }

    public void addItem(int index,T value){
        data.add(index, value);
        notifyDataSetChanged();
    }

    public void addItems(List<T> values){
        data.addAll(values);
        notifyDataSetChanged();
    }

    public void removeItem(T value){
        data.remove(value);
        notifyDataSetChanged();
    }

    public List<T> getItems(){
        return Collections.unmodifiableList(data);
    }

    public void setData(List<T> data) {
        this.data.clear();
        addItems(data);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IViewHolder holder = null;
        if (convertView == null){
            holder = onCreateViewHolder(getItemViewType(position));
            convertView = holder.createItemView(parent);
            convertView.setTag(holder);
            //初始化
            holder.initView();
        }
        else {
            holder = (IViewHolder)convertView.getTag();
        }
        //执行绑定
        holder.onBind(getItem(position),position);
        return convertView;
    }

    protected abstract IViewHolder<T> onCreateViewHolder(int viewType);

    protected interface IViewHolder<T> {
        View createItemView(ViewGroup parent);
        void initView();
        void onBind(T data, int pos);
        void onClick();
    }
}
