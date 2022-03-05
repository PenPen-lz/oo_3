package src;

public class StringRecord {
    private static String string;
    private int record;

    public StringRecord(String string) {
        record = 0;
        this.string = string;
    }

    public boolean startwith(String substring) {
        String subStr = string.substring(record);
        return subStr.startsWith(substring);
    }

    public boolean startwithDigit() {
        return Character.isDigit(string.charAt(record));
    }

    public Boolean end() {
        if (record == string.length()) {
            return true;
        } else {
            return false;
        }
    }

    public void moveRecord(int lenth) {
        record = record + lenth;
    }

    public void backRecord(int lenth) {
        record = record - lenth;
    }

    public String getSubstring() {
        return string.substring(record);
    }

    public void moveSpace() {
        while (startwith(" ") || startwith("\t")) {
            if (startwith(" ")) {
                moveRecord(" ".length());
            } else {
                moveRecord("\t".length());
            }
        }
    }

    public Boolean digitParse() throws RFormatException {
        if (startwith("+") || startwith("-")) {
            record++;
            if (startwithDigit()) {
                record--;
                return true;
            } else {
                throw new RFormatException();
            }
        } else if (startwithDigit()) {
            return true;
        } else {
            throw new RFormatException();
        }
    }

    public void featureParse(String string) throws RFormatException {
        if (startwith(string)) {
            moveRecord(string.length());
        } else {
            throw new RFormatException();
        }
    }
}
