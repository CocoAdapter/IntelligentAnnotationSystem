package sjtu.yhapter.reader.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

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
import io.reactivex.disposables.Disposable;
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
    protected HeaderElement headerElement;
    protected LineElement lineElement;
    protected FooterElement footerElement;

    protected int viewWidth;
    protected int viewHeight;

    protected int hPadding;
    protected int vPadding;

    protected BookLoader bookLoader;

    private PageData currPage;
    private PageData cancelPage;

    // TODO 线程安全性
    private List<PageData> preChapterPages;
    private List<PageData> currChapterPages;
    private List<PageData> nextChapterPages;

    // annotation
    private Map<PageData, Set<Annotation>> pageAnnotationMap;

    private Disposable preLoadDisp;

    public PageElement() {
        // data init
        currChapterPages = new ArrayList<>();
        pageAnnotationMap = new HashMap<>();

        bookLoader = new LocalBookLoader();
        bookLoader.setOnPreLoadingListener(this);

        // UI init
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

        headerElement.setRectF(new RectF(hPadding, vPadding,
                viewWidth - hPadding, vPadding + ScreenUtil.dpToPx(20)));
        footerElement.setRectF(new RectF(hPadding, viewHeight - vPadding - ScreenUtil.dpToPx(20),
                viewWidth - hPadding, viewHeight - vPadding));
        lineElement.setRectF(new RectF(hPadding, headerElement.getHeight() + vPadding,
                viewWidth - hPadding, viewHeight - footerElement.getHeight() - vPadding));

        // TODO 测试
        loadChapter();
        if (currChapterPages != null && !currChapterPages.isEmpty()) {
            currPage = currChapterPages.get(0);
            loadAnnotations();
        }
    }

    public PageData getCurrPage() {
        return currPage;
    }

    public boolean hasPrePage() {
        int index = currPage.position - 1;
        // find in cache
        if (index >= 0)
            return true;
        else {
            // fetch next chapter if miss
            if (preChapterPages != null && !preChapterPages.isEmpty()) {


                return true;
            } else if (bookLoader.toPreChapter()) {
                // cache error, try re-fetch
                // TODO 绘制加载界面
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
            if (preLoadDisp != null && !preLoadDisp.isDisposed())
                preLoadDisp.dispose();

            // move window left
            nextChapterPages = currChapterPages;
            currChapterPages = preChapterPages;
            preChapterPages = null;

            currPage = currChapterPages.get(currChapterPages.size() - 1);

            if (bookLoader.toPreChapter())
                bookLoader.preLoadingPre();
        } else
            currPage = currChapterPages.get(currPage.position - 1);
        drawPage(canvas);
    }

    public boolean hasNextPage() {
        int index = currPage.position + 1;
        // find in curr
        if (index < currChapterPages.size())
            return true;
        else  {
            // fetch in next
            if (nextChapterPages != null && !nextChapterPages.isEmpty()) {
                return true;
            } else if (bookLoader.toNextChapter()) {
                // cache error, try re-fetch
                // TODO 绘制加载界面, 强制触发重新加载？
            }
            // return false if no next
            return false;
        }
    }

    public void drawNextPage(Canvas canvas) {
        cancelPage = currPage;
        if (currPage.position + 1 >= currChapterPages.size()) {
            if (preLoadDisp != null && !preLoadDisp.isDisposed())
                preLoadDisp.dispose();

            preChapterPages = currChapterPages;
            currChapterPages = nextChapterPages; // move window right
            nextChapterPages = null;

            currPage = currChapterPages.get(0);

            if (bookLoader.toNextChapter())
                bookLoader.preLoadingNext();
        } else
            currPage = currChapterPages.get(currPage.position + 1);
        drawPage(canvas);
    }

    public void cancelPage() {
        currPage = cancelPage;
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
        if (currPage == null)
            return;

        // bg
        canvas.drawColor(Color.parseColor("#A8C5A8"));

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

    private void loadChapter() {
        try {
            BufferedReader br = bookLoader.getChapterReader();
            String title = bookLoader.getChapterData().getTitle();
            currChapterPages = getChapterPages(br, title);
            // TODO
            bookLoader.preLoadingNext();
        } catch (Exception e) {
            e.printStackTrace();

            currChapterPages.clear();
            currChapterPages = null;
        }

        // 回调
    }

    private List<PageData> getChapterPages(BufferedReader br, String title) throws Exception {
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
    }

    private Set<Annotation> annotations;
    private AtomicInteger count = new AtomicInteger(1);
    private Set<Annotation> loadAnnotations() {
        if (annotations != null)
            return annotations;

        annotations = new HashSet<>();
        // TODO test
        Annotation annotation = new Annotation();
        annotation.setId(count.getAndIncrement());
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotation.setContent("人的行为可能建立在坚固的岩石上面，也可能建立在潮湿的沼泽之中，但是一过某种程度，我就不管它是建立在什么上面的了。去年秋天我从");
        annotation.setStartIndex(537);
        annotation.setEndIndex(598);
        annotation.setType(AnnotationType.NORMAL.name());
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setId(count.getAndIncrement());
        annotation.setContent("身上出现的时候，心理不正常的人很快就会察觉并区抓住不放。由于这个缘故，我上大学的时候就被不公正地指责为小政客，因为我与闻一");
        annotation.setStartIndex(216);
        annotation.setEndIndex(276);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotation.setType(AnnotationType.NORMAL.name());
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setId(count.getAndIncrement());
        annotation.setContent("系的实际创始人却是我祖父的哥哥。他在一八五一年来到这里，买了个替身去参加南北战争，开始做起五金批发生意，也就是我父东今天");
        annotation.setStartIndex(1043);
        annotation.setEndIndex(1102);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotation.setType(AnnotationType.NORMAL.name());
        annotations.add(annotation);

        annotation = new Annotation();
        annotation.setId(count.getAndIncrement());
        annotation.setContent("的－－每逢我根据某种明白无误的迹象看出又有一次倾诉衷情在地平线上喷薄欲出的时候，我往往假装睡觉，假装心不在焉，或者装出不怀好");
        annotation.setStartIndex(309);
        annotation.setEndIndex(370);
        annotation.setBookId(1);
        annotation.setChapterId(1);
        annotation.setType(AnnotationType.NORMAL.name());
        annotations.add(annotation);

        return annotations;
    }

    @Override
    public void onPreLoadingPre(BufferedReader bufferedReader, ChapterData chapterData) {
        onPreLoading(true, bufferedReader, chapterData);
    }

    @Override
    public void onPreLoadingNext(BufferedReader bufferedReader, ChapterData chapterData) {
        onPreLoading(false, bufferedReader, chapterData);
    }

    private void onPreLoading(boolean isPre, BufferedReader bufferedReader, ChapterData chapterData) {
        if (bufferedReader == null)
            return;

        final String title = chapterData.getTitle();

        if (preLoadDisp != null && !preLoadDisp.isDisposed())
            preLoadDisp.dispose();

        Single.create((SingleOnSubscribe<List<PageData>>) emitter -> {
            try {
                emitter.onSuccess(getChapterPages(bufferedReader, title));
            } catch (Exception ignore) {}
            finally {
                IOUtil.close(bufferedReader);
            }
        }).subscribeOn(Schedulers.computation())
                .subscribe(new SingleObserver<List<PageData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        preLoadDisp = d;
                    }

                    @Override
                    public void onSuccess(List<PageData> pageDatas) {
                        if (isPre) {
                            LogUtil.log(PageElement.this, pageDatas == null ? "null" : "not null");
                        }
                        if (isPre)
                            preChapterPages = pageDatas;
                        else
                            nextChapterPages = pageDatas;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
