package cw_sort;

import java.util.List;

/**
 * Abstract class AbstractIntSorter stores method of sorting numbers
 */

public abstract class AbstractFloatSorter extends AbstractIntSorter
{
    /**
     * Abstract method implements sorting of integer or float numbers
     * @param list List of elements with values and names
     */
    abstract List<IElement> solve1(List<IElement> list);
}