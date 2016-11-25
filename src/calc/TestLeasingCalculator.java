package calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by yiqbal on 1/28/16.
 */
public class TestLeasingCalculator {
    public BigDecimal[] calculateMonthlyRate(final LeasingData leasingData, final CarData carData){
        int TWELVE_MONTHS = 12;
        int HUNDRED = 100;
        BigDecimal[] installments = {null, null, null, null};
        BigDecimal customerInstallment = getCustomerMonthlyRate(leasingData.getNumInstallMonths(),
                leasingData.getCustomerInterestRate(), carData, TWELVE_MONTHS, HUNDRED);

        BigDecimal dealerInstallment = getCustomerMonthlyRate(leasingData.getNumInstallMonths(),
                leasingData.getDealerInterestRate(), carData, TWELVE_MONTHS, HUNDRED);
        installments[0] = customerInstallment;
        installments[1] = customerInstallment.add(customerInstallment.multiply(new BigDecimal(19).divide(new BigDecimal(HUNDRED))));
        installments[2] = dealerInstallment;
        installments[3] = dealerInstallment.add(dealerInstallment.multiply(new BigDecimal(19).divide(new BigDecimal(HUNDRED))));


        return installments;
    }

    private BigDecimal getCustomerMonthlyRate(BigDecimal numInstallMonths, BigDecimal applicableInterestRate,
                                              CarData carData, int TWELVE_MONTHS, int HUNDRED) {
        final BigDecimal interestRate = halfUpDivide(halfUpDivide(applicableInterestRate,
                new BigDecimal(HUNDRED)), new BigDecimal(TWELVE_MONTHS));
        //println("interestRate is: " + interestRate);
        BigDecimal interestCoeff = PowerFinder.findPower(interestRate.add(BigDecimal.ONE), numInstallMonths);
        //println("interestCoeff is: " + interestCoeff);
        BigDecimal residualDivision = halfUpDivide(carData.getResidualValue(), interestCoeff);
        //println("residualDivision is: " + residualDivision);
        BigDecimal subtracted = carData.getTotalValue().subtract(residualDivision);
        BigDecimal subtractedLower =   BigDecimal.ONE.subtract(halfUpDivide(BigDecimal.ONE, interestCoeff));
        BigDecimal finalLowerPart =   halfUpDivide(subtractedLower, interestRate);
        return (subtracted.divide(finalLowerPart, 2, RoundingMode.HALF_UP)).abs();
    }

    private BigDecimal halfUpDivide(final BigDecimal first, final BigDecimal second) {
        //return first.divide(second, 2, RoundingMode.HALF_UP);
        //println("CEILING: ", first.divide(second, 3, RoundingMode.CEILING));
        //println("HALF_UP: ", first.divide(second, 3, RoundingMode.HALF_UP));
        return first.divide(second, 6, RoundingMode.HALF_UP);
    }

    static class LeasingData {
        BigDecimal numInstallMonths = null;
        BigDecimal customerInterestRate = null;
        BigDecimal advancePayment = null;
        BigDecimal dealerInterestRate = null;

        public LeasingData(BigDecimal numInstallMonths, BigDecimal customerInterestRate,
                           BigDecimal dealerInterestRate, BigDecimal advancePayment) {
            this.numInstallMonths = numInstallMonths;
            this.customerInterestRate = customerInterestRate;
            this.dealerInterestRate = dealerInterestRate;
            this.advancePayment = advancePayment;
        }

        public BigDecimal getNumInstallMonths() {
            return numInstallMonths;
        }

        public void setNumInstallMonths(BigDecimal numInstallMonths) {
            this.numInstallMonths = numInstallMonths;
        }

        public BigDecimal getCustomerInterestRate() {
            return customerInterestRate;
        }
        public BigDecimal getDealerInterestRate() {
            return dealerInterestRate;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.customerInterestRate = interestRate;
        }

        public BigDecimal getAdvancePayment() {
            return advancePayment;
        }

        public void setAdvancePayment(BigDecimal advancePayment) {
            this.advancePayment = advancePayment;
        }

