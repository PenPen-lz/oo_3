package src;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factory {

    public static Expression createExpression(StringRecord strRecord) throws RFormatException {
        boolean navi = false;
        strRecord.moveSpace();
        if (strRecord.end()) {
            throw new RFormatException();
        }
        if (strRecord.startwith("-")) {
            navi = true;
            strRecord.moveRecord(1);
        } else if (strRecord.startwith("+")) {
            strRecord.moveRecord(1);
        }

        strRecord.moveSpace();
        Term term = createTerm(strRecord, navi);
        strRecord.moveSpace();

        Expression expression = new Expression(term);
        if (!strRecord.end() &&
                !(strRecord.startwith("+")
                        || strRecord.startwith("-")
                        || strRecord.startwith(")"))) {
            throw new RFormatException();
        }

        while (strRecord.startwith("+") || strRecord.startwith("-")) {
            navi = strRecord.startwith("-");
            strRecord.moveRecord(1);
            strRecord.moveSpace();
            term = createTerm(strRecord, navi);
            expression.addTerm(term);
            strRecord.moveSpace();


            if (!strRecord.end() &&
                    !(strRecord.startwith("+")
                            || strRecord.startwith("-")
                            || strRecord.startwith(")"))) {
                throw new RFormatException();
            }


        }
        return expression;
    }

    private static Term createTerm(StringRecord strRecord, boolean termNavi)
            throws RFormatException {
        boolean factorNavi = false;
        if (strRecord.startwith("-")) {
            factorNavi = true;
            strRecord.moveRecord(1);
            strRecord.moveSpace();
        } else if (strRecord.startwith("+")) {
            strRecord.moveRecord(1);
            strRecord.moveSpace();
        }

        Factor factor = createFactor(strRecord, factorNavi);
        if (termNavi) {
            factor.resetCoeff(new BigInteger("-1"));
        }
        Term term = new Term(factor);
        strRecord.moveSpace();

        if (!strRecord.end()
                && !(strRecord.startwith("*")
                || strRecord.startwith("-")
                || strRecord.startwith("+")
                || strRecord.startwith(")"))) {
            throw new RFormatException();
        }


        while (strRecord.startwith("*")) {
            factorNavi = false;
            strRecord.moveRecord(1);
            strRecord.moveSpace();
            factorNavi = strRecord.startwith("-");
            if (strRecord.startwith("+") || strRecord.startwith("-")) {
                strRecord.moveRecord(1);
                if (strRecord.startwith("-")) {
                    factorNavi = true;
                }
            }
            factor = createFactor(strRecord, factorNavi);
            term.addFactor(factor);
            strRecord.moveSpace();

            if (!strRecord.end()
                    && !(strRecord.startwith("*")
                    || strRecord.startwith("-")
                    || strRecord.startwith("+")
                    || strRecord.startwith(")"))) {
                throw new RFormatException();
            }

        }
        term = term.simple();
        return term;
    }

    private static Factor createFactor(StringRecord strRecord,
                                       boolean factorNavi) throws RFormatException {
        Factor factor;
        if (strRecord.startwith("+") || strRecord.startwith("-")
                || strRecord.startwithDigit()) {
            factor = createConst(strRecord);
        } else if (strRecord.startwith("x")) {
            factor = createExpo(strRecord);
        } else if (strRecord.startwith("sin")) {
            factor = createTrigo(strRecord, "sin");
        } else if (strRecord.startwith("cos")) {
            factor = createTrigo(strRecord, "cos");
        } else if (strRecord.startwith("(")) {
            factor = createExpress(strRecord);
        } else {
            throw new RFormatException();
        }
        if (factor != null && factorNavi) {
            factor.resetCoeff(new BigInteger("-1"));
        }
        return factor;
    }

    private static Factor createExpress(StringRecord strRecord) throws RFormatException {
        strRecord.moveRecord(1);
        Factor factor = new ExpressFactor(createExpression(strRecord));
        strRecord.featureParse(")");
        return factor;
    }

    private static Factor createTrigo(StringRecord strRecord, String trigo)
            throws RFormatException {
        BigInteger index;

        strRecord.moveRecord(3);
        strRecord.moveSpace();
        strRecord.featureParse("(");
        strRecord.moveSpace();

        Factor trigofac = createFactor(strRecord, false);

        strRecord.moveSpace();
        strRecord.featureParse(")");
        strRecord.moveSpace();


        if (strRecord.startwith("**")) {
            strRecord.moveRecord(2);
            strRecord.moveSpace();
            strRecord.digitParse();

            String pattern = "[\\+-]?\\d+";
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(strRecord.getSubstring());
            if (matcher.find()) {
                String indextemp = matcher.group();
                index = new BigInteger(indextemp);
                checkIndex(index);
                strRecord.moveRecord(indextemp.length());
            } else {
                throw new RFormatException();
            }
        } else {
            index = new BigInteger("1");
        }

        Type type = trigo.equals("cos") ? Type.TrigoCos : Type.TrigoSin;
        if (type.equals(Type.TrigoCos)) {
            return new CosFactor(index, Type.TrigoCos, new BigInteger("1"), trigofac);
        } else {
            return new SinFactor(index, Type.TrigoSin, new BigInteger("1"), trigofac);
        }
    }

    private static Factor createConst(StringRecord strRecord) throws RFormatException {
        String pattern = "[\\+-]?\\d+";
        strRecord.digitParse();
        //如果是+-则考虑要判断接下来的是否是digit
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(strRecord.getSubstring());
        Factor factor;
        String string;
        if (matcher.find()) {
            string = matcher.group();
            strRecord.moveRecord(string.length());
            factor = new ExpoFactor(new BigInteger("0"), Type.Expo, new BigInteger(string));
        } else {
            throw new RFormatException();
        }
        return factor;
    }

    private static Factor createExpo(StringRecord strRecord) throws RFormatException {
        BigInteger index;
        String pattern = "[\\+-]?\\d+";
        Pattern p = Pattern.compile(pattern);
        String string;
        Factor factor;


        strRecord.moveRecord(1);
        strRecord.moveSpace();

        if (strRecord.startwith("**")) {
            strRecord.moveRecord(2);
            strRecord.moveSpace();

            if (strRecord.digitParse()) {

                Matcher matcher = p.matcher(strRecord.getSubstring());

                if (matcher.find()) {
                    string = matcher.group();
                } else {
                    throw new RFormatException();
                }
                strRecord.moveRecord(string.length());
                BigInteger indextmp = new BigInteger(string);
                checkIndex(indextmp);
                factor = new ExpoFactor(new BigInteger(string), Type.Expo, new BigInteger("1"));
                return factor;
            } else {
                throw new RFormatException();
            }
        } else {
            factor = new ExpoFactor(new BigInteger("1"), Type.Expo, new BigInteger("1"));
            return factor;
        }
    }

    public static void checkIndex(BigInteger index) throws RFormatException {
        if (index.abs().compareTo(new BigInteger("50")) > 0) {
            throw new RFormatException();
        }
    }
}
