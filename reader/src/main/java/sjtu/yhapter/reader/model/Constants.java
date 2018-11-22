package sjtu.yhapter.reader.model;

import android.graphics.Bitmap;
import android.view.ViewConfiguration;

import sjtu.yhapter.reader.App;

/**
 * Created by CocoAdapter on 2018/11/14.
 */

public class Constants {

    public static final int SLOP = ViewConfiguration.get(App.getInstance()).getScaledTouchSlop();

    public static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
}
