<?xml version="1.0" encoding="UTF-8"?>
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-->
<!--!                                                              !-->
<!--!    File Name    : client_profile_template.xsl                !-->
<!--!    Description  : Client Profile template (customized)       !-->
<!--!    Author       : Zubayer Ahamed                             !-->
<!--!    Date         : 25-Feb-2018                                !-->
<!--!    Copyright    : Copyright (c) coderslab.bd.com             !-->
<!--!                                                              !-->
<!--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	exclude-result-prefixes="fo">
	<xsl:template match="client">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<!-- page setup -->
			<fo:layout-master-set>
				<fo:simple-page-master master-name="firstmaster" page-height="29.7cm" page-width="21cm" margin-top="1.5cm" margin-bottom="1.5cm" margin-left="1.5cm" margin-right="1.5cm">
					<fo:region-body margin-top="1.5cm" margin-bottom="1cm"/>
					<fo:region-before region-name="header-first" extent="8cm" />
					<fo:region-after region-name="footer-last" extent="2cm" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="document">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference page-position="first" master-reference="firstmaster" />
						<fo:conditional-page-master-reference page-position="rest" master-reference="firstmaster" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<!-- page content -->
			<fo:page-sequence master-reference="firstmaster">
				<xsl:variable name="pageid" select="generate-id()" />

				<!-- page header -->
				<fo:static-content flow-name="header-first">
					<fo:block-container width="100%">
						<fo:block text-align="center">
							<xsl:variable name="siteLogo" select="siteLogo"/>
							<fo:external-graphic padding="0" margin="0" space-start="0" space-end="0" pause-before="0" pause-after="0" content-height="18px" content-width="150px" scaling="non-uniform" src="url('resources/upload/site/logo/{$siteLogo}')"/>
						</fo:block>
					</fo:block-container>
					<fo:block-container width="100%">
						<fo:block>
							<fo:table width="100%">
								<fo:table-column column-width="70%" />
								<fo:table-column column-width="30%" />
								<fo:table-body>
									<fo:table-cell>
										<fo:block font-size="12pt" font-weight="bold">
											Dealer Profile Info
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="right" font-size="7pt">
											Page No : <fo:page-number></fo:page-number>
										</fo:block>
									</fo:table-cell>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<!-- Page body -->
				<fo:flow flow-name="xsl-region-body">
					<fo:block-container width="70%">
						<fo:block>
							<fo:table table-layout="fixed" width="70%" border-collapse="collapse">
								<fo:table-column column-width="50%" />
								<fo:table-column column-width="50%" />

								<!-- table header -->
								<fo:table-body xsl:use-attribute-sets="table.font.size">
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Client name
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="clientName"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Email
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="email"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Mobile
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="mobile"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Fax
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="fax"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Website
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="website"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Address
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="address"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Status
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="status"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												SMS Service
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="smsService"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Register Date
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="registerDate"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Expire Date
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="expireDate"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Total Donars
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="totalDonar"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Active Donars
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="activeDonar"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block font-weight="bold">
												Inactive Donars
											</fo:block>
										</fo:table-cell>
										<fo:table-cell xsl:use-attribute-sets="dealer.table.td">
											<fo:block>
												<xsl:value-of select="inactiveDonar"/>
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
					<!-- dealer photo -->
					<fo:block-container width="30%" right="0mm" position="absolute">
						<fo:block text-align="right">
							<xsl:if test="photo != ''">
								<xsl:variable name="imagePath" select="photo"/>
								<fo:external-graphic padding="0" margin="0" border="1px solid #000000" space-start="0" space-end="0" pause-before="0" pause-after="0" content-height="110px" content-width="100px" scaling="non-uniform" src="url('resources/upload/client/{$imagePath}')"/>
							</xsl:if>
						</fo:block>
					</fo:block-container>

					<fo:block-container width="100%" margin-top="20px" right="0mm">
						<fo:block>
							<xsl:if test="donars/donar != ''">
								<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
									<fo:table-column column-width="5%" />
									<fo:table-column column-width="16.66%" />
									<fo:table-column column-width="16.66%" />
									<fo:table-column column-width="16.66%" />
									<fo:table-column column-width="11.66%" />
									<fo:table-column column-width="16.66%" />
									<fo:table-column column-width="16.66%" />
	
									<!-- client table header -->
									<fo:table-header xsl:use-attribute-sets="table.font.size">
										<fo:table-row>
											<fo:table-cell number-columns-spanned="7" xsl:use-attribute-sets="donar.table.th">
												<fo:block text-align="center">
													Donars List
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													No
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													Donar name
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													Category
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													Mobile
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													Payable Amount
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													Status
												</fo:block>
											</fo:table-cell>
											<fo:table-cell xsl:use-attribute-sets="donar.table.th">
												<fo:block>
													SMS Service
												</fo:block>
											</fo:table-cell>
										</fo:table-row>
									</fo:table-header>
									<fo:table-body>
										<xsl:apply-templates select="donars/donar"/>
									</fo:table-body>
								</fo:table>
							</xsl:if>
							
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- get Client -->
	<xsl:template match="donars/donar">
		<fo:table-row>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="no"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="donarName"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="categoryName"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="mobile"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="payableAmount"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="status"/>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell xsl:use-attribute-sets="donar.table.td">
				<fo:block>
					<xsl:value-of select="smsService"/>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- stylesheets -->
	<xsl:attribute-set name="border.full">
		<xsl:attribute name="border">1px solid #000000</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="table.font.size">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="dealer.table.td">
		<xsl:attribute name="padding-top">2px</xsl:attribute>
		<xsl:attribute name="padding-bottom">2px</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="donar.table.th">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="donar.table.td">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="padding">2px</xsl:attribute>
		<xsl:attribute name="border">1pt solid #000000</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>