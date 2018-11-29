package sjtu.yhapter.reader.loader.parser;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import sjtu.yhapter.reader.model.pojo.ChapterData;
import sjtu.yhapter.reader.util.IOUtil;

/**
 * Created by Yhapter on 2018/11/28.
 */
public interface PageParser {

    void parse(InputStream in, File file) throws Exception;

    void parse(File file) throws Exception;

    List<? extends ChapterData> getChapters();

    byte[] getChapterContent(InputStream in, ChapterData chapter);

    IOUtil.Charset getCharset();
}
