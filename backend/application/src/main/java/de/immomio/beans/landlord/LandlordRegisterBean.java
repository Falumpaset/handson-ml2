package de.immomio.beans.landlord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.immomio.beans.AbstractRegisterUserBean;

/**
 * @author Bastian Bliemeister
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandlordRegisterBean extends AbstractRegisterUserBean<LandlordCustomerRegisterBean> {

}
