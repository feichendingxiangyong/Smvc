/**
 * @filename XMLParser.java
 * @createtime 2015.7.21
 * @author dingxiangyong
 * @comment To parse xml file.
 */
package com.smvc.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.util.XMLDocumentFilterImpl;

/**
 * @author Big Martin
 *
 */
public class XMLParser {

    /**
     * Parse xml file to user object directly.But xml file should follow this rule:
     * if object name is Obj, xml structure should be <Objs> <Obj> <field1><field1>..<Obj><Objs>
     * @param fileName
     * @param clazz
     * @return
     */
    public <T> List<T> parseXml2Object(String fileName, Class<T> clazz, Map<String, String> nodeName2FieldMap) {
     List<T> resultList = new ArrayList<>();
     
     try {
      //get physic file
      DocumentBuilderFactory factory = DocumentBuilderFactory
        .newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(fileName);
      
      String className = clazz.getName();
      
      // 1.get document root element
      Element root = document.getDocumentElement();
      
      //check root name
      if (!(className + "s").equals(root.getNodeName()))
      {
          return resultList;
      }
      
      // 2.get next level eles of root
      NodeList nodeList = root.getChildNodes();
      
      //System.out.println(root.getNodeName());
      if (null != root) {
       for (int i = 0; i < nodeList.getLength(); i++) {
        Node child = nodeList.item(i);

        //System.out.println(child.getNodeName());
        if (!className.equals(root.getNodeName()))
        {
            return resultList;
        }
        
        //instance object
        T obj = clazz.newInstance();
        //set field value
        if (child.getNodeType() == Node.ELEMENT_NODE) {
         System.out.println(child.getAttributes().getNamedItem(
           "email").getNodeValue());
        }

        for (Node node = child.getFirstChild(); node != null; node = node
          .getNextSibling()) {

         if (node.getNodeType() == Node.ELEMENT_NODE) {
          // 输出node的属性;
          System.out.println(node.getNodeName());
          
          //get nodename set method
          String nodeName = node.getNodeName();
          String nodeValue = node.getFirstChild().getNodeValue();
          
          //if nodename and nodevalue are not empty
          if (!StringUtil.isEmpty(nodeName)
                  && !StringUtil.isEmpty(nodeValue))
          {
              String setMethodName = "set" + StringUtil.upperFirst(nodeName);
              Method setMethod = clazz.getDeclaredMethod(setMethodName, String.class);
              setMethod.setAccessible(true);
              
              setMethod.invoke(obj, nodeValue);
          }
         }
        }
        
        resultList.add(obj);
       }
      }

     } catch (ParserConfigurationException e) {
      e.printStackTrace();
     } catch (IOException e) {
      e.printStackTrace();
     } catch (SAXException e) {
      e.printStackTrace();
     } catch (Exception e) {
         e.printStackTrace();
     }
     
     return resultList;

    }

    public static void main(String[] args) {
      XMLParser dom = new XMLParser();
      //dom.createXml("F:\\employees.xml");
      //dom.parseXml("src/com/smvc/util/test.xml");
    }
}
