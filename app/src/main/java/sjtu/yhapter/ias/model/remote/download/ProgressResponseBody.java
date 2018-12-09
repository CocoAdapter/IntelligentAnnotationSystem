package sjtu.yhapter.ias.model.remote.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import sjtu.yhapter.ias.service.DownloadService;

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private DownloadListener downloadListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, DownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
        downloadListener.onStart(contentLength());
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        // TODO 如果后台没有加 Content-Length，会返回-1
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null)
            bufferedSource = Okio.buffer(new ProgressSource(responseBody.source()));
        return bufferedSource;
    }

    class ProgressSource extends ForwardingSource {
        long totalBytesRead = 0;

        ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            totalBytesRead += bytesRead != -1 ? bytesRead : 0;
            if (downloadListener != null && bytesRead != -1)
                downloadListener.onProgress(totalBytesRead);
            return bytesRead;
        }
    }
}
