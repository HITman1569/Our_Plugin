#output ?@fileName?".java"

#break: break_0

public DoubleBuffer ?@SCCID_37_variable_0?(double[] ?@SCCID_37_variable_1?, int offset, int length) {

        if ((length << 3) > ?@SCCID_37_variable_2?) {
            checkBounds(offset, length, ?@SCCID_37_variable_3?);
            int pos = position();
            int lim = limit();
            assert (pos <= lim);
            int rem = (pos <= lim ? lim - pos : 0);
            if (length > rem)
                throw new ?@SCCID_37_variable_4?();

            if (order() != ByteOrder.nativeOrder())
                ?@SCCID_37_variable_5?(?@SCCID_37_variable_6?
#break: break_37
