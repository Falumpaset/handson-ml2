package de.immomio.utils;

import de.immomio.common.xml.NameSpaceFilter;
import de.immomio.constants.exceptions.ImmomioTechnicalException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class XmlParser {
    public static <T> T parseXML(File file, JAXBContext jaxbContext, String namespace, Class<T> clazz) throws ImmomioTechnicalException {

        try (InputStream fis = new FileInputStream(file)) {

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            InputSource is = new InputSource(fis);

            NameSpaceFilter filter = new NameSpaceFilter(namespace, true);
            filter.setParent(reader);
            SAXSource source = new SAXSource(filter, is);

            return jaxbUnmarshaller.unmarshal(source, clazz).getValue();
        } catch (Exception e) {
            throw new ImmomioTechnicalException(e);
        }
    }
}
