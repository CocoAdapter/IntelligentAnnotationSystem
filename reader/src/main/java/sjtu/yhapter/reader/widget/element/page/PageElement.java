package sjtu.yhapter.reader.widget.element.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sjtu.yhapter.reader.App;
import sjtu.yhapter.reader.model.LineData;
import sjtu.yhapter.reader.model.PageData;
import sjtu.yhapter.reader.model.PointChar;
import sjtu.yhapter.reader.util.LogUtil;
import sjtu.yhapter.reader.util.ScreenUtil;
import sjtu.yhapter.reader.util.StringUtil;

/**
 * Created by CocoAdapter on 2018/11/19.
 */

public class PageElement {
    protected HeaderElement headerElement;
    protected LineElement lineElement;
    protected FooterElement footerElement;

    protected int viewWidth;
    protected int viewHeight;

    protected int hPadding;
    protected int vPadding;

    private PageData currPage;
    private PageData cancelPage;

    private List<PageData> currChapterPage;

    public PageElement(int viewWidth, int viewHeight, int hPadding, int vPadding) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.hPadding = hPadding;
        this.vPadding = vPadding;

        headerElement = new HeaderElement();
        lineElement = new LineElement();
        footerElement = new FooterElement();

        headerElement.setRectF(new RectF(hPadding, vPadding,
                viewWidth - hPadding, vPadding + ScreenUtil.dpToPx(20)));
        footerElement.setRectF(new RectF(hPadding, viewHeight - vPadding - ScreenUtil.dpToPx(20),
                viewWidth - hPadding, viewHeight - vPadding));
        lineElement.setRectF(new RectF(hPadding, headerElement.getHeight() + vPadding,
                viewWidth - hPadding, viewHeight - footerElement.getHeight() - vPadding));

        currChapterPage = new ArrayList<>();
        // TODO 测试
        try {
            currChapterPage = loadChapter();
            currPage = currChapterPage.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PageData getCurrPage() {
        return currPage;
    }

    public boolean hasPrePage() {
        int index = currPage.position - 1;
        return index >= 0;
    }

    public boolean hasNextPage() {
        int index = currPage.position + 1;
        return index < currChapterPage.size();
    }

    public void cancelPage() {
        currPage = cancelPage;
    }

    public void drawCurrPage(Canvas canvas) {
        drawPage(canvas);
    }

    public void drawPrePage(Canvas canvas) {
        if (hasPrePage()) {
            cancelPage = currPage;
            currPage = currChapterPage.get(currPage.position - 1);
            drawPage(canvas);
        }
    }

    public void print() {
        for (LineData lineData : currPage.lines) {
            LogUtil.log(this, lineData.toString());
        }
    }

    public void drawNextPage(Canvas canvas) {
        if (hasNextPage()) {
            cancelPage = currPage;
            currPage = currChapterPage.get(currPage.position + 1);
            drawPage(canvas);
        }
    }

    private void drawPage(Canvas canvas) {
        if (currPage == null)
            return; // TODO 空页面的绘制

        // bg
        canvas.drawColor(Color.rgb(255, 255, 240));

        // content
        headerElement.draw(canvas, currPage);

        footerElement.setTotalPageNum(currChapterPage.size());
        footerElement.draw(canvas, currPage);

        lineElement.draw(canvas, currPage);
    }

    private List<PageData> loadChapter() throws Exception {
        List<PageData> pages = new ArrayList<>();
        List<LineData> lines = new ArrayList<>();

        BufferedReader br = getBufferedReader();
        if (br == null)
            return null;
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
                    pageData.bookId = 1;
                    pageData.chapterId = 1;
                    pageData.title = "第一章";
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
            pageData.title = "第一章";
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

    private BufferedReader getBufferedReader() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(App.getInstance().getAssets().open("the_great_gatsby_test.txt")));
            return br;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
