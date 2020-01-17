package com.zjzyc.httpRange;

public interface HttpDownloadSplitter {
    public void download(String localFilePath, String downloadFileUrl,int threadNum,int length,int beginIndex);
    public void download(String localFilePath, String downloadFileUrl,int threadNum,int beginIndex);
    public void download(String localFilePath, String downloadFileUrl);
    public void download(String localFilePath, String downloadFileUrl,int threadNum);
    public void download(int threadNum,int length,int beginIndex);
}