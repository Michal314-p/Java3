package cw_sort;

import java.util.List;

/**
 * Abstract class AbstractIntSorter stores sorting algorithm methods and algorithm description methods
 */

import java.util.List;

public abstract class AbstractIntSorter
{
    public String desc;
    public boolean stability;
    public boolean in_place;

    public AbstractIntSorter() {}

    abstract List<IntElement> solve0(List<IntElement> var1);

    String description() {
        return this.desc;
    }

    boolean isStable() {
        return this.stability;
    }

    boolean isInSitu() {
        return this.in_place;
    }
}