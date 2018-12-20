package sjtu.yhapter.reader.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;
import java.util.Random;

import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.adapter.view.IdeaHolder;
import sjtu.yhapter.reader.model.pojo.Annotation;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaHolder> {
    private String username;
    private Annotation annotation;
    private String feedback;

    public IdeaAdapter() {

    }

    @NonNull
    @Override
    public IdeaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_idea, parent, false);
        return new IdeaHolder(content);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaHolder holder, int position) {
        holder.tvUsername.setText(username);
        holder.tvIdea.setText(annotation.getNote());
        holder.tvFeedback.setText("教师批注: " + feedback);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
