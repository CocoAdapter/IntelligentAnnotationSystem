package sjtu.yhapter.reader.widget.page;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

/**
 * Created by CocoAdapter on 2018/11/12.
 */

public interface PageData {
    int position();

    String title();

    List<String> texts();

    Map<Integer, Bitmap> bitmaps();
}
