package com.fks.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MD5Utils {

	// @Value("${key}")不能直接给静态变量赋值，可使用set方法赋值
	private static String SALT;
	private static String ALGORITH_NAME;
	private static int HASH_ITERATIONS;

	@Value("${salt}")
	public void setSALT(String SALT) {
		MD5Utils.SALT = SALT;
	}

	@Value("${algorithName}")
	public void setAlgorithName(String algorithName) {
		ALGORITH_NAME = algorithName;
	}

	@Value("${hashIterations}")
	public void setHashIterations(int hashIterations) {
		HASH_ITERATIONS = hashIterations;
	}

	/**
	 * 使用md5生成加密后的密码
	 * @return
	 */
	public static String encrypt(String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
		return newPassword;
	}
	
	/**
	 * 使用md5生成加密后的密码
	 * @return
	 */
	public static String encrypt(String username, String pswd) {
		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT), HASH_ITERATIONS).toHex();
		return newPassword;
	}
	
}
