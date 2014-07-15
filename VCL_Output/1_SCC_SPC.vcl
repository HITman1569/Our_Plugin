% SPC file for 1_SCC_Frame.vcl

#set fileName = "SCCID_1_Inst_0", "SCCID_1_Inst_1", "SCCID_1_Inst_2", "SCCID_1_Inst_3", "SCCID_1_Inst_4", "SCCID_1_Inst_5", "SCCID_1_Inst_6", "SCCID_1_Inst_7", "SCCID_1_Inst_8", "SCCID_1_Inst_9", "SCCID_1_Inst_10", "SCCID_1_Inst_11"
% Here I will set the rest of the place-holder variables

#set SCCID_1_variable_0 = "ByteBufferAsCharBufferB", "ByteBufferAsCharBufferL", "ByteBufferAsDoubleBufferB", "ByteBufferAsDoubleBufferL", "ByteBufferAsFloatBufferB", "ByteBufferAsFloatBufferL", "ByteBufferAsIntBufferB", "ByteBufferAsIntBufferL", "ByteBufferAsLongBufferB", "ByteBufferAsLongBufferL", "ByteBufferAsShortBufferB", "ByteBufferAsShortBufferL"
#set SCCID_1_variable_1 = "1", "1", "3", "3", "2", "2", "2", "2", "3", "3", "1", "1"

#while SCCID_1_variable_0, SCCID_1_variable_1, fileName
#adapt: "1_SCC_Frame.vcl"

% You can specify options for this SPC File here

#endadapt
#endwhile