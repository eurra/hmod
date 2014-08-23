
package hmod.domains.launcher.core;

import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public interface VariableCollection
{
    void setVariable(String name, Object val);
    <T> Map<String, T> getSubmapOfTypes(Class<T> type);
    String[] getAllVariables();
    Object getValue(String name);
    <T> T getValueOfType(String name, Class<T> type);
    boolean isSet(String name);
    void clearAll();
}
