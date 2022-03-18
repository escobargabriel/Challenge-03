package databaseInteracting;

import generated.AddProductRequest;
import generated.AddProductResponse;
import generated.ListProductRequest;
import generated.ListProductResponse;
import generated.productGrpc;
import io.grpc.ManagedChannel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileAccessXmlFile implements FileAccess {
  productGrpc.productBlockingStub prodStub;
  ManagedChannel channel;

  /**
   * Constructor method of class
   * @param channel channel to connect with server
   */
  public FileAccessXmlFile(ManagedChannel channel) {
    this.channel = channel;
    prodStub = productGrpc.newBlockingStub(channel);
  }

  /**
   * Method to import data from a XML file to database.
   * @param fileName String - name of XML file with the data.
   * @throws IOException exception throw when I/O problems occurs.
   * @throws ParserConfigurationException exception throw when the parser configuration problems occurs.
   * @throws SAXException exception throw when SAX problems occurs.
   */
  @Override
  public void importFile(String fileName) throws IOException, ParserConfigurationException, SAXException {
    System.out.println("Reading data from the XML file and store on the database.");
    File file = new File(fileName);
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    org.w3c.dom.Document document = documentBuilder.parse(file);
    document.getDocumentElement().normalize();
    System.out.println("Root Element: " + document.getDocumentElement().getNodeName());
    NodeList nodeList = document.getElementsByTagName("product");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      System.out.println("Node name " + node.getNodeName());
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
        String name = eElement.getElementsByTagName("name").item(0).getTextContent();
        int stock = Integer.parseInt(eElement.getElementsByTagName("stock").item(0).getTextContent());
        float price = Float.parseFloat(eElement.getElementsByTagName("price").item(0).getTextContent());
        AddProductRequest request =
            AddProductRequest.newBuilder().setName(name).setStock(stock).setPrice(price).build();
        AddProductResponse response = prodStub.addProduct(request);
        System.out.println(response.getName());
      }
    }
  }

  /**
   * Method to export data from the database to an XML file.
   * @param fileName String - name of file with the exported data.
   * @throws IOException exception throw when I/O problems occurs.
   */
  @Override
  public void exportFile(String fileName) throws IOException {
    try {
      Element root = new Element("Products");
      ListProductRequest request = ListProductRequest.newBuilder().build();
      Iterator<ListProductResponse> listProductResponseIterator = prodStub.listProduct(request);
      while (listProductResponseIterator.hasNext()) {
        Element productElement = new Element("product");
        Element idElement = new Element("id");
        Element nameElement = new Element("name");
        Element stockElement = new Element("stock");
        Element priceElement = new Element("price");
        ListProductResponse next = listProductResponseIterator.next();
        idElement.appendChild(String.valueOf(next.getId()));
        nameElement.appendChild(next.getName());
        stockElement.appendChild(String.valueOf(next.getStock()));
        priceElement.appendChild(String.valueOf(next.getPrice()));
        productElement.appendChild(idElement);
        productElement.appendChild(nameElement);
        productElement.appendChild(stockElement);
        productElement.appendChild(priceElement);
        root.appendChild(productElement);
      }
      Document document = new Document(root);
      File file = new File(fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      Serializer serializer = new Serializer(fileOutputStream, "UTF-8");
      serializer.setIndent(4);
      serializer.write(document);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
