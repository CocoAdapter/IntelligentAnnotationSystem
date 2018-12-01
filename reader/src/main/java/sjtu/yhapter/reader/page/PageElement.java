package sjtu.yhapter.reader.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.ChapterDataAdapter;
import sjtu.yhapter.reader.loader.BookLoader;
import sjtu.yhapter.reader.loader.LocalBookLoader;
import sjtu.yhapter.reader.model.pojo.Annotation;
import sjtu.yhapter.reader.model.pojo.BookData;
import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.model.pojo.LineData;
import sjtu.yhapter.reader.model.pojo.PageData;
import sjtu.yhapter.reader.model.pojo.PointChar;
import sjtu.yhapter.reader.reader.ReaderView;
import sjtu.yhapter.reader.util.IOUtil;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.util.StringUtil;
import sjtu.yhapter.reader.page.annotation.AnnotationType;

/**
 * for UI
 * Created by CocoAdapter on 2018/11/19.
 */

public class PageElement implements BookLoader.OnPreLoadingListener {
    protected final static int STATUS_LOADING = 0;
    protected final static int STATUS_FINISHED = 1;
    protected final static int STATUS_ERROR = 2;

    protected int status;

    protected StatusElement statusElement;
    protected HeaderElement headerElement;
    protected LineElement lineElement;
    protected FooterElement footerElement;

    protected int viewWidth;
    protected int viewHeight;

    protected int hPadding;
    protected int vPadding;

    protected ReaderView readerView;
    protected BookLoader bookLoader;

    private PageData currPage;
    private PageData cancelPage;

    private List<PageData> preChapterPages;
    private List<PageData> currChapterPages;
    private List<PageData> nextChapterPages;

    // annotation
    private Map<PageData, Set<Annotation>> pageAnnotationMap;

    private Disposable loadDisp;

    public PageElement(ReaderView readerView) {
        // widget init
        this.readerView = readerView;
        bookLoader = new LocalBookLoader();
        bookLoader.setOnPreLoadingListener(this);

        // data init
        currChapterPages = new ArrayList<>();
        pageAnnotationMap = new HashMap<>();

        // UI init
        statusElement = new StatusElement();
        headerElement = new HeaderElement();
        lineElement = new LineElement();
        footerElement = new FooterElement();
    }

    public void openBook() {
        BookData bookData = new BookData();
        bookData.setId(1);
        bookData.setPath(App.getInstance().getFilesDir().getPath() + "the_great_gatsby.txt");

        bookLoader.setBookData(bookData);
        bookLoader.openBook();
    }

    public void onSizeChanged(int viewWidth, int viewHeight, int hPadding, int vPadding) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.hPadding = hPadding;
        this.vPadding = vPadding;

        statusElement.setRectF(new RectF(hPadding, vPadding, viewWidth - hPadding, viewHeight - vPadding));
        headerElement.setRectF(new RectF(hPadding, vPadding,
                viewWidth - hPadding, vPadding + ScreenUtil.dpToPx(20)));
        footerElement.setRectF(new RectF(hPadding, viewHeight - vPadding - ScreenUtil.dpToPx(20),
                viewWidth - hPadding, viewHeight - vPadding));
        lineElement.setRectF(new RectF(hPadding, headerElement.getHeight() + vPadding,
                viewWidth - hPadding, viewHeight - footerElement.getHeight() - vPadding));

