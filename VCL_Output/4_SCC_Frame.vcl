#output ?@fileName?".java"

[] ?@SCCID_4_variable_0?, int offset, int length) {
        checkBounds(offset, length, ?@SCCID_4_variable_1?);
        if (length > remaining())
            throw new ?@SCCID_4_variable_2?();
        int end = offset + length;
        for (int i = offset; i < end; i++)?@SCCID_4_variable_3?