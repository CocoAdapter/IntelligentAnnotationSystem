package sjtu.yhapter.reader.model.pojo;

import android.os.Parcelable;

public interface Book extends Parcelable {
    Long id();

    String path();
}
