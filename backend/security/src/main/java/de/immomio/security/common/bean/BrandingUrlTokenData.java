package de.immomio.security.common.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonInclude
@Getter
@Setter
@NoArgsConstructor
public class BrandingUrlTokenData implements Serializable {

    private static final long serialVersionUID = -2139398223931303980L;

    private Long customerId;

    private String name;

    private String primaryColor;

    private String secondaryColor;

    private String primaryTextColor;

    private String secondaryTextColor;

    private String buttonTextColor;

    private String backgroundColor;

    private String cardBackgroundColor;

    private boolean active;

    private S3File logo;

    private String logoRedirectUrl;

    private List<CustomQuestionBean> globalQuestions;

    public BrandingUrlTokenData(LandlordCustomer customer,
            LandlordCustomerBrandingTheme theme,
            S3File logo,
            String logoRedirectUrl,
            List<CustomQuestionBean> globalQuestions) {
        this.customerId = customer.getId();
        this.globalQuestions = globalQuestions;
        this.logo = logo;
        this.logoRedirectUrl = logoRedirectUrl;
        if (theme != null) {
            this.name = theme.getName();
            this.primaryColor = theme.getPrimaryColor();
            this.secondaryColor = theme.getSecondaryColor();
            this.primaryTextColor = theme.getPrimaryTextColor();
            this.secondaryTextColor = theme.getSecondaryTextColor();
            this.buttonTextColor = theme.getButtonTextColor();
            this.backgroundColor = theme.getBackgroundColor();
            this.cardBackgroundColor = theme.getCardBackgroundColor();
            this.active = theme.isActive();
        }
    }
}
