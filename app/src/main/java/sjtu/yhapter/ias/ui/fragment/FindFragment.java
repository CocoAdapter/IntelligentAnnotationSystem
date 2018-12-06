package sjtu.yhapter.ias.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sjtu.yhapter.ias.R;

public class FindFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frgm_find, container, false);
        ImageView imageView = root.findViewById(R.id.img_test);
        Glide.with(getActivity()).load(R.drawable.find_test).into(imageView);
        return root;
    }
}
