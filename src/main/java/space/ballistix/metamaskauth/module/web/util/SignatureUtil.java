package space.ballistix.metamaskauth.module.web.util;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.security.SignatureException;

public class SignatureUtil {
    private static final String PREFIX = "\u0019Ethereum Signed Message:\n";

    public static String getAddressFromSignature(String signedHash, String originalMessage) throws SignatureException {
        String prefixedMessage = PREFIX + originalMessage.length() + originalMessage;

        String r = signedHash.substring(0, 66);
        String s = "0x"+signedHash.substring(66, 130);
        String v = "0x"+signedHash.substring(130, 132);

        Sign.SignatureData signMessage = new Sign.SignatureData(
                Numeric.hexStringToByteArray(v)[0],
                Numeric.hexStringToByteArray(r),
                Numeric.hexStringToByteArray(s)
        );

        String pubKey = Sign.signedMessageToKey(prefixedMessage.getBytes(), signMessage).toString(16);
        return "0x" + Keys.getAddress(pubKey);
    }
}