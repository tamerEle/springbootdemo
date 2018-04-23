package com.zjpavt.httpRange;


import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.RandomAccess;
import java.util.concurrent.*;

public class Client {
    /*
    http://127.0.0.1:9090/static/download.zip
    */
    public static final String DOWNLOAD_URL = "http://127.0.0.1:9090/download.txt";
    public static final String LOCAL_FILE_PATH = "D:\\down.txt";
    public static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {
        /*RandomAccessFile  oSavedFile = new RandomAccessFile(LOCAL_FILE_PATH,"rw");
        RandomAccessFile  oSavedFile2 = new RandomAccessFile(LOCAL_FILE_PATH,"rw");
        int nPos = 1;
        oSavedFile.seek(nPos);
        oSavedFile2.seek(nPos+10);
        oSavedFile.write("sda".getBytes());
        oSavedFile2.write("fasdfw".getBytes());*/
        Client client = new Client();
        //client.downloadSplitter(3);
        client.append2DownloadFile(LOCAL_FILE_PATH+"0",DOWNLOAD_URL);
    }
    private String downloadUrl;
    private String localFilePath;

    public Client() {
        this.downloadUrl = DOWNLOAD_URL;
        this.localFilePath = LOCAL_FILE_PATH;
    }
    public Client(String downloadUrl,String localFilePath){
        this.localFilePath = localFilePath;
        this.downloadUrl = downloadUrl;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public void downloadSplitter(int threadNum) throws IOException {
        URL uDownloadUrl = new URL(this.downloadUrl);
        URLConnection httpConnection = uDownloadUrl.openConnection();
        int downloadFileSize = httpConnection.getContentLength();
        log.debug(downloadFileSize + "");
        if(downloadFileSize <= 0){
            throw new IOException("file not exist！");
        }
        int packgeLength = downloadFileSize /threadNum;

        ThreadPoolExecutor threadPoolExecutor =
               // new ThreadPoolExecutor(5,1,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(),new DefaultThreadFactory("Sp1dsfa"));
                new ScheduledThreadPoolExecutor(5,new DefaultThreadFactory("downloadSplitter"));
        for(int i=0; i<threadNum - 1;i++){

            int finalI = i;
            threadPoolExecutor.execute(()->
            {
                try {
                    downloadBegin(finalI * packgeLength,packgeLength,this.localFilePath + finalI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            );
        }
        threadPoolExecutor.execute(()->
        {
            try {
                downloadBegin((threadNum -1) * packgeLength,downloadFileSize - (threadNum -1) * packgeLength,this.localFilePath + (threadNum -1) * packgeLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void append2DownloadFile(String localFilePath,String downloadUrl) throws IOException {
        File file = new File(localFilePath);
        if (!file.isFile()) {
            return;
        } else {
            file.createNewFile();
        }
        URL uDownloadUrl = new URL(downloadUrl);
        URLConnection urlConnection = uDownloadUrl.openConnection();
        int downloadFileSize = urlConnection.getContentLength();
        int localFileSize = (int) file.length();
        if (downloadFileSize <= 0) {
            throw new IOException("下载文件为空或者不存在");
        }else if(downloadFileSize < localFileSize){
            throw new IOException("下载文件小于本地文件大小");
        }
        downloadBegin((int) file.length(), (int) (downloadFileSize - file.length()), file.getAbsolutePath());
    }
    public void downloadBegin(int beginIndex,int length,String downloadFileName) throws IOException {
        log.info(beginIndex + " " + length);
        URL uDownloadUrl = new URL(this.downloadUrl);
        URLConnection httpConnection = uDownloadUrl.openConnection();

        httpConnection.setRequestProperty("User-Agent","NetFox");
// 设置断点续传的开始位置
        httpConnection.setRequestProperty("RANGE","bytes=" + beginIndex + "-" + (beginIndex + length));
        log.info(String.valueOf(httpConnection.getContentLength()));
        InputStream inputStream = httpConnection.getInputStream();
        File file = new File(downloadFileName);
        if (!file.isFile()) {
            file.createNewFile();
        } else {
            log.info(file.getName() + " length= " + String.valueOf(file.length()));
        }
        RandomAccessFile  oSavedFile = new RandomAccessFile(downloadFileName,"rw");
        long nPos = beginIndex;
// 定位文件指针到 nPos 位置
        oSavedFile.seek(nPos);
        byte[] b = new byte[1024];
        int nRead;
// 从输入流中读入字节流，然后写到文件中,mRead
        while((nRead=inputStream.read(b,0,1024)) > 0)
        {
            oSavedFile.write(b,0,nRead);
        }
        oSavedFile.close();
    }

    /*URL downloadUrl = new URL(DOWNLOAD_URL);
        URLConnection httpConnection = downloadUrl.openConnection();

        httpConnection.setRequestProperty("User-Agent","NetFox");
// 设置断点续传的开始位置
        httpConnection.setRequestProperty("RANGE","bytes=70-");
        log.info(String.valueOf(httpConnection.getContentLength()));
        InputStream inputStream = httpConnection.getInputStream();
        File file = new File(LOCAL_FILE_PATH);
        if (!file.isFile()) {
            file.createNewFile();
        } else {
            log.info(String.valueOf(file.length()));

            log.info(String.valueOf(file.length()));
        }
        RandomAccessFile  oSavedFile = new RandomAccessFile(LOCAL_FILE_PATH,"rw");
        long nPos = 70;
// 定位文件指针到 nPos 位置
        oSavedFile.seek(nPos);
        byte[] b = new byte[1024];
        int nRead;
// 从输入流中读入字节流，然后写到文件中
        while((nRead=inputStream.read(b,0,1024)) > 0)
        {
            oSavedFile.write(b,0,nRead);
        }*/
}
