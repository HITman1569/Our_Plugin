#output ?@fileName?".java"

#break: break_0

public CharSequence subSequence(int start, int end) {
        if ((start < 0)
            || (end > length())
            || (start > end))
            throw new IndexOutOfBoundsException();
        int len = end - start;
        return new ?@SCCID_30_variable_0?(hb,
                                      -1, 0, len, len,
                                      offset + position() + start);
    }
#break: break_30
