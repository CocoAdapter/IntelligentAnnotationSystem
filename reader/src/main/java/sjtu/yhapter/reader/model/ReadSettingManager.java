package sjtu.yhapter.reader.model;

import sjtu.yhapter.reader.util.SharedPrefUtil;

/**
 * Created by CocoAdapter on 2018/11/12.
 */

public class ReadSettingManager {
    private static final String SHARED_READ_BG = "shared_read_bg_color";

    private static volatile ReadSettingManager instance;

    private SharedPrefUtil sharedPrefUtil;

    private ReadSettingManager() {
        sharedPrefUtil = SharedPrefUtil.getInstance();
    }

    public static ReadSettingManager getInstance() {
        if (instance == null) {
            synchronized (ReadSettingManager.class) {
                if (instance == null) {
                    instance = new ReadSettingManager();
                }
            }
        }
        return instance;
    }

//    public void setPageBg(PageStyle pageStyle) {
//        sharedPreUtils.putInt(SHARED_READ_BG, pageStyle.ordinal());
//    }
}
