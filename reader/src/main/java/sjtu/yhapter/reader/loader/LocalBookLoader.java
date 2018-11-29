package sjtu.yhapter.reader.loader;

import java.io.*;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.model.pojo.BookData;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class LocalBookLoader extends BookLoader {
    private File file;

    public LocalBookLoader(BookData bookData) {
        super(bookData);
    }

    @Override
    public void openBook() {
        status = STATUS_PARSING;
        try {
            // TODO 数据库根据id找到路径
            file = new File(bookData.getPath());
            pageParser.parse(App.getInstance().getAssets().open("the_great_gatsby.txt"), file);
//            pageParser.parse(file);
            status = STATUS_FINISH;
        } catch (Exception e) {
            e.printStackTrace();
            status = STATUS_PARSING_ERROR;
        }
    }

    @Override
    protected boolean hasContent(ChapterData chapterData) {
        return true;
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

    @Override
    public void preLoadingPre() {
        int preIndex = currChapterIndex - 1;
        preLoading(true, preIndex);
    }

    @Override
    public void preLoadingNext() {
        int preIndex = currChapterIndex + 1;
        preLoading(false, preIndex);
    }

    private void preLoading(boolean isPre, int index) {
        if (onPreLoadingListener == null)
            return;

        final ChapterData nextChapter = getChapters().get(index);
        // no next chapter, or no content in the next chapter(unavailable)
        if (!hasNextChapter() || !hasContent(nextChapter))
            return;

        if (preLoadDisp != null)
            preLoadDisp.dispose();

        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
            LogUtil.log(this, "preLoading: " + index);
            emitter.onSuccess(getChapterReader(index));
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<BufferedReader>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        preLoadDisp = d;
                    }

                    @Override
                    public void onSuccess(BufferedReader o) {
                        if (isPre)
                            onPreLoadingListener.onPreLoadingPre(o, nextChapter);
                        else
                            onPreLoadingListener.onPreLoadingNext(o, nextChapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
