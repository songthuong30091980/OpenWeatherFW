package utilities.datadriven;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	
	private XMLReader() {}
	
	public static String getXMLData(String xmlDataFilePath, String dataType, String tagName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(xmlDataFilePath));
			Element rootTag = document.getDocumentElement();

			Element dataTypeTag = getChild(rootTag, dataType);

			return getValueByTagName(dataTypeTag, tagName);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Element getXMLRootTagData(String xmlDataFilePath) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(xmlDataFilePath));
			return document.getDocumentElement();
		} catch (Exception ex) {
			return null;
		}
	}

	public static Element getChild(Element parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName())) {
				return (Element) child;
			}
		}
		return null;
	}

	public static String getValueByTagName(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}
		return null;
	}

}
