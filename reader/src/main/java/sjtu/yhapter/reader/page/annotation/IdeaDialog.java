package sjtu.yhapter.reader.page.annotation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.adapter.IdeaAdapter;

public class IdeaDialog extends Dialog {
    private RecyclerView rvIdea;
    private ImageView imgExit;

    private IdeaAdapter ideaAdapter;

    public IdeaDialog(@NonNull Context context, Activity ownerActivity) {
        super(context, R.style.IdeaDialog);
        setOwnerActivity(ownerActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_idea);

        initData();
        initWidget();
        initListener();
    }

    private void initData() {

    }

    private void initWidget() {
        rvIdea = findViewById(R.id.rv_idea);
        imgExit = findViewById(R.id.img_exit);

        rvIdea.setLayoutManager(new LinearLayoutManager(getContext()));
        ideaAdapter = new IdeaAdapter();
        rvIdea.setAdapter(ideaAdapter);
    }

    private void initListener() {
        View.OnClickListener ocl = v -> {
            if (v.getId() == imgExit.getId()) {
                dismiss();
            }
        };
        imgExit.setOnClickListener(ocl);
    }

}
