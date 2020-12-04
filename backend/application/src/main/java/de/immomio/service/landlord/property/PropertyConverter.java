package de.immomio.service.landlord.property;

import de.immomio.data.landlord.bean.property.PropertyBean;
import de.immomio.data.landlord.bean.property.PropertySearcherPropertyBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.service.landlord.LandlordCustomerConverter;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PropertyConverter {

    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
    private LandlordCustomerConverter customerConverter;

    public PropertyConverter(LandlordCustomerConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    public PropertyBean convertToPropertyBean(Property property) {
        PropertyBean propertyBean = new PropertyBean();
        fillBaseData(propertyBean, property);
        return propertyBean;
    }

    public PropertyBean convertToPropertyBean(Property property, String applicationLink) {
        PropertyBean propertyBean = new PropertyBean();
        fillBaseData(propertyBean, property);
        propertyBean.setApplicationLink(applicationLink);
        return propertyBean;
    }

    public PropertySearcherPropertyBean convertToPropertySearcherPropertyBean(Property property) {
        PropertySearcherPropertyBean propertyBean = new PropertySearcherPropertyBean();
        fillBaseData(propertyBean, property);
        propertyBean.setCustomer(customerConverter.convertCustomerToBean(property.getCustomer()));
        return propertyBean;
    }

    private void fillBaseData(PropertyBean propertyBean, Property property) {
        propertyBean.setId(property.getId());
        propertyBean.setData(property.getData());
        propertyBean.setValidUntil(property.getValidUntil());
        propertyBean.setRuntimeInDays(property.getRuntimeInDays());
        propertyBean.setCreated(property.getCreated());
        propertyBean.setUpdated(property.getUpdated());
        propertyBean.setExternalId(property.getExternalId());
        propertyBean.setShowSelfDisclosureQuestions(property.isShowSelfDisclosureQuestions());
        propertyBean.setWbs(property.getPrioset().getData().getWbs());
        propertyBean.setTitleImage(property.getData().getTitleImage());
        propertyBean.setType(property.getType());

        Double totalRentGross = property.getData().getTotalRentGross();
        if (totalRentGross == null) {
            propertyBean.setTotalRentGross(numberFormat.format(totalRentGross));
        }

        Double size = property.getData().getSize();
        if (size != null) {
            propertyBean.setSize(numberFormat.format(size));
        }

    }

}
