package de.immomio.importer.worker;

import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.importer.worker.service.WorkerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RunWith(SpringRunner.class)
public class WorkerServiceTest {

    @InjectMocks
    private WorkerService workerService;

    @Test
    public void propertyShouldBeUpdatedFalseNameEquals() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        propertyData.setName("test1");
        property.setData(propertyData);

        PropertyData propertyData1 = new PropertyData();
        propertyData1.setName("test1");
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertFalse(b);
    }

    @Test
    public void propertyShouldBeUpdatedTrueNameDiffers() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        propertyData.setName("test12");
        property.setData(propertyData);

        PropertyData propertyData1 = new PropertyData();
        propertyData1.setName("test1");
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertTrue(b);
    }

    @Test
    public void propertyShouldBeUpdatedTrueRegionNull() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        Address address = new Address();
        address.setStreet("street");
        propertyData.setAddress(address);
        property.setData(propertyData);

        PropertyData propertyData1 = new PropertyData();
        Address address1 = new Address();
        address1.setRegion("test");
        address1.setStreet("street");
        propertyData1.setAddress(address1);
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertFalse(b);
    }

    @Test
    public void propertyShouldBeUpdatedTrueRegionSet() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        Address address = new Address();
        address.setStreet("street");
        address.setRegion("region1");

        propertyData.setAddress(address);
        property.setData(propertyData);

        PropertyData propertyData1 = new PropertyData();
        Address address1 = new Address();
        address1.setRegion("region2");
        address1.setStreet("street");
        propertyData1.setAddress(address1);
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertTrue(b);
    }

    @Test
    public void propertyShouldBeUpdatedTrueRegionNullStreetDiffers() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        Address address = new Address();
        address.setStreet("street");
        propertyData.setAddress(address);
        property.setData(propertyData);

        PropertyData propertyData1 = new PropertyData();
        Address address1 = new Address();
        address1.setRegion("test");
        address1.setStreet("street2");
        propertyData1.setAddress(address1);
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertTrue(b);
    }

    @Test
    public void propertyShouldBeUpdatedFalseDocumentsDiffers() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        Address address = new Address();
        address.setStreet("street");
        propertyData.setAddress(address);
        property.setData(propertyData);
        propertyData.setAttachments(List.of(new S3File()));

        PropertyData propertyData1 = new PropertyData();
        Address address1 = new Address();
        address1.setStreet("street");
        propertyData1.setAddress(address1);
        property1.setData(propertyData1);

        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertFalse(b);
    }

    @Test
    public void propertyShouldBeUpdatedTrueDocumentsAndRentDiffers() {
        Property property = new Property();
        Property property1 = new Property();

        PropertyData propertyData = new PropertyData();
        Address address = new Address();
        address.setStreet("street");
        propertyData.setAddress(address);
        property.setData(propertyData);
        propertyData.setBasePrice(11d);
        propertyData.setAttachments(List.of(new S3File()));

        PropertyData propertyData1 = new PropertyData();
        Address address1 = new Address();
        address1.setStreet("street");
        propertyData1.setAddress(address1);
        propertyData1.setBasePrice(12d);
        property1.setData(propertyData1);


        boolean b = workerService.propertyShouldBeUpdated(property, property1);

        Assert.assertTrue(b);
    }
}
