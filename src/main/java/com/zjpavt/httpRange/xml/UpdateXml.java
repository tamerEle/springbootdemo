package com.zjpavt.httpRange.xml;

import com.zjpavt.util.ConfigUtil;
import com.zjpavt.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import util.FileUtil;
import util.RSASignature;

import java.io.File;
import java.util.HashMap;

@Slf4j
@Controller
public class UpdateXml {

    private static HashMap<String, XmlFileMapper> name2FileMapper = new HashMap<>();
    static{
        XmlFileMapper xmlFileMapper =
                new XmlFileMapper("C:\\idea\\git\\springbootdemo\\src\\main\\resources\\PavtUpdate.xml",
                        "C:\\idea\\svn\\PavtUpdate\\target\\PavtUpdate-2.5.jar");
        name2FileMapper.put("PavtUpdate",xmlFileMapper);
        xmlFileMapper =
                new XmlFileMapper("C:\\idea\\git\\springbootdemo\\src\\main\\resources\\PavtFirmware.xml",
                        "C:\\idea\\svn\\PavtFirmware\\target\\PavtFirmware-2.5.jar");
        name2FileMapper.put("PavtFirmware",xmlFileMapper);
        xmlFileMapper =
                new XmlFileMapper("C:\\idea\\git\\springbootdemo\\src\\main\\resources\\PavtBootUpdate.xml",
                        "C:\\Users\\yann\\Desktop\\jdk-8u161-windows-x64.exe");
        name2FileMapper.put("PavtBootUpdate",xmlFileMapper);
    }

    public static void main(String[] args) {
        new UpdateXml().update();
    }

    public void update(){
        File xmlFile = null;
        File downloadFile = null;
        for (XmlFileMapper xmlFileMapper : name2FileMapper.values()) {
            xmlFile = new File(xmlFileMapper.getXmlPath());
            //Assert.isTrue(!xmlFile.exists(),xmlFile.getName()+"not exist!");
            if (!xmlFile.exists()) {
                log.warn(xmlFile.getName() + "not exist");
                continue;
            }
            downloadFile = new File(xmlFileMapper.getFilePath());
            Document document = XmlUtil.parse(xmlFile);
            Element updateDom = document.getDocumentElement();
            Element signEle = (Element) updateDom.getElementsByTagName("sign").item(0);
            log.info("历史签名: " + signEle.getTextContent());
            /*(XmlDocument)*/
            String str = sign(downloadFile.getPath(), ConfigUtil.PRIVATE_KEY,"");
            //log.warn("签名字符串" + str);
            signEle.setTextContent(str);
            XmlUtil.save(document,new File(xmlFileMapper.getXmlPath()));
        }
    }

    private static String sign(String filePath, String keyPath, String savepath) {
        String signstr = "";
        try {
            long start = System.currentTimeMillis();
            System.out.println("---------------私钥签名过程------------------");
            signstr = RSASignature.sign(FileUtil.fileToByte(filePath),keyPath /*RSAEncrypt.loadKeyByFile(keyPath)*/);
            /*if (!savepath.endsWith("/")) {
                savepath = savepath + "/";
            }
            savepath = savepath + "sign.txt";
            FileUtil.write(signstr != null ? signstr.getBytes() : new byte[0], new File(savepath));*/
            System.out.println("签名原串：" + filePath);
            System.out.println("签名串：" + signstr);
            System.out.println("签名串已保存到：" + savepath);
            System.out.println("---------------------------------------------");
            long end = System.currentTimeMillis();
            System.out.println("程序运行时间： " + (end - start) / 1000L + "s");
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return signstr;
    }
    static class XmlFileMapper {
        private String xmlPath;
        private String filePath;

        public XmlFileMapper(String xmlPath, String filePath) {
            this.xmlPath = xmlPath;
            this.filePath = filePath;
        }

        public String getXmlPath() {
            return xmlPath;
        }

        public void setXmlPath(String xmlPath) {
            this.xmlPath = xmlPath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
