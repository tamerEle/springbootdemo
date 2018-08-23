package com.zjpavt.util;

/**
 * @program: PavtUpdate
 * @description:
 * @author: zyc
 * @create: 2018-06-19 15:26
 **/

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class XmlUtil {
    public static final String UPDATE_TAG = "update";
    public static final String VERSION_TAG = "version";
    public static final String SH_TAG = "sh";
    public static final String UPDATETIME_TAG = "updatetime";
    public static final String URL_TAG = "url";
    public static final String NAME_TAG = "name";
    public static final String UPDATEINFO_TAG = "updateinfo";
    public static final String SIGN_TAG = "sign";

    /**
     * 将字符串转化为document
     * @param xmlString
     * @return
     */
    public static Document parse(String xmlString) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.debug(xmlString);
            e.printStackTrace();
        }
        try {
            document = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
        } catch (SAXException|IOException e) {
            log.warn("XML REQUEST RETURN STRING: " + xmlString);
            return null;
        }
        return document;
    }


    public static Document parse(File xmlString) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.debug(xmlString.getPath());
            e.printStackTrace();
        }
        try {
            document = builder.parse(xmlString);
        } catch (SAXException|IOException e) {
            log.warn("XML REQUEST RETURN STRING: " + xmlString);
            return null;
        }
        return document;
    }
    public static String getContentTextFormDocument(Document document, String tagName) {
        NodeList tagList = document.getElementsByTagName(tagName);
        if (tagList.getLength() > 1) {
            log.error(tagName+" is not only tag in document:" +tagList.getLength());
        }
        if (tagList.getLength() == 0) {
            log.error(tagName+" without the tag name in document:" +tagList.getLength());
            return null;
        }
        return document.getElementsByTagName(tagName).item(0).getTextContent();
    }
    /**
     * get text as TextNode in the node
     * @param node
     * @return
     */
    public static String getNodeValue(Node node){
        Node nodeText = node.getFirstChild();
        if(nodeText != null){
            if(nodeText.getNodeType() == 3 || nodeText.getNodeType() == 4){
                if(nodeText.getNodeValue().equals("")){
                    return null;
                }
                return nodeText.getNodeValue();
            }
        }
        return null;
    }

    /**
     * get Node with the nodeName is name in parNode
     * @param parNode
     * @param name
     * @return
     */
    public static ArrayList<Node> getNodeByNameChilds(Node parNode, String name){
        ArrayList<Node> list = new ArrayList<Node>();
        NodeList children = parNode.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node child = children.item(i);
            if(child.getNodeName().equals(name)){
                list.add(child);
            }
        }
        return list;
    }

    /**
     * 遍历Nodelist的text 以“，”分隔
     * @param list
     * @return
     */
    public static String listNodeToString(ArrayList<Node> list){
        StringBuilder result = new StringBuilder();
        for(Node node:list){
            result.append(node.getTextContent());
            result.append(",");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    /**
     * output child's NodeName and Text in list
     * @param list
     */
    public static void outNodeListValue(NodeList list){
        for(int i=0;i<list.getLength();i++){
            Node child = list.item(i);
            if(XmlUtil.getNodeValue(child) != null){
                System.out.println(child.getNodeName()+ "'" + XmlUtil.getNodeValue(child)+"'");
            }
        }
    }
    /**
     * 	获得parNode 中第一个标签为name 的Node
     * @param parNode
     * @param name
     * @return
     */
    public static Node getNodeByNameChild(Node parNode,String name){
        if (parNode == null) {
            return null;
        }
        NodeList childs = parNode.getChildNodes();
        for(int i=0;i<childs.getLength();i++){
            Node child = childs.item(i);
            if(child.getNodeName().equals(name)){
                return child;
            }
        }
        return null;
    }
    public static String getNodeAttribute (Node node, String attrName) {
        Element element = (Element) node;
        return element.getAttribute(attrName);
    }

    public static String getNodeTextContent (Node node) {
        if(node == null) return null;
        return node.getTextContent();
    }

    /**
     * 保存xml到文件
     * @param doc
     * @param file
     */
    public static void save(Document doc,File file) {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
