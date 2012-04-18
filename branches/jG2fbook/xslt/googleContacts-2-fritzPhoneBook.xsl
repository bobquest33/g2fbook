<?xml version="1.0"?>

<!--
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
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:at="http://www.w3.org/2005/Atom" xmlns:gd="http://schemas.google.com/g/2005" version="1.0">
<xsl:output method="xml" version="1.0" encoding="utf-8" indent="yes"/>

  <xsl:template match="/">
    <phonebooks>
      <phonebook>
        <!-- Continue if the entry contains at least one interesting phone number -->
        <xsl:apply-templates select="at:feed/at:entry[gd:phoneNumber/@rel='http://schemas.google.com/g/2005#mobile' or gd:phoneNumber/@rel='http://schemas.google.com/g/2005#home' or gd:phoneNumber/@rel='http://schemas.google.com/g/2005#work']"/>
      </phonebook>
    </phonebooks>
  </xsl:template>

  <xsl:template match="at:feed/at:entry">
    <contact>
      <category/>
      <person>
        <realName>
          <xsl:value-of select="at:title"/>
        </realName>
     
<xsl:variable name="imageUrl">
<xsl:apply-templates select="at:link[@rel='http://schemas.google.com/contacts/2008/rel#photo']"/>
</xsl:variable>


<xsl:variable name="name">
  <xsl:call-template name="last-substring-after">
    <xsl:with-param name="string" select="$imageUrl"/>
    <xsl:with-param name="separator" select="'/'"/>
  </xsl:call-template>
</xsl:variable>
<xsl:if test="$name != ''">
   <imageURL>file:///var/media/ftp/FlashDisk-01/FRITZ/fonpix/<xsl:value-of select="$name"/>.jpg</imageURL>
</xsl:if>
     
      </person>
      <telephony>
        <!-- if there are multiple number of one type (rel) use only the first one -->
        <xsl:apply-templates select="gd:phoneNumber[not(@rel=preceding-sibling::gd:phoneNumber/@rel)]"/>
      </telephony>
      <services>
	<!-- insert email that is marked a primary
        <email/> -->
	<xsl:apply-templates select="gd:email[@primary='true']"/>
      </services>
      <setup>
        <ringTone/>
        <ringVolume/>
      </setup>
    </contact>
  </xsl:template>
  
  <xsl:template match="at:link">
  <ImageURL><xsl:value-of select="./@href"/></ImageURL>
  </xsl:template>

  <xsl:template match="gd:email">
    <email classifier="private"><xsl:value-of select="./@address"/></email>
  </xsl:template>

  <xsl:template match="gd:phoneNumber">
    <!-- First entry of contact get prio=1 in the fbox -->
    <xsl:variable name="prio">
      <xsl:choose>
        <xsl:when test="position() = 1">1</xsl:when>
        <xsl:otherwise>0</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- evaluate type -->
    <xsl:variable name="type">
      <xsl:choose>
        <xsl:when test="@rel = 'http://schemas.google.com/g/2005#mobile'">mobile</xsl:when>
        <xsl:when test="@rel = 'http://schemas.google.com/g/2005#home'">home</xsl:when>
        <xsl:when test="@rel = 'http://schemas.google.com/g/2005#work'">work</xsl:when>
        <xsl:otherwise>SKIP</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- format number -->
    <xsl:variable name="number">
      <xsl:choose>
	<!-- domestic -->
        <xsl:when test="contains(.,'+49')">
          <!-- +49 / 0 replace -->
          <xsl:variable name="tmpnumber"><xsl:value-of select="concat('0',substring-after(.,'+49'))"/></xsl:variable>
          <!-- only numbers may stay -->
          <xsl:value-of select="translate($tmpnumber,translate($tmpnumber,'1234567890',''),'')"/>
	</xsl:when>
	<!-- foreign -->
        <xsl:otherwise>
          <!-- only number and + may stay -->
          <xsl:value-of select="translate(.,translate(.,'1234567890+',''),'')"/>
	</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>


<!-- generate output -->
    <xsl:if test="$type != 'SKIP'">
      <number type="{$type}" prio="{$prio}"><xsl:value-of select="$number"/></number>
    </xsl:if>
  </xsl:template>

<!-- to seperate filename from url -->
<!-- Template zur Ausgabe des Dateinamens: -->

<xsl:template name="last-substring-after">
  <xsl:param name="string"/>
  <xsl:param name="separator"/>
  <xsl:choose>
    <xsl:when test="contains($string, $separator)">
      <xsl:call-template name="last-substring-after">
        <xsl:with-param name="string"
                        select="substring-after($string, $separator)"/>
        <xsl:with-param name="separator"
                        select="$separator"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$string"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
