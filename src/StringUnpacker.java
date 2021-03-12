import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUnpacker {
    private static final Pattern ALLOWED_SYMBOLS = Pattern.compile("[A-z0-9\\[\\]]*");
    private static final Pattern PACKED_BLOCK = Pattern.compile("(\\d+)\\[(\\w+)]");
    private static final Integer MULTIPLIER = 1;
    private static final Integer SEQUENCE = 2;
    private static final Integer MAX_VALUE_LENGTH = 9;

    public static String unpackString(String packed) {
        Matcher matcher = PACKED_BLOCK.matcher(packed);
        while (matcher.find()) {
            packed = packed.replace(matcher.group(0), getReplacement(matcher));
            matcher = PACKED_BLOCK.matcher(packed);
        }
        return packed;
    }

    private static String getReplacement(Matcher matcher) {
        StringBuilder result = new StringBuilder();
        int times = Integer.parseInt(matcher.group(MULTIPLIER));
        String sequence = matcher.group(SEQUENCE);
        for (int i = 0; i < times; i++) {
            result.append(sequence);
        }
        return result.toString();
    }

    public static boolean validate(String packed) {
        if (!ALLOWED_SYMBOLS.matcher(packed).matches()) {
            return false;
        }
        int braces = 0;
        boolean isPreviousDigit = false;
        boolean isPreviousOpenBrace = false;
        int digitCount = 0;
        for (Character current : packed.toCharArray()) {
            switch (current) {
                case '[':
                    braces++;
                    if (!isPreviousDigit) {
                        return false;
                    }
                    digitCount = 0;
                    isPreviousDigit = false;
                    isPreviousOpenBrace = true;
                    break;
                case ']':
                    braces--;
                    if (isPreviousDigit || braces < 0 || isPreviousOpenBrace) {
                        return false;
                    }
                    digitCount = 0;
                    isPreviousDigit = false;
                    isPreviousOpenBrace = false;
                    break;
                default:
                    if (Character.isDigit(current)) {
                        isPreviousDigit = true;
                        digitCount++;
                        if (digitCount > MAX_VALUE_LENGTH) {
                            return false;
                        }
                    } else {
                        if (isPreviousDigit) {
                            return false;
                        }
                        digitCount = 0;
                    }
                    isPreviousOpenBrace = false;
            }
        }
        return braces == 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (validate(input)) {
            System.out.println(unpackString(input));
        } else {
            System.out.println("Wrong input");
        }
    }
}
