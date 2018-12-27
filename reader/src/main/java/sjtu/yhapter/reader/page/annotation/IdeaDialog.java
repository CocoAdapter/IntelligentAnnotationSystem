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

import java.util.Map;

import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.adapter.IdeaAdapter;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.SharedPrefUtil;

public class IdeaDialog extends Dialog {
    private RecyclerView rvIdea;
    private ImageView imgExit;

    private IdeaAdapter ideaAdapter;

    private Annotation annotation;
    private String feedback;

    public IdeaDialog(@NonNull Context context, Activity ownerActivity) {
        super(context, R.style.IdeaDialog);
        setOwnerActivity(ownerActivity);
        ideaAdapter = new IdeaAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_idea);

        initData();
        initWidget();
        initListener();
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    private void initData() {

    }

    private void initWidget() {
        rvIdea = findViewById(R.id.rv_idea);
        imgExit = findViewById(R.id.img_exit);

        rvIdea.setLayoutManager(new LinearLayoutManager(getContext()));
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

    @Override
    public void show() {
        ideaAdapter.setAnnotation(annotation);
        ideaAdapter.setFeedback(feedback);
        ideaAdapter.setUsername("用户: " + App.USER_ID);
        ideaAdapter.notifyDataSetChanged();
        super.show();
    }
}
