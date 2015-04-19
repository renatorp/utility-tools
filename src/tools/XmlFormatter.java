package tools;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author renato.ribeiro
 * Pretty-prints xml, supplied as a string.
 */
public class XmlFormatter {

	public static String formatVersion2(String unformattedXml) throws TransformerFactoryConfigurationError, ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {

		final Document document = parseXmlFile(unformattedXml);
		
		/*
		 Based on the DOM specification, whitespaces outside the tags are perfectly valid and they are properly preserved. To remove them, we can use XPath’s normalize-space to locate all the whitespace nodes and remove them first.
		*/
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
	                                                  document,
	                                                  XPathConstants.NODESET);
	 
	    for (int i = 0; i < nodeList.getLength(); ++i) {
	        Node node = nodeList.item(i);
	        node.getParentNode().removeChild(node);
	    }
	 
		
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(document);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;
	}
	
    public static String format(String unformattedXml) throws ParserConfigurationException, SAXException, IOException {
    	final Document document = parseXmlFile(unformattedXml);
    	DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
    	LSSerializer lsSerializer = domImplementation.createLSSerializer();
    	lsSerializer.getDomConfig().setParameter("format-pretty-print",true);
    	return lsSerializer.writeToString(document);   
    }

    private static Document parseXmlFile(String in) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(in));
        return db.parse(is);
    }

    public static void main(String[] args) {
        String unformattedXml ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><receita nome=\"pão\" tempo_de_preparo=\"5 minutos\" tempo_de_cozimento=\"1 hora\"> <titulo>Pão simples</titulo> <ingredientes> <ingrediente quantidade=\"3\" unidade=\"xícaras\">Farinha</ingrediente> <ingrediente quantidade=\"7\" unidade=\"gramas\">Fermento</ingrediente> <ingrediente quantidade=\"1.5\" unidade=\"xícaras\" estado=\"morna\">Água</ingrediente> <ingrediente quantidade=\"1\" unidade=\"colheres de chá\">Sal</ingrediente> </ingredientes> <instrucoes> <passo>Misture todos os ingredientes, e dissolva bem.</passo> <passo>Cubra com um pano e deixe por uma hora em um local morno.</passo> <passo>Misture novamente, coloque numa bandeja e asse num forno.</passo> </instrucoes> </receita>";
    	
/*    	String unformattedXml = "<?xml version=\"1.0\" encoding=\"UTF-16\"?> "+
"<receita nome=\"pão\" tempo_de_cozimento=\"1 hora\" tempo_de_preparo=\"5 minutos\">"+
 "   <titulo>Pão simples</titulo>"+
 "   <ingredientes>"+
 "       <ingrediente quantidade=\"3\" unidade=\"xícaras\">Farinha</ingrediente>"+
"        <ingrediente quantidade=\"7\" unidade=\"gramas\">Fermento</ingrediente>"+
"        <ingrediente estado=\"morna\" quantidade=\"1.5\" unidade=\"xícaras\">Água</ingrediente>"+
"        <ingrediente quantidade=\"1\" unidade=\"colheres de chá\">Sal</ingrediente>"+
"    </ingredientes>"+
"    <instrucoes>"+
"        <passo>Misture todos os ingredientes, e dissolva bem.</passo>"+
"        <passo>Cubra com um pano e deixe por uma hora em um local morno.</passo>"+
"        <passo>Misture novamente, coloque numa bandeja e asse num forno.</passo>"+
"    </instrucoes>"+
"</receita>";*/

        try {
			System.out.println(XmlFormatter.formatVersion2(unformattedXml));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
