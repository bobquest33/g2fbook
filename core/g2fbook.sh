#!/bin/bash
#########################################################################################
##
#	g2fbox.sh
#
# Konvertierung von google contacts in ein xml Format das als Telefonbuchbackup
# fuer eine AVM Fritz!Box verwendet werden kann.
#
# Systemvoraussetzungen
# GNU Linux / Cygwin Umgebung mit folgenden Tools im Pfad:
#  -bash
#  -curl
#  -tr
#  -grep
#  -xsltproc (gehoert zu Gnome libxslt)
#
#
#########################################################################################
#     
#     Copyright (C) 2010 J. Elfring
#     
#     Dieses Programm ist freie Software. Sie koennen es unter den Bedingungen der
#     GNU General Public License, wie von der Free Software Foundation veroeffentlicht,
#     weitergeben und/oder modifizieren, entweder gemaess Version 3 der Lizenz oder
#     (nach Ihrer Wahl) jeder spaeteren Version.
#     
#     Die Veroeffentlichung dieses Programms erfolgt in der Hoffnung, dass es Ihnen von
#     Nutzen sein wird, aber OHNE IRGENDEINE GARANTIE, sogar ohne die implizite
#     Garantie der MARKTREIFE oder der VERWENDBARKEIT FUER EINEN BESTIMMTEN ZWECK.
#     Details finden Sie in der GNU General Public License.
#     
#     Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem
#     Programm erhalten haben. Falls nicht, siehe <http://www.gnu.org/licenses/>.
#     
#########################################################################################

option=$1
googleEmail=$2
googlePass=$3

echo ""
echo "g2fbook.sh - Copyright (C) 2010 J. Elfring"
echo "Fuer dieses Programm besteht KEINERLEI GARANTIE."
echo "Dies ist freie Software, die Sie unter bestimmten Bedingungen weitergeben duerfen."
echo "Fuer weitere Details schauen Sie bitte in dieses File."
echo ""


# Hilfe
if [ $# != 3 -o  _$1 != _F -a _$1 != _G -a _$1 != _B ]
then
	echo " Aufruf:"
	echo " $ sh $0 {F|G|B} username@gmail.com password "
	echo "   F - Erstellung des Backupfiles fuer die Fritz!Box"
	echo "   G - Erstellung des Backupfiles fuer gammu (Mobiltelefonbuch Backup)"
	echo "   B - Erstellt beide Files"
	echo "   Username und Passwort fuer Ihre Anmeldung bei google"
	echo ""
	exit 99
fi


echo ""
echo "Rufe Daten bei Google ab..."
# Bei Google anmelden
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
        echo "Fehler bei der Authentifizierung."
	echo $googleAuth
        exit 1
fi
authTag=`echo $googleAuth | tr [:space:] '\n' | grep -E '^Auth'`

# Die XML-Kontaktliste abrufen
curl http://www.google.com/m8/feeds/contacts/$googleEmail/full?max-results=99999 \
	--header "Authorization: GoogleLogin $authTag" \
	--silent \
	--insecure \
	--fail \
	> googleContacts.$$.xml
if [ $? != 0 ]
then
        echo "Fehler beim lesen der Kontakte"
        exit 2
fi


if [ $option == F -o $option == B ]
then	
	echo "Schreibe FritzBoxPhoneBook.xml..."
	xsltproc googleContacts-2-fritzPhoneBook.xml googleContacts.$$.xml > FritzBoxPhoneBook.xml
fi	

if [ $option == G -o $option == B ]
then	
	echo "Schreibe GammuPhoneBookBackup.txt..."
	xsltproc googleContacts-2-gammuPhoneBackup.xml googleContacts.$$.xml > GammuPhoneBookBackup.txt
fi	

echo ""
rm -f googleContacts.$$.xml
