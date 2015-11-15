package pl.edu.agh.util;

public enum MonthsEnum {

    stycznia("01"),
    lutego("02"),
    marca("03"),
    kwietnia("04"),
    maja("05"),
    czerwca("06"),
    lipca("07"),
    sierpnia("08"),
    września("09"),
    października("10"),
    listopada("11"),
    grudnia("12");

    private String month;

    MonthsEnum(String month) {
        this.month = month;
    }

    public static String getMonthIndex(String month) {
        for (MonthsEnum monthsEnum : MonthsEnum.values()) {
            if (monthsEnum.name().equals(month)) {
                return monthsEnum.month;
            }
        }
        return null;
    }
}
