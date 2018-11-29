package sjtu.yhapter.reader.loader.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.model.pojo.TxtChapterData;
import sjtu.yhapter.reader.util.IOUtil;

/**
 * Created by Yhapter on 2018/11/28.
 */
public class TxtPageParser implements PageParser {
    //默认从文件中获取数据的长度
    private final static int BUFFER_SIZE = 512 * 1024;
    //没有标题的时候，每个章节的最大长度
    private final static int MAX_LENGTH_WITH_NO_CHAPTER = 10 * 1024;

    // "序(章)|前言"
    private final static Pattern mPreChapterPattern = Pattern.compile("^(\\s{0,10})((\u5e8f[\u7ae0\u8a00]?)|(\u524d\u8a00)|(\u6954\u5b50))(\\s{0,10})$", Pattern.MULTILINE);

    //正则表达式章节匹配模式
    // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
    private static final String[] CHAPTER_PATTERNS = new String[]{"^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\f\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"};
    private Pattern chapterPattern;

    private IOUtil.Charset charset;

    private List<TxtChapterData> chapters;

    @Override
    public void parse(InputStream in, File file) throws Exception {
        FileOutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int length = 0;
        while ((length = in.read(buffer)) != -1) {
            os.write(buffer, 0, length);
            os.flush();
        }
        IOUtil.close(in);
        IOUtil.close(os);

        parse(file);
    }

    @Override
    public void parse(File file) throws Exception {
        charset = IOUtil.getCharset(new FileInputStream(file));
        //寻找匹配文章标题的正则表达式，判断是否存在章节名
        boolean hasChapter = checkChapterType(new FileInputStream(file), charset);
        parse(new FileInputStream(file), hasChapter);

        // DB operation
    }

    @Override
    public List<? extends ChapterData> getChapters() {
        return chapters;
    }

