package de.immomio.importer.converter.openimmo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.immomio.common.xml.NoNamesWriter;
import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Openimmo;
import de.immomio.openimmo.constants.OpenImmoConstants;
import de.immomio.utils.XmlParser;
import de.immomio.utils.compare.CompareBean;
import de.immomio.utils.compare.DiffUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static Property loadProperty(Object object, String resourceName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper.readValue(new File(object.getClass().getResource(resourceName).getFile()), Property.class);
    }

    public static Immobilie loadImmobilie(Object object, String resourceName) throws FileNotFoundException,
            JAXBException, SAXException, ImmomioTechnicalException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Openimmo.class);
        File file = new File(object.getClass().getResource(resourceName).getFile());
        return XmlParser.parseXML(file, jaxbContext, OpenImmoConstants.OPENIMMO_NAMESPACE, Openimmo.class)
                .getAnbieter()
                .get(0)
                .getImmobilie()
                .get(0);
    }

    public static String getXML(Immobilie immobilie) throws JAXBException, XMLStreamException {
        StringWriter stringOut = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(Openimmo.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(immobilie, NoNamesWriter.filter(stringOut));

        return stringOut.toString();
    }

    public static void assertDiffs(Object object1, Object object2) {
        List<CompareBean> differences = DiffUtils.getDifferences(object1, object2);
        differences.forEach(compareBean -> {
            assertEquals(compareBean.getValue(), compareBean.getPrevValue(), compareBean.getFieldName());
        });

        assertEquals(0, differences.size());
    }
}
