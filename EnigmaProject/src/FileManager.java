/**
 * Program Name: FileManager.java
 * Purpose: This class is responsible for storing the encrypted and decrypted
 * contents types in the Enigma Machine Model, and outputs them to a file.
 * 
 * PROJECT CODER(S): Gui Miranda, Section 4
 * Date: July 31, 2023
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	
	private static FileManager _instance;
	private String filePath;
	private String decryptedContent, encryptedContent;

    private FileManager(String filePath) {
        this.filePath = filePath;
        decryptedContent = encryptedContent = "";
    }
    
    public static FileManager getInstance(String filePath) {
        if (_instance == null) {
        	_instance = new FileManager(filePath);
        }
        return _instance;
    }

    public void buildDecryptedContent(String content) {
        this.decryptedContent += content;
    }

    public void buildEncryptedContent(String content) {
        this.encryptedContent += content;
    }

    public void writeContentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(decryptedContent + "\n" + encryptedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
