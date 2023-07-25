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
	/* Private instance variables */

  private ArrayList<EnigmaView> views;
  
  private HashMap<String, Boolean> keys;
  private HashMap<String, Boolean> lamps;
  
  private EnigmaRotor[] rotors = new EnigmaRotor[3];

    public EnigmaModel() 
    {
        views = new ArrayList<EnigmaView>();
        
        keys = populateKeysMap();
        lamps = populateKeysMap();
        
        rotors[0] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[0]);
        rotors[1] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[1]);
        rotors[2] = new EnigmaRotor(EnigmaConstants.ROTOR_PERMUTATIONS[2]);
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
        String permutation = rotors[index].getPermutation();
        String letter = permutation.substring(offset, (offset+1));
        
        return letter;
    }

/**
 * Called automatically by the view when the specified key is pressed.
 *
 * @param key The key the user pressed as a one-character string
 */

    public void keyPressed(String key)
    {
        keys.put(key, true);
        lamps.put(key, true);
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
    	lamps.put(key, false);
    	this.update();
    }

/**
 * Called automatically by the view when the rotor at the specified
 * index (0-2) is clicked.
 *
 * @param index The index of the rotor that was clicked
 */

    public void rotorClicked(int index) 
    {
        rotors[index].advance();
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

class EnigmaRotor 
{
	String permutation;
	int offset;
	
/**
 * Initializes an EnigmaRotor object.
 *
 * @param rotorPermutation The permutation string used for encoding/decoding of the rotor.
 */
	public EnigmaRotor(String rotorPermutation)
	{
		permutation = rotorPermutation;
		offset = 0;
	}
	
/**
 * Returns the rotor's offset value.
 */
	public int getOffset()
	{
		return offset;
	}
	
/**
 * Returns the rotor's permutation value;
 */
	public String getPermutation()
	{
		return permutation;
	}
	
/**
 * Advances the rotor by one. 
 */
	public void advance()
	{
		offset++;
		
		if (offset > 26)
			offset = 0;
	}
}

