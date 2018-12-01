package sjtu.yhapter.reader.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sjtu.yhapter.reader.App;
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

/**
 * for UI 预加载数据太头痛了，有问题，暂时不考虑预加载了
 * Created by CocoAdapter on 2018/11/19.
 */

public class PageElement {
    // 用异步后这个状态码似乎没用了
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

    protected int currChapterIndex;

    private PageData currPage;
    private PageData cancelPage;

    private List<PageData> preChapterPages;
    private List<PageData> currChapterPages;
    private List<PageData> nextChapterPages;

    private int lastChapterIndex;

    // annotation
    private Map<PageData, Set<Annotation>> pageAnnotationMap;

    private Disposable loadDisp;
    private Disposable preLoadPreDisp, preLoadNextDisp;

    public PageElement(ReaderView readerView) {
        // widget init
        this.readerView = readerView;
        bookLoader = new LocalBookLoader();

        // data init
        currChapterPages = new ArrayList<>();
        pageAnnotationMap = new HashMap<>();

        // UI init
        statusElement = new StatusElement();
        headerElement = new HeaderElement();
        lineElement = new LineElement();
        footerElement = new FooterElement();

        currChapterIndex = 0;
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

        dealLoadChapterPages(currChapterIndex, pageData -> {
            if (pageData != null && !pageData.isEmpty()) {
                currChapterPages = pageData;
                // TODO 这里页码可能错误
                if (currPage != null)
                    currPage = currChapterPages.get(currPage.position);
                else
                    currPage = currChapterPages.get(0);

                readerView.prepareCurrPage();
            }
        }, pageData -> {
            readerView.postInvalidate();
        });

    }

    public PageData getCurrPage() {
        return currPage;
    }

    public boolean hasPrePage() {
        if (!canTurnPage())
            return false;

        int index = currPage.position - 1;
        // find in cache
        if (index >= 0)
            return true;
        else {
            // fetch next chapter if miss
            if (preChapterPages != null && !preChapterPages.isEmpty()) {
                return true;
            } else if (currChapterIndex - 1 >= 0){
                // no cache, may be in caching
                return true;
            }
            // return false if no next
            return false;
        }
    }

    /**
     * can be only called after hasPrePage
     * TODO 没有检查是否call在hasPrePage之后，这里需要重新设计，下同
     * @param canvas canvas
     */
    public void drawPrePage(Canvas canvas) {
        cancelPage = currPage;
        if (currPage.position - 1 < 0) {
            // move window left
            int preChapter = currChapterIndex - 1;
            lastChapterIndex = currChapterIndex;
            currChapterIndex = preChapter;

            abortPreLoadChapter();
            nextChapterPages = currChapterPages;
            currChapterPages = preChapterPages;
            preChapterPages = null;

            if (currChapterPages == null) {
                currPage = null;

                dealLoadChapterPages(currChapterIndex, pageData -> {
                    if (pageData != null) {
                        currChapterPages = pageData;
                        currPage = currChapterPages.get(currChapterPages.size() - 1);

                        readerView.prepareCurrPage();
                    }
                }, pageData -> {
                    readerView.postInvalidate();
                });
            } else {
                currPage = currChapterPages.get(currChapterPages.size() - 1);
            }
        } else
            currPage = currChapterPages.get(currPage.position - 1);

        drawPage(canvas);
    }

    public boolean hasNextPage() {
        if (!canTurnPage())
            return false;

        int index = currPage.position + 1;
        // find in curr
        if (index < currChapterPages.size())
            return true;
        else  {
            // fetch in next
            if (nextChapterPages != null && !nextChapterPages.isEmpty()) {
                return true;
            } else if (currChapterIndex + 1 < bookLoader.getChapters().size()) {
                // just no cache yet
                return true;
            }
            // return false if no next
            return false;
        }
    }

    public void drawNextPage(Canvas canvas) {
        cancelPage = currPage;

        if (currPage.position + 1 >= currChapterPages.size()) {
            int nextChapterIndex = currChapterIndex + 1;
            lastChapterIndex = currChapterIndex;
            currChapterIndex = nextChapterIndex;

            abortPreLoadChapter();
            preChapterPages = currChapterPages;
            currChapterPages = nextChapterPages; // move window right
            nextChapterPages = null;

            if (currChapterPages == null) {
                currPage = null;

                dealLoadChapterPages(currChapterIndex, pageData -> {
                    if (pageData != null) {
                        currChapterPages = pageData;
                        currPage = currChapterPages.get(0);

                        readerView.prepareCurrPage();
                    }
                }, pageData -> {
                    readerView.invalidate();
//                    preLoadChapter(currChapterIndex);
                });
            } else {
                currPage = currChapterPages.get(0);
//                preLoadChapter(currChapterIndex);
            }
        } else
            currPage = currChapterPages.get(currPage.position + 1);

        drawPage(canvas);
    }

