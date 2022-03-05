package src;

import java.math.BigInteger;

public class ExpoFactor extends Factor {

    private BigInteger index;
    private Type type;
    private BigInteger coeff;
    private Expression expressionfactor;

    public ExpoFactor(BigInteger index, Type type, BigInteger coeff) {
        this.index = index;
        this.type = type;
        this.coeff = coeff;
        expressionfactor = null;
    }

    public void setCoeff(BigInteger coeff) {
        this.coeff = coeff;
    }

    public void resetCoeff(BigInteger extraCoeff) {
        coeff = coeff.multiply(extraCoeff);
    }

    public void resetIndex(BigInteger extraIndex) {
        index = index.add(extraIndex);
    }

    public Type getType() {
        return type;
    }

    public BigInteger getCoeff() {
        return coeff;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Expression getExpressionfactor() {
        return expressionfactor;
    }

    public boolean coeffEquals0() {
        return (coeff.compareTo(new BigInteger("0")) == 0);
    }

    public boolean indexEquals0() {
        return (index.compareTo(new BigInteger("0")) == 0);
    }

    public boolean coeffEquals1() {
        return (coeff.compareTo(new BigInteger("1")) == 0);
    }

    public boolean indexEquals1() {
        return (index.compareTo(new BigInteger("1")) == 0);
    }

    public Term diff() {
        Factor factor;
        if (coeff.multiply(index).compareTo(new BigInteger("0")) == 0) {
            factor = new ExpoFactor(new BigInteger("0"), Type.Expo, new BigInteger("0"));
        } else {
            factor = new ExpoFactor(
                    index.subtract(new BigInteger("1")), Type.Expo, coeff.multiply(index));
        }
        return new Term(factor);
    }

    public Factor clones() {
        return new ExpoFactor(index, Type.Expo, coeff);
    }

    public String toString() {
        if (coeffEquals0()) {
            return "0";
        }
        if (indexEquals0()) {
            return "1";
        } else if (indexEquals1()) {
            return ("x");
        } else {
            return ("x" + "**" + index);
        }
    }

    public Boolean equals(Factor factor) {
        if (factor.getType().equals(Type.Expo)) {
            return true;
        }
        return false;
    }
}
