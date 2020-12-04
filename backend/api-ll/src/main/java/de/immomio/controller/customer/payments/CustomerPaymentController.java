package de.immomio.controller.customer.payments;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import de.immomio.billing.landlord.LandlordStripeBilling;
import de.immomio.billing.provider.AbstractBillingProvider;
import de.immomio.billing.provider.card.StripeDeleteSourceObject;
import de.immomio.billing.provider.card.StripeNewDefaultSourceObject;
import de.immomio.billing.provider.card.StripeSourceObject;
import de.immomio.billing.provider.card.response.StripeAllSourcesResponse;
import de.immomio.billing.provider.card.response.StripeDeleteSourceResponse;
import de.immomio.billing.provider.card.response.StripeSourceResponse;
import de.immomio.billing.provider.exception.DeleteCardException;
import de.immomio.billing.provider.initialize.StripePaymentInitializeObject;
import de.immomio.controller.BaseController;
import de.immomio.data.base.type.customer.CustomerPaymentDetails;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author Bastian Bliemeister.
 */
@Controller
@RequestMapping(value = "/customers")
@Slf4j
public class CustomerPaymentController extends BaseController {

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    @Autowired
    private LandlordStripeBilling stripeBilling;

    @Autowired
    private List<AbstractBillingProvider<LandlordCustomer, ?, ?, ?, ?, ?>> billingProviders;

    @RequestMapping(value = {"/{id}/payments/initialized/{method}"}, method = RequestMethod.GET)
    public ResponseEntity<Boolean> paymentInitialized(@PathVariable Long id, @PathVariable PaymentMethodType method) {
        if (id == null || method == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();

        AbstractBillingProvider<LandlordCustomer, ?, ?, ?, ?, ?> billingProvider = null;
        for (AbstractBillingProvider<LandlordCustomer, ?, ?, ?, ?, ?> tmp : billingProviders) {
            if (!tmp.isApplicable(method)) {
                continue;
            }

            billingProvider = tmp;
            break;
        }

        if (billingProvider == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                billingProvider.paymentMethodInitialized(customer) ? Boolean.TRUE : Boolean.FALSE, HttpStatus.OK
        );
    }

    @RequestMapping(value = {"/{id}/payments/initializeStripePayment"}, method = RequestMethod.POST)
    public ResponseEntity<InitializeResponseBean> paymentInitializeStripe(@PathVariable Long id,
                                                                          @RequestBody @Valid
                                                                                  StripePaymentInitializeObject initializeObject) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();

        boolean initSuccess = stripeBilling.initializePaymentMethod(customer, initializeObject);

        if (!initSuccess) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String stripeId = customer.getPaymentDetails().get(CustomerPaymentDetails.STRIPE_CUSTOMERID.toString());

        if (StringUtils.isEmpty(stripeId)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        InitializeResponseBean irb = new InitializeResponseBean();

        irb.setStripeCustomerId(stripeId);

        return new ResponseEntity<>(irb, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}/payments/addSource"}, method = RequestMethod.POST)
    public ResponseEntity<Object> paymentAddStripeSource(@PathVariable Long id,
                                                         @RequestBody @Valid StripeSourceObject stripeSource) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();
        StripeSourceResponse response;
        try {
            response = stripeBilling.addSource(customer, stripeSource);
        } catch (CardException | APIException | AuthenticationException | APIConnectionException | InvalidRequestException e) {
            String errorMsg = "Unable to add payment source.";
            log.error(errorMsg, e);
            return internalServerError(errorMsg, Collections.singletonList(e.getMessage()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}/payments/deleteSource"}, method = RequestMethod.POST)
    public ResponseEntity<StripeDeleteSourceResponse> paymentDeleteStripeSource(@PathVariable Long id,
                                                                                @RequestBody @Valid
                                                                                        StripeDeleteSourceObject deleteSource) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();
        StripeDeleteSourceResponse deleteResult;
        try {
            deleteResult = stripeBilling.deleteSource(customer, deleteSource);
        } catch (DeleteCardException | APIConnectionException | AuthenticationException | InvalidRequestException | CardException | APIException e) {
            log.error("unable to delete payment card.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(deleteResult, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}/payments/updateDefaultSource"}, method = RequestMethod.POST)
    public ResponseEntity<StripeSourceResponse> paymentUpdateDefaultCard(@PathVariable Long id, @RequestBody @Valid
            StripeNewDefaultSourceObject defaultSource) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();
        StripeSourceResponse newDefault;
        try {
            newDefault = stripeBilling.updateDefault(customer, defaultSource.getSourceId());
        } catch (CardException | APIException | InvalidRequestException | AuthenticationException | APIConnectionException e) {
            log.error("unable to update payment card.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(newDefault, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}/payments/sources"}, method = RequestMethod.GET)
    public ResponseEntity<StripeAllSourcesResponse> paymentGetCustomerCards(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LandlordCustomer customer = landlordCustomerRepository.findById(id).get();
        StripeAllSourcesResponse response;
        try {
            response = stripeBilling.getSources(customer);
        } catch (CardException | APIException | InvalidRequestException | AuthenticationException | APIConnectionException e) {
            log.error("unable to fetch payment card.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
