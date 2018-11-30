package sjtu.yhapter.reader.loader;

import java.io.*;
import java.util.List;

import io.reactivex.disposables.Disposable;
import sjtu.yhapter.reader.loader.parser.PageParser;
import sjtu.yhapter.reader.loader.parser.TxtPageParser;
import sjtu.yhapter.reader.model.pojo.BookData;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.model.pojo.ReadingRecord;
import sjtu.yhapter.reader.util.LogUtil;

/**
 * Created by Yhapter on 2018/11/28.
 */
public abstract class BookLoader {
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FINISH = 2;
    public static final int STATUS_ERROR = 3;
//    public static final int STATUS_EMPTY = 4;
    public static final int STATUS_PARSING = 5;
    public static final int STATUS_PARSING_ERROR = 6;
//    public static final int STATUS_CATEGORY_EMPTY = 7;

    protected int status = STATUS_LOADING;

    protected PageParser pageParser;

    protected int currChapterIndex;
    protected int lastChapterIndex;

    protected BookData bookData;
    protected ReadingRecord readingRecord;

    protected OnPreLoadingListener onPreLoadingListener;
    protected Disposable preLoadDisp;

    protected OnPageChangeListener onPageChangeListener;

    public BookLoader() {

    }

    public void setBookData(BookData bookData) {
        this.bookData = bookData;
        // TODO 数据库中查
        readingRecord = new ReadingRecord();
        currChapterIndex = readingRecord.getChapterIndex();
        lastChapterIndex = currChapterIndex;

        pageParser = new TxtPageParser();
    }

    public void skipToChapter(int chapterIndex) {
        currChapterIndex = chapterIndex;
    }

    public int getCurrChapterIndex() {
        return currChapterIndex;
    }

    public boolean toNextChapter() {
        currChapterIndex++;
        if (currChapterIndex >= pageParser.getChapters().size()) {
            currChapterIndex--;
            return false;
        }
        return true;
    }

    public boolean toPreChapter() {
        currChapterIndex--;
        if (currChapterIndex < 0) {
            currChapterIndex++;
            return false;
        }
        return true;
    }

    public BufferedReader getChapterReader() {
        return getChapterReader(currChapterIndex);
    }

    public ChapterData getChapterData() {
        return getChapters().get(currChapterIndex);
    }

    public abstract void preLoadingPre();

    public abstract void preLoadingNext();

    public int getStatus() {
        return status;
    }

    public abstract void openBook();

    public List<? extends ChapterData> getChapters() {
        return pageParser.getChapters();
    }

    protected boolean hasNextChapter() {
        return currChapterIndex + 1 < getChapters().size();
    }

    protected BufferedReader getChapterReader(int chapterIndex) {
        if (status != STATUS_FINISH)
            return null;

        ChapterData chapter = pageParser.getChapters().get(chapterIndex);
        if (hasContent(chapter)) {
            return getChapterReader(chapter);
        }
        return null;
    }

    protected abstract boolean hasContent(ChapterData chapterData);

    protected abstract BufferedReader getChapterReader(ChapterData chapterData);

    public void setOnPreLoadingListener(OnPreLoadingListener onPreLoadingListener) {
        this.onPreLoadingListener = onPreLoadingListener;
    }

    public interface OnPreLoadingListener {
        void onPreLoadingPre(BufferedReader bufferedReader, ChapterData chapterData);

        void onPreLoadingNext(BufferedReader bufferedReader, ChapterData chapterData);
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

    public static void main(String[] args) throws IOException {
        BookData bookData = new BookData();
        bookData.setId(1);
        bookData.setPath("the_great_gatsby.txt");

        BookLoader bookLoader = new LocalBookLoader();
        bookLoader.setBookData(bookData);
        bookLoader.openBook();

        BufferedReader reader = bookLoader.getChapterReader();
        String line = null;
        if (reader == null)
            return;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
