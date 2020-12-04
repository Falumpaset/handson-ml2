package de.immomio.service.calculator.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 */

@Slf4j
@Service
public class MathematicalCalculator {

    public double calculateNewIncomeFactor(double income, double totalRentGross, int minFactor, int maxFactor) {
        double incomeFactor = income / totalRentGross;

        if (incomeFactor < minFactor) {
            return 0.0;
        }

        if (minFactor <= incomeFactor  && incomeFactor <= maxFactor) {
            return (incomeFactor - minFactor) / (maxFactor - minFactor);
        }

        if (maxFactor < incomeFactor) {
            return 1.0;
        }

        return 0.0;
    }
}
