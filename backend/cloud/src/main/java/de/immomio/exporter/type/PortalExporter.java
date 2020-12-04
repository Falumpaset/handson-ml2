//package de.immomio.exporter.type;
//
//
//import de.immomio.constants.portal2.Portal;
//import de.immomio.exporter.AbstractExporter;
//import de.immomio.exporter.exception.ExporterException;
//import de.immomio.model.entity.landlord.customer.LandlordCustomer;
//import de.immomio.model.entity.landlord.customer.credential.Credential;
//import de.immomio.model.entity.landlord.customer.property.Contact;
//import de.immomio.model.entity.landlord.customer.property.Property;
//import de.immomio.model.entity.landlord.customer.property.portal2.PropertyPortal;
//import de.immomio.model.entity.landlord.customer.user.LandlordUser;
//import de.immomio.model.entity.landlord.product.LandlordProduct;
//import de.immomio.model.repository.core.landlord.customer.property.portal2.BaseLandlordPropertyPortalRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//
//public class PortalExporter extends TypeExporterImpl {
//
//    @Autowired
//    private BaseLandlordPropertyPortalRepository propertyPortalRepository;
//
//    @Override
//    public boolean activate(Property property, Credential credential, LandlordProduct product) {
//        if (property == null || credential == null || product == null)
//            return false;
//
//        LandlordCustomer customer = property.getCustomer();
//
//        return activate(property, credential, product, customer);
//    }
//
//    private boolean activate(Property property, Credential credential, LandlordProduct product,
//                             LandlordCustomer customer) {
//        Portal portal2 = credential.getPortal();
//
//        if (portal2 == null)
//            return false; // TODO
//
//        if (containsPortal(property, credential) != null) {
//            LOG.info("PortalMapping already exists for " + portal2.name() + "[credentialID: " + credential.getId()
//                    + "] -> trying to update ...");
//
//            return update(property, credential, product);
//        }
//
//        AbstractExporter exporter = getExporter(portal2);
//
//        if (exporter == null)
//            return false; // TODO
//
//        LandlordUser user = getResponsible(property);
//
//        if (user == null)
//            return false; // TODO
//
//        Contact contact = userToContact(user);
//
//        boolean bool;
//        try {
//            bool = exporter.activate(property, customer, credential, contact, product);
//        } catch (ExporterException e) {
//            LOG.error("Error activating flat [" + property + "] on " + portal2.name(), e);
//
//            return false;
//        }
//
//        if (bool)
//            addPortalMappings(exporter.getReleasedPortals(), property, credential, product);
//
//        return bool;
//    }
//
//    @Override
//    public boolean update(Property property, Credential credential, LandlordProduct product) {
//        if (property == null || credential == null || product == null)
//            return false;
//
//        LandlordCustomer customer = property.getCustomer();
//
//        return update(property, credential, product, customer);
//    }
//
//    private boolean update(Property property, Credential credential, LandlordProduct product,
//                           LandlordCustomer customer) {
//        Portal portal2 = credential.getPortal();
//
//        if (portal2 == null)
//            return false; // TODO
//
//        if (containsPortal(property, credential) == null) {
//            LOG.info("PortalMapping did not exists for " + portal2.name() + "[credentialID: " + credential.getId()
//                    + "]");
//
//            return false;
//        }
//
//        AbstractExporter exporter = getExporter(portal2);
//
//        if (exporter == null)
//            return false; // TODO
//
//        LandlordUser user = getResponsible(property);
//
//        if (user == null)
//            return false; // TODO
//
//        Contact contact = userToContact(user);
//
//        boolean bool;
//        try {
//            bool = exporter.update(property, customer, credential, contact, product);
//        } catch (ExporterException e) {
//            LOG.error("Error updating flat [" + property + "] on " + portal2.name(), e);
//
//            return false;
//        }
//
//        if (bool)
//            addPortalMappings(exporter.getReleasedPortals(), property, credential, product);
//
//        return bool;
//    }
//
//    @Override
//    public boolean deactivate(Property property, Credential credential) {
//        Portal portal2 = credential.getPortal();
//
//        if (portal2 == null)
//            return false; // TODO
//
//        AbstractExporter exporter = getExporter(portal2);
//
//        if (exporter == null)
//            return false; // TODO
//
////		exporter.deactivate(flat, customer, credential, product);
//
//        return false;
//    }
//
//    @Override
//    public boolean delete(Property property, Credential credential) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    protected PropertyPortal containsPortal(Property property, Credential credentials) {
//
//        if (property == null || credentials == null)
//            return null;
//
//        for (PropertyPortal portal2 : property.getPortals())
//            if (portal2.getCredentials().equals(credentials))
//                return portal2;
//
//        return null;
//    }
//
//    protected boolean addPortalMappings(List<Portal> portals, Property property, Credential credentials,
//                                        LandlordProduct product) {
//        boolean bool = true;
//
//        for (Portal portal2 : portals)
//            if (!addPortalMapping(property, credentials, product, portal2))
//                bool = false; // TODO LOG
//
//        return bool;
//    }
//
//    protected boolean addPortalMapping(Property property, Credential credentials, LandlordProduct product) {
//        return addPortalMapping(property, credentials, product, null);
//    }
//
//    protected boolean addPortalMapping(Property property, Credential credentials, LandlordProduct product,
//                                       Portal portal2) {
//        if (property == null || credentials == null || product == null)
//            return false;
//
//        if (portal2 == null)
//            portal2 = credentials.getPortal();
//
//        LOG.info("AbstractExporter.addPortalMapping(...) -> " + portal2);
//
//        PropertyPortal propertyPortal = containsPortal(property, credentials);
//
//        if (propertyPortal == null) {
//            LOG.info("add PortalMapping -> " + portal2.name());
//
//            propertyPortal = new PropertyPortal();
//
//            propertyPortal.setPortal(portal2);
//            propertyPortal.setCredentials(credentials);
//
//            propertyPortalRepository.save(propertyPortal);
//        } else {
//            LOG.info("refresh PortalMapping -> " + portal2.name());
//        }
//
//        propertyPortal.setPortal(portal2);
////		TODO portalMapping.setProductID(product.getId());
//
//        return true;
//    }
//
//    protected boolean removePortalMapping(Property property, Credential credentials) {
//        LOG.info("trying to remove PortalMapping");
//
//        PropertyPortal existingPortal = containsPortal(property, credentials);
//
//        while (existingPortal != null) {
//            LOG.info("remove PortalMapping -> " + existingPortal);
//
//            propertyPortalRepository.delete(existingPortal);
//        }
//
//        return true;
//    }
//}
