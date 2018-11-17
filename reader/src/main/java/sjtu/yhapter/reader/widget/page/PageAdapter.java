package sjtu.yhapter.reader.widget.page;

import android.graphics.Canvas;

/**
 * Created by CocoAdapter on 2018/11/17.
 */

public interface PageAdapter {

    Object getItem(int position);

    int getCount();

    boolean isEmpty();

    void draw(int position, Canvas canvas);
}
