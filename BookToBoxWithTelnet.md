#Automatic import into the fritzbox

A little POC that works with expect and telnet

Argument 1: Fritzbox IP
Argument 2: Fritzbox web password

```
#!/usr/bin/expect

set timeout 20
set ip [lindex $argv 0]
set password [lindex $argv 1]

spawn telnet $ip

expect "Fritz!Box web password:" {
        send "$password\r"
}

expect "#" {
        send "cat > /tmp/FritzBoxPhoneBook.xml\r"
        sleep 1
        set fp [open FritzBoxPhoneBook.xml r]
        while {-1 != [gets $fp buf]} {
                send -- "$buf\r"
        }
        send "\004"
        close $fp
        send "pbd --importto --id=%u /tmp/FritzBoxPhoneBook.xml\r"
        sleep 1
        close
}                     
```