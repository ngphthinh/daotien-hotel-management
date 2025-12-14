package iuh.fit.se.group1.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PaymentSuggestionService {

    public List<Integer> getPaymentOptions(int totalThousand) {
        Set<Integer> options = new TreeSet<>();

        options.add(totalThousand);

        // Xác định các mệnh giá phù hợp
        int[] denominations = getDenominations(totalThousand);

        // Tìm mệnh giá lớn nhất (major milestone)
        int majorDenom = denominations[denominations.length - 1];
        int nextMajor = roundUp(totalThousand, majorDenom);

        // Khoảng cách đến mốc lớn
        int distanceToMajor = nextMajor - totalThousand;

        // Nếu cách mốc lớn từ 3-30 đơn vị, thêm các bước nhỏ
        if (distanceToMajor >= 3 && distanceToMajor <= 30) {
            // Thêm các mệnh giá nhỏ: 1, 2, 5
            int[] smallDenoms = {1, 2, 5};
            for (int small : smallDenoms) {
                int rounded = roundUp(totalThousand, small);
                if (rounded > totalThousand && rounded < nextMajor) {
                    options.add(rounded);
                }
            }
            // Thêm mốc 10 hoặc 20 nếu phù hợp
            for (int medium : new int[]{10, 20}) {
                int rounded = roundUp(totalThousand, medium);
                if (rounded > totalThousand && rounded < nextMajor) {
                    options.add(rounded);
                }
            }
        }

        // Tìm tổ hợp tiền gần nhất có thể đưa
        for (int denom : denominations) {
            int rounded = roundUp(totalThousand, denom);
            if (rounded > totalThousand && rounded <= totalThousand * 1.5) {
                options.add(rounded);
            }
        }

        return options.stream().limit(6).collect(Collectors.toList());
    }

    private int[] getDenominations(int total) {
        if (total <= 10) return new int[]{1, 2, 5, 10};
        if (total <= 50) return new int[]{5, 10, 20, 50};
        if (total <= 100) return new int[]{10, 20, 50, 100};
        if (total <= 500) return new int[]{20, 50, 100, 200, 500};
        if (total <= 1000) return new int[]{50, 100, 200, 500, 1000};
        return new int[]{100, 200, 500, 1000, 2000, 5000};
    }

    private int roundUp(int value, int unit) {
        return ((value + unit - 1) / unit) * unit;
    }
}