 /**
 * Program Name: EnigmaModel.java
 * Purpose: This class defines the starter version of the EnigmaModel class,
 * but it doesn't completely implement any of the methods. That will be your job.
 * NOTE: this class holds the main method for the application.
 * BASE CODE Coders: Eric Roberts and Jed Rembold, Willamette University, OR
 * 
 * PROJECT CODER(S): Gui Miranda, Section 4
 * Date: July 24, 2023
 */

import java.util.ArrayList;
import java.util.HashMap;

public class EnigmaModel 
{
	/* Public instance variables */
	public static FileManager fileManager;
	
	/* Private instance variables */
    private ArrayList<EnigmaView> views;
  
    private HashMap<String, Boolean> keys;
    private HashMap<String, Boolean> lamps;
  
    private EnigmaRotor[] rotors = new EnigmaRotor[3];
    private EnigmaRotor reflector;

    public EnigmaModel() 
    {
    	fileManager = FileManager.getInstance("./encryption.txt");

        views = new ArrayList<EnigmaView>();
        
        keys = populateKeysMap();
        lamps = populateKeysMap();
        
        rotors[0] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[0], invertKey(EnigmaConstants.ROTOR_PERMUTATIONS[0]));
        rotors[1] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[1], invertKey(EnigmaConstants.ROTOR_PERMUTATIONS[1]));
        rotors[2] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[2], invertKey(EnigmaConstants.ROTOR_PERMUTATIONS[2]));
        
        reflector = new EnigmaRotor(EnigmaConstants.REFLECTOR_PERMUTATION, invertKey(EnigmaConstants.REFLECTOR_PERMUTATION));
    }

/**
 * Adds a view to this model.
 *
 * @param view The view being added
 */

    public void addView(EnigmaView view)
    {
        views.add(view);
    }

/**
 * Sends an update request to all the views.
 */

    public void update() 
    {
        for (EnigmaView view : views)
        {
            view.update();
        }
    }

/**
 * Returns true if the specified letter key is pressed.
 *
 * @param letter The letter key being tested as a one-character string.
 */

    public boolean isKeyDown(String letter)
    {
    	return keys.get(letter);
    }

/**
 * Returns true if the specified lamp is lit.
 *
 * @param letter The lamp being tested as a one-character string.
 */

    public boolean isLampOn(String letter) 
    {
    	return lamps.get(letter);
    }

/**
 * Returns the letter visible through the rotor at the specified index.
 *
 * @param index The index of the rotor (0-2)
 * @return The letter visible in the indicated rotor
 */

    public String getRotorLetter(int index)
    {
        int offset = rotors[index].getOffset();
        String letter = Character.toString(EnigmaConstants.ALPHABET.charAt(offset));
                
        return letter;
    }

/**
 * Called automatically by the view when the specified key is pressed.
 *
 * @param key The key the user pressed as a one-character string
 */

    public void keyPressed(String key)
    {
        rotorClicked();
        fileManager.buildDecryptedContent(key);
        keys.put(key, true);
        
        int keyIndex = EnigmaConstants.ALPHABET.indexOf(key);
        
        for (int i = 2; i >= 0; i--)
        {
        	//cross rotors from right to left
            keyIndex = rotors[i].applyPermutation(keyIndex, 
							            		  rotors[i].getR2LPermutation(), 
							            		  rotors[i].getOffset());
        }
        
        //reverse through the reflector
        keyIndex = reflector.applyPermutation(keyIndex, 
											  reflector.getR2LPermutation(), 
											  0);
        
        for (int i = 0; i <= 2; i++)
        {
        	//cross rotors from left to right
            keyIndex = rotors[i].applyPermutation(keyIndex, 
							            		  rotors[i].getL2RPermutation(), 
							            		  rotors[i].getOffset());
        }

    	String letter = Character.toString(EnigmaConstants.ALPHABET.charAt(keyIndex));
    	fileManager.buildEncryptedContent(letter);
        lamps.put(letter, true);
                
        this.update();
    }

/**
 * Called automatically by the view when the specified key is released.
 *
 * @param key The key the user released as a one-character string
 */

    public void keyReleased(String key)
    {
    	keys.put(key, false);
    	
        int keyIndex = EnigmaConstants.ALPHABET.indexOf(key);

    	for (int i = 2; i >= 0; i--)
        {
        	//cross rotors from right to left
            keyIndex = rotors[i].applyPermutation(keyIndex, 
							            		  rotors[i].getR2LPermutation(), 
							            		  rotors[i].getOffset());
        }
        
        //reverse through the reflector
        keyIndex = reflector.applyPermutation(keyIndex, 
											  reflector.getR2LPermutation(), 
											  0);
        
        for (int i = 0; i <= 2; i++)
        {
        	//cross rotors from left to right
            keyIndex = rotors[i].applyPermutation(keyIndex, 
							            		  rotors[i].getL2RPermutation(), 
							            		  rotors[i].getOffset());
        }
     
    	String letter = Character.toString(EnigmaConstants.ALPHABET.charAt(keyIndex));
    	lamps.put(letter, false);
    	    	     
    	this.update();
    }

/**
 * Called automatically by the view when the rotor at the specified
 * index (0-2) is clicked.
 *
 * @param index The index of the rotor that was clicked
 */

    public void rotorClicked() 
    {
    	EnigmaRotor fastRotor = rotors[2];
        EnigmaRotor mediumRotor = rotors[1];
        EnigmaRotor slowRotor = rotors[0];

        boolean carryMedium = fastRotor.advance();
        if (carryMedium) {
            boolean carrySlow = mediumRotor.advance();
            if (carrySlow) {
                slowRotor.advance();
            }
        }
    }
    
/**
 * Called by EnigmaRotor to invert permutation to left-to-right.
 *
 * @param key The encryption key
 * @return invertedKey The inverted encryption key
 */

    public String invertKey(String key)
    {
    	String invertedKey = "";
    	
    	for (char alphabetChar : EnigmaConstants.ALPHABET.toCharArray())
    	{
    		int index = key.indexOf(alphabetChar);
    		char invertedChar = EnigmaConstants.ALPHABET.charAt(index);
    		invertedKey += invertedChar;
    	}
    	
    	return invertedKey;
    }    
    
/**
 * Called automatically when the Model is created.
 * Populates the keys HashMap with the alphabet letters.
 */
    
    private HashMap<String, Boolean> populateKeysMap()
    {
    	HashMap<String, Boolean> alphabetKeys = new HashMap<String, Boolean>();
    	
    	for (char c = 'A'; c <= 'Z'; c++)
            alphabetKeys.put(Character.toString(c), false);
    	
    	return alphabetKeys;
    }

/* Main program */

    public static void main(String[] args)
    {
        EnigmaModel model = new EnigmaModel();
        EnigmaView view = new EnigmaView(model);
        model.addView(view);        
    }
  
}//end class EnigmaModel


