package databaseInteracting;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface FileAccess {

  void importFile(String fileName) throws IOException, ParserConfigurationException, SAXException;

  void exportFile(String fileName) throws IOException;

}
