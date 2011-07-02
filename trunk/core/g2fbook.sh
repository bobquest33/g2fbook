#!/bin/bash
#########################################################################################
#     
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

googleEmail=$1
googlePass=$2

echo ""
echo "*g2fbook.sh - Copyright (C) 2010 J. Elfring"
echo "*This program comes with ABSOLUTELY NO WARRANTY."
echo "*This is free software, and you are welcome to redistribute it"
echo "*for further details please look into license.txt"
echo ""

# Help!
if [ $# -lt 1 -o $# -gt 2 ]
then
	echo " Invocation:"
	echo " $ sh $0 username@gmail.com [password]"
	echo "   username and password for your google account"
	echo "   if the password is not provided on the commandline,"
	echo "   the script will ask for it."
	echo ""
	exit 99
fi

if [ $# -eq 1 ]
then
	echo " Please enter Password for ${1}"
	echo -e " Password: \c"
	# Hide input from Terminal
	stty_orig=`stty -g`
	stty -echo
	read googlePass
	stty $stty_orig
	echo -e "\n"
fi


echo ""
echo "Downloading data from google..."
# Google Auth
googleAuth=$(curl https://www.google.com/accounts/ClientLogin \
		--data Email=$googleEmail -d Passwd=$googlePass \
		--data accountType=HOSTED_OR_GOOGLE \
		--data source=g2fbook \
		--data service=cp \
		--silent \
		--insecure \
		--fail
		)
if [ $? != 0 ]
then
        echo "Authentication failed for $googleEmail"
	echo $googleAuth
        exit 1
fi
authTag=`echo $googleAuth | tr [:space:] '\n' | grep -E '^Auth'`

# Get the contact feed
curl http://www.google.com/m8/feeds/contacts/$googleEmail/full?max-results=99999 \
	--header "Authorization: GoogleLogin $authTag" \
	--silent \
	--insecure \
	--fail \
	> googleContacts.$$.xml
if [ $? != 0 ]
then
        echo "Download of contact feed failed for $googleEmail"
        exit 2
fi


echo "Writing FritzBoxPhoneBook.xml..."
xsltproc googleContacts-2-fritzPhoneBook.xml googleContacts.$$.xml > FritzBoxPhoneBook.xml

echo "Writing GammuPhoneBookBackup.txt..."
xsltproc googleContacts-2-gammuPhoneBackup.xml googleContacts.$$.xml > GammuPhoneBookBackup.txt

echo ""
rm -f googleContacts.$$.xml
