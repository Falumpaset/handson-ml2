package de.immomio.security.common.bean;

import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.List;

@Getter
@Setter
public class PreviewBrandingUrlToken extends AbstractToken {

    private static final long serialVersionUID = -2139398223931303980L;

    private BrandingUrlTokenData data;

    public PreviewBrandingUrlToken(LandlordCustomer customer, LandlordCustomerBrandingTheme theme, S3File logo, List<CustomQuestionBean> globalQuestions) {
        super(DateTime.now().getMillis());

        String logoRedirectUrl = customer.getCustomerSettings() != null ? customer.getCustomerSettings().getLogoRedirectUrl() : null;
        this.data = new BrandingUrlTokenData(customer, theme, logo, logoRedirectUrl, globalQuestions);
    }

    public PreviewBrandingUrlToken() {
        super(DateTime.now().getMillis());
    }
}
