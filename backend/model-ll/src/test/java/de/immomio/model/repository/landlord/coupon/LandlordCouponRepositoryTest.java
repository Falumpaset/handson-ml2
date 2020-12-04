package de.immomio.model.repository.landlord.coupon;

import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import de.immomio.data.landlord.entity.couponusage.LandlordCouponUsage;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.couponusage.LandlordCouponUsageRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.discount.LandlordDiscountRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordCoupon;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LandlordCouponRepositoryTest extends BaseDataJpaTest {

    @Autowired

    private LandlordCouponRepository couponRepository;

    @Autowired
    private LandlordDiscountRepository discountRepository;

    @Autowired
    private LandlordUserRepository userRepository;

    @Autowired
    private LandlordCouponUsageRepository couponUsageRepository;

    {
        describe("create landlord coupon", () -> it("create coupon - success", () -> {
            LandlordCoupon landlordCoupon = buildGenericLandlordCoupon();
            LandlordCoupon savedCoupon = couponRepository.save(landlordCoupon);

            compareLandlordCoupon(landlordCoupon, savedCoupon);
        }));

        describe("find landlord coupon", () -> {

            it("find coupon", () -> {
                LandlordCoupon landlordCoupon = buildGenericLandlordCoupon();
                LandlordCoupon savedCoupon = couponRepository.save(landlordCoupon);

                compareLandlordCoupon(couponRepository.findById(savedCoupon.getId()).get(), savedCoupon);
            });

            it("unknown coupon", () -> assertTrue(couponRepository.findById(-42L).isEmpty()));
        });

        describe("delete landlord coupon", () -> {

            it("delete coupon - success", () -> {
                LandlordCoupon landlordCoupon = buildGenericLandlordCoupon();
                LandlordCoupon savedCoupon = couponRepository.save(landlordCoupon);
                couponRepository.delete(savedCoupon);
                assertTrue(couponRepository.findById(savedCoupon.getId()).isEmpty());
            });

            it("check delete cascade", () -> {
                LandlordCoupon landlordCoupon = buildGenericLandlordCoupon();
                LandlordCoupon savedCoupon = couponRepository.save(landlordCoupon);

                final LandlordCouponUsage couponUsage = savedCoupon.getCouponUsages().get(0);
                final LandlordUser couponUser = couponUsage.getUser();
                final Long couponId = savedCoupon.getId();
                final Long discountId = savedCoupon.getDiscount().getId();
                final Long usageId = couponUsage.getId();
                final Long userId = couponUser.getId();

                assertNotNull(couponId);
                assertNotNull(discountId);
                assertNotNull(userId);
                assertNotNull(usageId);

                withAuthentication(couponUser, () -> {
                    couponRepository.deleteById(couponId);

                    assertTrue(couponRepository.findById(couponId).isEmpty());
                    assertTrue(discountRepository.findById(discountId).isPresent());
                    assertTrue(couponUsageRepository.findById(usageId).isEmpty());
                    assertTrue(userRepository.findById(userId).isPresent());
                });
            });

        });
    }

    private LandlordCoupon buildGenericLandlordCoupon() {
        LandlordUser landlordUser = this.userRepository.save(TestHelper.generateLandlordUser());
        LandlordDiscount landlordDiscount = this.discountRepository.save(TestHelper.generateLandlordDiscount());
        LandlordCoupon landlordCoupon = TestHelper.generateLandlordCoupon(landlordDiscount);

        List<LandlordCouponUsage> couponUsages = new ArrayList<>();
        couponUsages.add(this.couponUsageRepository.save(TestHelper.generateLandlordCouponUsage(landlordUser)));
        landlordCoupon.setCouponUsages(couponUsages);
        return landlordCoupon;
    }

}
