% SPC file for 37_SCC_Frame.vcl

#set fileName = "SCCID_37_Inst_0", "SCCID_37_Inst_1", "SCCID_37_Inst_2", "SCCID_37_Inst_3"
% Here I will set the rest of the place-holder variables

#set SCCID_37_variable_0 = "get", "get", "put", "put"
#set SCCID_37_variable_1 = "dst", "dst", "src", "src"
#set SCCID_37_variable_2 = "Bits.JNI_COPY_TO_ARRAY_THRESHOLD", "Bits.JNI_COPY_TO_ARRAY_THRESHOLD", "Bits.JNI_COPY_FROM_ARRAY_THRESHOLD", "Bits.JNI_COPY_FROM_ARRAY_THRESHOLD"
#set SCCID_37_variable_3 = "dst.length", "dst.length", "src.length", "src.length"
#set SCCID_37_variable_4 = "BufferUnderflowException", "BufferUnderflowException", "BufferOverflowException", "BufferOverflowException"
#set SCCID_37_variable_5 = "Bits.copyToLongArray", "Bits.copyToLongArray", "Bits.copyFromLongArray", "Bits.copyFromLongArray"
#set SCCID_37_variable_6 = "ix", "ix", "src", "src"

#while SCCID_37_variable_0, SCCID_37_variable_1, SCCID_37_variable_2, SCCID_37_variable_3, SCCID_37_variable_4, SCCID_37_variable_5, SCCID_37_variable_6, fileName
#adapt: "37_SCC_Frame.vcl"

% You can specify options for this SPC File here

#endadapt
#endwhile