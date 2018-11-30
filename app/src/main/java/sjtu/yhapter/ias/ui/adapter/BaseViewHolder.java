package sjtu.yhapter.ias.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseViewHolder<T> implements BaseAdapter.IViewHolder<T> {
    private View view;
    private Context context;

    protected abstract int getItemLayoutId();

    @Override
    public View createItemView(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayoutId(), parent, false);
        context = parent.getContext();
        return view;
    }

    protected Context getContext(){
        return context;
    }

    protected <V extends View> V findViewById(int id){
        return view.findViewById(id);
    }

    @Override
    public void onClick() {
    }
}
