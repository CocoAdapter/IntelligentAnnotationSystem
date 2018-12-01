package sjtu.yhapter.reader.loader;

import java.io.*;
import java.util.List;

import io.reactivex.disposables.Disposable;
import sjtu.yhapter.reader.loader.parser.PageParser;
import sjtu.yhapter.reader.loader.parser.TxtPageParser;
import sjtu.yhapter.reader.model.pojo.BookData;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.model.pojo.ReadingRecord;

/**
 * Created by Yhapter on 2018/11/28.
 */
public abstract class BookLoader {
    public static final int STATUS_PARSING = 1;
    public static final int STATUS_PARSING_FINISHED = 2;
    public static final int STATUS_PARSING_ERROR = 3;

    protected int status = STATUS_PARSING;

    protected PageParser pageParser;

    protected int currChapterIndex;
    protected int lastChapterIndex;

    protected BookData bookData;
    protected ReadingRecord readingRecord;

    protected OnPreLoadingListener onPreLoadingListener;
    protected Disposable preLoadPreDisp;
    protected Disposable preLoadNextDisp;

    protected OnPageChangeListener onPageChangeListener;

    public void setBookData(BookData bookData) {
        this.bookData = bookData;
        // TODO 数据库中查
        readingRecord = new ReadingRecord();
        currChapterIndex = readingRecord.getChapterIndex();
        lastChapterIndex = currChapterIndex;

        pageParser = new TxtPageParser();
    }

    public abstract void openBook();

    public List<? extends ChapterData> getChapters() {
        return pageParser.getChapters();
    }

    public BufferedReader getChapterReader(int chapterIndex) {
        if (status != STATUS_PARSING_FINISHED)
            return null;

        if (chapterIndex < 0 || chapterIndex >= pageParser.getChapters().size())
            return null;

        ChapterData chapter = pageParser.getChapters().get(chapterIndex);
        if (hasContent(chapter)) {
            return getChapterReader(chapter);
        }
        return null;
    }

    protected abstract boolean hasContent(ChapterData chapterData);

    protected abstract BufferedReader getChapterReader(ChapterData chapterData);

    public interface OnPreLoadingListener {
        void onPreLoadingPre(BufferedReader br, ChapterData chapterData);

        void onPreLoadingNext(BufferedReader br, ChapterData chapterData);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        /**
         * 作用：章节切换的时候进行回调
         *
         * @param pos:切换章节的序号
         */
//        void onChapterChange(int pos);

        /**
         * 作用：请求加载章节内容
         *
         * @param requestChapters:需要下载的章节列表
         */
//        void requestChapters(List<TxtChapter> requestChapters);

        /**
         * 作用：章节目录加载完成时候回调
         *
         * @param chapters：返回章节目录
         */
        void onChaptersLoaded(List<? extends ChapterData> chapters);

        /**
         * 作用：章节页码数量改变之后的回调。==> 字体大小的调整，或者是否关闭虚拟按钮功能都会改变页面的数量。
         *
         * @param count:页面的数量
         */
//        void onPageCountChange(int count);

        /**
         * 作用：当页面改变的时候回调
         *
         * @param pos:当前的页面的序号
         */
//        void onPageChange(int pos);
    }
}
