package de.immomio.constants.property;

/**
 * @author Maik Kingma
 */

public enum EnergyClassType {

    A_P("A+"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    H("H");

    private String symbol;

    EnergyClassType(String symbol) {
        this.symbol = symbol;
    }

    public static EnergyClassType bySymbol(String symbol) {
        for (EnergyClassType energyClassType : EnergyClassType.values()) {
            if (energyClassType.symbol.equals(symbol)) {
                return energyClassType;
            }
        }
        return null;
    }

    public String getSymbol() {
        return symbol;
    }

}
