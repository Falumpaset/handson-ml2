package de.immomio.service.landlord;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.shared.bean.discount.LandlordAddonDiscountBean;
import de.immomio.model.repository.service.landlord.discount.CustomerProductAddonDiscountRepository;
import de.immomio.model.repository.service.landlord.discount.DiscountRepository;
import de.immomio.utils.TestHelper;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
@RunWith(SpringRunner.class)
public class LandlordDiscountServiceTest {

    @Mock
    private CustomerProductAddonDiscountRepository productAddonDiscountRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Spy
    @InjectMocks
    private LandlordDiscountService landlordDiscountService;

    @Test
    public void createLandlordAddonDiscountsWithNewDiscount() {
        LandlordAddonDiscountBean addonDiscountsBean = new LandlordAddonDiscountBean(0.4, new Date(),1L);

        doReturn(new LandlordCustomerProductAddonDiscount()).when(landlordDiscountService).findOrCreateAddonDiscount(any(), any());
        doReturn(new LandlordCustomerProductAddonDiscount()).when(landlordDiscountService).createLandlordAddonDiscount(any(), any(), any());

        landlordDiscountService.createLandlordAddonDiscounts(new LandlordCustomer(), Collections.singletonList(addonDiscountsBean));

        verify(landlordDiscountService, times(1)).createLandlordAddonDiscount(any(), any(), any());
        verify(landlordDiscountService, never()).updateLandlordAddonDiscount(any(), any());
    }

    @Test
    public void createLandlordAddonDiscountsWithExistingDiscount() {
        LandlordAddonDiscountBean addonDiscountsBean = new LandlordAddonDiscountBean(0.4, new Date(),1L);
        doReturn(TestHelper.createLandlordCustomerProductAddonDiscount(1L, null, null)).when(landlordDiscountService).findOrCreateAddonDiscount(any(), any());
        doNothing().when(landlordDiscountService).updateLandlordAddonDiscount(any(), any());

        landlordDiscountService.createLandlordAddonDiscounts(new LandlordCustomer(), Collections.singletonList(addonDiscountsBean));

        verify(landlordDiscountService, never()).createLandlordAddonDiscount(any(), any(), any());
        verify(landlordDiscountService, times(1)).updateLandlordAddonDiscount(any(), any());
    }

    @Test
    public void updateLandlordDiscountWithDifferentDate() {
        LandlordAddonDiscountBean addonDiscountsBean = new LandlordAddonDiscountBean();
        addonDiscountsBean.setValue(0.5);
        addonDiscountsBean.setEndDate(LocalDate.now().plusMonths(5).toDate());
        LandlordDiscount landlordDiscount = TestHelper.createLandlordDiscount(0.5, LocalDate.now().toDate(), 0L);
        LandlordCustomerProductAddonDiscount productAddonDiscount = TestHelper
                .createLandlordCustomerProductAddonDiscount(1L,
                        landlordDiscount, null);

        landlordDiscountService.updateLandlordAddonDiscount(addonDiscountsBean, productAddonDiscount);

        verify(productAddonDiscountRepository, times(1)).save(any(LandlordCustomerProductAddonDiscount.class));
    }

    @Test
    public void updateLandlordDiscountNotNecessary() {
        LandlordAddonDiscountBean addonDiscountsBean = new LandlordAddonDiscountBean();
        addonDiscountsBean.setValue(0.5);
        Date endDate = LocalDate.now().plusMonths(5).toDate();
        addonDiscountsBean.setEndDate(endDate);
        LandlordDiscount landlordDiscount = TestHelper.createLandlordDiscount(0.5, endDate, 0L);
        LandlordCustomerProductAddonDiscount productAddonDiscount = TestHelper
                .createLandlordCustomerProductAddonDiscount(1L,
                        landlordDiscount, null);

        landlordDiscountService.updateLandlordAddonDiscount(addonDiscountsBean, productAddonDiscount);

        verify(productAddonDiscountRepository, times(0)).save(any(LandlordCustomerProductAddonDiscount.class));
    }

    @Test
    public void findOrCreateAddonDiscountFound() {
        LandlordDiscount landlordDiscount = TestHelper.createLandlordDiscount(0.5, LocalDate.now().toDate(), 1L);
        LandlordProductAddon productAddon = TestHelper.createProductAddon(1L);
        LandlordCustomerProductAddonDiscount addonDiscount = TestHelper
                .createLandlordCustomerProductAddonDiscount(1L, landlordDiscount, productAddon);

        LandlordAddonDiscountBean discountBean = new LandlordAddonDiscountBean();
        discountBean.setAddonId(1L);

        when(productAddonDiscountRepository.findAllByCustomer(any())).thenReturn(Collections.singletonList(addonDiscount));

        LandlordCustomerProductAddonDiscount createdAddonDiscount = landlordDiscountService.findOrCreateAddonDiscount(discountBean, null);
        Assert.assertEquals(createdAddonDiscount.getProductAddon().getId(), discountBean.getAddonId());
    }

    @Test
    public void findOrCreateAddonDiscountCreated() {
        LandlordDiscount landlordDiscount = TestHelper.createLandlordDiscount(0.5, LocalDate.now().toDate(), 1L);
        LandlordProductAddon productAddon = TestHelper.createProductAddon(1L);
        LandlordCustomerProductAddonDiscount addonDiscount = TestHelper
                .createLandlordCustomerProductAddonDiscount(1L, landlordDiscount, productAddon);

        LandlordAddonDiscountBean discountBean = new LandlordAddonDiscountBean();
        discountBean.setAddonId(2L);

        when(productAddonDiscountRepository.findAllByCustomer(any())).thenReturn(Collections.singletonList(addonDiscount));

        LandlordCustomerProductAddonDiscount createdAddonDiscount = landlordDiscountService.findOrCreateAddonDiscount(discountBean, null);
        Assert.assertTrue(createdAddonDiscount.isNew());
    }
}