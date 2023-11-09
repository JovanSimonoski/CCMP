import java.io.Serializable;
import java.math.BigInteger;

public class SecurityParameters implements Serializable {
    private final String key;
    private final BigInteger counter;
    private final String IV;

    public SecurityParameters(String key, BigInteger counter, String IV) {
        this.key = key;
        this.counter = counter;
        this.IV = IV;
    }

    public String getKey() {
        return key;
    }

    public BigInteger getCounter() {
        return counter;
    }

    public String getIV() {
        return IV;
    }
}