        // TODO 测试
        loadChapter(false);
    }

    public PageData getCurrPage() {
        return currPage;
    }

    public boolean hasPrePage() {
        if (status != STATUS_FINISHED)
            return false;

        int index = currPage.position - 1;
        // find in cache
        if (index >= 0)
            return true;
        else {
            // fetch next chapter if miss
            if (preChapterPages != null && !preChapterPages.isEmpty()) {
                return true;
            } else if (bookLoader.hasPreChapter()){
                // no cache
                return true;
            }
            // return false if no next
            return false;
        }
    }

    /**
     * TODO 没有检查是否call在hasPrePage之后，这里需要重新设计，下同
     * @param canvas
     */
    public void drawPrePage(Canvas canvas) {
        cancelPage = currPage;
        if (currPage.position - 1 < 0) {
            bookLoader.abortPreLoad();

            // move window left
            nextChapterPages = currChapterPages;
            currChapterPages = preChapterPages;
            preChapterPages = null;

            if (currChapterPages == null) {
                currPage = null;
                if (bookLoader.toPreChapter()) {
                    loadChapter(true);
                }
            } else {
                currPage = currChapterPages.get(currChapterPages.size() - 1);
                if (bookLoader.toPreChapter())
                    bookLoader.preLoadingPre();
            }
        } else
            currPage = currChapterPages.get(currPage.position - 1);
        drawPage(canvas);
    }

    public boolean hasNextPage() {
        if (status != STATUS_FINISHED)
            return false;

        int index = currPage.position + 1;
        // find in curr
        if (index < currChapterPages.size())
            return true;
        else  {
            // fetch in next
            if (nextChapterPages != null && !nextChapterPages.isEmpty()) {
                return true;
            } else if (bookLoader.hasNextChapter()) {
                // no cache
                return true;
            }
            // return false if no next
            return false;
        }
    }

    public void drawNextPage(Canvas canvas) {
        cancelPage = currPage;
        if (currPage.position + 1 >= currChapterPages.size()) {
            bookLoader.abortPreLoad();

            preChapterPages = currChapterPages;
            currChapterPages = nextChapterPages; // move window right
            nextChapterPages = null;

            if (currChapterPages == null) {
                currPage = null;
                if (bookLoader.toNextChapter()) {
                    loadChapter(false);
                }
            } else {
                currPage = currChapterPages.get(0);
                if (bookLoader.toNextChapter())
                    bookLoader.preLoadingNext();
            }

        } else
            currPage = currChapterPages.get(currPage.position + 1);
        drawPage(canvas);
    }

    public void cancelPage() {
        currPage = cancelPage;

        // TODO bug, 加载到下一章取消了加载又
    }

    public void drawCurrPage(Canvas canvas) {
        drawPage(canvas);
    }

    public void addAnnotation(Annotation annotation) {
        if (annotation == null)
            return;

        // TODO fake init
        if (annotation.getId() == 0)
            annotation.setId(count.getAndIncrement());

        annotations.add(annotation);
        pageAnnotationMap.get(currPage).add(annotation);
    }

    public void delAnnotation(Annotation annotation) {
        if (annotation == null)
            return;

        annotations.remove(annotation);
        pageAnnotationMap.get(currPage).remove(annotation);
    }

    /**
     * check if a annotation exists at (x, y), and return it if it does.
     *
     * @param x x of MotionEvent
     * @param y y of MotionEvent
     * @return the responding annotation representation if it is, null otherwise
     */
    public Annotation checkIfAnnotation(int x, int y) {
        return lineElement.checkIfAnnotation(x, y, currPage);
    }

    // 公共接口
    public void setOnPageChangeListener(BookLoader.OnPageChangeListener onPageChangeListener) {
        if (bookLoader != null)
            bookLoader.setOnPageChangeListener(onPageChangeListener);
    }

    public void skipToChapter(int index) {
        bookLoader.abortPreLoad();
        if (loadDisp != null && !loadDisp.isDisposed())
            loadDisp.dispose();

        preChapterPages = null;
        nextChapterPages = null;

        // show loading
        currChapterPages = null;
        currPage = null;
        readerView.prepareCurrPage();
        // do skip
        bookLoader.skipToChapter(index);
        loadChapter(false);
    }

    private Set<Annotation> getAnnotationsOfPage(PageData pageData) {
        if (annotations == null || annotations.isEmpty())
            return null;

        List<Annotation> annotations = new ArrayList<>();

        // getSelectLines
        long charStart = pageData.lines.get(0).getChars().get(0).chapterIndex;
        List<PointChar> tem = pageData.lines.get(pageData.lines.size() - 1).getChars();
        long charEnd = tem.get(tem.size() - 1).chapterIndex;

        Collections.sort(annotations, (o1, o2) -> (int) (o1.getStartIndex() - o2.getStartIndex()));
        for (Annotation annotation : annotations) {
            if (annotation.getStartIndex() < charStart)
                continue;
            if (annotation.getStartIndex() <= charEnd) {
                annotations.add(annotation);
            } else
                break;
        }

        return new HashSet<>(annotations);
    }

    private void drawPage(Canvas canvas) {
        // bg
        canvas.drawColor(Color.parseColor("#A8C5A8"));

        if (currPage == null) {
            statusElement.draw(canvas, null);
            return;
        }

        // content
        headerElement.draw(canvas, currPage);

        footerElement.setTotalPageNum(currChapterPages.size());
        footerElement.draw(canvas, currPage);

        if (pageAnnotationMap.get(currPage) == null) {
            Set<Annotation> annotations = getAnnotationsOfPage(currPage);
            pageAnnotationMap.put(currPage, annotations);
        }
        lineElement.setAnnotations(pageAnnotationMap.get(currPage));
        lineElement.draw(canvas, currPage);
    }

    private void loadChapter(boolean last) {
        status = STATUS_LOADING;

        bookLoader.abortPreLoad();
        if (loadDisp != null && !loadDisp.isDisposed())
            loadDisp.dispose();

        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
            emitter.onSuccess(bookLoader.getChapterReader());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(br -> {
                    String title = bookLoader.getChapterData().getTitle();
                    currChapterPages = getChapterPages(br, title);

                    if (currChapterPages != null && !currChapterPages.isEmpty()) {
                        currPage = currChapterPages.get(last ? currChapterPages.size() - 1 : 0);
                        // TODO 加载标注
                    } else {
                        currPage = null;
                    }

                    status = STATUS_FINISHED;
                    readerView.prepareCurrPage();
                    return currPage;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PageData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loadDisp = d;
                    }

                    @Override
                    public void onSuccess(PageData currPage) {
                        readerView.postInvalidate();
//                        if (last)
//                            bookLoader.preLoadingNext();
//                        else
//                            bookLoader.preLoadingPre();
                        bookLoader.preLoadingNext();
                        bookLoader.preLoadingPre();
                    }

                    @Override
                    public void onError(Throwable e) {
                        status = STATUS_ERROR;
                    }
                });
        // 回调
    }

    // 线程在这里竞争了, 用synchronized 不是个很好的解决方法，应该让LineElement线程安全
    private synchronized List<PageData> getChapterPages(BufferedReader br, String title) {
        try {
            if (br == null)
                return null;

            List<PageData> pages = new ArrayList<>();
            List<LineData> lines = new ArrayList<>();

            int contentHeight = lineElement.getHeight();

            String paragraph;
            lineElement.resetParse();
            while ((paragraph = br.readLine()) != null) {
                paragraph = paragraph.replaceAll("\\s", ""); // remove blank
                if (paragraph.equals("")) continue;

                paragraph = "\u3000\u3000" + paragraph; // paragraph indent
                paragraph = StringUtil.halfToFull(paragraph);

                int wordCount;
                String subStr;
                while (paragraph.length() > 0) {
                    contentHeight -= lineElement.getLineHeight();
                    if (contentHeight <= 0) {
                        PageData pageData = new PageData();
                        // TODO
                        pageData.bookId = 1;
                        pageData.chapterId = 1;
                        pageData.title = title;
                        pageData.position = pages.size();
                        pageData.lines = new ArrayList<>(lines);
                        pageData.startIndex = lines.get(0).getChars().get(0).chapterIndex;
                        List<PointChar> pcs = lines.get(lines.size() - 1).getChars();
                        pageData.endIndex = pcs.get(pcs.size() - 1).chapterIndex;
                        pages.add(pageData);
                        // reset
                        lines.clear();
                        contentHeight = lineElement.getHeight();
                        lineElement.resetBaseLine();
                        continue;
                    }

                    wordCount = lineElement.getWordCountOfLine(paragraph);
                    subStr = paragraph.substring(0, wordCount);
                    if (!subStr.endsWith("\n")) {
                        LineData lineData = lineElement.measureLineData(subStr);
                        lines.add(lineData);
                        // 行间距
                    }

                    paragraph = paragraph.substring(wordCount);
                }
                // 处理段间距
            }

            if (lines.size() != 0) {
                PageData pageData = new PageData();
                pageData.bookId = 1;
                pageData.chapterId = 1;
                pageData.title = title;
                pageData.position = pages.size();
                pageData.lines = new ArrayList<>(lines);
                pageData.startIndex = lines.get(0).getChars().get(0).chapterIndex;
                List<PointChar> pcs = lines.get(lines.size() - 1).getChars();
                pageData.endIndex = pcs.get(pcs.size() - 1).chapterIndex;
                pages.add(pageData);
                lineElement.resetBaseLine();
                // reset
                lines.clear();
            }
            return pages;
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(br);
        }
    }

    private Set<Annotation> annotations = new HashSet<>();
    private AtomicInteger count = new AtomicInteger(1);

    @Override
    public void onPreLoadingPre(BufferedReader bufferedReader, ChapterData chapterData) {
        onPreLoading(true, bufferedReader, chapterData);
    }

    @Override
    public void onPreLoadingNext(BufferedReader bufferedReader, ChapterData chapterData) {
        onPreLoading(false, bufferedReader, chapterData);
    }

    private void onPreLoading(boolean isPre, BufferedReader bufferedReader, ChapterData chapterData) {
        if (bufferedReader == null || chapterData == null) {
            if (isPre)
                preChapterPages = null;
            else
                nextChapterPages = null;
            return;
        }

        final String title = chapterData.getTitle();

        List<PageData> pages = getChapterPages(bufferedReader, title);
        if (isPre) {
            preChapterPages = pages;
        } else {
            nextChapterPages = pages;
        }
    }
}
