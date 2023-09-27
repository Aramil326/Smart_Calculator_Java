package s21.azathotp.model.credit;

public class CreditData {
    private final String OVERPAYMENT;
    private final String TOTAL_PAYMENT;

    public CreditData(String overpayment, String totalPayment) {
        this.OVERPAYMENT = overpayment;
        this.TOTAL_PAYMENT = totalPayment;
    }

    public String getOverpayment() {
        return OVERPAYMENT;
    }

    public String getTotalPayment() {
        return TOTAL_PAYMENT;
    }
}
