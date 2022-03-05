package src;

import java.math.BigInteger;

public class Factor {
    private BigInteger index;
    private Type type;
    private BigInteger coeff;
    private Expression expressionfactor;

    public void setCoeff(BigInteger coeff) {
        this.coeff = coeff;
    }

    public void resetCoeff(BigInteger extraCoeff) {
        this.coeff = this.coeff.multiply(extraCoeff);
    }

    public void resetIndex(BigInteger extraIndex) {
        this.index = this.index.add(extraIndex);
    }

    public Type getType() {
        return this.type;
    }

    public BigInteger getCoeff() {
        return this.coeff;
    }

    public BigInteger getIndex() {
        return this.index;
    }

    public Expression getExpressionfactor() {
        return this.expressionfactor;
    }

    public boolean coeffEquals0() {
        return this.coeff.compareTo(new BigInteger("0")) == 0;
    }

    public boolean indexEquals0() {
        return this.index.compareTo(new BigInteger("0")) == 0;
    }

    public boolean coeffEquals1() {
        return this.coeff.compareTo(new BigInteger("1")) == 0;
    }

    public boolean indexEquals1() {
        return this.index.compareTo(new BigInteger("1")) == 0;
    }

    public Factor() {
    }

    public Factor(BigInteger index, Type type, BigInteger coeff) {
        this.index = index;
        this.type = type;
        this.coeff = coeff;
        this.expressionfactor = null;
    }

    public Factor(Expression expressionfactor) {
        this.type = Type.Express;
        this.expressionfactor = expressionfactor;
        this.index = new BigInteger("1");
        this.coeff = new BigInteger("1");
    }

    public String toString() {
        String string = "";
        return string;
    }

    public String tostring() {
        return this.coeffEquals1() ? this.toString() : this.coeff + "*" + this.toString();
    }

    public Term diff() {
        return new Term();
    }

    public Factor clones() {
        return new Factor(index, type, coeff);
    }

    public Boolean equals(Factor factor) {
        if (getType().equals(Type.Express) || factor.getType().equals(Type.Express)) {
            return false;
        } else {
            return true;
        }
    }
}