    public void cancelPage() {
        abortPreLoadChapter();

        if (currChapterIndex > lastChapterIndex) {
            if (preChapterPages != null) {
                int tem = lastChapterIndex;
                lastChapterIndex = currChapterIndex;
                currChapterIndex = tem;
                nextChapterPages = currChapterPages;
                currChapterPages = preChapterPages;
                preChapterPages = null;

            } else {
                // 前一章为空 ？？ 为什么会出现这种情况
                LogUtil.log(this, "前一章为空 ？？ 为什么会出现这种情况");
            }
        } else if (currChapterIndex < lastChapterIndex) {
            int tem = lastChapterIndex;
            lastChapterIndex = currChapterIndex;
            currChapterIndex = tem;

            preChapterPages = currChapterPages;
            currChapterPages = nextChapterPages;
            nextChapterPages = null;
        }

        currPage = cancelPage;
        cancelPage = null;
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
        abortPreLoadChapter();

        // TODO public方法, index 可能越界
        currChapterIndex = index;

        // rest
        currChapterPages = null;
        preChapterPages = null;
        nextChapterPages = null;
        currPage = null;

        // show loading
        // 存在一个问题，就算是加载很快，也会刷一下
        readerView.prepareCurrPage();

        dealLoadChapterPages(index, pageData -> {
            if (pageData != null && !pageData.isEmpty()) {
                currChapterPages = pageData;
                currPage = currChapterPages.get(0);

                readerView.prepareCurrPage();
            }
        }, pageData -> {
            readerView.postInvalidate();
//            preLoadChapter(index);
        });
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

    /**
     * non-concurrent, the new call will cancel the pre call
     *
     * @param chapterIndex chapterIndex
     * @param onBgThread action on List<PageData>, which may be null, runs on bg thread
     * @param onUiThread action on List<PageData>, may be null, runs on ui thread
     */
    private void dealLoadChapterPages(int chapterIndex, Action<List<PageData>> onBgThread, Action<List<PageData>> onUiThread) {
        status = STATUS_LOADING;

        abortPreLoadChapter();
        if (loadDisp != null && !loadDisp.isDisposed())
            loadDisp.dispose();

        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
            BufferedReader br = bookLoader.getChapterReader(chapterIndex);
            if (br != null)
                emitter.onSuccess(br);
            else
                emitter.onError(new Exception());
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(br -> {
                    ChapterData chapter = bookLoader.getChapters().get(chapterIndex);
                    List<PageData> pageData = getChapterPages(br, chapter);
                    if (onBgThread != null)
                        onBgThread.call(pageData);
                    return pageData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PageData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loadDisp = d;
                    }

                    @Override
                    public void onSuccess(List<PageData> pageData) {
                        status = STATUS_FINISHED;

                        if (onUiThread != null)
                            onUiThread.call(pageData);
                    }
                    @Override
                    public void onError(Throwable e) {
                        currChapterPages = null;
                        status = STATUS_ERROR;
                        if (onUiThread != null)
                            onUiThread.call(null);
                    }
                });
    }

    // 线程在这里竞争了, 用synchronized 不是个很好的解决方法，应该让LineElement线程安全
    private synchronized List<PageData> getChapterPages(BufferedReader br, ChapterData chapter) throws Exception {
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
                        pageData.title = chapter.getTitle();
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
                        // TODO 这里可以传入给 lineElement 设置 baseLine
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
                pageData.title = chapter.getTitle();
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
            throw new Exception(e);
        } finally {
            IOUtil.close(br);
        }
    }

    private Set<Annotation> annotations = new HashSet<>();
    private AtomicInteger count = new AtomicInteger(1);

    private void abortPreLoadChapter() {
        if (preLoadPreDisp != null && !preLoadPreDisp.isDisposed())
            preLoadPreDisp.dispose();
        if (preLoadNextDisp != null && !preLoadNextDisp.isDisposed())
            preLoadNextDisp.dispose();
    }

    // TODO 这个预加载 实在是太有问题了，很容易出错，暂时先不管了
    private void preLoadChapter(int currChapterIndex) {
        abortPreLoadChapter();

        final int pre = currChapterIndex - 1;
        final int next = currChapterIndex + 1;

//        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
//            BufferedReader br = bookLoader.getChapterReader(pre);
//            if (br != null)
//                emitter.onSuccess(br);
//            else
//                emitter.onError(new Exception());
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.computation())
//                .subscribe(new SingleObserver<BufferedReader>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        preLoadPreDisp = d;
//                    }
//
//                    @Override
//                    public void onSuccess(BufferedReader br) {
//                        ChapterData chapter = bookLoader.getChapters().get(pre);
//                        try {
//                            preChapterPages = getChapterPages(br, chapter);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        preChapterPages = null;
//                    }
//                });

        Single.create((SingleOnSubscribe<BufferedReader>) emitter -> {
            BufferedReader br = bookLoader.getChapterReader(next);
            if (br != null)
                emitter.onSuccess(br);
            else
                emitter.onError(new Exception());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new SingleObserver<BufferedReader>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        preLoadNextDisp = d;
                    }

                    @Override
                    public void onSuccess(BufferedReader br) {
                        ChapterData chapter = bookLoader.getChapters().get(next);
                        try {
                            nextChapterPages = getChapterPages(br, chapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        nextChapterPages = null;
                    }
                });
    }

    private boolean canTurnPage() {
        if (status == STATUS_LOADING)
            return false;

        if (status == STATUS_ERROR) {
            LogUtil.log(this, "status error, cannot turn page");
            return false;
        }

        return true;
    }

    private interface Action<T> {
        void call(T t);
    }
}
