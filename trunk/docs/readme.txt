


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


Usage:
 Create a directory of your choice.
 Put the files of g2fbook there (I guess you did that by now).
 run 'sh ./g2fbook' from there and the help will show up.


Requirements:
GNU Linux / Cygwin environment with theese tools in path:
 bash
 curl
 tr
 grep
 xsltproc (contained in gnome libxslt)


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

