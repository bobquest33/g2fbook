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
help=`echo $1 | tr -d '[:punct:]'`
if [ _$help == "_help" -o _$help == "_h" ]
then
	echo " Invocation:"
	echo " $ sh $0 [username@gmail.com [password]]"
	echo " $ sh $0 {-h | --help}"
	echo "   username and password for your google account"
	echo "   if username or password are not provided on the"
	echo "   commandline, the script will ask for it."
	echo ""
	exit 99
fi

if  [ $# -eq 0 ]
then
	echo " Plase enter your google account"
	echo -e " Account: \c"
	read googleEmail
fi


if [ $# -le 1 ]
then
	echo " Please enter Password for $googleEmail"
	echo -e " Password: \c"
	# Hide input from Terminal
	stty_orig=`stty -g`
	stty -echo
	read googlePass
	stty $stty_orig
	echo -e "\n"
fi


echo "Downloading data from google..."
# Google Auth
googleAuth=$(curl https://www.google.com/accounts/ClientLogin \
		--data-urlencode Email="$googleEmail" --data-urlencode Passwd="$googlePass" \
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
	echo "If you are sure the credentials were correct,"
	echo "please try to logout/login to google via your browser."
	echo "Perhaps a captcha is required."
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
xsltproc lib/googleContacts-2-fritzPhoneBook.xml googleContacts.$$.xml > FritzBoxPhoneBook.xml

echo "Writing GammuPhoneBookBackup.txt..."
xsltproc lib/googleContacts-2-gammuPhoneBackup.xml googleContacts.$$.xml > GammuPhoneBookBackup.txt

echo ""
rm -f googleContacts.$$.xml
