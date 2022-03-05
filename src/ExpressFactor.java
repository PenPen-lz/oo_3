package src;

import java.math.BigInteger;

public class ExpressFactor extends Factor {

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

    public ExpressFactor(Expression expressionfactor) {
        type = Type.Express;
        this.expressionfactor = expressionfactor;
        index = new BigInteger("1");
        coeff = new BigInteger("1");
    }

    @Override
    public Term diff() {
        Expression expression = expressionfactor.diff();
        Factor factor = new ExpressFactor(expression);
        return new Term(factor);
    }

    @Override
    public String toString() {
        String str = expressionfactor.toString();
        if (str.equals("0")) {
            return "0";
        }
        return ("(" + str + ")");
    }

    public Factor clones() {
        return new ExpressFactor(expressionfactor);
    }

    public Boolean equals(Factor factor) {
        return false;
    }
}
