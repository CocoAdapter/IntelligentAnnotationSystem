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

    @Override
    public void preLoadingPre() {
        preLoading(true, currChapterIndex - 1);
    }

    @Override
    public void preLoadingNext() {
        preLoading(false, currChapterIndex + 1);
    }

    @Override
    public void abortPreLoad() {
        if (preLoadPreDisp != null && !preLoadPreDisp.isDisposed())
            preLoadPreDisp.dispose();

        if (preLoadNextDisp != null && !preLoadNextDisp.isDisposed())
            preLoadNextDisp.dispose();
    }

    private void preLoading(boolean isPre, int index) {
        if (onPreLoadingListener == null)
            return;

        if (isPre) {
            if (preLoadPreDisp != null && !preLoadPreDisp.isDisposed())
                preLoadPreDisp.dispose();
        } else {
            if (preLoadNextDisp != null && !preLoadNextDisp.isDisposed())
                preLoadNextDisp.dispose();
        }

        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
            BufferedReader bf = getChapterReader(index);
            if (bf != null)
                emitter.onSuccess(getChapterReader(index));
            else
                emitter.onError(new NullPointerException());
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new SingleObserver<BufferedReader>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (isPre)
                            preLoadPreDisp = d;
                        else
                            preLoadNextDisp = d;
                    }

                    @Override
                    public void onSuccess(BufferedReader bf) {
                        ChapterData nextChapter = getChapters().get(index);
                        if (isPre)
                            onPreLoadingListener.onPreLoadingPre(bf, nextChapter);
                        else {
                            onPreLoadingListener.onPreLoadingNext(bf, nextChapter);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onPreLoadingListener.onPreLoadingNext(null, null);
                    }
                });
    }
}
