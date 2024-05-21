package ntukhpi.ddy.webjavaddylab3.entity.comparator;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class UkrainianStringComparator implements Comparator<String> {
    private Collator collator;

    public UkrainianStringComparator() {
        collator = Collator.getInstance(new Locale("uk", "UA"));
    }

    @Override
    public int compare(String s1, String s2) {
        return collator.compare(s1, s2);
    }
}
