
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "OPTYPE", namespace = "http://www.dmg.org/PMML-4_3")
@XmlEnum
public enum OpType {

    @XmlEnumValue("categorical")
    CATEGORICAL("categorical"),
    @XmlEnumValue("ordinal")
    ORDINAL("ordinal"),
    @XmlEnumValue("continuous")
    CONTINUOUS("continuous");
    private final String value;

    OpType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OpType fromValue(String v) {
        for (OpType c: OpType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
