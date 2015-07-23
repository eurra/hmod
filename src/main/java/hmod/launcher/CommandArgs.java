
package hmod.launcher;

/**
 * Represents an ordered collection of command arguments, which are accessible
 * through an index.
 * @author Enrique Urra C.
 */
public interface CommandArgs
{
    /**
     * Gets the total count of arguments stored in the collection.
     * @return the argument count.
     */
    int getCount();
    
    /**
     * Gets a raw argument at the specified position.
     * @param index the index for retrieving the argument.
     * @return the argument as an object reference.
     * @throws IndexOutOfBoundsException if the index is invalid.
     */
    Object getObject(int index) throws IndexOutOfBoundsException;
    
    /**
     * Gets a string argument at the specified position.
     * @param index the index for retrieving the argument.
     * @return the argument as a string reference.
     * @throws IndexOutOfBoundsException if the index is invalid.
     * @throws LauncherException if the argument at the specified position is not a string.
     */
    String getString(int index) throws IndexOutOfBoundsException, LauncherException;
    
    /**
     * Gets an argument at the specified position as an specific type.
     * @param <T> The type of the argument to be retrieved.
     * @param index the index for retrieving the argument.
     * @param type the class of the argument to retrieve.
     * @return the argument as the specified type. 
     * @throws IndexOutOfBoundsException if the index is invalid.
     * @throws LauncherException if the argument at the specified position is not compatible
     *  with the specified type..
     */
    <T> T getArgAs(int index, Class<T> type) throws IndexOutOfBoundsException, LauncherException;
    
    /**
     * Gets all the stored arguments as an object array.
     * @return an object array with the raw arguments.
     */
    Object[] getAllArgs();
}
