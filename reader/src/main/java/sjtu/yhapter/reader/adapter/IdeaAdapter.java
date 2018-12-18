package sjtu.yhapter.reader.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import sjtu.yhapter.reader.R;
import sjtu.yhapter.reader.adapter.view.IdeaHolder;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaHolder> {
    private final int count;

    public IdeaAdapter() {
        count = new Random(System.currentTimeMillis()).nextInt(5) + 1;
    }

    @NonNull
    @Override
    public IdeaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_idea, parent, false);
        return new IdeaHolder(content);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return count;
    }
}
