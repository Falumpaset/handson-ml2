package de.immomio.broker;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;

/**
 * @author Niklas Lindemann
 */
public class TestUtils {
    public static PropertyProposal createPropertyProposal(
            Property property,
            Double score,
            PropertyProposalState state
    ) {
        PropertyProposal propertyProposal = new PropertyProposal();
        propertyProposal.setScore(score);
        propertyProposal.setProperty(property);
        propertyProposal.setState(state);
        return propertyProposal;
    }
}
