/**
 * @filename XMLParser.java
 * @createtime 2015.7.21
 * @author dingxiangyong
 * @comment To parse xml file.
 */
package org.smvc.framework.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.smvc.framework.mvc.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Big Martin
 *
 */
public class XMLParser {

    /**
     * Parse xml file to user object directly.But xml file should follow this
     * rule: if object name is Obj, xml structure should be
     * <Objs> <Obj> <field1><field1>..<Obj><Objs>
     * 
     * @param filePath xmlfile path
     * @param clazz xml mapped clazz
     * @return object
     */
    public <T> T parseXml2Object(String filePath, Class<T> clazz) {
        return parseXml2Object(filePath, clazz, new HashMap<String, String>());
    }

    /**
     * Parse xml file to user object directly.But xml file should follow this
     * rule: if object name is Obj, xml structure should be
     * <Objs> <Obj> <field1><field1>..<Obj><Objs>
     * 
     * @param filePath xmlfile path
     * @param clazz xml mapped clazz
     * @param nodeName2FieldMap xml node name to class field name map
     * @return object
     */
    public static <T> T parseXml2Object(String filePath, Class<T> clazz, Map<String, String> nodeName2FieldMap) {
        T result = null;

        try {
            // get physic file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(filePath);

            String className = clazz.getSimpleName();

            // 1.get document root element
            Element root = document.getDocumentElement();

            // check root name
            if (!(className).equals(root.getNodeName()) && !nodeName2FieldMap.containsKey(root.getNodeName())) {
                return result;
            }

            // 2.get next level eles of root
            if (null != root) {
                // instance object
                result = clazz.newInstance();

                for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        // get nodename set method
                        String nodeName = node.getNodeName();
                        String nodeValue = node.getFirstChild().getNodeValue();

                        // if nodename and nodevalue are not empty
                        if (!StringUtil.isEmpty(nodeName) && !StringUtil.isEmpty(nodeValue)) {
                            if (nodeName2FieldMap.containsKey(nodeName)) {
                                nodeName = nodeName2FieldMap.get(nodeName);
                            }
                            String setMethodName = "set" + StringUtil.upperFirst(nodeName);
                            Method setMethod = clazz.getDeclaredMethod(setMethodName, String.class);
                            setMethod.setAccessible(true);

                            setMethod.invoke(result, nodeValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;

    }

    public static void main(String[] args) {
        XMLParser dom = new XMLParser();
        Map<String, String> map = new HashMap<String, String>();
        map.put("smvc-config", "ApplicationContext");
        map.put("controller-package", "scanPackage");
        // dom.createXml("F:\\employees.xml");
        ApplicationContext context = dom.parseXml2Object(
                "/Users/dingxiangyong/Documents/j2ee_workspace/Smvc/WebContent/WEB-INF/smvc.xml",
                ApplicationContext.class, map);
        System.out.println(context);
    }
}
