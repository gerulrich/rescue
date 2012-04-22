package net.cloudengine.xml;

import java.io.File;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.cloudengine.model.config.AppProperty;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {

	
	public Collection<AppProperty> getAllFromFile(File file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("property");
			
			for (int s = 0; s < nodeLst.getLength(); s++) {
				
				Node fstNode = nodeLst.item(s);
				
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("name");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					
					System.out.println("First Name : " + ((Node) fstNm.item(0)).getNodeValue());
					
					NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("value");
					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
					NodeList lstNm = lstNmElmnt.getChildNodes();
					System.out.println("Last Name : "+ ((Node) lstNm.item(0)).getNodeValue());
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static void main(String argv[]) {

		try {
			File file = new File("c:\\MyXMLFile.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("employee");
			System.out.println("Information of all employees");

			for (int s = 0; s < nodeLst.getLength(); s++) {

				Node fstNode = nodeLst.item(s);

				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element fstElmnt = (Element) fstNode;
					NodeList fstNmElmntLst = fstElmnt
							.getElementsByTagName("firstname");
					Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
					NodeList fstNm = fstNmElmnt.getChildNodes();
					System.out.println("First Name : "
							+ ((Node) fstNm.item(0)).getNodeValue());
					NodeList lstNmElmntLst = fstElmnt
							.getElementsByTagName("lastname");
					Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
					NodeList lstNm = lstNmElmnt.getChildNodes();
					System.out.println("Last Name : "
							+ ((Node) lstNm.item(0)).getNodeValue());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}