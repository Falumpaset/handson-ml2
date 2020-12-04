package de.immomio.model.repository.landlord.couponusage;

import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import de.immomio.data.landlord.entity.couponusage.LandlordCouponUsage;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.coupon.LandlordCouponRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.discount.LandlordDiscountRepository;
import de.immomio.utils.TestComparatorHelper;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LandlordCouponUsageRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordUserRepository userRepository;

    @Autowired
    private LandlordCouponRepository couponRepository;

    @Autowired
    private LandlordCouponUsageRepository couponUsageRepository;

    @Autowired
    private LandlordDiscountRepository discountRepository;

    {
        describe("create landlord coupon", () -> it("create coupon - success", () -> {
            LandlordCouponUsage landlordCouponUsage = buildGenericLandlordCouponUsage();
            LandlordCouponUsage savedCoupon = couponUsageRepository.save(landlordCouponUsage);

            TestComparatorHelper.compareLandlordCouponUsage(landlordCouponUsage, savedCoupon);
        }));

        describe("find landlord coupon", () -> {

            it("find coupon", () -> {
                LandlordCouponUsage landlordCoupon = buildGenericLandlordCouponUsage();
                LandlordCouponUsage savedCoupon = couponUsageRepository.save(landlordCoupon);

                withAuthentication(savedCoupon.getUser(), () -> TestComparatorHelper.compareLandlordCouponUsage(
                        couponUsageRepository.findById(savedCoupon.getId()).get(), savedCoupon));
            });

            it("unknown coupon", () -> {
                LandlordCouponUsage landlordCoupon = buildGenericLandlordCouponUsage();
                withAuthentication(landlordCoupon.getUser(), () -> assertTrue(couponUsageRepository.findById(-42L).isEmpty()));
            });
        });

        describe("delete landlord coupon", () -> {

            it("delete coupon - success", () -> {
                LandlordCouponUsage landlordCouponUsage = buildGenericLandlordCouponUsage();
                LandlordCouponUsage savedCouponUsage = couponUsageRepository.save(landlordCouponUsage);
                withAuthentication(savedCouponUsage.getUser(), () -> {
                    couponUsageRepository.delete(savedCouponUsage);
                    assertTrue(couponUsageRepository.findById(savedCouponUsage.getId()).isEmpty());
                });
            });

            it("check delete cascade", () -> {
                LandlordCouponUsage landlordCouponUsage = buildGenericLandlordCouponUsage();
                LandlordCouponUsage savedCouponUsage = couponUsageRepository.save(landlordCouponUsage);

                final LandlordCoupon coupon = savedCouponUsage.getCoupon();
                final LandlordUser couponUser = savedCouponUsage.getUser();
                final Long couponId = coupon.getId();
                final Long usageId = savedCouponUsage.getId();
                final Long couponUserId = couponUser.getId();

                assertNotNull(couponId);
                assertNotNull(usageId);
                assertNotNull(couponUserId);

                withAuthentication(savedCouponUsage.getUser(), () -> {
                    couponUsageRepository.deleteById(usageId);

                    assertTrue(couponRepository.findById(couponId).isPresent());
                    assertTrue(couponUsageRepository.findById(usageId).isEmpty());
                    assertTrue(userRepository.findById(couponUserId).isPresent());
                });
            });

        });
    }

    private LandlordCouponUsage buildGenericLandlordCouponUsage() {
        LandlordUser landlordUser = this.userRepository.save(TestHelper.generateLandlordUser());
        LandlordDiscount landlordDiscount = this.discountRepository.save(TestHelper.generateLandlordDiscount());
        LandlordCoupon landlordCoupon = TestHelper.generateLandlordCoupon(landlordDiscount);

        LandlordCouponUsage couponUsage = this.couponUsageRepository.save(
                TestHelper.generateLandlordCouponUsage(landlordUser));
        List<LandlordCouponUsage> couponUsages = new ArrayList<>();
        couponUsages.add(couponUsage);
        landlordCoupon.setCouponUsages(couponUsages);

        LandlordCoupon savedCoupon = this.couponRepository.save(landlordCoupon);
        couponUsage.setCoupon(savedCoupon);
        return couponUsage;
    }

}
