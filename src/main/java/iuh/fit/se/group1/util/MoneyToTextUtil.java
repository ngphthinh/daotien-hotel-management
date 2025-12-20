package iuh.fit.se.group1.util;

public class MoneyToTextUtil {

    private static final String[] NUMBER = {
            "không", "một", "hai", "ba", "bốn",
            "năm", "sáu", "bảy", "tám", "chín"
    };

    private static final String[] UNIT = {
            "", "nghìn", "triệu", "tỷ"
    };

    public static String convert(double money) {
        if (money == 0) {
            return "Không đồng";
        }

        StringBuilder result = new StringBuilder();
        int unitIndex = 0;

        while (money > 0) {
            int block = (int) (money % 1000);
            if (block != 0) {
                String blockText = readBlock(block);
                result.insert(0, blockText + " " + UNIT[unitIndex] + " ");
            }
            money /= 1000;
            unitIndex++;
        }

        String text = result.toString().trim();
        text = Character.toUpperCase(text.charAt(0)) + text.substring(1);

        return text + " đồng";
    }

    private static String readBlock(int number) {
        int hundred = number / 100;
        int ten = (number % 100) / 10;
        int unit = number % 10;

        StringBuilder sb = new StringBuilder();

        if (hundred > 0) {
            sb.append(NUMBER[hundred]).append(" trăm ");
        }

        if (ten > 1) {
            sb.append(NUMBER[ten]).append(" mươi ");
            if (unit == 1) sb.append("mốt ");
            else if (unit == 5) sb.append("lăm ");
            else if (unit > 0) sb.append(NUMBER[unit]).append(" ");
        } else if (ten == 1) {
            sb.append("mười ");
            if (unit == 5) sb.append("lăm ");
            else if (unit > 0) sb.append(NUMBER[unit]).append(" ");
        } else if (ten == 0 && unit > 0 && hundred > 0) {
            sb.append("lẻ ").append(NUMBER[unit]).append(" ");
        } else if (unit > 0) {
            sb.append(NUMBER[unit]).append(" ");
        }

        return sb.toString().trim();
    }
}
