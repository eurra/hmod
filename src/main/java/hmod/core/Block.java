
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface Block extends Statement
{
    int getChildsCount();
    Statement[] getChilds();
}
