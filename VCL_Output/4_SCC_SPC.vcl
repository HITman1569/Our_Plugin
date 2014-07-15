% SPC file for 4_SCC_Frame.vcl

#set fileName = "SCCID_4_Inst_0", "SCCID_4_Inst_1", "SCCID_4_Inst_2", "SCCID_4_Inst_3", "SCCID_4_Inst_4", "SCCID_4_Inst_5", "SCCID_4_Inst_6", "SCCID_4_Inst_7", "SCCID_4_Inst_8", "SCCID_4_Inst_9", "SCCID_4_Inst_10", "SCCID_4_Inst_11", "SCCID_4_Inst_12", "SCCID_4_Inst_13"
% Here I will set the rest of the place-holder variables

#set SCCID_4_variable_0 = "src", "src", "src", "src", "src", "src", "src", "dst", "dst", "dst", "dst", "dst", "dst", "dst"
#set SCCID_4_variable_1 = "src.length", "src.length", "src.length", "src.length", "src.length", "src.length", "src.length", "dst.length", "dst.length", "dst.length", "dst.length", "dst.length", "dst.length", "dst.length"
#set SCCID_4_variable_2 = "BufferOverflowException", "BufferOverflowException", "BufferOverflowException", "BufferOverflowException", "BufferOverflowException", "BufferOverflowException", "BufferOverflowException", "BufferUnderflowException", "BufferUnderflowException", "BufferUnderflowException", "BufferUnderflowException", "BufferUnderflowException", "BufferUnderflowException", "BufferUnderflowException"
#set SCCID_4_variable_3 = "this.put", "this.put", "this.put", "this.put", "this.put", "this.put", "this.put", "dst", "dst", "dst", "dst", "dst", "dst", "dst"

#while SCCID_4_variable_0, SCCID_4_variable_1, SCCID_4_variable_2, SCCID_4_variable_3, fileName
#adapt: "4_SCC_Frame.vcl"

% You can specify options for this SPC File here

#endadapt
#endwhile