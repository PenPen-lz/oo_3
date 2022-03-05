package src;

import java.util.ArrayList;

public class Expression {
    private ArrayList<Term> terms;

    public Expression() {
        terms = new ArrayList<>();
    }

    public Expression(Term term) {
        terms = new ArrayList<>();
        terms.add(term);
    }

    public void addTerm(Term term) {
        for (Term item : terms) {
            if (item.equals(term)) {
                item.reset(term);
                return;
            }
        }
        terms.add(term);
    }

    public void addExpress(Expression expression) {
        int i = 0;
        int num = expression.gettermsSize();
        for (i = 0; i < num; i++) {
            addTerm(expression.getTermByIndex(i));
        }
    }

    public Term getTermByIndex(int i) {
        return terms.get(i);
    }

    public int gettermsSize() {
        return terms.size();
    }

    public String toString() {
        String string = "";
        int i = 0;
        for (Term item : terms) {
            String tmp = item.toString();
            if (tmp.charAt(0) == '-') {
                string = string + tmp;
            } else if (!tmp.equals("0")) {
                string = string + "+" + tmp;
            } else if (tmp.equals("0")) {
                i++;
            }
        }
        if (string.length() == 0 || string.length() == 1 && string.charAt(0) == '+') {
            string = "0";
        } else if (string.length() != 0 && string.charAt(0) == '+') {
            string = string.substring(1);
        }
        return string;

    }

    public Expression diff() {
        Expression diffExpression = new Expression();
        for (Term term : terms) {
            diffExpression.addExpress(term.diff());
        }
        return diffExpression;
    }

    private int getNumOfTerm() {
        return terms.size();
    }

    public int numValidTerm() {
        int i = 0;
        for (Term term : terms) {
            if (!term.getCoeff().equals(0)) {           //通过减少toString()的调用，减少TLE
                i++;
            }
        }
        return i;
    }

    public Term validTerm() {
        for (Term term : terms) {
            if (!term.getCoeff().equals(0)) {          //通过减少toString()的调用，减少TLE
                return term;
            }
        }
        System.out.println("it can only be used after ensure numValinTerm is 1");
        return null;
    }

    public Expression clones() {
        Expression expression = new Expression();
        for (Term term : terms) {
            expression.addTerm(term.clones());
        }
        return expression;
    }
}