        @Override
        public String toString() {
            return "LeasingData{" +
                    "numInstallMonths=" + numInstallMonths +
                    ", customerInterestRate=" + customerInterestRate +
                    ", dealerInterestRate=" + dealerInterestRate +
                    ", advancePayment=" + advancePayment +
                    '}';
        }
    }

    static class CarData {
        BigDecimal totalValue = null;
        BigDecimal residualValue = null;

        private CarData(){};

        public CarData(BigDecimal totalValue, BigDecimal residualValue, boolean isCarNew) {
            this.totalValue = totalValue;
            this.residualValue = residualValue;
            this.isCarNew = isCarNew;
        }

        public boolean isCarNew() {
            return isCarNew;
        }

        public void setCarNew(boolean carNew) {
            isCarNew = carNew;
        }

        public BigDecimal getTotalValue() {
            return totalValue;
        }

        public void setTotalValue(BigDecimal totalValue) {
            this.totalValue = totalValue;
        }

        public BigDecimal getResidualValue() {
            return residualValue;
        }

        public void setResidualValue(BigDecimal residualValue) {
            this.residualValue = residualValue;
        }

        boolean isCarNew = true;

        @Override
        public String toString() {
            return "CarData{" +
                    "totalValue=" + totalValue +
                    ", residualValue=" + residualValue +
                    ", isCarNew=" + isCarNew +
                    '}';
        }
    }

    //  interestCoeff = (1+interestRate) power numInstallMonths  = PowerFinder(1+interestRate, numInstallMonths)

    // installment =  (totalValue - (residualValue / interestCoeff)) / ((1-(1/interestCoeff) / interestRate)

    public static void main(String[] args) {
        TestLeasingCalculator leasingCalculator = new TestLeasingCalculator();
        /*
        LeasingData leasingData = new LeasingData(new BigDecimal(24), new BigDecimal(10), new BigDecimal(10000));
        CarData carData = new CarData(new BigDecimal(80000), new BigDecimal(10000), true);
        leasingCalculator.println(carData, leasingData, leasingCalculator.calculateMonthlyRate(leasingData, carData));

        leasingData = new LeasingData(new BigDecimal(24), new BigDecimal(9), new BigDecimal(1000));
        carData = new CarData(new BigDecimal(3500), new BigDecimal(1000), true);
        leasingCalculator.println(carData, leasingData, leasingCalculator.calculateMonthlyRate(leasingData, carData));
        */

        BigDecimal price = new BigDecimal(75000);
        BigDecimal advance = new BigDecimal(15000);
        BigDecimal residual = new BigDecimal(16000);
        BigDecimal customerInterestRate = new BigDecimal(3.75);
        BigDecimal dealerInterestRate = new BigDecimal(4.75);
        BigDecimal [] periods = {new BigDecimal(30), new BigDecimal(36), new BigDecimal(42), new BigDecimal(48),
                new BigDecimal(54), new BigDecimal(60)};
        CarData carData = new CarData(price, residual, true);
        LeasingData leasingData = null;
        for(BigDecimal period : periods) {
            leasingData = new LeasingData(period, customerInterestRate, dealerInterestRate, advance);
            BigDecimal[] bigDecimals = leasingCalculator.calculateMonthlyRate(leasingData, carData);
            leasingCalculator.println(carData, leasingData, bigDecimals[0], bigDecimals[1], bigDecimals[2] , bigDecimals[3], "\n");
        }





    }

    public static void processCarAndLeasing() {

    }

    void println(Object ... lines) {
        for( Object obj : lines) {
            System.out.println(obj);
        }
    }
}


class PowerFinder {

    public static BigDecimal findPower(BigDecimal x, BigDecimal y) {
        if (y == BigDecimal.ZERO)
            return BigDecimal.ONE;
        return findPower(x, y.subtract(new BigDecimal(1))).multiply(x);
    }

    public static void main(String[] args) {
        //System.out.println("Power: " + findPower(2, 1));
    }
}