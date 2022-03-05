package src;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {

    private ArrayList<Factor> factors;
    private BigInteger coeff;

    public BigInteger getCoeff() {
        return coeff;
    }

    public Term() {
        factors = new ArrayList<>();
        coeff = new BigInteger("1");
    }

    public Term(Factor factor) {
        factors = new ArrayList<>();
        coeff = factor.getCoeff();
        factor.setCoeff(new BigInteger("1"));
        factors.add(factor);
    }

    public void addFactor(Factor factor) {
        coeff = coeff.multiply(factor.getCoeff());
        if (factor.getType().compareTo(Type.Express) != 0) {
            for (Factor item : this.factors) {
                if (item.equals(factor)) {
                    item.resetIndex(factor.getIndex());
                    return;
                }
            }
        }
        factor.setCoeff(new BigInteger("1"));
        factors.add(factor);
    }

    public void addTerm(Term term) {
        coeff = coeff.multiply(term.getCoeff());

        int i;
        for (i = 0; i < term.getfactorsSize(); i++) {
            addFactor(term.getFactorByIndex(i));
        }
    }

    public Factor getFactorByIndex(int i) {
        return factors.get(i);
    }

    public int getfactorsSize() {
        return factors.size();
    }

    public String toString() {
        if (coeff.compareTo(new BigInteger("0")) == 0) {
            return "0";
        }
        String string = "";
        int i = 0;
        for (Factor factor : factors) {
            if (factor.getType().compareTo(Type.Express) == 0) {
                if (factors.size() == 1 && coeff.equals(1)) {
                    return factor.getExpressionfactor().toString();
                }
            }
            String factorString = factor.toString();

            if (factorString.equals("0")) {
                string = "0";
                return string;
            } else if (!factorString.equals("1")) {
                string = string + "*" + factorString;
            } else if (factorString.equals("1")) {
                i++;
            }
        }

        if (string.length() != 0 && string.charAt(0) == '*') {
            if (coeff.compareTo(new BigInteger("1")) == 0) {
                string = string.substring(1);
            } else {
                string = coeff + string;
            }
        } else if (i > 0 && string.length() == 0) {
            string = coeff + "";
        }
        return string;
    }

    public Expression diff() {
        int i = 0;
        int num = this.factors.size();
        Expression expression = new Expression();
        for (Factor factor : factors) {
            if (i == num) {
                break;
            }
            Term thisfactordiff = factor.diff();

            if (!thisfactordiff.getCoeff().equals(0)) {
                thisfactordiff.addTerm(subdiff(i));
                thisfactordiff.resetcoeff(coeff);
                thisfactordiff = thisfactordiff.simple();
                expression.addTerm(thisfactordiff);
            } else {
                thisfactordiff = new Term(
                        new ExpoFactor(new BigInteger("0"), Type.Expo, new BigInteger("0")));
                expression.addTerm(thisfactordiff);
            }
            i++;
        }
        return expression;
    }

    public Term simple() {
        Term term = new Term();
        term.setcoeff(coeff);
        for (Factor factor : factors) {
            if (factor.getType().equals(Type.Express)) {
                if (factor.getExpressionfactor().numValidTerm() == 1) {
                    term.addTerm(factor.getExpressionfactor().validTerm().clones().simple());
                } else {
                    term.addFactor(new ExpressFactor(factor.getExpressionfactor()));
                }
            } else {
                term.addFactor(factor);
            }
        }
        return term;
    }

    private void setcoeff(BigInteger coeff) {
        this.coeff = coeff;
    }

    public void resetcoeff(BigInteger coeff) {
        this.coeff = coeff.multiply(this.coeff);
    }

    public Term subdiff(int num) {
        int i = 0;
        Term term = new Term();
        for (Factor factor : this.factors) {
            if (num != i) {
                term.addFactor(factor);
            }
            i++;
        }
        return term;
    }

    public boolean containsExpress() {
        for (Factor factor : factors) {
            if (factor.getType().compareTo(Type.Express) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsExpo() {
        for (Factor factor : factors) {
            if (factor.getType().compareTo(Type.Expo) == 0) {
                if (!factor.indexEquals0()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsCos() {
        for (Factor factor : factors) {
            if (factor.getType().compareTo(Type.TrigoCos) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsSin() {
        for (Factor factor : factors) {
            if (factor.getType().compareTo(Type.TrigoSin) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Term extra) {
        if (extra.containsExpress() || containsExpress()) {
            return false;
        }
        if (extra.containsSin() != containsSin() || extra.containsCos() != containsCos()
                || extra.containsExpo() != containsExpo()) {
            return false;
        }
        boolean help;
        for (Factor factor : factors) {
            help = true;
            int i;
            int num = extra.getfactorsSize();
            for (i = 0; i < num; i++) {

                Factor factor1 = extra.getFactorByIndex(i);
                if (factor.getIndex().equals(factor1.getIndex())
                        && factor.equals(factor1)) {
                    help = false;
                    break;
                }
            }
            if (help) {
                return false;
            }
        }
        return true;
    }

    public void reset(Term extraTerm) {
        coeff = coeff.add(extraTerm.getCoeff());
    }

    public Term clones() {
        Term term = new Term();
        term.setcoeff(coeff);
        for (Factor factor : factors) {
            term.addFactor(factor.clones());
        }
        return term;
    }

}

