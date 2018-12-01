package sjtu.yhapter.reader.loader;

import java.io.*;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.model.pojo.ChapterData;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class LocalBookLoader extends BookLoader {
    private File file;

    @Override
    public void openBook() {
        status = STATUS_PARSING;
        try {
            // TODO 数据库根据id找到路径
            file = new File(bookData.getPath());
            pageParser.parse(App.getInstance().getAssets().open("the_great_gatsby.txt"), file);
//            pageParser.parse(file);
            status = STATUS_PARSING_FINISHED;
            if (onPageChangeListener != null) {
                onPageChangeListener.onChaptersLoaded(pageParser.getChapters());
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = STATUS_PARSING_ERROR;
        }
    }

    @Override
    protected boolean hasContent(ChapterData chapterData) {
        return status == STATUS_PARSING_FINISHED;
    }

    @SuppressWarnings("all")
    public BufferedReader getChapterReader(ChapterData chapterData) {
        try {
            byte[] content = pageParser.getChapterContent(new FileInputStream(file), chapterData);
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            return new BufferedReader(new InputStreamReader(bais, pageParser.getCharset().getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
