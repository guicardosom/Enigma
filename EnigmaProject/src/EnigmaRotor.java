public class EnigmaRotor
{
	private String r2lPermutation, l2rPermutation;
	private int offset;
	
/**
 * Initializes an EnigmaRotor object.
 *
 * @param rotorPermutation The permutation string used for right-to-left encoding/decoding of the rotor.
 * @param invertedPermutation The permutation string used for left-to-right encoding/decoding of the rotor.
 */
	public EnigmaRotor(String rotorPermutation, String invertedPermutation)
	{
		r2lPermutation = rotorPermutation;
		l2rPermutation = invertedPermutation;
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
 * Returns the rotor's right-to-left permutation value;
 */
	public String getR2LPermutation()
	{
		return r2lPermutation;
	}
	
/**
 * Returns the rotor's left-to-right permutation value;
 */
	public String getL2RPermutation()
	{
		return l2rPermutation;
	}
	
/**
 * Advances the rotor by one. 
 */
	public boolean advance()
	{
		offset = (offset + 1) % 26;
	    return offset == 0;
	}
	
/**
 * Applies the encoding process using the rotor's permutation.
 *
 * @param index The rotor's index.
 * @param permutation The rotor's permutation.
 * @param offset The rotor's offset.
 */
	public int applyPermutation(int index, String permutation, int offset)
	{
		int len = permutation.length();
		
		//Compute the index of the letter after shifting it by the offset, wrapping around if necessary.
		int shiftedIndex = (index + offset) % len;
		
		//Look up the character at that index in the permutation string.
		char shiftedCharacter = permutation.charAt(shiftedIndex);
		
		//Return the index of the new character after subtracting the offset, wrapping if necessary.
		int newIndex = (EnigmaConstants.ALPHABET.indexOf(shiftedCharacter) - offset + len) % len;
		
		return newIndex;
	}
}