package com.zjzyc.httpRange.Client;

public class HttpDownloadSplitterImpl implements HttpDownloadSplitter {
    private String localFilePath;
    private String downloadFileUrl;
    private int threadNum;
    private int beginIndex;
    private int length;

    /**
     *
     * @param localFilePath     the position that the file need download to
     * @param downloadFileUrl   need to download file's URL
     * @param threadNum         use how many thread to download the online file
     * @param length            the length  that every packet to download
     * @param beginIndex        the begin index in the file that we need to start download
     *      for the @localFilePath and @downloadFileUrl  if in the methods we not supply, the program will use the member
     *  offer by the object. for the @thread and @length ,if we just offered the length or thread ,the program will splitter
     *  the file to download by the single parameter,else if we offer the parameter both ,the wo just start the a few threads that
     *  the number offered by threadNum(ps. the number of threads is meaning to the size of thread pool).
     *
     */
    @Override
    public void download(String localFilePath, String downloadFileUrl, int threadNum, int length, int beginIndex) {

    }
    @Override
    public void download(String localFilePath, String downloadFileUrl, int threadNum, int beginIndex) {

    }

    @Override
    public void download(String localFilePath, String downloadFileUrl) {

    }

    @Override
    public void download(String localFilePath, String downloadFileUrl, int threadNum) {

    }

    @Override
    public void download(int threadNum, int length, int beginIndex) {

    }
}
