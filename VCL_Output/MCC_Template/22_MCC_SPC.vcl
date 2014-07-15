% SPC file for 22_MCC_Frame.vcl

#set fileName = "MCCID_22_Inst_0"
% Here I will set all the place-holder variables


#while SCCID_37_variable_0, SCCID_37_variable_1, SCCID_37_variable_2, SCCID_37_variable_3, SCCID_37_variable_4, SCCID_37_variable_5, SCCID_37_variable_6, fileName

	#select fileName
		#option MCCID_22_Inst_0
			#insert-after break_0:
    		#endinsert
		#endoption
	#endselect
#adapt: "37_SCC_Frame.vcl"
	#select fileName
		#option MCCID_22_Inst_0
			#insert-after break_37:
x(pos), dst,
                                          offset << 3,
                                          length << 3);
            else
                Bits.copyToByteArray(ix(pos), dst,
                                     offset << 3,
                                     length << 3);
            position(pos + length);
        } else {
            super.get(dst, offset, length);
        }
        return this;



    }
			#endinsert
		#endoption
	#endselect
#endadapt
#endwhile