import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Storage {

    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder builder;
    private Document document;
    private Element rootElement;


    public Storage() throws ParserConfigurationException {
        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
        document = builder.newDocument();

        rootElement = document.createElement("SystemParameters");
        document.appendChild(rootElement);


    }

    public void createElement(Parameter parameter){

        Element subParam = document.createElement("parameter");
        rootElement.appendChild(subParam);

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

    public void writeToFile() throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        File file = new File("F:\\parameters.xml");
        file.getParentFile().mkdirs();

        file.createNewFile();

        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public void ParseFile(File file) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        document.getDocumentElement().normalize();
        System.out.println("Root element: " + document.getDocumentElement().getNodeName());

        NodeList nodeList = document.getElementsByTagName("parameter");

        for (int i = 0 ; i < nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
            System.out.println("Current element: " +  node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)node;
                System.out.println("Parameter id: " + element.getAttribute("id"));

                System.out.println("Parameter address: " + element.getElementsByTagName("address").item(0).getTextContent());
                System.out.println("Parameter double value: " + element.getElementsByTagName("doubleValue").item(0).getTextContent());
                System.out.println("Parameter ADC value: " + element.getElementsByTagName("adcValue").item(0).getTextContent());


            }
        }
    }



    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException {

        DefaultParameters defaultParameters = new DefaultParameters();


        Storage storage = new Storage();

        File file = new File("F:\\parameters.xml");

        try {
            storage.ParseFile(file);
        } catch (SAXException e) {
            e.printStackTrace();
        }


//        for(Parameter p: defaultParameters.getParameters()){
//            storage.createElement(p);
//        }
//
//        storage.writeToFile();
    }


}
