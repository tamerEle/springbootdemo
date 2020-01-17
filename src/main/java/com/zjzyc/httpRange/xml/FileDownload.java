package com.zjzyc.httpRange.xml;

import com.zjzyc.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(path = "/firmware/download")
public class FileDownload {

    private static HashMap<String, String> name2FileMapper = new HashMap<>(4);

    static{
        name2FileMapper.put("PavtUpdate","C:\\idea\\svn\\PavtUpdate\\target\\PavtUpdate-2.5.jar");
        name2FileMapper.put("PavtFirmware","C:\\idea\\svn\\PavtFirmware\\target\\PavtFirmware-2.5.jar");
        name2FileMapper.put("PavtBootUpdate","C:\\Users\\yann\\Desktop\\jdk-8u161-windows-x64.exe");
    }

    private static final String RANGE = "Range";

    /**
     * 下载文件类
     * @param request
     * @param response
     * @param mapperName
     * @throws IOException
     */
    @RequestMapping(path = "/{fileName}")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "fileName") String mapperName) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        String fileName;
        File sourceFile;
        RandomAccessFile randomAccessFile = null;
        try {
            //response.getOutputStream 不存在返回.
            try {
                os = response.getOutputStream();
            } catch (IOException e) {
                log.warn("can't open outputStream!");
                return;
            }
            //file不存在直接返回
            fileName = name2FileMapper.get(mapperName);
            if (fileName == null) {
                log.warn("the file not exist " + mapperName);
                return;
            }
            sourceFile = new File(fileName);
            if (!sourceFile.isFile()) {
                log.warn("without this file" + fileName);
                return;
            }
            long downloadFileFullSize = sourceFile.length();
            int dataLength;
            byte[] byteBuf = new byte[1024];
            response.setCharacterEncoding(ConfigUtil.DEFAULT_CHARSET);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + mapperName);
            String range = request.getHeader(RANGE);
            //使用断点续传
            if (range != null) {
                randomAccessFile = new RandomAccessFile(fileName, "r");
                long sourceFileSize = randomAccessFile.length();
                BeginEndIndex beginEndIndex = getPartFileSize(range, sourceFileSize);
                downloadFileFullSize = beginEndIndex.getDownloadSize();
                Assert.notNull(beginEndIndex, "don't match range format");
                //是否下载整个文件   不许显示调用
                String contentRange = beginEndIndex.getContentRange();
                response.addHeader("Content-Length", String.valueOf(downloadFileFullSize));
                response.addHeader("Content-Range", contentRange);
                response.setStatus(206);
                randomAccessFile.seek(beginEndIndex.getBegin());
                //已发送的长度 sended  需要发送的长度 downloadSize
                long countedLength = 0;
                //当文件已经读取完毕 或者 已经达到需要返回的大小
                while ((dataLength = randomAccessFile.read(byteBuf)) != -1 && countedLength < downloadFileFullSize) {
                    countedLength += dataLength;
                    if (countedLength > downloadFileFullSize) {
                        int finalLength = (int) (dataLength - (countedLength - downloadFileFullSize));
                        os.write(byteBuf, 0, finalLength);
                    } else {
                        os.write(byteBuf,0,dataLength);
                    }
                }
                return;
                //不使用断点续传
            } else {
                response.addHeader("Content-Length",String.valueOf(downloadFileFullSize));
                response.setStatus(200);
                is = new BufferedInputStream(new FileInputStream(fileName));
                while ((dataLength = is.read(byteBuf)) != -1) {
                    os.write(byteBuf,0,dataLength);
                }
                return;
            }
        }finally {
            closeStream(is);
            closeStream(os);
            closeStream(randomAccessFile);
        }
    }

    private void closeStream(Closeable is) throws IOException {
        if (is != null) {
            is.close();
        }
    }

    /**
     * 从header 的range 中获取断点的信息
     * @param range     range字符串
     * @param fileSize  源文件大小
     * @return      断点的开始 结束信息
     */
    private  BeginEndIndex getPartFileSize(String range,long fileSize) {

        String temp=range.substring(range.lastIndexOf("=")+1);
        String []args=temp.split("-");
        if(args.length==1){
            long start = Long.parseLong(args[0]);
            return new BeginEndIndex(start,fileSize-1,fileSize);
        }
        if(args.length==2){
            long start=Long.parseLong(args[0]);
            long end=Long.parseLong(args[1]);
            if (end >= fileSize) {
                log.warn("download size bigger than the source size!!");
            }
            return new BeginEndIndex(start,Math.min(end,fileSize),fileSize);
        }
        return null;
    }

    /**
     * 断点信息类
     */
    class BeginEndIndex {
        public BeginEndIndex(long begin, long end, long size) {
            this.begin = begin;
            this.end = end;
            this.size = size;
        }

        BeginEndIndex(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }
        private long begin;
        private long end;
        private long size;

        public long getDownloadSize() {
            return end - begin + 1;
        }
        public long getBegin() {
            return begin;
        }

        public void setBegin(long begin) {
            this.begin = begin;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public String getContentRange() {
            return "bytes " + begin + "-" + end + "/" + size;
        }
    }

}
