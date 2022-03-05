package src;

import java.math.BigInteger;

public class SinFactor extends Factor {

    private Factor canfactor;

    private BigInteger index;
    private Type type;
    private BigInteger coeff;
    private Expression expressionfactor;

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

    public SinFactor(BigInteger index, Type type, BigInteger coeff, Factor canfactor) {
        this.index = index;
        this.type = type;
        this.coeff = coeff;
        expressionfactor = null;
        this.canfactor = canfactor;
    }

    @Override
    public Term diff() {
        Factor sinPart = new SinFactor(
                index.subtract(new BigInteger("1")), Type.TrigoSin,
                coeff.multiply(index), canfactor);
        Factor cosPart = new CosFactor(new BigInteger("1"),
                Type.TrigoCos, new BigInteger("1"), canfactor);
        if (cosPart.indexEquals0()) {
            cosPart = new ExpoFactor(cosPart.getIndex(), Type.Expo, cosPart.getCoeff());
        }
        if (sinPart.indexEquals0()) {
            sinPart = new ExpoFactor(sinPart.getIndex(), Type.Expo, sinPart.getCoeff());
        }
        Term canPart = canfactor.diff();
        Term term = new Term(cosPart);
        term.addFactor(sinPart);
        term.addTerm(canPart);
        return term;
    }

    public Factor clones() {
        return new SinFactor(index, Type.TrigoSin, coeff, canfactor);
    }

    public String toString() {
        if (coeffEquals0()) {
            return "0";
        }
        if (indexEquals0()) {
            return "1";
        } else {
            String can = canfactor.toString();
            if (can.equals("0") || canfactor.coeffEquals0()) {
                can = "(0)";
            } else if (canfactor.getType().equals(Type.Express)) {
                can = "(" + can + ")";
            } else if (can.equals("1")) {
                can = "(" + canfactor.getCoeff() + ")";
            } else if (canfactor.coeffEquals1()) {
                can = "(" + can + ")";
            } else {
                can = "(" + canfactor.getCoeff() + "*" + can + ")";
            }
            if (indexEquals1()) {
                return ("sin(" + can + ")");
            } else {
                return ("sin(" + can + ")**" + index);
            }

        }
    }

    public Factor getCanfactor() {
        return canfactor;
    }

    public Boolean equals(Factor factor) {
        if (!factor.getType().equals(Type.TrigoSin)) {
            return false;
        } else {
            SinFactor sinfactor = (SinFactor) factor;
            Factor sincanfactor = sinfactor.getCanfactor();
            if (sincanfactor.getIndex().compareTo(canfactor.getIndex()) == 0
                    && sincanfactor.getCoeff().compareTo(canfactor.getCoeff()) == 0
                    && sincanfactor.equals(canfactor)) {
                return true;
            } else {
                return false;
            }

        }
    }
}
