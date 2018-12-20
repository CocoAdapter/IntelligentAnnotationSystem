package sjtu.yhapter.reader.adapter.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sjtu.yhapter.reader.R;

public class IdeaHolder extends RecyclerView.ViewHolder {
    public TextView tvUsername, tvIdea, tvFeedback;

    public IdeaHolder(View itemView) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.tv_username);
        tvIdea = itemView.findViewById(R.id.tv_idea);
        tvFeedback = itemView.findViewById(R.id.tv_feedback);
    }
}
