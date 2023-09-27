package s21.azathotp.model.credit;

import com.sun.javafx.binding.StringFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.model.helpers.StringChecker;

import java.time.LocalDate;
import java.time.YearMonth;

public class CreditModel {
    private final Calculator CALCULATOR;
    private final ObservableList<String> MONTHLY_PAYMENT_DATA = FXCollections.observableArrayList();

    public CreditModel(Calculator CALCULATOR) {
        this.CALCULATOR = CALCULATOR;
    }

    public ObservableList<String> getMonthlyPaymentData() {
        return MONTHLY_PAYMENT_DATA;
    }

    public CreditData calculateAnnuity(String creditAmount, String creditTerm, String interestRate) throws ExpressionError {
        validateInputData(creditAmount, creditTerm, interestRate);
        MONTHLY_PAYMENT_DATA.clear();

        double ratePerMonth = CALCULATOR.calculate(interestRate + "/(100 * 12)", "");
        double monthlyPayment = CALCULATOR.calculate(
                String.format("%s*(%s/(1-(1+%s)^-%s))", creditAmount, ratePerMonth, ratePerMonth, creditTerm), "");
        double overpayment = CALCULATOR.calculate(
                String.format("%s * %s - %s", monthlyPayment, creditTerm, creditAmount), "");
        double totalPayment = CALCULATOR.calculate(String.format("%s + %s", overpayment, creditAmount), "");

        monthlyPayment = Math.round(monthlyPayment);
        overpayment = Math.round(overpayment);
        totalPayment = Math.round(totalPayment);

        MONTHLY_PAYMENT_DATA.add(String.format("For every month: %s", monthlyPayment));

        return new CreditData(Double.toString(overpayment), Double.toString(totalPayment));
    }

    public CreditData calculateDifferentiated(String creditAmount, String creditTerm, String interestRate) throws ExpressionError {
        validateInputData(creditAmount, creditTerm, interestRate);
        MONTHLY_PAYMENT_DATA.clear();

        double monthlyPrincipalPayment = CALCULATOR.calculate(
                StringFormatter.format("%s / %s", creditAmount, creditTerm).getValue(),
                "");
        double creditAmountInDouble = Double.parseDouble(creditAmount);

        int year = LocalDate.now().getYear();
        int monthIndex = LocalDate.now().getMonthValue() + 1;
        double totalPayment = 0;

        for (int i = 1; i <= Double.parseDouble(creditTerm); i++) {
            int monthLength = YearMonth.of(year, monthIndex).lengthOfMonth();
            int yearLength = YearMonth.of(year, monthIndex).lengthOfYear();

            monthIndex++;

            if (monthIndex % 13 == 0) {
                year += monthIndex / 12;
                monthIndex = 1;
            }

            double monthlyAmountOfAccruedInterest = CALCULATOR.calculate(
                    String.format("%s * %s / 100 * %s / %s",
                            creditAmountInDouble - monthlyPrincipalPayment * (i - 1),
                            interestRate,
                            monthLength,
                            yearLength),
                    "");

            double monthlyPayment = CALCULATOR.calculate(
                    String.format("%s+%s", monthlyAmountOfAccruedInterest, monthlyPrincipalPayment),
                    "");
            totalPayment += monthlyPayment;
            MONTHLY_PAYMENT_DATA.add(String.format("Month %s: %s", i, monthlyPayment));
        }

        double overpayment = CALCULATOR.calculate(String.format("%s-%s", totalPayment, creditAmount), "");
        return new CreditData(Double.toString(overpayment), Double.toString(totalPayment));
    }

    private void validateInputData(String creditAmount, String creditTerm, String interestRate) throws ExpressionError {
        if (!StringChecker.isNum(creditAmount) || !StringChecker.isNum(creditTerm) || !StringChecker.isNum(interestRate)) {
            throw new ExpressionError("input data must be numeric values");
        } else if (Double.parseDouble(creditAmount) <= 0) {
            throw new ExpressionError("credit amount must be greater than zero");
        } else if (Double.parseDouble(creditTerm) <= 0) {
            throw new ExpressionError("credit term must be greater than zero");
        } else if (Double.parseDouble(interestRate) <= 0) {
            throw new ExpressionError("interest rate must be greater than zero");
        }
    }
}
