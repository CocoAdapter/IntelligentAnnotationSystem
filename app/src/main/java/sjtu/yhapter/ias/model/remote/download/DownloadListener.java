package sjtu.yhapter.ias.model.remote.download;

public interface DownloadListener {
    /**
     * called when the download start
     * @param totalSize total size of the file to be downloaded
     */
    void onStart(long totalSize);

    /**
     * called when the current already downloaded size is updated
     * @param currSize the current downloaded size
     */
    void onProgress(long currSize);

    /**
     * called when the download is failed, which means the onFinish() will never be called
     * @param errorInfo error info
     */
    void onFail(String errorInfo);

    /**
     * called when the download is finished with no error
     */
    void onFinish();
}
