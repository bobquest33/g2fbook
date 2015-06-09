Currently, the transformation does some substitution to the country code in all phone numbers.<br>
If the number on gmail starts with +49, this code will be substituted by 0. Eg. +491234567 will become 01234567.<br>
This is ok as long as you are in Germany, but you will experience problems if not.<br>
<br>
<br><br><br>

To get it sorted out, you will currently have to edit the file googleContacts-2-fritzPhoneBook.xml.<br>
This is the code responsible for the substitution:<br>
<br>
<pre><code>79    &lt;!-- format number --&gt;<br>
80    &lt;xsl:variable name="number"&gt;<br>
81      &lt;xsl:choose&gt;<br>
82        &lt;!-- domestic --&gt;<br>
83        &lt;xsl:when test="contains(.,'+49')"&gt;<br>
84          &lt;!-- +49 / 0 replace --&gt;<br>
85          &lt;xsl:variable name="tmpnumber"&gt;&lt;xsl:value-of select="concat('0',substring-after(.,'+49'))"/&gt;&lt;/xsl:variable&gt;<br>
86          &lt;!-- only numbers may stay --&gt;<br>
87          &lt;xsl:value-of select="translate($tmpnumber,translate($tmpnumber,'1234567890',''),'')"/&gt;<br>
88        &lt;/xsl:when&gt;<br>
89        &lt;!-- foreign --&gt;<br>
90        &lt;xsl:otherwise&gt;<br>
91          &lt;!-- only number and + may stay --&gt;<br>
92          &lt;xsl:value-of select="translate(.,translate(.,'1234567890+',''),'')"/&gt;<br>
93        &lt;/xsl:otherwise&gt;<br>
94      &lt;/xsl:choose&gt;<br>
95    &lt;/xsl:variable&gt;<br>
</code></pre>
<sup>Line numbers apply to r12 of the file</sup><br>
<br>
In line 83 and line 85, you can edit the country code that will be removed (+49)<br>
In line 85 you can edit the number that will replace the country code (0)<br>

If you don't want any substitution that code,<br>
you can simply remove<br>
lines 81 - 90 and 93 - 94