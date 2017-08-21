package com.entuition.lambda.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.DateUtils;

public class Utilities {
	
	public static final String ENCODING_FORMAT = "UTF8";
	public static final String SIGNATURE_METHOD = "HmacSHA256";
	
	private static SecureRandom RANDOM = new SecureRandom();
    static {
        RANDOM.generateSeed(16);
    }
	
    /**
     * 
     * @param content
     * @param key
     * @return
     */
	public static String sign(String content, String key) {
    	try {
    		byte[] data = content.getBytes(ENCODING_FORMAT);
    		Mac mac = Mac.getInstance(SIGNATURE_METHOD);
    		mac.init(new SecretKeySpec(key.getBytes(ENCODING_FORMAT), SIGNATURE_METHOD));
    		char[] signature = Hex.encodeHex(mac.doFinal(data));
    		return new String(signature);
    	} catch (Exception e) {
    		
    	}
    	return null;
    }
	
	/**
	 * TODO add to identifier for content
	 * @param username
	 * @param password
	 * @return
	 */
	public static String getSaltedPassword(String username, String password) {
		return sign(username + Configuration.APP_NAME, password);
	}
	
	/**
	 * 
	 * @param stringToSign
	 * @param key
	 * @param targetSignature
	 * @return
	 */
	public static boolean validateSignature(String stringToSign, String key, String targetSignature) {
		String computedSignature = sign(stringToSign, key);
		return slowStringComparison(targetSignature, computedSignature);
	}
	
	/**
     * This method is low performance string comparison function. The purpose of
     * this method is to prevent timing attack.
     */
    public static boolean slowStringComparison(String givenSignature, String computedSignature) {
        if (null == givenSignature || null == computedSignature
                || givenSignature.length() != computedSignature.length())
            return false;

        int n = computedSignature.length();
        boolean signaturesMatch = true;

        for (int i = 0; i < n; i++) {
            signaturesMatch &= (computedSignature.charAt(i) == givenSignature.charAt(i));
        }

        return signaturesMatch;
    }

	/**
	 * check the request has valid timestamp.(30min)
	 * @param timestamp
	 * @return
	 */
	public static boolean isTimestampValid(String timestamp) {
		long timestampLong = 0L;
		final long window = 15 * 60 * 1000L;

        if (null == timestamp) {
            return false;
        }

        timestampLong = DateUtils.parseISO8601Date(timestamp).getTime();
        
        Long now = new Date().getTime();

        long before15Mins = new Date(now - window).getTime();
        long after15Mins = new Date(now + window).getTime();

        return (timestampLong >= before15Mins && timestampLong <= after15Mins);
	}
	
	public static String generateRandomString() {
		byte[] randomBytes = new byte[16];
        RANDOM.nextBytes(randomBytes);
        String randomString = new String(Hex.encodeHex(randomBytes));
        return randomString;
	}
	
	public static String getTimestamp() {
		return DateUtils.formatISO8601Date(new Date());
	}
	
	/**
	 * check username is valid, check it frontend.
	 * @param username
	 * @return
	 */
	public static boolean isValidUsername(String username) {
		int length = username.length();
        if (length < 3 || length > 16) {
            return false;
        }

        char c = 0;
        for (int i = 0; i < length; i++) {
            c = username.charAt(i);
            if (!Character.isLetterOrDigit(c) && '_' != c && '.' != c && '@' != c) {
                return false;
            }
        }

        return true;
	}
	
	/**
	 * check it in frontend.
	 * @param password
	 * @return
	 */
	public static boolean isValidPassword(String password) {
		int length = password.length();
		return (length >= 6 && length <= 128);
	}
	
	public static boolean isEmpty(String str) {
		if (null == str || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	public static String mapToString(Map<String, String> map) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null) ? URLEncoder.encode(key, "UTF-8") : "");
                stringBuilder.append("=");
                stringBuilder.append((value != null) ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }

    public static Map<String, String> stringToMap(String input) {
        Map<String, String> map = new HashMap<String, String>();

        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return map;
    }
}
