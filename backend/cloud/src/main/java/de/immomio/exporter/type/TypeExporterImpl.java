//package de.immomio.exporter.type;
//
//
//import de.immomio.constants.portal2.Portal;
//import de.immomio.exporter.AbstractExporter;
//import de.immomio.model.entity.landlord.customer.LandlordCustomer;
//import de.immomio.model.entity.landlord.customer.property.Contact;
//import de.immomio.model.entity.landlord.customer.property.Property;
//import de.immomio.model.entity.landlord.customer.user.LandlordUser;
//import de.immomio.model.entity.landlord.customer.user.profile.LandlordUserProfile;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class TypeExporterImpl implements TypeExporter {
//
//    protected static final Logger LOG = LoggerFactory.getLogger(TypeExporterImpl.class);
//
//    @Autowired
//    private List<AbstractExporter> exporters = new ArrayList<>();
//
//    protected AbstractExporter getExporter(Portal portal2) {
//        for (AbstractExporter exporter : exporters) {
//            if (exporter.isApplicable(portal2)) {
//                LOG.debug("searching exporter for portal2 " + portal2 + " -> " + exporter.getClass().getSimpleName()
//                        + " [true]");
//                return exporter;
//            }
//
//            LOG.debug("searching exporter for portal2 " + portal2 + " -> " + exporter.getClass().getSimpleName()
//                    + " [false]");
//        }
//
//        return null;
//    }
//
//    protected Contact userToContact(LandlordUser user) {
//        Contact contact = new Contact();
//
//        if (user == null)
//            return contact;
//
//        contact.setEmail(user.getEmail());
//
//        LandlordUserProfile profile = user.getProfile();
//
//        if (profile == null)
//            return contact;
//
//        contact.setFirstName(profile.getFirstname());
//        contact.setName(profile.getName());
//
//        return contact;
//    }
//
//    protected LandlordUser getResponsible(Property property) {
//        if (property.getCustomer() == null)
//            return null;
//
//        LandlordCustomer customer = property.getCustomer();
//
//        if (customer == null)
//            return null;
//
//        LandlordUser user = property.getUser();
//
//        if (user != null)
//            return user;
//
//        return customer.getResposibleUser();
//    }
//}
