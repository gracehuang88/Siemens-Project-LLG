package com.simens.contest.gll;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Logan L
 */
public class CommonUtil {
	
	public static ByteArrayInputStream reteriveByteArrayInputStream(File file) {
		ByteArrayInputStream b = null;
		try {
			b = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return b;
	}
	
	public static File CreateDirectory(String path) {
		File theDir = new File(path);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + path);
			boolean result = theDir.mkdir();

			if (result) {
				System.out.println("DIR created");
			}
			
		}
		return theDir;
	}

}
