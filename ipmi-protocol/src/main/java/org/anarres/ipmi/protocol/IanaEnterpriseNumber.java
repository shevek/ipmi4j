package org.anarres.ipmi.protocol;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public enum IanaEnterpriseNumber {
    /** IBM. Kristine Adamson &lt;adamson@us.ibm.com&gt; */
    IBM(2, "IBM"),
    /** Hewlett-Packard. Harry Lynch &lt;harry.lynch@hp.com&gt; */
    Hewlett_Packard(11, "Hewlett-Packard"),
    /** Intel Corporation. Adam Kaminski &lt;adam.kaminski@intel.com&gt; */
    Intel_Corporation(343, "Intel Corporation"),
    /** Dell Inc.. David L. Douglas &lt;david_l_douglas@dell.com&gt; */
    Dell_Inc(674, "Dell Inc."),
    /** Alerting Specifications Forum. Steven Williams &lt;steven.d.williams@intel.com&gt; */
    Alerting_Specifications_Forum(4542, "Alerting Specifications Forum"),
    /** IBM Corporation. Victor Sample &lt;vsample@us.ibm.com&gt; */
    IBM_Corporation(4769, "IBM Corporation"),
    /** INVENTEC CORPORATION. JH CHYAN &lt;chyan.jh@inventec.com&gt; */
    INVENTEC_CORPORATION(6569, "INVENTEC CORPORATION"),
    /** Intelligent Platform Management Interface forum. Thomas M. Slaight &lt;tom.slaight@intel.com&gt; */
    Intelligent_Platform_Management_Interface_forum(7154, "Intelligent Platform Management Interface forum"),
    /** IBM eServer X. Lynn Fore &lt;sls@us.ibm.com&gt; */
    IBM_eServer_X(20301, "IBM eServer X"),
    EOD(-1, null);

    private final int number;
    private final String name;

    private IanaEnterpriseNumber(int number, String name) {
        this.number = number;
        this.name = name;
    }

    @Nonnegative
    public int getNumber() {
        return number;
    }

    @Nonnull
    public String getName() {
        return name;
    }
}
