#output ?@fileName?".java"

?@SCCID_1_variable_0?(ByteBuffer bb) {   // package-private

        super(-1, 0,
              bb.remaining() >> ?@SCCID_1_variable_1?,
              bb.remaining() >> ?@SCCID_1_variable_1?);
        this.bb = bb;
        // enforce limit == capacity
        int cap = this.capacity();
        this.limit(cap);
        int pos = this.position();
        assert (pos <= cap);
        offset = pos;



    }