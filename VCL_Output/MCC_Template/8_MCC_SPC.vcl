% SPC file for 8_MCC_Frame.vcl

#set fileName = "MCCID_8_Inst_0"
% Here I will set all the place-holder variables

#set SCCID_30_variable_0 = "HeapCharBuffer"

#while SCCID_30_variable_0, fileName

	#select fileName
		#option MCCID_8_Inst_0
			#insert-after break_0:
    		#endinsert
		#endoption
	#endselect
#adapt: "30_SCC_Frame.vcl"
	#select fileName
		#option MCCID_8_Inst_0
			#insert-after break_30:

			#endinsert
		#endoption
	#endselect
#endadapt
#endwhile