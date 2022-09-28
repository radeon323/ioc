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
        Node rootNode = doc.getFirstChild();
        NodeList beans = rootNode.getOwnerDocument().getElementsByTagName("bean");

        for (int i = 0; i < beans.getLength(); i++) {
            beanDefinitions.add(parseBean(beans, i));
        }
        return beanDefinitions;
    }

    private static BeanDefinition parseBean(NodeList beans, int i) {
        BeanDefinition beanDefinition = new BeanDefinition();

        if (beans.item(i).getNodeType() == Node.ELEMENT_NODE) {
            Element beanElement = (Element) beans.item(i);
            beanDefinition.setId(beanElement.getAttribute("id"));
            beanDefinition.setBeanClassName(beanElement.getAttribute("class"));

            NodeList properties = beans.item(i).getChildNodes();

            parseProperties(properties, beanDefinition);
        }

        return beanDefinition;
    }


    private static void parseProperties(NodeList properties, BeanDefinition beanDefinition) {
        Map<String, String> valueDependencies = new HashMap<>();
        Map<String, String> refDependencies = new HashMap<>();

        for (int i = 0; i < properties.getLength(); i++) {

            if (properties.item(i).getNodeType() != Node.ELEMENT_NODE || !properties.item(i).getNodeName().equals("property")) {
                continue;
            }

            Element propertyElement = (Element) properties.item(i);

            Node nameNode = propertyElement.getAttributes().getNamedItem("name");
            Node valueNode = propertyElement.getAttributes().getNamedItem("value");
            Node refNode = propertyElement.getAttributes().getNamedItem("ref");

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
