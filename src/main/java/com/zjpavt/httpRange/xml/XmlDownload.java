package com.zjpavt.httpRange.xml;

import com.zjpavt.util.ConfigUtil;
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
@RequestMapping(path = "/firmware")
public class XmlDownload {

    private static HashMap<String, String> name2FileMapper = new HashMap<>(4);

    static{
        name2FileMapper.put("PavtUpdate","C:\\idea\\git\\springbootdemo\\src\\main\\resources\\Update.xml");
    }

    @RequestMapping(path = "/{xmlName}.xml")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "xmlName") String xmlName) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        //response.getOutputStream 不存在返回.
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            log.info("can't open outputStream!");
            return;
        }

        //file不存在直接返回
        String fileName = name2FileMapper.get(xmlName);
        if (fileName == null) {
            os.write("the file not exist".getBytes());
            return;
        }
        File xmlFile = new File(fileName);
        if (!xmlFile.isFile()) {
            os.write("without this file".getBytes());
            return;
        }
        //inputStream 不存在直接返回
        try {
            is = new BufferedInputStream(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            log.error("can't open the file inputStream");
            try {
                os.write("can't open the file inputStream".getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        int dataLength;
        byte[] datas = new byte[1024];
        Assert.notNull(is,"request.getInputStream is not null!");

        response.setCharacterEncoding(ConfigUtil.DEFAULT_CHARSET);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + xmlName +".xml");
        while ((dataLength = is.read(datas)) != -1) {
            os.write(datas, 0, dataLength);
        }
    }
}
