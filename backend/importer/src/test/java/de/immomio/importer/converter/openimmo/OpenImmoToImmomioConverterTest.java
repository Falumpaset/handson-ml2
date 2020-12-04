package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.openimmo.BueroPraxen;
import de.immomio.openimmo.Haus;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Objektkategorie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class OpenImmoToImmomioConverterTest {

    @InjectMocks
    @Spy
    private OpenImmoToImmomioConverter openImmoToImmomioConverter;

    @Mock
    private OpenImmoToImmomioConverterCommercial openImmoToImmomioConvertCommercial;
    @Mock
    private OpenImmoToImmomioConverterFlat openImmoToImmomioConvertFlat;
    @Mock
    private OpenImmoToImmomioConverterGarage openImmoToImmomioConvertGarage;

    @Test
    public void givenImmobilieWithOneCategory_propertyTypeShouldBeExtracted() throws
            OpenImmoToImmomioConverterException {
        Immobilie commercialImmobilie = new Immobilie();
        Objektart objektArt = new Objektart();
        Objektkategorie objektkategorie = new Objektkategorie();

        objektArt.getBueroPraxen().add(new BueroPraxen());
        objektkategorie.setObjektart(objektArt);
        commercialImmobilie.setObjektkategorie(objektkategorie);

        PropertyType type = openImmoToImmomioConverter.convert(commercialImmobilie).getType();
        assertEquals(type, PropertyType.COMMERCIAL);
    }

    @Test
    public void givenImmobilieWithMoreThanOneEntryInCategory_exceptionShouldBeThrown() {
        Immobilie commercialImmobilie = new Immobilie();
        Objektart objektArt = new Objektart();
        Objektkategorie objektkategorie = new Objektkategorie();

        objektArt.getBueroPraxen().add(new BueroPraxen());
        objektArt.getBueroPraxen().add(new BueroPraxen());
        objektkategorie.setObjektart(objektArt);
        commercialImmobilie.setObjektkategorie(objektkategorie);

        assertThrows(OpenImmoToImmomioConverterException.class,
                () -> openImmoToImmomioConverter.convert(commercialImmobilie));
    }

    @Test
    public void givenImmobilieWithMoreThanOneCategory_exceptionShouldBeThrown() {
        Immobilie commercialImmobilie = new Immobilie();
        Objektart objektArt = new Objektart();
        Objektkategorie objektkategorie = new Objektkategorie();

        objektArt.getBueroPraxen().add(new BueroPraxen());
        objektArt.getHaus().add(new Haus());
        objektkategorie.setObjektart(objektArt);
        commercialImmobilie.setObjektkategorie(objektkategorie);

        assertThrows(OpenImmoToImmomioConverterException.class,
                () -> openImmoToImmomioConverter.convert(commercialImmobilie));
    }

    @Test
    public void givenImmobilieWithZeroCategory_flatPropertyShouldBeCreated() throws
            OpenImmoToImmomioConverterException {
        Immobilie commercialImmobilie = new Immobilie();
        Objektart objektArt = new Objektart();
        Objektkategorie objektkategorie = new Objektkategorie();

        objektkategorie.setObjektart(objektArt);
        commercialImmobilie.setObjektkategorie(objektkategorie);

        PropertyType type = openImmoToImmomioConverter.convert(commercialImmobilie).getType();
        assertEquals(type, PropertyType.FLAT);
    }

    @Test
    public void givenImmobilieWithValidObjectType_propertyWithSameObjectTypeShouldBeCreated() throws
            OpenImmoToImmomioConverterException {
        Immobilie commercialImmobilie = new Immobilie();
        Objektart objektArt = new Objektart();
        Objektkategorie objektkategorie = new Objektkategorie();

        objektArt.getBueroPraxen().add(new BueroPraxen());
        objektkategorie.setObjektart(objektArt);
        commercialImmobilie.setObjektkategorie(objektkategorie);

        var propertyType = openImmoToImmomioConverter.convert(commercialImmobilie).getType();

        Mockito.verify(openImmoToImmomioConvertCommercial).addImmobilie(Mockito.any(), Mockito.any());

        assertEquals(propertyType, PropertyType.COMMERCIAL);
    }

}
