package com.zjpavt.httprange;



import com.zjpavt.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @program: springbootdemo
 * @description:
 * @author: zyc
 * @create: 2018-04-19 17:23
 **/
@Slf4j
@RestController
public class HttpDownloadServer  {

    private  RandomAccessFile accessFile;
    private ServletOutputStream outputStream;
    private long position;
    private  long start,end;
    //远程文件
    private File file=new File("d:\\a.txt");
    /**
     * 下载文件
     * */
    @RequestMapping("/downloadFile")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        outputStream=response.getOutputStream();
        try {
            accessFile=new RandomAccessFile(file,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding(ConfigUtil.DEFAULT_CHARSET);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
        log.info("开始下载文件");
        log.info("文件名："+file.getName());
        if(request.getHeader("Range")!=null){
            getPartFileSize(request.getHeader("Range"));
            //文件长度为0返回
            if(accessFile.length()-start<=0 ){
                log.error("文件长度不符合原文件");
                closeStream(accessFile,outputStream);
                return;
            }
            if(end==0){
                response.addHeader("Content-Length",""+(accessFile.length()-start));
            }
            else {
                response.addHeader("Content-Length",""+(end-start+1));
            }
            log.info("range:"+request.getHeader("Range"));
            response.setHeader("Content-Range",request.getHeader("Range")+" "+file.length()+"/"+file.length());
            resumeDownload();
            closeStream(accessFile,outputStream);
            return;
        }

        //本地不存在下载文件
        try {
            operateFile(accessFile,outputStream);
            response.setHeader("Content-Length",""+file.length());
            closeStream(accessFile,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //从rang中获取数据
    private  void getPartFileSize(String range) {
        String temp=range.substring(range.lastIndexOf("=")+1);
        String []args=temp.split("-");
        if(args.length==1){
            start=Long.parseLong(args[0]);
        }
        if(args.length==2){
            start=Long.parseLong(args[0]);
            end=Long.parseLong(args[1]);
        }
    }
    //单线程读写文件
    private void operateFile(RandomAccessFile in,OutputStream out) throws IOException {
        //byte [] buf=new byte[(int) file.length()];
        byte [] buf=new byte[2];
        log.info("read data");
        try{
            while(in.read(buf)>0) {
                out.write(buf);
                position=in.getFilePointer();
                if(position==32){
                    throw new DefineException();
                }
            }
        }catch (DefineException e){
            // not do something
            log.error("throw  DefineException");
            closeStream(accessFile,outputStream);
        }
    }

    //关闭流
    private  void closeStream(RandomAccessFile in,OutputStream out) throws IOException {
        in.close();out.close();
        log.info(" close stream");
    }
    //断点续传
    private void resumeDownload() throws IOException {
        byte[] bytes=new byte[2];
        log.info("start :"+start+"  end: "+end);
        accessFile.seek(start);
        log.info("accessfile pointer "+accessFile.getFilePointer());
        operateFile(accessFile,outputStream);
    }
    class DefineException extends Exception{
        public DefineException() {
        }
    }

}

