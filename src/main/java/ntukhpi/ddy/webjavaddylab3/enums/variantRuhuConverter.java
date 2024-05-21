package ntukhpi.ddy.webjavaddylab3.enums;

import jakarta.persistence.AttributeConverter;

public class variantRuhuConverter implements AttributeConverter<variantRuhu, Integer> {
    @Override
    public Integer convertToDatabaseColumn(variantRuhu variantRuhu) {
        return variantRuhu.ordinal();
    }

    @Override
    public variantRuhu convertToEntityAttribute(Integer codVR) {
        return variantRuhu.values()[codVR];
    }
}
