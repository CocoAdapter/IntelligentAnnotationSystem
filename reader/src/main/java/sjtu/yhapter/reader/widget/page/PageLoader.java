package sjtu.yhapter.reader.widget.page;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

import sjtu.yhapter.reader.model.PageData;

/**
 * Created by CocoAdapter on 2018/11/15.
 */

public class PageLoader {
    protected int index = 10;

    private boolean isLastMovingNext;

    public PageData getCurrPageData() {
        return new PageData() {
            @Override
            public int position() {
                return index;
            }

            @Override
            public String title() {
                return null;
            }

            @Override
            public List<String> texts() {
                return null;
            }

            @Override
            public Map<Integer, Bitmap> bitmaps() {
                return null;
            }
        };
    }

    public PageData getPrePageData() {
        isLastMovingNext = false;
        index--;
        return new PageData() {
            @Override
            public int position() {
                return index;
            }

            @Override
            public String title() {
                return null;
            }

            @Override
            public List<String> texts() {
                return null;
            }

            @Override
            public Map<Integer, Bitmap> bitmaps() {
                return null;
            }
        };
    }

    public PageData getNextPageData() {
        isLastMovingNext = true;
        index++;
        return new PageData() {
            @Override
            public int position() {
                return index;
            }

            @Override
            public String title() {
                return null;
            }

            @Override
            public List<String> texts() {
                return null;
            }

            @Override
            public Map<Integer, Bitmap> bitmaps() {
                return null;
            }
        };
    }

    public void cancelPrepare() {
        if (isLastMovingNext)
            index--;
        else
            index++;
    }
}
