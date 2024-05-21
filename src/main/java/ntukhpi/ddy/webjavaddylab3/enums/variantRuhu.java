package ntukhpi.ddy.webjavaddylab3.enums;

public enum variantRuhu {
    daily("Щоденний"),
    unpaired("Непарні"),
    paired("Парні");

    private final String displayName;

    variantRuhu(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static variantRuhu getByVariant(String nameVR) {
        int index = -1;
        variantRuhu[] vrValues = values();
        boolean flFound = false;
        while (!flFound && index<vrValues.length-1) {
            index++;
            if (vrValues[index].getDisplayName().equals(nameVR)) {
                flFound = true;
            }
        }
        variantRuhu vr = null;
        if (!flFound) {
            vr = variantRuhu.values()[0];
        } else {
            vr = variantRuhu.values()[index];
        }
        return vr;
    }

    public static variantRuhu getVariantById(int index) {
        if (index >= variantRuhu.values().length) {
            return variantRuhu.values()[0];
        } else {
            return variantRuhu.values()[index];
        }
    }

    public static String[]  getVariants() {
        variantRuhu[] vr = values();
        String[] vrNames = new String[vr.length];
        for (int i = 0; i < vr.length; i++) {
            vrNames[i] = vr[i].getDisplayName();
        }
        return vrNames;
    }
}
