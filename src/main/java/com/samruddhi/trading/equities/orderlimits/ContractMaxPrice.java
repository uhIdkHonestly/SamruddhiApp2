package com.samruddhi.trading.equities.orderlimits;

public class ContractMaxPrice {

    /** Do may want to have logic from DB or allowed contract price from Database or rule engine
     *
     * @param ticker
     * @param midOptionContractPrice
     * @param stockPrice
     * @return
     */
    public static boolean validateMaxContractPriceByTicker(String ticker, double midOptionContractPrice, double stockPrice) {
        double allowedMaxContractPrice = 0;
      if (stockPrice < 30) {
          allowedMaxContractPrice = 1.0;
        } else if (stockPrice < 120) {
          allowedMaxContractPrice =  5;
        } else if (stockPrice < 200) {
          allowedMaxContractPrice = 7;
        } else if (stockPrice < 500) {
          allowedMaxContractPrice = 9;
        } else {
          allowedMaxContractPrice = 10;
        }

        if(midOptionContractPrice > allowedMaxContractPrice)
            return false;
        return true;
    }
}
