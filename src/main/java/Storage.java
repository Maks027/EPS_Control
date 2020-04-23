import lombok.Getter;
import org.w3c.dom.*;
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
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Storage {

//    private DocumentBuilderFactory builderFactory;
//    private DocumentBuilder builder;
//    @Getter
//    private Document document;
//    private Element rootElement;


    public Storage() {


    }

    public Document createDocument(){
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        Element rootElement;

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        document = builder.newDocument();

        rootElement = document.createElement("SystemParameters");
        document.appendChild(rootElement);
        return document;
    }

    public void createElement(Document document, Parameter parameter){

        Element root = document.getDocumentElement();

        Element subParam = document.createElement("parameter");
        root.appendChild(subParam);

        Attr attr = document.createAttribute("id");
        attr.setValue(parameter.getId().name());
        subParam.setAttributeNode(attr);

        Element id = document.createElement("address");
        id.appendChild(document.createTextNode(String.valueOf(parameter.getAddress())));
        subParam.appendChild(id);

        Element adcValue = document.createElement("adcValue");
        adcValue.appendChild(document.createTextNode(String.valueOf(parameter.getAdcValue())));
        subParam.appendChild(adcValue);

        Element doubleValue = document.createElement("doubleValue");
        doubleValue.appendChild(document.createTextNode(String.valueOf(parameter.getDoubleValue())));
        subParam.appendChild(doubleValue);
    }

    public void replaceNode(Document doc, Parameter parameter){
        Element root = doc.getDocumentElement();

        Element newParameterElement = doc.createElement("parameter");
        root.appendChild(newParameterElement);

        Attr attr = doc.createAttribute("id");
        attr.setValue(parameter.getId().name());
        newParameterElement.setAttributeNode(attr);

        Element id = doc.createElement("address");
        id.appendChild(doc.createTextNode(String.valueOf(parameter.getAddress())));
        newParameterElement.appendChild(id);

        Element adcValue = doc.createElement("adcValue");
        adcValue.appendChild(doc.createTextNode(String.valueOf(parameter.getAdcValue())));
        newParameterElement.appendChild(adcValue);

        Element doubleValue = doc.createElement("doubleValue");
        doubleValue.appendChild(doc.createTextNode(String.valueOf(parameter.getDoubleValue())));
        newParameterElement.appendChild(doubleValue);

        Element oldParameterElement = (Element)root.getFirstChild();
        root.replaceChild(newParameterElement, oldParameterElement);
    }

    public void writeToFile(File file, Document document) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(document);

 //       file = new File("F:\\parameters.xml");
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamResult result = new StreamResult(file);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private Document readFile(File file){

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document d = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            d = dBuilder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //       document.getDocumentElement().normalize();
        return d;
    }

    public void ParseFile(File file, Map<P_ID, Parameter> parameterMap){

        Document doc = readFile(file);
        NodeList nodeList = getNodeList(doc);

        for (int i = 0 ; i < nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
 //           System.out.println("Current element: " +  node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)node;
                Parameter tempParameter;
                tempParameter = parameterMap.get(P_ID.valueOf(element.getAttribute("id")));

                tempParameter.setDoubleValue(element.getElementsByTagName("doubleValue").item(0).getTextContent());

                parameterMap.replace(P_ID.valueOf(element.getAttribute("id")), tempParameter);

//                System.out.println("Parameter id: " + element.getAttribute("id"));
//
//                System.out.println("Parameter address: " + element.getElementsByTagName("address").item(0).getTextContent());
//                System.out.println("Parameter double value: " + element.getElementsByTagName("doubleValue").item(0).getTextContent());
//                System.out.println("Parameter ADC value: " + element.getElementsByTagName("adcValue").item(0).getTextContent());
            }
        }
    }

    public void mapToFile(Map<P_ID, Parameter> parameterMap, File file){
        Document doc = createDocument();
        parameterMap.forEach((k, v) -> createElement(doc, v));
        writeToFile(file, doc);
    }

    private NodeList getNodeList(Document doc){
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = null;
//        try {
//            dBuilder = dbFactory.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//        Document document = null;
//        try {
//            document = dBuilder.parse(file);
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        doc.getDocumentElement().normalize();

        //NodeList nodeList = document.getElementsByTagName("parameter");
        return doc.getElementsByTagName("parameter");
    }

    public void parseValueFromFile(File file, P_ID pId) {
        Document doc = readFile(file);
        NodeList nodeList = getNodeList(doc);

        for (int i = 0 ; i < nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)node;

                if (pId.name().equals(element.getAttribute("id"))){
                    System.out.println("Parameter id: " + element.getAttribute("id"));

                    System.out.println("Parameter address: " + element.getElementsByTagName("address").item(0).getTextContent());
                    System.out.println("Parameter double value: " + element.getElementsByTagName("doubleValue").item(0).getTextContent());
                    System.out.println("Parameter ADC value: " + element.getElementsByTagName("adcValue").item(0).getTextContent());

                }
            }
        }
    }

    public void replaceValue(File file, P_ID pId, double newDoubleValue, double newK){

        Document doc = readFile(file);
        NodeList nodeList = getNodeList(doc);

        Parameter p = new Parameter();

        for (int i = 0 ; i < nodeList.getLength() ; i++){
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)node;
                if (pId.name().equals(element.getAttribute("id"))){
                    p.setId(pId);
                    p.setAddress(Integer.valueOf(element.getElementsByTagName("address").item(0).getTextContent()));
                    p.setK(newK);
                    p.setDoubleValue(newDoubleValue);

                    element.getParentNode().removeChild(element);
                }
            }
        }

        createElement(doc, p);
        writeToFile(file, doc);
    }



    public static void main(String[] args) {

        DefaultParameters defaultParameters = new DefaultParameters();

        Storage storage = new Storage();



        File file = new File("F:\\parameters.xml");

        Document doc = storage.createDocument();

//        storage.ParseFile(file);



        storage.parseValueFromFile(file, P_ID.BATTERY_VOLTAGE);
        storage.replaceValue(file, P_ID.BATTERY_VOLTAGE, 500, 1);
        storage.parseValueFromFile(file, P_ID.BATTERY_VOLTAGE);


//        try {
//            storage.ParseFile(file);
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//
//        Map<P_ID, Parameter> parameterMap = defaultParameters.getParameterMap();
//
//        parameterMap.forEach((k, v) -> storage.createElement(doc, v));
//
//        storage.writeToFile(file, doc);
    }


}
