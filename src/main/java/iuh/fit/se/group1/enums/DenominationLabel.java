package iuh.fit.se.group1.enums;

public enum DenominationLabel {
    VND_1000(1000L),
    VND_2000(2000L),
    VND_5000(5000L),
    VND_10000(10000L),
    VND_20000(20000L),
    VND_50000(50000L),
    VND_100000(100000L),
    VND_200000(200000L),
    VND_500000(500000L);

    private long value;

    private DenominationLabel (Long value){
        this.value = value;
    }




}