    // TODO index 把章节标题去掉
    private void parse(InputStream in, boolean hasChapter) throws Exception {
        chapters = new ArrayList<>();
        //加载章节
        byte[] buffer = new byte[BUFFER_SIZE];
        //获取到的块起始点，在文件中的位置
        long curOffset = 0;
        //读取的长度
        int length;
        //获取文件中的数据到buffer，直到没有数据为止
        while ((length = in.read(buffer, 0, buffer.length)) != -1) {
            if (hasChapter) {
                String block = new String(buffer, 0, length, charset.getName());
                //当前Block下使过的String的指针
                int seekPos = 0;
                //进行正则匹配
                Matcher matcher = chapterPattern.matcher(block);
                //如果存在相应章节
                while (matcher.find()) {
                    //获取匹配到的字符在字符串中的起始位置
                    int chapterStart = matcher.start();
                    //如果 seekPos == 0 && nextChapterPos != 0 表示当前block处前面有一段内容
                    //第一种情况一定是序章 第二种情况可能是上一个章节的内容
                    if (seekPos == 0 && chapterStart != 0) {
                        //获取当前章节的内容
                        String chapterContent = block.substring(seekPos, chapterStart);
                        //设置指针偏移
                        seekPos += chapterContent.length();
                        if (curOffset == 0) {
                            //如果当前对整个文件的偏移位置为0的话，那么就是序章
                            TxtChapterData preChapter = new TxtChapterData();
                            preChapter.setTitle("序章");
                            preChapter.setStartIndex(0);
                            preChapter.setEndIndex(chapterContent.getBytes(charset.getName()).length); //获取String的byte值,作为最终值

                            //如果序章大小大于30才添加进去
                            if (preChapter.getEndIndex() - preChapter.getStartIndex() > 30) {
                                chapters.add(preChapter);
                            }

                            //新起一章
                            TxtChapterData curChapter = new TxtChapterData();
                            curChapter.setTitle(matcher.group());
                            curChapter.setStartIndex(preChapter.getEndIndex());
                            chapters.add(curChapter);
                        } else {
                            //否则就block分割之后，上一个章节的剩余内容
                            TxtChapterData preChapter = chapters.get(chapters.size() - 1);
                            //将当前段落添加上一章去
                            preChapter.setEndIndex(preChapter.getEndIndex() + chapterContent.getBytes(charset.getName()).length);
                            //如果章节内容太小，则移除
                            if (preChapter.getEndIndex() - preChapter.getStartIndex() < 30) {
                                chapters.remove(preChapter);
                            }

                            //创建当前章节
                            TxtChapterData curChapter = new TxtChapterData();
                            curChapter.setTitle(matcher.group());
                            curChapter.setStartIndex(preChapter.getEndIndex());
                            chapters.add(curChapter);
                        }
                    } else {
                        //是否存在章节
                        if (chapters.size() != 0) {
                            //获取章节内容
                            String chapterContent = block.substring(seekPos, matcher.start());
                            seekPos += chapterContent.length();

                            //获取上一章节
                            TxtChapterData preChapter = chapters.get(chapters.size() - 1);
                            preChapter.setEndIndex(preChapter.getStartIndex() + chapterContent.getBytes(charset.getName()).length);

                            //如果章节内容太小，则移除
                            if (preChapter.getEndIndex() - preChapter.getStartIndex() < 30) {
                                chapters.remove(preChapter);
                            }

                            //创建当前章节
                            TxtChapterData curChapter = new TxtChapterData();
                            curChapter.setTitle(matcher.group());
                            curChapter.setStartIndex(preChapter.getEndIndex());
                            chapters.add(curChapter);
                        } else {
                            //如果章节不存在则创建章节
                            TxtChapterData curChapter = new TxtChapterData();
                            curChapter.setTitle(matcher.group());
                            curChapter.setStartIndex(0);
                            chapters.add(curChapter);
                        }
                    }
                }
            } else {
                // TODO 没有章节信息，如果文件很大需要虚拟分章
            }

            curOffset += length;
            if (hasChapter) {
                // 设置上一章的结尾
                TxtChapterData lastChapter = chapters.get(chapters.size() - 1);
                lastChapter.setEndIndex(curOffset);
            }
        }

        IOUtil.close(in);
    }

    /**
     * 1. 检查文件中是否存在章节名
     * 2. 判断文件中使用的章节名类型的正则表达式
     *
     * @return 是否存在章节名
     */
    private boolean checkChapterType(InputStream in, IOUtil.Charset charset) throws IOException {
        //首先获取128k的数据
        byte[] buffer = new byte[BUFFER_SIZE / 4];
        int length = in.read(buffer, 0, buffer.length);
        //进行章节匹配
        for (String str : CHAPTER_PATTERNS) {
            Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(new String(buffer, 0, length, charset.getName()));
            //如果匹配存在，那么就表示当前章节使用这种匹配方式
            if (matcher.find()) {
                chapterPattern = pattern;
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("all")
    public byte[] getChapterContent(InputStream in, ChapterData chapterData) {
        try {
            TxtChapterData txtChapterData = (TxtChapterData) chapterData;
            int length = (int) (txtChapterData.getEndIndex() - txtChapterData.getStartIndex());
            byte[] content = new byte[length];
            in.skip(txtChapterData.getStartIndex());
            in.read(content, 0, length);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(in);
        }
        return null;
    }

    @Override
    public IOUtil.Charset getCharset() {
        return charset;
    }

    public static void main(String[] args) throws Exception {
        TxtPageParser parser = new TxtPageParser();
//        parser.parse(new File("the_great_gatsby.txt"));
        parser.parse(new FileInputStream("the_great_gatsby.txt"), new File("test"));

        List<TxtChapterData> chapters = parser.chapters;
        System.out.println(chapters.toString());

        byte[] bytes = parser.getChapterContent(new FileInputStream("the_great_gatsby.txt"), chapters.get(1));
        System.out.println(new String(bytes));
    }
}
