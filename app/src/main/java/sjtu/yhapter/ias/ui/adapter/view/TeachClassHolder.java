package sjtu.yhapter.ias.ui.adapter.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.TeachClass;
import sjtu.yhapter.ias.ui.base.adapter.ViewHolderImpl;
import sjtu.yhapter.reader.util.LogUtil;

public class TeachClassHolder extends ViewHolderImpl<TeachClass> {
    private View llClassInfo;
    private View llReadList; // TODO 阅读书单加红点，如果有更新

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_teach_class;
    }

    @Override
    public void initView() {
        llClassInfo = findViewById(R.id.ll_class_info);
        llReadList = findViewById(R.id.ll_readlist);

        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.ll_class_info:
                    LogUtil.log("llClassInfo");
                    break;
                case R.id.ll_readlist:
                    LogUtil.log("llReadList");
                    break;
            }
        };
        llClassInfo.setOnClickListener(ocl);
        llReadList.setOnClickListener(ocl);
    }

    @Override
    public void onBind(TeachClass data, int pos) {
        ImageView imgStatus = findViewById(R.id.img_status);
        TextView tvClassInfo = llClassInfo.findViewById(R.id.tv_class_info);
        TextView tvReadList = llReadList.findViewById(R.id.tv_read_list);
        if (data.getStatus() == 1) {
            Glide.with(getContext())
                    .load(R.drawable.ic_teach_class_status_waiting)
                    .into(imgStatus);

            tvClassInfo.setEnabled(false);
            tvReadList.setEnabled(false);
            llClassInfo.setClickable(false);
            llReadList.setClickable(false);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.ic_teach_class_status_done)
                    .into(imgStatus);
            tvClassInfo.setEnabled(true);
            tvReadList.setEnabled(true);
            llClassInfo.setClickable(true);
            llReadList.setClickable(true);
        }
    }
}
