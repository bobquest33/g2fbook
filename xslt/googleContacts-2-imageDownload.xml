<?xml version="1.0"?>

<!--
    Copyright (C) 2011 R. Engelmann


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
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:at="http://www.w3.org/2005/Atom" xmlns:gd="http://schemas.google.com/g/2005" version="1.0">
<xsl:output method="text" />

   <xsl:template match="/">
   
        <xsl:apply-templates select="at:feed/at:entry[gd:phoneNumber/@rel='http://schemas.google.com/g/2005#mobile' or gd:phoneNumber/@rel='http://schemas.google.com/g/2005#home' or gd:phoneNumber/@rel='http://schemas.google.com/g/2005#work']"/>

  </xsl:template>




<xsl:template match="at:feed/at:entry">
<!--        
<xsl:variable name="imageUrl">
<xsl:apply-templates select="at:link[@rel='http://schemas.google.com/contacts/2008/rel#photo']"/>
</xsl:variable>
<xsl:if test="$imageUrl != ''">
  <xsl:value-of select="$imageUrl"/>
</xsl:if>
-->     
<xsl:apply-templates select="at:link[@rel='http://schemas.google.com/contacts/2008/rel#photo']"/>

  </xsl:template>
  
<xsl:template match="at:link">
  <xsl:value-of select="./@href"/>
<xsl:text>&#xa;</xsl:text>
  </xsl:template>

  

</xsl:stylesheet>
