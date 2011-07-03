


           XXX      XX   XX                      XX
          X   X    X      X                       X
              X    X      X                       X
  XXXXXX      X   XXXX    XXXXX   XXXXX   XXXXX   X  XX
 X    X      X     X      X    X X     X X     X  X  X
 X    X     X      X      X    X X     X X     X  X X
 X    X    X       X      X    X X     X X     X  XXX
  XXXXX   X        X      X    X X     X X     X  X  X
      X   XXXXX   XXXX   XXXXXX   XXXXX   XXXXX  XX   XX
      X
  XXXX


This is a little script to download the contacts from google mail
and to convert them into other formats.


It consists of:
 A shell script to download the contacts (g2fbook.sh),
 The XSLT stylesheets to do the actual conversion,
 Some documents.
 For native Windows use, it also contains directory with part of the
 cygwin system and a batch file to set up the environment.


Usage:
 Create a directory of your choice.
 Put the files of g2fbook there (I guess you did that by now).
 run 'sh ./g2fbook -h' from there and the help will show up.
 On Windows, please start 'g2fbook.bat -h' from a terminal box.

Requirements for the native shell-scripts:
GNU Linux / Cygwin environment with theese tools in path:
 bash
 curl
 tr
 grep
 xsltproc (contained in gnome libxslt)


Thankyou!
 Ralf Heiringhoff, for providing the interactive login.
 Redhat, for building cygwin.


License:
Copyright (C) 2010 J. Elfring
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

The cygwin-rt directory contains a part of the cygwin environment.
It's components are used to get g2fbook running under windows.
More information including licensing and source code can be found here:
http://www.cygwin.com/

