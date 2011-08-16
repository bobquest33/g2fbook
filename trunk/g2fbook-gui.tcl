#!/usr/bin/wish

wm title . g2fbook
wm resizable . 0 0

bind . <Return> pRun
bind . <Escape> exit

set vEmail [lindex $argv 0]

label .lWelcome -text "Welcome to g2fbook"
label .lEmail -text "Your google account name:"
label .lPassword -text "Your google password:"
label .lOutput -text "Script output:"
entry .eEmail -textvariable vEmail -width 30
entry .ePassword -textvariable vPassword -width 30 -show *
button .bAction -text "Start" -width 10 -command pRun
button .bExit -text "Exit" -command exit -width 10
text .tOutputField -width 55 -state disabled -yscrollcommand {.scroll set}
scrollbar .scroll -command {.tOutputField yview}

grid .lWelcome -row 0 -column 0 -columnspan 2
grid .lEmail -row 1 -column 0 -sticky "w"
grid .eEmail -row 1 -column 1 -sticky "w"
grid .lPassword -row 2 -column 0 -sticky "w"
grid .ePassword -row 2 -column 1 -sticky "w"
grid .bAction -row 3 -column 0
grid .bExit -row 3 -column 1
grid .lOutput -row 4 -column 0 -sticky "w"
grid .tOutputField -row 5 -column 0 -columnspan 2 -sticky "w"
grid .scroll -row 5 -column 3 -sticky "ns"

proc pRun {} {
	global vBackendText vEmail vPassword
	.tOutputField configure -state normal
	.tOutputField delete 0.0 end
	.tOutputField configure -state disabled
	
	if {$vEmail == "" || $vPassword == ""} {
		.tOutputField configure -state normal
		.tOutputField insert end "Please enter account name and password.\n"
		.tOutputField insert end "You may also specify the account name as option to the program.\n"
		.tOutputField configure -state disabled
	} else {
		set vCommand [concat "bash -e ./g2fbook.sh" $vEmail $vPassword]
		#set vCommand "ls -ltr /usr/bin"
		if [catch {open "|$vCommand |& cat"} vBackendText] {
			.tOutputField configure -state normal
			.tOutputField insert end "Backend failed.\n"
			.tOutputField configure -state disabled
		} else {
			fileevent $vBackendText readable pLog
			.tOutputField configure -state normal
			.tOutputField insert end "Starting backend.\n"
			.tOutputField configure -state disabled
			.bAction config -state disabled
			.bExit config -state disabled
		}
	}
}

proc pLog {} {
	global vBackendText
	if [eof $vBackendText] {
		pStop
	} else {
		gets $vBackendText line
		.tOutputField configure -state normal
		.tOutputField insert end $line\n
		.tOutputField see end
		.tOutputField configure -state disabled
	}
}

proc pStop {} {
	global vBackendText
	catch {close $vBackendText}
	.tOutputField configure -state normal
	.tOutputField insert end "Backend finished.\n"
	.tOutputField configure -state disabled
	.bAction config -state normal
	.bExit config -state normal
}
