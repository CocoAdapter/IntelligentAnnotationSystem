package sjtu.yhapter.ias.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.ias.App;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.pojo.TeachClass;

public class BookShelfMenu extends PopupWindow {
    public static final long ID_RECENTLY_READ = -1L;
    public static final long ID_COLLECTION = -2L;
    private static final String[] defaultItems = new String[] {"近期阅读", "我的收藏"};

    private ListView listView;
    private ArrayAdapter<String> adapter;

    // TODO 可以用baseAdapter来实现
    private List<String> tem;
    private Map<String, Long> itemsMap;
    private Action action;

    public BookShelfMenu(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_book_shelf_menu, null);
        setContentView(contentView);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);

        Activity activity = (Activity) context;
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);

        setWidth((int) (p.x * 0.6));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        listView = contentView.findViewById(R.id.list_view);

        itemsMap = new HashMap<>();
        tem = new ArrayList<>();
        tem.addAll(Arrays.asList(defaultItems));
        itemsMap.put(defaultItems[0], ID_RECENTLY_READ);
        itemsMap.put(defaultItems[1], ID_COLLECTION);

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            dismiss();
            long iid = itemsMap.get(adapter.getItem(position));
            if (action != null)
                action.onItemClick(iid);
        });
    }

    public void refreshItems(List<TeachClass> teachClasses) {
        adapter.clear();
        itemsMap.clear();

        adapter.addAll(defaultItems);
        itemsMap.put(defaultItems[0], ID_RECENTLY_READ);
        itemsMap.put(defaultItems[1], ID_COLLECTION);

        for (TeachClass t : teachClasses) {
            String key = t.getClassname() == null ? "" : t.getClassname(); // TODO
            long value = t.getClassid();
            adapter.add(key);
            itemsMap.put(key, value);
        }
        adapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(Action action) {
        this.action = action;
    }

    public interface Action {
        void onItemClick(long id);
    }
}
