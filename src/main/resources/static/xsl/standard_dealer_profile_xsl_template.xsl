<?xml version="1.0" encoding="UTF-8"?>
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! -->
<!--! ! -->
<!--! File Name : student-template.xsl ! -->
<!--! Description : Student template (customized) ! -->
<!--! Author : Zubayer ! -->
<!--! Date : 17-Jan-2018 ! -->
<!--! Copyright : Copyright (c) coderslab.com ! -->
<!--! ! -->
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! -->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:template match="dealer">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simpleA4"
					page-height="29.7cm" page-width="21cm" margin-top="2cm"
					margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simpleA4">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="16pt" font-weight="bold" space-after="5mm">
						Company Name: Coders Lab Bangladesh
					</fo:block>
					<fo:block font-size="10pt">
						<fo:table table-layout="fixed" width="100%"
							border-collapse="separate">
							<fo:table-column column-width="4cm" />
							<fo:table-column column-width="4cm" />
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="dealerId" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block>
											<xsl:value-of select="dealerName" />
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>