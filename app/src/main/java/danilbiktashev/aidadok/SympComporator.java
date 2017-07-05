package danilbiktashev.aidadok;

import java.util.Comparator;

/**
 * Created by mikhailkoroteev on 24.05.17.
 */

public class SympComporator implements Comparator<symp>
{
    public int compare(symp left, symp right) {
        Integer leftInt = Integer.parseInt(left.idCharapter);
        Integer rightInt = Integer.parseInt(right.idCharapter);
        return leftInt.compareTo(rightInt);
    }
}
