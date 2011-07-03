#!/bin/bash
#
# Extracts binarys and the needed libs from cygwin
# and puts them into one single directory for later use.
# if the option to the script is purge, the destination will
# be newly created.
#     
#########################################################################################
#     Copyright (C) 2010 J. Elfring
#     
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#  
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#  
#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#########################################################################################


cygdir="../cygwin-rt"
filelist="cygpack.list"



if [ _$1 == _purge ]
then
	find $cygdir -mindepth 1 -exec rm {} \;
	echo "$cygdir purged, rc=$?"
fi

for item in `cat $filelist`
do
	echo ""
	echo "New item: $item"
	itemfile=`which $item`
	itemfilename=`basename $itemfile`
	if [ -e $cygdir/$itemfilename ]
	then
		echo "$item: Program already there"
	else
		cp -p $itemfile $cygdir/
		echo "$item copied, rc=$?"
	fi
	for lib in `ldd \`which $item\` | cut -d " " -f 3 | grep -v cygdrive`
	do
		libfile=`basename $lib`
		if [ -e $cygdir/$libfile ]
		then
			echo "$lib: Lib already there"
		else
			cp -p $lib $cygdir/
			echo "$lib copied, rc=$?"
		fi
	done
done
