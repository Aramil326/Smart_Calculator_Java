package s21.azathotp.model.credit;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.exceptions.ExpressionError;

import java.util.Arrays;
import java.util.List;

public class CreditModelTest {
    private final CreditModel creditModel = new CreditModel(new Calculator());
    private final double EPS = 1e-7;

    @Test
    void Credit_Calculator_Annuity_Test_1() {
        CreditData data = creditModel.calculateAnnuity("300000", "6", "20");
        ObservableList<String> monthlyPaymentData = creditModel.getMonthlyPaymentData();

        String monthlyPaymentExpected = "For every month: 52957.0";
        double overpaymentExpected = 17741.0;
        double totalPaymentExpected = 317741.0;

        Assertions.assertEquals(monthlyPaymentExpected, monthlyPaymentData.get(0));
        Assertions.assertEquals(overpaymentExpected, Double.parseDouble(data.getOverpayment()), EPS);
        Assertions.assertEquals(totalPaymentExpected, Double.parseDouble(data.getTotalPayment()), EPS);
    }

    @Test
    void Credit_Calculator_Annuity_Test_1_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("-300000", "6", "20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_2_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "-6", "20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_3_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "6", "-20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_4_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("0", "6", "-20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_5_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "0", "-20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_6_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "6", "0"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_7_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000fas", "6", "20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_8_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "a", "20"));
    }

    @Test
    void Credit_Calculator_Annuity_Test_9_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateAnnuity("300000", "6", "asdf"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_1() {
        CreditData data = creditModel.calculateDifferentiated("300000", "6", "20");
        ObservableList<String> monthlyPaymentData = creditModel.getMonthlyPaymentData();

        List<String> monthlyPaymentExpected = Arrays.asList("Month 1: 55095.890411", "Month 2: 54109.5890411", "Month 3: 53397.260274", "Month 4: 52540.9836066", "Month 5: 51584.6994536", "Month 6: 50846.9945356");
        double overpaymentExpected = 17575.417322;
        double totalPaymentExpected = 317575.4173219;

        for (int i = 0; i < monthlyPaymentData.size() && i < monthlyPaymentExpected.size(); i++) {
            Assertions.assertEquals(monthlyPaymentExpected.get(i), monthlyPaymentData.get(i));
        }
        Assertions.assertEquals(overpaymentExpected, Double.parseDouble(data.getOverpayment()), EPS);
        Assertions.assertEquals(totalPaymentExpected, Double.parseDouble(data.getTotalPayment()), EPS);
    }

    @Test
    void Credit_Calculator_Differentiated_Test_1_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("-300000", "6", "20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_2_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "-6", "20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_3_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "6", "-20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_4_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("0", "6", "-20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_5_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "0", "-20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_6_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "6", "0"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_7_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000fas", "6", "20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_8_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "a", "20"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_9_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("300000", "6", "asdf"));
    }

    @Test
    void Credit_Calculator_Differentiated_Test_10_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> creditModel.calculateDifferentiated("", "6", "asdf"));
    }
}
