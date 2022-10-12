package com.olshevchenko.ioc.reader.xmlreader;

import com.olshevchenko.ioc.entity.BeanDefinition;
import com.olshevchenko.ioc.exception.SourceParseException;
import com.olshevchenko.ioc.reader.BeanDefinitionReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class XMLDomBeanDefinitionReader implements BeanDefinitionReader {

    private static final String BEANS = "beans";
    private static final String BEAN = "bean";
    private static final String ID = "id";
    private static final String CLASS = "class";
    private static final String PROPERTY = "property";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String REF = "ref";

    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private final File contextFile;

    public XMLDomBeanDefinitionReader(String path) {
        contextFile = new File(path);
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = dbf.newDocumentBuilder().parse(contextFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new SourceParseException("Error parsing XML", e);
        }
        Node rootNode = doc.getFirstChild().getOwnerDocument().getElementsByTagName(BEANS).item(0);
        NodeList beans = rootNode.getOwnerDocument().getElementsByTagName(BEAN);

        for (int i = 0; i < beans.getLength(); i++) {
            beanDefinitions.add(parseBean(beans, i));
        }
        return beanDefinitions;
    }

    private static BeanDefinition parseBean(NodeList beans, int i) {
        BeanDefinition beanDefinition = new BeanDefinition();

        if (beans.item(i).getNodeType() == Node.ELEMENT_NODE) {
            Element beanElement = (Element) beans.item(i);
            beanDefinition.setId(beanElement.getAttribute(ID));
            beanDefinition.setBeanClassName(beanElement.getAttribute(CLASS));

            NodeList properties = beans.item(i).getChildNodes();

            parseProperties(properties, beanDefinition);
        }

        return beanDefinition;
    }


    private static void parseProperties(NodeList properties, BeanDefinition beanDefinition) {
        Map<String, String> valueDependencies = new HashMap<>();
        Map<String, String> refDependencies = new HashMap<>();

        for (int i = 0; i < properties.getLength(); i++) {

            if (properties.item(i).getNodeType() != Node.ELEMENT_NODE || !properties.item(i).getNodeName().equals(PROPERTY)) {
                continue;
            }

            Element propertyElement = (Element) properties.item(i);

            Node nameNode = propertyElement.getAttributes().getNamedItem(NAME);
            Node valueNode = propertyElement.getAttributes().getNamedItem(VALUE);
            Node refNode = propertyElement.getAttributes().getNamedItem(REF);

            if (nameNode != null && valueNode != null) {
                String name = nameNode.toString().substring(0, 4);
                String value = valueNode.toString().substring(0, 5);
                valueDependencies.put(propertyElement.getAttribute(name),propertyElement.getAttribute(value));
            }

            if (nameNode != null && refNode != null) {
                String name = nameNode.toString().substring(0, 4);
                String ref = refNode.toString().substring(0, 3);
                refDependencies.put(propertyElement.getAttribute(name),propertyElement.getAttribute(ref));
            }

            beanDefinition.setDependencies(valueDependencies);
            beanDefinition.setRefDependencies(refDependencies);
        }
    }


}
