import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();

	private int ThreadsNumber;
	private String hash;
	private int wordLength;

	public Cracker(int threadNum, String hv, int length) {

		ThreadsNumber = threadNum;
		hash = new String(hv);
		wordLength = length;
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val < 16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		return result;
	}


	private class Work implements Runnable{

		private int ID;
		private int startInd;
		private int endInd;

		public Work(int id, int start, int end) {

			ID = id;
			startInd = start;
			endInd = end;
		}

		private void check(String ans) {

			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] messageDigest = md.digest(ans.getBytes());
				String hashtext = hexToString(messageDigest);
				if (hashtext.equals(hash)) {
					System.out.println(ans);
				}
			} catch (NoSuchAlgorithmException ies) {
				ies.printStackTrace();
			}
		}

		private void helper(String ans) {

			if (ans.length() == wordLength) {
				//System.out.println(ans[0]);
				check(ans);
				return;
			} else {
				for (int i = 0; i < CHARS.length; i++) {
					helper(ans + CHARS[i]);
				}
			}
		}

		@Override
		public void run() {

			for (int i=startInd; i<=endInd; i++) {
				helper(String.valueOf(CHARS[i]));
			}
		}
	}

	private Work getWorkInstance(int id, int start, int end) {
		return new Work(id, start, end);
	}

	public static void main(String args[]) {

		Cracker ck;
		if (args.length == 1) {

			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] messageDigest = md.digest(args[0].getBytes());
				String hashtext = hexToString(messageDigest);
				System.out.println(hashtext);
			} catch (NoSuchAlgorithmException ies) {
				ies.printStackTrace();
			}
		} else {

			int numThreads = Integer.parseInt(args[2]);
			int numLetters = Integer.parseInt(args[1]);
			String hash = new String(args[0]);
			Thread threads[] = new Thread[numThreads];
			ck = new Cracker(numThreads, hash, numLetters);

			for (int i=0; i<numThreads; i++) {
				int start=0, end=0;
				if (i == numThreads-1) {
					start = (numThreads - 1) * (CHARS.length) / numThreads;
					end = CHARS.length - 1;
				} else {
					start = i * (CHARS.length)/numThreads;
					end = ((i+1) * (CHARS.length) / numThreads) - 1;
				}
				threads[i] = new Thread(ck.getWorkInstance(i, start, end));
				threads[i].start();
			}
			for (int i=0; i<numThreads; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException ies) {
					ies.printStackTrace();
				}
			}
			System.out.println("All done.");
		}
	}

	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

}