# ************************************************************
# Sequel Pro SQL dump
# Version 4004
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.66-log)
# Database: wasp
# Generation Time: 2013-03-12 22:30:20 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table acct_grant
# ------------------------------------------------------------



# Dump of table acct_grantjob
# ------------------------------------------------------------



# Dump of table acct_invoice
# ------------------------------------------------------------



# Dump of table acct_ledger
# ------------------------------------------------------------



# Dump of table acct_quote
# ------------------------------------------------------------

LOCK TABLES `acct_quote` WRITE;
/*!40000 ALTER TABLE `acct_quote` DISABLE KEYS */;

INSERT INTO `acct_quote` (`id`, `created`, `updated`, `uuid`, `amount`, `comment`, `isactive`, `jobid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:22:17','2013-03-12 12:39:41',X'8E7D5D4224494BFAB5EBAC102DC6EB31',2600,NULL,NULL,1,NULL,1);

/*!40000 ALTER TABLE `acct_quote` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quotemeta
# ------------------------------------------------------------

LOCK TABLES `acct_quotemeta` WRITE;
/*!40000 ALTER TABLE `acct_quotemeta` DISABLE KEYS */;

INSERT INTO `acct_quotemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `quoteid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'4183111F83BA4A80B84B6FAC2675E7FD','acctQuote.library_cost',0,NULL,'600',1,NULL),
	(2,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'4CAC1868E9D64F3FBB842A3FE7885D26','acctQuote.sample_cost',0,NULL,'0',1,NULL),
	(3,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'2247B2E13C3B4E20995274F46BB12B63','acctQuote.lane_cost',0,NULL,'2000',1,NULL);

/*!40000 ALTER TABLE `acct_quotemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quoteuser
# ------------------------------------------------------------



# Dump of table acct_workflowcost
# ------------------------------------------------------------



# Dump of table adaptor
# ------------------------------------------------------------

LOCK TABLES `adaptor` WRITE;
/*!40000 ALTER TABLE `adaptor` DISABLE KEYS */;

INSERT INTO `adaptor` (`id`, `created`, `updated`, `uuid`, `adaptorsetid`, `barcodenumber`, `barcodesequence`, `iname`, `isactive`, `name`, `sequence`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'74CE3F2253344D8E85031666095358F5',1,1,'CGCTGCTG','illuminaHelptagLibrary1',1,'helptag Adaptor','CGCTGCTG',NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A837C8CC868D41FA87A2D0A612C9912B',2,1,'ATCACG','illuminaTrueseqDnaIndexed1',1,'TruSeq Adaptor, Index 1 (ATCACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1ED57115281E490F9D6ADFCDC287EDD1',2,2,'CGATGT','illuminaTrueseqDnaIndexed2',1,'TruSeq Adaptor, Index 2 (CGATGT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(4,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'DD24F801802E40059160FEA94CDA878E',2,3,'TTAGGC','illuminaTrueseqDnaIndexed3',1,'TruSeq Adaptor, Index 3 (TTAGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(5,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'75C5A4D3C3F546CB9F33B26150CE8EBC',2,4,'TGACCA','illuminaTrueseqDnaIndexed4',1,'TruSeq Adaptor, Index 4 (TGACCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(6,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'46C2438D72644CD3B3AE4D0177891551',2,5,'ACAGTG','illuminaTrueseqDnaIndexed5',1,'TruSeq Adaptor, Index 5 (ACAGTG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(7,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'ACB825EC1CEF4EA19E3B7B4BEDEFFF44',2,6,'GCCAAT','illuminaTrueseqDnaIndexed6',1,'TruSeq Adaptor, Index 6 (GCCAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(8,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'7E2A7308F5B7416A92DABE5EEFFE95AF',2,7,'CAGATC','illuminaTrueseqDnaIndexed7',1,'TruSeq Adaptor, Index 7 (CAGATC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(9,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'BFC01DAF7CA449A8A8C6A922D56171B9',2,8,'ACTTGA','illuminaTrueseqDnaIndexed8',1,'TruSeq Adaptor, Index 8 (ACTTGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(10,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'775649FC55FC43B3AA42E46C89519F22',2,9,'GATCAG','illuminaTrueseqDnaIndexed9',1,'TruSeq Adaptor, Index 9 (GATCAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(11,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'DFB519B5DAEC4489BE2C72F43A997A18',2,10,'TAGCTT','illuminaTrueseqDnaIndexed10',1,'TruSeq Adaptor, Index 10 (TAGCTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(12,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1F5A641100534A7EA3108CD0CDE121DA',2,11,'GGCTAC','illuminaTrueseqDnaIndexed11',1,'TruSeq Adaptor, Index 11 (GGCTAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(13,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'C2E47D1ABF1045E7A8329374E85DB2D1',2,12,'CTTGTA','illuminaTrueseqDnaIndexed12',1,'TruSeq Adaptor, Index 12 (CTTGTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(14,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'29C4FCCBA0154ACEB6C16B5ECAB9F0DA',2,13,'AGTCAA','illuminaTrueseqDnaIndexed13',1,'TruSeq Adaptor, Index 13 (AGTCAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(15,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'429A5ECE605B4ED3AEA98F825A6F8102',2,14,'AGTTCC','illuminaTrueseqDnaIndexed14',1,'TruSeq Adaptor, Index 14 (AGTTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(16,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'65F7417AA9AA443A90A6AEAB0A777470',2,15,'ATGTCA','illuminaTrueseqDnaIndexed15',1,'TruSeq Adaptor, Index 15 (ATGTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(17,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'663AF2664CEC4907B1733A69B42F5830',2,16,'CCGTCC','illuminaTrueseqDnaIndexed16',1,'TruSeq Adaptor, Index 16 (CCGTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(18,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'54AF4AF9A596433FB6BEF530B0347DC9',2,17,'GTAGAG','illuminaTrueseqDnaIndexed17',1,'TruSeq Adaptor, Index 17 (GTAGAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(19,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'976447EFE6744C22AD4905AED287D209',2,18,'GTCCGC','illuminaTrueseqDnaIndexed18',1,'TruSeq Adaptor, Index 18 (GTCCGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(20,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'289674BD9F824956887106C292989C68',2,19,'GTGAAA','illuminaTrueseqDnaIndexed19',1,'TruSeq Adaptor, Index 19 (GTGAAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(21,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'4618F8F9BC6744248BE8C74DD14629B8',2,20,'GTGGCC','illuminaTrueseqDnaIndexed20',1,'TruSeq Adaptor, Index 20 (GTGGCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(22,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'3C20CB6CAB544DD980FED6243CC072B3',2,21,'GTTTCG','illuminaTrueseqDnaIndexed21',1,'TruSeq Adaptor, Index 21 (GTTTCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(23,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'2185A85583B44B79935B5CCF2C4FE5FC',2,22,'CGTACG','illuminaTrueseqDnaIndexed22',1,'TruSeq Adaptor, Index 22 (CGTACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(24,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F6AD3ECC38A549958710059CFB2BE9CB',2,23,'GAGTGG','illuminaTrueseqDnaIndexed23',1,'TruSeq Adaptor, Index 23 (GAGTGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(25,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'60A2CFC1592B4FF3AC95AD8AF956934C',2,24,'GGTAGC','illuminaTrueseqDnaIndexed24',1,'TruSeq Adaptor, Index 24 (GGTAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(26,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'D1F370E031B144FABEC4AB0005D7919B',2,25,'ACTGAT','illuminaTrueseqDnaIndexed25',1,'TruSeq Adaptor, Index 25 (ACTGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(27,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CB50600D2D2D4F4E83E102F7E46A2324',2,26,'ATGAGC','illuminaTrueseqDnaIndexed26',1,'TruSeq Adaptor, Index 26 (ATGAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(28,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'533B15B322334435B573156477239FA1',2,27,'ATTCCT','illuminaTrueseqDnaIndexed27',1,'TruSeq Adaptor, Index 27 (ATTCCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(29,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F24F17CA762C4BC0A01B784806CA4BF2',2,28,'CAAAAG','illuminaTrueseqDnaIndexed28',1,'TruSeq Adaptor, Index 28 (CAAAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(30,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'B74DAB4CF9BC497FB2E980B0F6B4879E',2,29,'CAACTA','illuminaTrueseqDnaIndexed29',1,'TruSeq Adaptor, Index 29 (CAACTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(31,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1960178394D74B3695E77F493E9B22FC',2,30,'CACCGG','illuminaTrueseqDnaIndexed30',1,'TruSeq Adaptor, Index 30 (CACCGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(32,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'3F549C17B8744FF1B2817A34B0C6B3B0',2,31,'CACGAT','illuminaTrueseqDnaIndexed31',1,'TruSeq Adaptor, Index 31 (CACGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(33,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'6DD39FCB02104E2C8A4A8A2870EE051A',2,32,'CACTCA','illuminaTrueseqDnaIndexed32',1,'TruSeq Adaptor, Index 32 (CACTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(34,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'90A3FB32443647DEB082FA43D2B9BBF0',2,33,'CAGGCG','illuminaTrueseqDnaIndexed33',1,'TruSeq Adaptor, Index 33 (CAGGCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(35,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'9797718DCE15418AA267CDC19C576BB3',2,34,'CATGGC','illuminaTrueseqDnaIndexed34',1,'TruSeq Adaptor, Index 34 (CATGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(36,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A5D2DEC7E15E4CEE921CB0376DEBD334',2,35,'CATTTT','illuminaTrueseqDnaIndexed35',1,'TruSeq Adaptor, Index 35 (CATTTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(37,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'095D77EFEDA0474AA40F74B52F95C397',2,36,'CCAACA','illuminaTrueseqDnaIndexed36',1,'TruSeq Adaptor, Index 36 (CCAACA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(38,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'4F2363E3D68C41B2A21A48FEB470538E',2,37,'CGGAAT','illuminaTrueseqDnaIndexed37',1,'TruSeq Adaptor, Index 37 (CGGAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(39,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'420A960043E843F587840ECE72F4C539',2,38,'CTAGCT','illuminaTrueseqDnaIndexed38',1,'TruSeq Adaptor, Index 38 (CTAGCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(40,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1C92E9D1CBCA42A88B490DF343D448D0',2,39,'CTATAC','illuminaTrueseqDnaIndexed39',1,'TruSeq Adaptor, Index 39 (CTATAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(41,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'7C9FF8E58255497A907EBC900E3A37FE',2,40,'CTCAGA','illuminaTrueseqDnaIndexed40',1,'TruSeq Adaptor, Index 40 (CTCAGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(42,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A3CE814C316D4FF7B4A0A81E249BF234',2,41,'GACGAC','illuminaTrueseqDnaIndexed41',1,'TruSeq Adaptor, Index 41 (GACGAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(43,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F0FF35CA396B4D82B58B47144A1D2D0A',2,42,'TAATCG','illuminaTrueseqDnaIndexed42',1,'TruSeq Adaptor, Index 42 (TAATCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(44,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'B2693BD950A644DFBF79AD4A8A43AF17',2,43,'TACAGC','illuminaTrueseqDnaIndexed43',1,'TruSeq Adaptor, Index 43 (TACAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(45,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A3AFD8440BCB4EC3AE1101385EB6F385',2,44,'TATAAT','illuminaTrueseqDnaIndexed44',1,'TruSeq Adaptor, Index 44 (TATAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(46,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'0AC9EFD65DBE41DBB68E11654643F140',2,45,'TCATTC','illuminaTrueseqDnaIndexed45',1,'TruSeq Adaptor, Index 45 (TCATTC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(47,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'FCB575F91A154978B3F902D1888391E0',2,46,'TCCCGA','illuminaTrueseqDnaIndexed46',1,'TruSeq Adaptor, Index 46 (TCCCGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(48,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'01B47218078741528C565AFEFC7253CA',2,47,'TCGAAG','illuminaTrueseqDnaIndexed47',1,'TruSeq Adaptor, Index 47 (TCGAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(49,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CD9B9B90F34F4181B24501C730C24BD5',2,48,'TCGGCA','illuminaTrueseqDnaIndexed48',1,'TruSeq Adaptor, Index 48 (TCGGCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL);

/*!40000 ALTER TABLE `adaptor` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptormeta
# ------------------------------------------------------------



# Dump of table adaptorset
# ------------------------------------------------------------

LOCK TABLES `adaptorset` WRITE;
/*!40000 ALTER TABLE `adaptorset` DISABLE KEYS */;

INSERT INTO `adaptorset` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `sampletypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'D433BC2F91F9407697F4CBCAC284B511','helptagLibrary',1,'HELP-tag Library',1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A62D573D5D454A76B44A9E1CF6263CDE','truseqIndexedDna',1,'TruSEQ INDEXED DNA',2,NULL);

/*!40000 ALTER TABLE `adaptorset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetmeta
# ------------------------------------------------------------

LOCK TABLES `adaptorsetmeta` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta` DISABLE KEYS */;

INSERT INTO `adaptorsetmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `adaptorsetid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'DB9FDDF3768B455383FB42FBDC215923','truseqIndexedDna.truseq',1,NULL,'1',2,0);

/*!40000 ALTER TABLE `adaptorsetmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetresourcecategory
# ------------------------------------------------------------

LOCK TABLES `adaptorsetresourcecategory` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory` DISABLE KEYS */;

INSERT INTO `adaptorsetresourcecategory` (`id`, `created`, `updated`, `uuid`, `adaptorsetid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'46BB40EAFEEE4B11BCA6234A272FB9B9',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'80BE51B417F748188FDD86889B126C86',2,1,NULL),
	(3,'2013-03-12 12:39:46','2013-03-12 18:20:20',X'040B0013AB2E48659D87E672469D6DD4',2,2,NULL);

/*!40000 ALTER TABLE `adaptorsetresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table barcode
# ------------------------------------------------------------

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;

INSERT INTO `barcode` (`id`, `created`, `updated`, `uuid`, `barcode`, `barcodefor`, `isactive`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 12:39:41',X'E6D4F2E79BEE4C77A21DBA3FC1BF1E1A','QER344555',NULL,NULL,1),
	(2,'2012-12-20 11:07:41','2013-03-12 12:39:41',X'4A6455F0D69E4684A358A9B07188FFEB','QWE34555',NULL,NULL,1),
	(3,'2012-12-20 11:08:04','2013-03-12 12:39:41',X'69CC29C7F5A04B759AF76D34BC9800AD','FRE45676678',NULL,NULL,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'E9C0F27C4EFF47109210131F8B986CB6','C126NACXX','WASP',1,1),
	(5,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'3CE4D4DD81FA4E439D42F3DA73CB147B','D1884ACXX','WASP',1,1),
	(6,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'0B6E56EC9A5D49F6AF37D6F8BF5213C8','634H7AAXX','WASP',1,1);

/*!40000 ALTER TABLE `barcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`, `VERSION`, `JOB_INSTANCE_ID`, `CREATE_TIME`, `START_TIME`, `END_TIME`, `STATUS`, `EXIT_CODE`, `EXIT_MESSAGE`, `LAST_UPDATED`)
VALUES
	(1,1,1,'2012-12-20 11:20:04','2012-12-20 11:20:05',NULL,'STARTED','UNKNOWN','','2012-12-20 11:20:05'),
	(2,2,2,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:44','COMPLETED','COMPLETED','','2012-12-20 11:23:44'),
	(3,2,3,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:47','COMPLETED','COMPLETED','','2012-12-20 11:23:47'),
	(4,2,4,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:34','COMPLETED','COMPLETED','','2012-12-20 11:23:34'),
	(5,1,5,'2012-12-20 11:24:31','2012-12-20 11:24:31',NULL,'STARTED','UNKNOWN','','2012-12-20 11:24:31'),
	(6,1,6,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07'),
	(7,1,7,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07'),
	(8,1,8,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07');

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_CONTEXT
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_CONTEXT` (`JOB_EXECUTION_ID`, `SHORT_CONTEXT`, `SERIALIZED_CONTEXT`)
VALUES
	(1,'{\"map\":\"\"}',NULL),
	(2,'{\"map\":\"\"}',NULL),
	(3,'{\"map\":\"\"}',NULL),
	(4,'{\"map\":\"\"}',NULL),
	(5,'{\"map\":\"\"}',NULL),
	(6,'{\"map\":\"\"}',NULL),
	(7,'{\"map\":\"\"}',NULL),
	(8,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_SEQ` (`ID`)
VALUES
	(0),
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_INSTANCE
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_INSTANCE` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`, `VERSION`, `JOB_NAME`, `JOB_KEY`)
VALUES
	(1,0,'default.waspJob.jobflow.v1','3f794b8fbab28919466a7935e6eca9de'),
	(2,0,'wasp.sample.jobflow.v1','51334d32df46a49f5100d2f4b05a3075'),
	(3,0,'wasp.sample.jobflow.v1','8bbc65792cb26a927825ca2b3e8b6013'),
	(4,0,'wasp.sample.jobflow.v1','bc9287957155fa18e0927a383947448d'),
	(5,0,'wasp.facilityLibrary.jobflow.v1','734d58c5ddb306e1be1713c8a1f584bd'),
	(6,0,'default.waspJob.jobflow.v1','51ced8531d8f43d8574461e2ab140593'),
	(7,0,'wasp.userLibrary.jobflow.v1','50f1d00f0e7c89f7dbc0da1db6bcc79f'),
	(8,0,'wasp.userLibrary.jobflow.v1','5e0221792fa693d721979abaeb0a073f');

/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_PARAMS
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_PARAMS` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_PARAMS` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_PARAMS` (`JOB_INSTANCE_ID`, `TYPE_CD`, `KEY_NAME`, `STRING_VAL`, `DATE_VAL`, `LONG_VAL`, `DOUBLE_VAL`)
VALUES
	(1,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(2,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(2,'STRING','sampleId','21','1969-12-31 19:00:00',0,0),
	(3,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(3,'STRING','sampleId','22','1969-12-31 19:00:00',0,0),
	(4,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(4,'STRING','sampleId','23','1969-12-31 19:00:00',0,0),
	(5,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(5,'STRING','sampleId','24','1969-12-31 19:00:00',0,0),
	(6,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(7,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(7,'STRING','sampleId','25','1969-12-31 19:00:00',0,0),
	(8,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(8,'STRING','sampleId','26','1969-12-31 19:00:00',0,0);

/*!40000 ALTER TABLE `BATCH_JOB_PARAMS` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_SEQ` (`ID`)
VALUES
	(0),
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION
# ------------------------------------------------------------

LOCK TABLES `BATCH_STEP_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`, `VERSION`, `STEP_NAME`, `JOB_EXECUTION_ID`, `START_TIME`, `END_TIME`, `STATUS`, `COMMIT_COUNT`, `READ_COUNT`, `FILTER_COUNT`, `WRITE_COUNT`, `READ_SKIP_COUNT`, `WRITE_SKIP_COUNT`, `PROCESS_SKIP_COUNT`, `ROLLBACK_COUNT`, `EXIT_CODE`, `EXIT_MESSAGE`, `LAST_UPDATED`)
VALUES
	(1,85225,'default.waspJob.step.listenForExitCondition',1,'2012-12-20 11:20:05',NULL,'STARTED',85224,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(2,5798,'default.waspJob.step.adminApprove',1,'2012-12-20 11:20:05','2012-12-20 11:21:12','COMPLETED',5796,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:21:12'),
	(3,13243,'default.waspJob.step.quote',1,'2012-12-20 11:20:05','2012-12-20 11:22:17','COMPLETED',13241,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:17'),
	(4,14330,'default.waspJob.step.piApprove',1,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14328,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(5,24717,'wasp.sample.step.listenForExitCondition',2,'2012-12-20 11:20:05','2012-12-20 11:23:44','COMPLETED',24715,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(6,14375,'wasp.sample.step.listenForJobApproved',2,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14373,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(7,4342,'wasp.sample.step.listenForSampleReceived',2,'2012-12-20 11:20:05','2012-12-20 11:20:58','COMPLETED',4340,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:20:58'),
	(8,4462,'wasp.sample.step.listenForSampleReceived',3,'2012-12-20 11:20:05','2012-12-20 11:20:59','COMPLETED',4460,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:20:59'),
	(9,14385,'wasp.sample.step.listenForJobApproved',3,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14383,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(10,25070,'wasp.sample.step.listenForExitCondition',3,'2012-12-20 11:20:05','2012-12-20 11:23:47','COMPLETED',25068,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(11,14367,'wasp.sample.step.listenForJobApproved',4,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14365,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(12,23271,'wasp.sample.step.listenForExitCondition',4,'2012-12-20 11:20:05','2012-12-20 11:23:34','COMPLETED',23269,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(13,4529,'wasp.sample.step.listenForSampleReceived',4,'2012-12-20 11:20:05','2012-12-20 11:21:00','COMPLETED',4527,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:21:00'),
	(14,3,'default.waspJob.step.approved',1,'2012-12-20 11:22:25','2012-12-20 11:22:25','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(15,10745,'wasp.sample.step.sampleQC',3,'2012-12-20 11:22:25','2012-12-20 11:23:47','COMPLETED',10743,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(16,10354,'wasp.sample.step.sampleQC',2,'2012-12-20 11:22:25','2012-12-20 11:23:44','COMPLETED',10352,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(17,8953,'wasp.sample.step.sampleQC',4,'2012-12-20 11:22:25','2012-12-20 11:23:34','COMPLETED',8951,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(18,3,'wasp.sample.step.notifySampleAccepted',4,'2012-12-20 11:23:34','2012-12-20 11:23:34','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(19,3,'wasp.sample.step.notifySampleAccepted',2,'2012-12-20 11:23:44','2012-12-20 11:23:44','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(20,3,'wasp.sample.step.notifySampleAccepted',3,'2012-12-20 11:23:47','2012-12-20 11:23:47','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(21,54261,'wasp.library.step.listenForExitCondition',5,'2012-12-20 11:24:31',NULL,'STARTED',54260,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(22,54271,'wasp.library.step.libraryQC',5,'2012-12-20 11:24:31',NULL,'STARTED',54270,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(23,33286,'default.waspJob.step.listenForExitCondition',6,'2012-12-20 11:27:07',NULL,'STARTED',33285,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(24,33356,'default.waspJob.step.quote',6,'2012-12-20 11:27:07',NULL,'STARTED',33355,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(25,33271,'default.waspJob.step.piApprove',6,'2012-12-20 11:27:07',NULL,'STARTED',33270,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(26,33273,'default.waspJob.step.adminApprove',6,'2012-12-20 11:27:07',NULL,'STARTED',33272,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(27,33376,'wasp.library.step.listenForExitCondition',7,'2012-12-20 11:27:07',NULL,'STARTED',33375,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(28,33212,'wasp.library.step.listenForJobApproved',7,'2012-12-20 11:27:07',NULL,'STARTED',33211,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(29,33291,'wasp.library.step.listenForLibraryReceived',7,'2012-12-20 11:27:07',NULL,'STARTED',33290,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(30,33220,'wasp.library.step.listenForJobApproved',8,'2012-12-20 11:27:07',NULL,'STARTED',33219,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(31,33173,'wasp.library.step.listenForLibraryReceived',8,'2012-12-20 11:27:07',NULL,'STARTED',33172,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(32,33200,'wasp.library.step.listenForExitCondition',8,'2012-12-20 11:27:07',NULL,'STARTED',33199,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11');

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION_CONTEXT
# ------------------------------------------------------------

LOCK TABLES `BATCH_STEP_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_CONTEXT` (`STEP_EXECUTION_ID`, `SHORT_CONTEXT`, `SERIALIZED_CONTEXT`)
VALUES
	(1,'{\"map\":\"\"}',NULL),
	(2,'{\"map\":\"\"}',NULL),
	(3,'{\"map\":\"\"}',NULL),
	(4,'{\"map\":\"\"}',NULL),
	(5,'{\"map\":\"\"}',NULL),
	(6,'{\"map\":\"\"}',NULL),
	(7,'{\"map\":\"\"}',NULL),
	(8,'{\"map\":\"\"}',NULL),
	(9,'{\"map\":\"\"}',NULL),
	(10,'{\"map\":\"\"}',NULL),
	(11,'{\"map\":\"\"}',NULL),
	(12,'{\"map\":\"\"}',NULL),
	(13,'{\"map\":\"\"}',NULL),
	(14,'{\"map\":\"\"}',NULL),
	(15,'{\"map\":\"\"}',NULL),
	(16,'{\"map\":\"\"}',NULL),
	(17,'{\"map\":\"\"}',NULL),
	(18,'{\"map\":\"\"}',NULL),
	(19,'{\"map\":\"\"}',NULL),
	(20,'{\"map\":\"\"}',NULL),
	(21,'{\"map\":\"\"}',NULL),
	(22,'{\"map\":\"\"}',NULL),
	(23,'{\"map\":\"\"}',NULL),
	(24,'{\"map\":\"\"}',NULL),
	(25,'{\"map\":\"\"}',NULL),
	(26,'{\"map\":\"\"}',NULL),
	(27,'{\"map\":\"\"}',NULL),
	(28,'{\"map\":\"\"}',NULL),
	(29,'{\"map\":\"\"}',NULL),
	(30,'{\"map\":\"\"}',NULL),
	(31,'{\"map\":\"\"}',NULL),
	(32,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_STEP_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_SEQ` (`ID`)
VALUES
	(0),
	(32);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table confirmemailauth
# ------------------------------------------------------------



# Dump of table department
# ------------------------------------------------------------

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;

INSERT INTO `department` (`id`, `created`, `updated`, `uuid`, `isactive`, `isinternal`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:35','2013-03-12 12:39:41',X'0600D7F8266244A1B6859FB093D53732',1,1,'Internal - Default Department',1),
	(2,'2012-05-23 15:55:35','2013-03-12 12:39:41',X'4F8F79D5CC3A4A63BE2639D92BCF65DC',1,0,'External - Default Department',1),
	(3,'2012-06-14 13:47:08','2013-03-12 12:39:41',X'3FC1CF2A815B40FE924EF12048C3EE3B',1,1,'Genetics',1);

/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table departmentuser
# ------------------------------------------------------------

LOCK TABLES `departmentuser` WRITE;
/*!40000 ALTER TABLE `departmentuser` DISABLE KEYS */;

INSERT INTO `departmentuser` (`id`, `created`, `updated`, `uuid`, `departmentid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-30 19:57:15','2013-03-12 12:39:41',X'B6C57D2B806E4450BB0F8DE05271090D',1,3,1),
	(2,'2012-06-14 13:47:08','2013-03-12 12:39:41',X'A881201ACDE745439CBADDC805D4D8D2',3,3,1),
	(3,'2012-06-14 14:13:04','2013-03-12 12:39:41',X'24F8A6B1AED74C46898601C832289071',2,13,1);

/*!40000 ALTER TABLE `departmentuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table file
# ------------------------------------------------------------



# Dump of table filegroup
# ------------------------------------------------------------



# Dump of table filegroup_rel
# ------------------------------------------------------------



# Dump of table filegroupmeta
# ------------------------------------------------------------



# Dump of table filemeta
# ------------------------------------------------------------



# Dump of table filetype
# ------------------------------------------------------------

LOCK TABLES `filetype` WRITE;
/*!40000 ALTER TABLE `filetype` DISABLE KEYS */;

INSERT INTO `filetype` (`id`, `created`, `updated`, `uuid`, `description`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:44','2013-03-12 18:20:20',X'01D299359B9D466F873071F50AA0B326','A human-readable text based file format for storing DNA sequences alongside their quality scores','fastq',1,'FASTQ',NULL),
	(2,'2013-03-12 12:39:44','2013-03-12 18:20:20',X'1720F1FE13DC4F8E837A112B440B0399','Binary version of a Sequence Alignment/MAP (SAM) file. A generic format for storing large nucleotide sequence alignnments','bam',1,'BAM',NULL),
	(3,'2013-03-12 12:39:46','2013-03-12 18:20:20',X'DE177E2D436143CC92FE5313844EBACE','QC files generated by CASAVA for assessing HiSeq flowcell / sequencing quality','waspIlluminaHiseqQcMetrics',1,'Illumina HiSeq QC Metrics',NULL);

/*!40000 ALTER TABLE `filetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table filetypemeta
# ------------------------------------------------------------



# Dump of table job
# ------------------------------------------------------------

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;

INSERT INTO `job` (`id`, `created`, `updated`, `uuid`, `createts`, `isactive`, `labid`, `name`, `userid`, `viewablebylab`, `workflowid`, `lastupdatebyuser`, `current_quote`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'36D0AF55F9B74DA9AB8DBA837D8EC613','2012-12-20 11:20:04',1,2,'chipseq1',10,0,2,10,NULL),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'30F0DEEE5F1242FCAFA69093D07E7572','2012-12-20 11:27:07',1,2,'chipseq2',10,0,2,10,NULL);

/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobcellselection
# ------------------------------------------------------------

LOCK TABLES `jobcellselection` WRITE;
/*!40000 ALTER TABLE `jobcellselection` DISABLE KEYS */;

INSERT INTO `jobcellselection` (`id`, `created`, `updated`, `uuid`, `cellindex`, `jobid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'33403B8BEA284B24A0C2B73288FFFFC2',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'30E5A435E80048A7A9112D7F96570D47',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'459304390DE14BA0BF6435DDBBDF7683',2,2,NULL);

/*!40000 ALTER TABLE `jobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraft
# ------------------------------------------------------------

LOCK TABLES `jobdraft` WRITE;
/*!40000 ALTER TABLE `jobdraft` DISABLE KEYS */;

INSERT INTO `jobdraft` (`id`, `created`, `updated`, `uuid`, `createts`, `labid`, `name`, `status`, `submittedjobid`, `userid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'53FAC00A2BFF4D51ADD38F878F0F195C','2012-12-20 11:15:26',2,'chipseq1','SUBMITTED',1,10,2,10),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A11C5D31230240C2A867F965A67DA9CC','2012-12-20 11:25:19',2,'chipseq2','SUBMITTED',2,10,2,10);

/*!40000 ALTER TABLE `jobdraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `jobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `jobdraftcellselection` DISABLE KEYS */;

INSERT INTO `jobdraftcellselection` (`id`, `created`, `updated`, `uuid`, `cellindex`, `jobdraftid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CDC1E64D4B504263BCF05C74FA40DD5D',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'D04B8699D0D942489E86F6DAC7FB8697',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'ED0D7B6337404A7497D79A0C42CC076F',2,2,NULL);

/*!40000 ALTER TABLE `jobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftfile
# ------------------------------------------------------------



# Dump of table jobdraftmeta
# ------------------------------------------------------------

LOCK TABLES `jobdraftmeta` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta` DISABLE KEYS */;

INSERT INTO `jobdraftmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `jobdraftid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:15:35','2013-03-12 18:20:20',X'2B414BC52B3749B49AF336D6008777C2','illuminaHiSeq2000.readLength',0,NULL,'100',1,NULL),
	(2,'2012-12-20 11:15:35','2013-03-12 18:20:20',X'6E73EC5D09154644A4640870C4C84B81','illuminaHiSeq2000.readType',0,NULL,'paired',1,NULL),
	(3,'2012-12-20 11:18:37','2013-03-12 12:39:41',X'0D9D036C80DA4631A61804070C1EAFD1','chipSeqPlugin.samplePairsTvsC',0,NULL,'1:3;2:3;',1,10),
	(4,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'3E357CD343C0435EA4BE1D669503E36D','bowtieAligner.mismatches',0,NULL,'2',1,NULL),
	(5,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'C4BD998792DC421AB6C4C8C99FABBE7A','bowtieAligner.seedLength',0,NULL,'32',1,NULL),
	(6,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'88945C0A7B2C48B8829E7ECEDE74A572','bowtieAligner.reportAlignmentNum',0,NULL,'1',1,NULL),
	(7,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'D0540B8E6C8A4EA6A05C2F4B06B7420C','bowtieAligner.discardThreshold',0,NULL,'1',1,NULL),
	(8,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'8F64A0AD6373400C828645DD4C157F36','bowtieAligner.isBest',0,NULL,'yes',1,NULL),
	(9,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'E6FB6714F5A1474D8444B256D93FBC0B','macsPeakcaller.pValueCutoff',0,NULL,'100000',1,NULL),
	(10,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'9C87C487C5C848DCA79BA9FBCA247A7A','macsPeakcaller.bandwidth',0,NULL,'300',1,NULL),
	(11,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'309870D4ED504AC0AD21E3560DBEC310','macsPeakcaller.genomeSize',0,NULL,'1000000000',1,NULL),
	(12,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'21F788CCC1CD44429E243D40C2087FB2','macsPeakcaller.keepDup',0,NULL,'no',1,NULL),
	(13,'2012-12-20 11:20:03','2013-03-12 12:39:41',X'930CFB21A7C54A78958C81842A47F1B6','statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f',0,NULL,'User-submitted Job Comment::Please expedite. Grant deadline approaching!',1,10),
	(14,'2012-12-20 11:25:27','2013-03-12 18:20:20',X'D7891087406A4E99B16E6863C9277FB5','illuminaHiSeq2000.readLength',0,NULL,'100',2,NULL),
	(15,'2012-12-20 11:25:27','2013-03-12 18:20:20',X'B979FCBF050344F9A0EA377D3F4DDD43','illuminaHiSeq2000.readType',0,NULL,'paired',2,NULL),
	(16,'2012-12-20 11:26:56','2013-03-12 12:39:41',X'D6F43276F26B42CBA6EE65B6C868229D','chipSeqPlugin.samplePairsTvsC',0,NULL,'5:4;',2,10),
	(17,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'7B1CE9A25CA047ED9FD56F150CDE2DE9','bowtieAligner.mismatches',0,NULL,'2',2,NULL),
	(18,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'C69A3C7783EF4FD7BEEDBF9DA83D013B','bowtieAligner.seedLength',0,NULL,'32',2,NULL),
	(19,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'A8EAE3257FB242B8B68FD9FA3563A818','bowtieAligner.reportAlignmentNum',0,NULL,'1',2,NULL),
	(20,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'791E465E188648D1BB799D682B957FD2','bowtieAligner.discardThreshold',0,NULL,'1',2,NULL),
	(21,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'F3240694CA7C4A23BA776B69EC22559A','bowtieAligner.isBest',0,NULL,'yes',2,NULL),
	(22,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'1A2DEC06ECD44D20A45209178742E1E5','macsPeakcaller.pValueCutoff',0,NULL,'100000',2,NULL),
	(23,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'6B8BFEBD54A348749DA5BD7DAE1C9948','macsPeakcaller.bandwidth',0,NULL,'300',2,NULL),
	(24,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'5A3A6640ED2348DBA917FB1C088EDF67','macsPeakcaller.genomeSize',0,NULL,'1000000000',2,NULL),
	(25,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'16A2D17D40544E8F8C2E0DE6C20EB6C1','macsPeakcaller.keepDup',0,NULL,'no',2,NULL);

/*!40000 ALTER TABLE `jobdraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobdraftresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory` DISABLE KEYS */;

INSERT INTO `jobdraftresourcecategory` (`id`, `created`, `updated`, `uuid`, `jobdraftid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:15:30','2013-03-12 12:39:41',X'F307C284860B4461BD50ABEFB53F0B82',1,1,10),
	(2,'2012-12-20 11:25:22','2013-03-12 12:39:41',X'EE3F2413C44E4D4C84867CB618C9A534',2,1,10);

/*!40000 ALTER TABLE `jobdraftresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftsoftware
# ------------------------------------------------------------

LOCK TABLES `jobdraftsoftware` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware` DISABLE KEYS */;

INSERT INTO `jobdraftsoftware` (`id`, `created`, `updated`, `uuid`, `jobdraftid`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:18:39','2013-03-12 12:39:41',X'1F86469592304CE994AB0193E09EAB96',1,1,10),
	(2,'2012-12-20 11:18:43','2013-03-12 12:39:41',X'77358ABCB2414D12AA3405399A036694',1,2,10),
	(3,'2012-12-20 11:26:58','2013-03-12 12:39:41',X'0DC80BE22D2742F7AD4F013163747949',2,1,10),
	(4,'2012-12-20 11:27:01','2013-03-12 12:39:41',X'E5DD1BE83332485AAF302CB4862F429C',2,2,10);

/*!40000 ALTER TABLE `jobdraftsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobfile
# ------------------------------------------------------------



# Dump of table jobmeta
# ------------------------------------------------------------

LOCK TABLES `jobmeta` WRITE;
/*!40000 ALTER TABLE `jobmeta` DISABLE KEYS */;

INSERT INTO `jobmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `jobid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'7A66A16CC47543F18C4FADE9492B8BA8','illuminaHiSeq2000.readLength',0,NULL,'100',1,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'21D7DB4FDDAC4AEABE80A7B5D7D37D16','illuminaHiSeq2000.readType',0,NULL,'paired',1,10),
	(3,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'FD40B5C2186D415292952D561166AFC1','chipSeqPlugin.samplePairsTvsC',0,NULL,'1:3;2:3;',1,10),
	(4,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0DE62BF82CDF4DF8AD0FB944809B1D8D','bowtieAligner.mismatches',0,NULL,'2',1,10),
	(5,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CD301F39A53F4800A99662C86485A8CB','bowtieAligner.seedLength',0,NULL,'32',1,10),
	(6,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D2099ACCBC1744718B46F4FD3249C49D','bowtieAligner.reportAlignmentNum',0,NULL,'1',1,10),
	(7,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'FF64189D307D496E876C56EAA145B1A9','bowtieAligner.discardThreshold',0,NULL,'1',1,10),
	(8,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'2814E7D88A1F43D3B638380A47C9351C','bowtieAligner.isBest',0,NULL,'yes',1,10),
	(9,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'844B74CAF233487286C89707E2DDD135','macsPeakcaller.pValueCutoff',0,NULL,'100000',1,10),
	(10,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'84D67513FB1E46E4A0934A53951A5B1A','macsPeakcaller.bandwidth',0,NULL,'300',1,10),
	(11,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'508D3E379FED42268A3F849F5E2323C5','macsPeakcaller.genomeSize',0,NULL,'1000000000',1,10),
	(12,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5F621AF7B9BE417DB9EE681C9AD68E62','macsPeakcaller.keepDup',0,NULL,'no',1,10),
	(13,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0639FC0AB8984BFB9B879D667712F0FE','statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f',0,NULL,'User-submitted Job Comment::Please expedite. Grant deadline approaching!',1,10),
	(14,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'3703E092646B4D6A8971092FC0EAF993','illuminaHiSeq2000.readLength',0,NULL,'100',2,10),
	(15,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'4FEDB2FD3DBB4693AF91CCD64758FEB6','illuminaHiSeq2000.readType',0,NULL,'paired',2,10),
	(16,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'6B4B06E24FE346F5ADB5045407F28490','chipSeqPlugin.samplePairsTvsC',0,NULL,'5:4;',2,10),
	(17,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'1C4218E66B1948BA8EE429BAF64E9FDA','bowtieAligner.mismatches',0,NULL,'2',2,10),
	(18,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'C60111B7FFC44C2C8A3B023F0C48F9BA','bowtieAligner.seedLength',0,NULL,'32',2,10),
	(19,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'B9F1300AECE8473EB2DCAC53E4AE6977','bowtieAligner.reportAlignmentNum',0,NULL,'1',2,10),
	(20,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'9E4EDF5094894DE29F272D959552D0E2','bowtieAligner.discardThreshold',0,NULL,'1',2,10),
	(21,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A1CE15AC47324FA5A29F0358AD92036D','bowtieAligner.isBest',0,NULL,'yes',2,10),
	(22,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'29AF18E361D64C9CB076CB844216B747','macsPeakcaller.pValueCutoff',0,NULL,'100000',2,10),
	(23,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'35AFE60ABDF549C1A741831424348A7F','macsPeakcaller.bandwidth',0,NULL,'300',2,10),
	(24,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'CBA1605DDBEC4ECCB2335092600656CB','macsPeakcaller.genomeSize',0,NULL,'1000000000',2,10),
	(25,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'095AF7364D34459F8CCD90CF6CBF77A7','macsPeakcaller.keepDup',0,NULL,'no',2,10);

/*!40000 ALTER TABLE `jobmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory` DISABLE KEYS */;

INSERT INTO `jobresourcecategory` (`id`, `created`, `updated`, `uuid`, `jobid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D069A1FF224C43A0ADE999AA2CF56C15',1,1,10),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'35D1A90B8CC3494C819000233B799347',2,1,10);

/*!40000 ALTER TABLE `jobresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsample
# ------------------------------------------------------------

LOCK TABLES `jobsample` WRITE;
/*!40000 ALTER TABLE `jobsample` DISABLE KEYS */;

INSERT INTO `jobsample` (`id`, `created`, `updated`, `uuid`, `jobid`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1DC5B9E2253442F1972943AD50213208',1,21,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5470E73ECD304346AF3BCB8CF0B7EE35',1,22,10),
	(3,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'4A7F4DCDE2C942D28000529620D9B618',1,23,10),
	(4,'2012-12-20 11:24:31','2013-03-12 12:39:41',X'9412EFF595544BE6A1C2B938820E1AAB',1,24,1),
	(5,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'224E4CF8393A468FAB547D62A59FF0A2',2,25,10),
	(6,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'4C2324626C7047B1B8CD876A817899B1',2,26,10);

/*!40000 ALTER TABLE `jobsample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsamplemeta
# ------------------------------------------------------------



# Dump of table jobsoftware
# ------------------------------------------------------------

LOCK TABLES `jobsoftware` WRITE;
/*!40000 ALTER TABLE `jobsoftware` DISABLE KEYS */;

INSERT INTO `jobsoftware` (`id`, `created`, `updated`, `uuid`, `jobid`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'836C82CFD5C34960A506B46DDA97B0FE',1,1,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'892792FA25B5473BB3E118D32B070AEA',1,2,10),
	(3,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'7A6D0B1DB5664D3FBC650A99B0481447',2,1,10),
	(4,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A0DE50A80D1D401D9EDF84BCC2C8E537',2,2,10);

/*!40000 ALTER TABLE `jobsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobuser
# ------------------------------------------------------------

LOCK TABLES `jobuser` WRITE;
/*!40000 ALTER TABLE `jobuser` DISABLE KEYS */;

INSERT INTO `jobuser` (`id`, `created`, `updated`, `uuid`, `jobid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'24EF9B555AFE4BBFB70F3315F53278CA',1,9,10,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'556AB7474A0E4010A10E3AA51F0B2F23',1,10,5,10),
	(3,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'312D55702B494548AEDA82257AA9D190',2,9,10,10),
	(4,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'80C538D387864FCDA2AEC4945311714F',2,10,5,10);

/*!40000 ALTER TABLE `jobuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table lab
# ------------------------------------------------------------

LOCK TABLES `lab` WRITE;
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;

INSERT INTO `lab` (`id`, `created`, `updated`, `uuid`, `departmentid`, `isactive`, `name`, `primaryuserid`, `lastupdatebyuser`)
VALUES
	(1,'2012-06-14 14:08:21','2013-03-12 12:39:41',X'941E253E1EDC48ABB54A51C2EDFF4C4A',1,1,'Default lab',1,1),
	(2,'2012-06-14 14:07:33','2013-03-12 12:39:41',X'23F247CE1672458097A7465806AC1AC4',3,1,'Cancer Genetics',5,1),
	(3,'2012-06-14 14:07:27','2013-03-12 12:39:41',X'423F5A4E97E2453396CB8AA73717BBF0',3,1,'Godwin Lab',7,1),
	(4,'2012-06-14 14:06:49','2013-03-12 12:39:41',X'713C9DD331F54F00A4AA8DD61D11C4AC',3,1,'Williams Lab',8,1),
	(5,'2012-06-14 14:05:52','2013-03-12 12:39:41',X'8ED1BB7CD0ED42609600E4CBE1EB2703',2,1,'Zebra Fish Lab',9,1);

/*!40000 ALTER TABLE `lab` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labmeta
# ------------------------------------------------------------

LOCK TABLES `labmeta` WRITE;
/*!40000 ALTER TABLE `labmeta` DISABLE KEYS */;

INSERT INTO `labmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `labid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'63C2A4BAEDB74A9CA73A04D3FE190614','lab.internal_external_lab',0,NULL,'internal',2,NULL),
	(2,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'557986714D7E4A7390F269D1E08A64D5','lab.phone',0,NULL,'718-123-4567',2,NULL),
	(3,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'41D0FF826324422A9EEF500974142A49','lab.building_room',0,NULL,'Price 220',2,NULL),
	(4,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'EC7EED5C1FC142278492236270B102A8','lab.billing_contact',0,NULL,'John Greally',2,NULL),
	(5,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'3DAED9710C814667AC67970EF30D72EC','lab.billing_institution',0,NULL,'Einstein',2,NULL),
	(6,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'12B988232CDD49218D12DF04C88510BE','lab.billing_departmentId',0,NULL,'3',2,NULL),
	(7,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'4846665F0F024DC18CDDB05F2A55F1C9','lab.billing_building_room',0,NULL,'Price 220',2,NULL),
	(8,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'574D6A2F5E444F088E5AFF17515D83DF','lab.billing_address',0,NULL,'1301 Morris Park Ave.',2,NULL),
	(9,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'730B4683C8094D1784FB4E7796C50659','lab.billing_city',0,NULL,'Bronx',2,NULL),
	(10,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A00A28C0B2C14A5DBDFFAF005844966F','lab.billing_state',0,NULL,'NY',2,NULL),
	(11,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'770F142D62FB430D9EC82BB1D4FC72CB','lab.billing_country',0,NULL,'US',2,NULL),
	(12,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'6F9F7D3D9D0141B19FCA62CCC75ACFC8','lab.billing_zip',0,NULL,'10461',2,NULL),
	(13,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A7FB728067AD48A785D07FE98B7C4C4E','lab.billing_phone',0,NULL,'718-123-4567',2,NULL),
	(14,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'5E094706824542568A1A97D1274E436E','lab.internal_external_lab',0,NULL,'internal',3,NULL),
	(15,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'44477DEA40C945EDADFA8E2FAF209464','lab.phone',0,NULL,'718-678-1112',3,NULL),
	(16,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'320DC89BE7E146E5882240EBD5EF152E','lab.building_room',0,NULL,'Price 353',3,NULL),
	(17,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'5F9DA7EEAAFD44DA953BB9AE57D5343C','lab.billing_contact',0,NULL,'Aaron Goldin',3,NULL),
	(18,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'323E8FF304034F17989B48C9B1BC8502','lab.billing_institution',0,NULL,'Einstein',3,NULL),
	(19,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'DC2C37A4B4B8476EAF8743746AE3B79E','lab.billing_departmentId',0,NULL,'3',3,NULL),
	(20,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'BA8734BE5126414AA29F701331D60535','lab.billing_building_room',0,NULL,'Price 353',3,NULL),
	(21,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'D244C77610A2420596B4348F5A6AAB12','lab.billing_address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(22,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'A06C45B67D3E416DB6E06556AC9AE142','lab.billing_city',0,NULL,'Bronx',3,NULL),
	(23,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'E146A709033F4CEEBAA13371971F3F4E','lab.billing_state',0,NULL,'NY',3,NULL),
	(24,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'087D32B89E4849FE91EE352D048B9353','lab.billing_country',0,NULL,'US',3,NULL),
	(25,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'17A7289B4B104A03891212D4A5FD5FB4','lab.billing_zip',0,NULL,'10461',3,NULL),
	(26,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'B847BE70F9F74C2D85D8830019BABB3D','lab.billing_phone',0,NULL,'718-678-1112',3,NULL),
	(27,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'FE571184D47F4BBAA3BCB98216A28CD8','lab.internal_external_lab',0,NULL,'internal',4,NULL),
	(28,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'32046E36DBD246A4AB8F147A54BF3FA0','lab.phone',0,NULL,'718-678-1019',4,NULL),
	(29,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'CDBFA00D096D4195AF4ACE4E128A0BB3','lab.building_room',0,NULL,'Price 321',4,NULL),
	(30,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'953DAA481AF041FF89C2AB67A6CA807B','lab.billing_contact',0,NULL,'Adam Auton',4,NULL),
	(31,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'8046A75FD5BE42849E8D79D1452AE882','lab.billing_institution',0,NULL,'Einstein',4,NULL),
	(32,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'96A79F78EEFF4FDD98D3D8CA1B13AE3B','lab.billing_departmentId',0,NULL,'3',4,NULL),
	(33,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'D9345AFBED634570ADF8FF2801BD64C0','lab.billing_building_room',0,NULL,'Price 321',4,NULL),
	(34,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'67D0AB5902E945B8B8E20F5AD43CF881','lab.billing_address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(35,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'3A0742AD0DD14F43BDD1BD0C21BB2ED9','lab.billing_city',0,NULL,'Bronx',4,NULL),
	(36,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'9E461DFB365A465F871E521B56CDDEE6','lab.billing_state',0,NULL,'NY',4,NULL),
	(37,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'0B2CEA5D66C44AC988C8CBB1B448EF50','lab.billing_country',0,NULL,'US',4,NULL),
	(38,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'EBB205A2F8754FF1947E776246BF37C4','lab.billing_zip',0,NULL,'10461',4,NULL),
	(39,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'6A05B161A2D342538ABEC44264853B36','lab.billing_phone',0,NULL,'718-678-1019',4,NULL),
	(40,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'93197D8F33A0476589AB7615365072BC','lab.internal_external_lab',0,NULL,'external',5,NULL),
	(41,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'5225040D48ED4088B03311BDB21EEE37','lab.phone',0,NULL,'212-321-1091',5,NULL),
	(42,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'CE73E8FB04334BEEB0F42FF56FE89441','lab.building_room',0,NULL,'Hammer 1101',5,NULL),
	(43,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'A1795F7F414448CFBB04AC2D173D122F','lab.billing_contact',0,NULL,'Leslie Trokie',5,NULL),
	(44,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'3FE6D464ACD943F28788AA94B4643A1A','lab.billing_institution',0,NULL,'NYU Medical',5,NULL),
	(45,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'0366FFD126AA4712B3C63D163408CD1F','lab.billing_departmentId',0,NULL,'3',5,NULL),
	(46,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'FCA4BA3B40A746AFA84E3ED26331E184','lab.billing_building_room',0,NULL,'Hammer 1101',5,NULL),
	(47,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'6E949A7357BE4D438DD009738033326E','lab.billing_address',0,NULL,'16-50 32nd Street',5,NULL),
	(48,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'94EBA94D5F0A4F3DA3E29D2994FF73F3','lab.billing_city',0,NULL,'New York',5,NULL),
	(49,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'FF2A76A7B6134931BC8F7933657A2213','lab.billing_state',0,NULL,'NY',5,NULL),
	(50,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'D86CE42B3B9F45D8B878E5C5191424E7','lab.billing_country',0,NULL,'US',5,NULL),
	(51,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'9904A5A9AC674AEB976A97D7FF56CC46','lab.billing_zip',0,NULL,'10002',5,NULL),
	(52,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'0AAF3AAC74F14945956693B62A10230D','lab.billing_phone',0,NULL,'212-321-1091',5,NULL),
	(53,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'1906738D78B84877A5F23229A6A6A54D','lab.internal_external_lab',0,NULL,'internal',1,NULL),
	(54,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'73EA09CB2E9D4836BA1FF45B46C43E7B','lab.phone',0,NULL,'N/A',1,NULL),
	(55,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'C79604793E0446148B4DCA0F70894F18','lab.building_room',0,NULL,'N/A',1,NULL),
	(56,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'ABF6C4F45948459C9B228320FBE82986','lab.billing_contact',0,NULL,'N/A',1,NULL),
	(57,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'3DE24864434540D4B1946B99DA8DD492','lab.billing_institution',0,NULL,'N/A',1,NULL),
	(58,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'F09B44E49B434DD0A87D33FCB55998A5','lab.billing_departmentId',0,NULL,'1',1,NULL),
	(59,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'A737E90224DD47E4BF0CEA110C9EE4FF','lab.billing_building_room',0,NULL,'N/A',1,NULL),
	(60,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'938D953D39C1496BAC715865BCE795AC','lab.billing_address',0,NULL,'N/A',1,NULL),
	(61,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'7FC6469F71DB46DDAC80D40BD47465D6','lab.billing_city',0,NULL,'N/A',1,NULL),
	(62,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'FC2374CCE9914288B2D100D4A6D66266','lab.billing_state',0,NULL,'NY',1,NULL),
	(63,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'70697A3125D4431C8C0AA73BA22C0477','lab.billing_country',0,NULL,'US',1,NULL),
	(64,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'C0D22C73D08247F086D594D00D00BF99','lab.billing_zip',0,NULL,'N/A',1,NULL),
	(65,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'2BCCB620F5D947A7ADE7FF29E08DCD71','lab.billing_phone',0,NULL,'N/A',1,NULL);

/*!40000 ALTER TABLE `labmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labpending
# ------------------------------------------------------------



# Dump of table labpendingmeta
# ------------------------------------------------------------



# Dump of table labuser
# ------------------------------------------------------------

LOCK TABLES `labuser` WRITE;
/*!40000 ALTER TABLE `labuser` DISABLE KEYS */;

INSERT INTO `labuser` (`id`, `created`, `updated`, `uuid`, `labid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:46','2013-03-12 12:39:41',X'013E4F12B503438E872CF4BF6DFF6E05',1,6,1,1),
	(2,'2012-05-30 20:22:24','2013-03-12 12:39:41',X'560BBFF18C724606A37BB9E8F13EDDB2',2,6,5,3),
	(3,'2012-05-30 21:13:54','2013-03-12 12:39:41',X'F569EE4186A944598B3ECEB56B47796A',2,8,6,5),
	(4,'2012-05-30 22:03:56','2013-03-12 12:39:41',X'9B0A0DC86F9444AC81835CAC63D77112',3,6,7,1),
	(5,'2012-05-31 13:59:23','2013-03-12 12:39:41',X'99837551B5884E2BAA098A1B211B45E7',4,6,8,3),
	(6,'2012-05-31 14:00:02','2013-03-12 12:39:41',X'8EB4A086650949BEA30028DE93BE09B3',5,6,9,1),
	(7,'2012-05-31 14:02:38','2013-03-12 12:39:41',X'89F17FF358774A6591FF3B1A62E672E4',2,7,10,5),
	(8,'2012-05-31 14:15:29','2013-03-12 12:39:41',X'12D310ECF107431D975A99E632FCE330',3,8,12,7);

/*!40000 ALTER TABLE `labuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table meta
# ------------------------------------------------------------



# Dump of table project
# ------------------------------------------------------------



# Dump of table resource
# ------------------------------------------------------------

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;

INSERT INTO `resource` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 12:39:41',X'3D34408426E84F5993615A98AAEEF6EB','Basil',1,'Basil',2,1,1),
	(2,'2012-12-20 11:07:41','2013-03-12 12:39:41',X'594F181283B745FA96D6F5A12EE75331','Sybil',1,'Sybil',2,1,1),
	(3,'2012-12-20 11:08:04','2013-03-12 12:39:41',X'157FDCA7A30F4A2EB0FB91FE34B92E75','Manuel',1,'Manuel',2,2,1);

/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcebarcode
# ------------------------------------------------------------

LOCK TABLES `resourcebarcode` WRITE;
/*!40000 ALTER TABLE `resourcebarcode` DISABLE KEYS */;

INSERT INTO `resourcebarcode` (`id`, `created`, `updated`, `uuid`, `barcodeid`, `resourceid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'03F0048B61EB4B33854AD3820C37CF84',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FFB8285BF0D2430F85A9E942FA5A8A35',2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3EC6F146A21143BDB6CF3B65CCA710D0',3,3,NULL);

/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory
# ------------------------------------------------------------

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;

INSERT INTO `resourcecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'ACAF0DF14AC8486D84E6DE9C99C35183','illuminaHiSeq2000',1,'Illumina HiSeq 2000',2,0),
	(2,'2012-12-20 11:03:32','2013-03-12 18:19:05',X'AB92B77D27454A44AD550FC4690DE1F6','illuminaMiSeqPersonalSequencer',1,'Illumina MiSeq Personal Sequencer',2,0);

/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'3B4E6C3D4F07478DB2DCEC1A7791AC94','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired',1,0),
	(2,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'C32D031490924449A0A1387B566199DB','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150',1,0),
	(3,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'7564F451030F4E8A8113F80D3479FD27','illuminaHiSeq2000.platformUnitSelector',3,NULL,'A:A;B:B',1,0),
	(4,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'508123F338284DCE92E2AC83E40B8918','illuminaMiSeqPersonalSequencer.allowableUiField.readType',1,NULL,'single:single;paired:paired',2,0),
	(5,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'DB39A10C06114C698F6A1B889A7B5C0C','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',2,NULL,'25:25;36:36;50:50;100:100;150:150',2,0);

/*!40000 ALTER TABLE `resourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecell
# ------------------------------------------------------------



# Dump of table resourcemeta
# ------------------------------------------------------------

LOCK TABLES `resourcemeta` WRITE;
/*!40000 ALTER TABLE `resourcemeta` DISABLE KEYS */;

INSERT INTO `resourcemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `resourceid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 18:20:20',X'C5BB896E6A3744A28317AF908A05C2AE','resource.commission_date',0,NULL,'2012-10-03',1,NULL),
	(2,'2012-12-20 11:07:21','2013-03-12 18:20:20',X'B6153AFAD64A444C9BDEBD982295FDB6','resource.decommission_date',0,NULL,'',1,NULL),
	(3,'2012-12-20 11:07:41','2013-03-12 18:20:20',X'33A17C3BBB9C487ABC8F2CF318B73495','resource.commission_date',0,NULL,'2012-08-02',2,NULL),
	(4,'2012-12-20 11:07:41','2013-03-12 18:20:20',X'36621C0C70DC4EEA860AC5CF7CDD350C','resource.decommission_date',0,NULL,'',2,NULL),
	(5,'2012-12-20 11:08:04','2013-03-12 18:20:20',X'F7EC9B745E5B466DB18CC39C91FD157C','resource.commission_date',0,NULL,'2012-05-24',3,NULL),
	(6,'2012-12-20 11:08:04','2013-03-12 18:20:20',X'0952142168D94A09AED62890CFCC909B','resource.decommission_date',0,NULL,'',3,NULL);

/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype
# ------------------------------------------------------------

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;

INSERT INTO `resourcetype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'2E983A6CB6AA4CC684361306E60B278A','aligner',0,'Aligner',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'2341D262EEC245EA8E0730DB150D398C','mps',1,'Massively Parallel DNA Sequencer',NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'81495112AA1541919E1D5425A956CEB9','amplicon',1,'DNA Amplicon',NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3DA110F3F99F43E9A30C8F62F4CC7B4B','peakcaller',1,'Peak Caller',NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'77C2DE20FD924DA98613F8B190A2869F','sequenceRunProcessor',1,'Sequence Run Processor',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BB531174C958478892E5BB0AE85EAB26','bisulfiteSeqPipeline',1,'Bi-sulphite-seq Pipeline',NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B9AE73ED75264B3C95467AEBCADCBC0D','helptagPipeline',1,'HELP-tag Pipeline',NULL),
	(8,'2013-03-12 12:39:44','2013-03-12 18:20:20',X'F749BFB96A624F048276DE808C5E4E1D','referenceBasedAligner',1,'Reference-based Aligner',NULL);

/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roleset
# ------------------------------------------------------------

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;

INSERT INTO `roleset` (`id`, `created`, `updated`, `uuid`, `childroleid`, `parentroleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'625D4EFC70E14C98A560E50F8F91B259',4,4,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3650068744664DF3A2CB54B914B01F6C',1,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CA88D3335BB94DE6BDC5F34A06617FA6',5,5,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B5D4653C948948F39D679904F6F71ECD',3,3,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8BC1E403045E4588A741E6210522A8F4',14,14,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D2AE85AB31FD4DE2AEDE6B26EF51A994',9,9,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'EACCB52327FA4E73AB61C34A743556EF',10,10,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'98C8DB18CBBF456AB96A6819722D6DB5',7,7,NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5DCFDC78DFC44A0E96ECCFB2225FCF32',13,13,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5EB0A6FADFE148FDB32BB5A50DE004E0',8,8,NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'6CF698B68CBD43BD8949D0E26C143750',12,12,NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D102A3C819F04F319EA21683A29B26ED',6,6,NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3D0EB3C03063419D9DB532A35CB804B2',2,2,NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'38DBD848C0F74FBF8C3452B450B6B06D',11,11,NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'359FA0887C2E4645AF14528C7C367180',15,15,NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'7BE12CE4D26F4928AFC0C6E976992D69',5,1,NULL),
	(17,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CC731AC0FD2E438F898D32731620193C',7,6,NULL),
	(18,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FE7348CAB944DE583E041158809D86E',8,6,NULL),
	(19,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'C836C4DCEF124828B5429CFA61B8115F',8,7,NULL),
	(20,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D8850F3408684930AD64D00512979DDB',10,9,NULL),
	(21,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'F476FB317231445ABC4084FD2582080B',1,11,NULL),
	(22,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B9A6915609BA4062991E9BEDED4C7B68',2,11,NULL),
	(23,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CD0351630C9B4726A993DBB1B63CE559',3,11,NULL),
	(24,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'AFE6BBF22AEF453BA98DEFD35F6519E6',5,11,NULL),
	(25,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'09BE517B49CF431CA329E59D1D2706B2',16,16,NULL);

/*!40000 ALTER TABLE `roleset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table run
# ------------------------------------------------------------



# Dump of table runcell
# ------------------------------------------------------------



# Dump of table runcellfile
# ------------------------------------------------------------



# Dump of table runfile
# ------------------------------------------------------------



# Dump of table runmeta
# ------------------------------------------------------------



# Dump of table sample
# ------------------------------------------------------------

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;

INSERT INTO `sample` (`id`, `created`, `updated`, `uuid`, `isactive`, `isgood`, `isreceived`, `name`, `parentid`, `receivedts`, `receiver_userid`, `samplesubtypeid`, `sampletypeid`, `submitter_jobid`, `submitter_labid`, `submitter_userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'21C067CBCD5E4A5D9CF336122A57CC6C',1,1,1,'C126NACXX',NULL,'2012-12-20 11:09:20',NULL,10,5,NULL,1,1,1),
	(2,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'E3D2122DD8774E6A80DC4E9ABDC792D8',1,1,1,'C126NACXX/1',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(3,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'2B883563E874413AAE644215B77F5C31',1,1,1,'C126NACXX/2',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'2FC2EF97688C42019A30B1D5CCFF31B5',1,1,1,'C126NACXX/3',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(5,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'F2B63DB0978945FCA8C22FECD1F49F7B',1,1,1,'C126NACXX/4',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(6,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'91BC6122ADB54014BFC35A6AD2B9E44D',1,1,1,'C126NACXX/5',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(7,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'C4FF093DF0EC4A158B437A39CDFED733',1,1,1,'C126NACXX/6',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(8,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'56F36A53FE2F42CE87D972AC6F24157A',1,1,1,'C126NACXX/7',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(9,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A025EB5DE3584149A6ACB28F69A74C53',1,1,1,'C126NACXX/8',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(10,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7BDBF52F1F4A4A1EA4957E9E5E9AB07D',1,1,1,'D1884ACXX',NULL,'2012-12-20 11:09:48',NULL,10,5,NULL,1,1,1),
	(11,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'B7ADA8B094AA4A00A1FAB767493B892F',1,1,1,'D1884ACXX/1',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(12,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'AC2302D836F44407BD95253445E1106D',1,1,1,'D1884ACXX/2',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(13,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7BCCEC84415A4A56A2A2EF3611318795',1,1,1,'D1884ACXX/3',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(14,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'C4CFC201F13C48A085FAC0678C530DB6',1,1,1,'D1884ACXX/4',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(15,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'9D1E4202B0B041E6967BD45D3DA65068',1,1,1,'D1884ACXX/5',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(16,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'9EF563B0B8CD438884B68953F85C7569',1,1,1,'D1884ACXX/6',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(17,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'268BC89C99BE41B1AFC7FAF63420437F',1,1,1,'D1884ACXX/7',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(18,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'89A1B2770C104C7DB078766D770409B9',1,1,1,'D1884ACXX/8',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(19,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'50A5C5F01E674E4F88B6170F3E4D3563',1,1,1,'634H7AAXX',NULL,'2012-12-20 11:10:27',NULL,9,5,NULL,1,1,1),
	(20,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'D1B58BAEDF524441BCB70A271330957B',1,1,1,'634H7AAXX/1',NULL,'2012-12-20 11:10:27',1,NULL,4,NULL,1,1,1),
	(21,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'AA6153C99C40421C852913DC5C32FF6F',1,NULL,0,'sIP1',NULL,NULL,NULL,5,2,1,2,10,10),
	(22,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'BF89D6304B3B40A2A325AC8830DAE145',1,NULL,0,'sIP2',NULL,NULL,NULL,5,2,1,2,10,10),
	(23,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'13890D717B1B403283544AFA8F26E0CA',1,NULL,0,'sINPUT',NULL,NULL,NULL,5,2,1,2,10,10),
	(24,'2012-12-20 11:24:31','2013-03-12 12:39:41',X'300AF739354F43AFB4D51682A4CF607C',1,NULL,NULL,'sIP1_lib',21,NULL,NULL,7,7,1,2,10,1),
	(25,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'85E789CF42414F428BE1EA2B00405982',1,NULL,0,'l1INPUT',NULL,NULL,NULL,6,1,2,2,10,10),
	(26,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'D6E601D71CE04434AA7469D80F9A98DA',1,NULL,0,'lIP1',NULL,NULL,NULL,6,1,2,2,10,10);

/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplebarcode
# ------------------------------------------------------------

LOCK TABLES `samplebarcode` WRITE;
/*!40000 ALTER TABLE `samplebarcode` DISABLE KEYS */;

INSERT INTO `samplebarcode` (`id`, `created`, `updated`, `uuid`, `barcodeid`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-13 11:19:59',X'C15F3E669FF34669A94E028C9F20BA01',4,1,1),
	(2,'2012-12-20 11:09:48','2013-03-13 11:19:59',X'1DE365FE85764ECB9A9A50E5C154D7BF',5,10,1),
	(3,'2012-12-20 11:10:27','2013-03-13 11:19:59',X'032FE194FEEF42668FB5B7D3366D9C66',6,19,1);

/*!40000 ALTER TABLE `samplebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraft
# ------------------------------------------------------------

LOCK TABLES `sampledraft` WRITE;
/*!40000 ALTER TABLE `sampledraft` DISABLE KEYS */;

INSERT INTO `sampledraft` (`id`, `created`, `updated`, `uuid`, `filegroupid`, `jobdraftid`, `labid`, `name`, `samplesubtypeid`, `sampletypeid`, `sourcesampleid`, `status`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:17:27','2013-03-12 12:39:41',X'6E0E7F8AA4F849C7870BBD85D9E7E0B1',NULL,1,2,'sIP1',5,2,NULL,NULL,10,10),
	(2,'2012-12-20 11:17:34','2013-03-12 12:39:41',X'E5F506CCEFDA4D9EB6F602F7FD3A8F1B',NULL,1,2,'sIP2',5,2,NULL,NULL,10,10),
	(3,'2012-12-20 11:17:41','2013-03-12 12:39:41',X'0EC36DA6EDC54D0ABF063A7F8F54D624',NULL,1,2,'sINPUT',5,2,NULL,NULL,10,10),
	(4,'2012-12-20 11:26:22','2013-03-12 12:39:41',X'CB69E6C5BFAE4CAA9F1B001808781CCB',NULL,2,2,'l1INPUT',6,1,NULL,NULL,10,10),
	(5,'2012-12-20 11:26:37','2013-03-12 12:39:41',X'1BD1CD9EAF764B49B150FFAD483BD203',NULL,2,2,'lIP1',6,1,NULL,NULL,10,10);

/*!40000 ALTER TABLE `sampledraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftjobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `sampledraftjobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` DISABLE KEYS */;

INSERT INTO `sampledraftjobdraftcellselection` (`id`, `created`, `updated`, `uuid`, `jobdraftcellselectionid`, `libraryindex`, `sampledraftid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'733108152DB646D3A3664D6F2A363883',1,1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'17E6225632AD4354875296C793755029',1,2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'41BFCC5EECE04067A296BF78D8273BDF',1,3,3,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FBCC36BA7064922A5866BCD8F5803DF',2,1,4,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1AD4E94DB7AD4C218A2F31CD1F3C6F96',3,1,5,NULL);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta
# ------------------------------------------------------------

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;

INSERT INTO `sampledraftmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `sampledraftid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'B069F2E8B3D449A3903E41F82C54566F','genericBiomolecule.species',0,NULL,'Human',1,NULL),
	(2,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'E580903047C54568A1524D46360E6371','genericDna.concentration',0,NULL,'26',1,NULL),
	(3,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'E36D00BBA0744516BE06F52792AC4399','genericDna.volume',0,NULL,'10',1,NULL),
	(4,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'1F71EE5B11BA4B45AEA220871268B0A2','genericDna.buffer',0,NULL,'TE',1,NULL),
	(5,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'84A400204CD3473398DC19455FEC1C8F','genericDna.A260_280',0,NULL,'1.76',1,NULL),
	(6,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'405FCFA0C8C940DB933D70B577E1BEA0','genericDna.A260_230',0,NULL,'1.9',1,NULL),
	(7,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'196C80B5CB084EF884C2658F4968D6BF','chipseqDna.fragmentSize',0,NULL,'250',1,NULL),
	(8,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'7B2662CE30664BE4945F8D7981259031','chipseqDna.fragmentSizeSD',0,NULL,'25',1,NULL),
	(9,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'28C3557CEC274A79BF73DE4464C975A2','chipseqDna.antibody',0,NULL,'goat',1,NULL),
	(10,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'9E2BA20D2F8B42F0BA906AE392718951','genericBiomolecule.species',0,NULL,'Human',2,NULL),
	(11,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'048977847FCC43C7B9CC3F6A2EA7C0EF','genericDna.concentration',0,NULL,'23',2,NULL),
	(12,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'67DA3F1E26034524B28509489779EA2F','genericDna.volume',0,NULL,'10',2,NULL),
	(13,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'BF8E6E076B2D4201B0598D794C259705','genericDna.buffer',0,NULL,'TE',2,NULL),
	(14,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'E0DCBBEBBBF74007B7398FE539E9E9EB','genericDna.A260_280',0,NULL,'1.74',2,NULL),
	(15,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'16D75635779A4799900DC7B29E383742','genericDna.A260_230',0,NULL,'1.86',2,NULL),
	(16,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'64C793BF0C0049A49D06902C7C2D26C7','chipseqDna.fragmentSize',0,NULL,'250',2,NULL),
	(17,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'295671BD9E644040A71D4D45F1D15283','chipseqDna.fragmentSizeSD',0,NULL,'25',2,NULL),
	(18,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'19EF906CF04F48E895A41874ECCD2C55','chipseqDna.antibody',0,NULL,'goat',2,NULL),
	(19,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'B10AE89D59C14B56ADA06111ED930C2C','genericBiomolecule.species',0,NULL,'Human',3,NULL),
	(20,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'9F72C5CBB38441E38409EB0D85FD943A','genericDna.concentration',0,NULL,'21',3,NULL),
	(21,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'50C6BEC7694A422D967015D10BBA8C68','genericDna.volume',0,NULL,'10',3,NULL),
	(22,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'B139493D35414248BACF158A45DB9734','genericDna.buffer',0,NULL,'TE',3,NULL),
	(23,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'F33FE6F24137473E907055A7A03F2960','genericDna.A260_280',0,NULL,'1.65',3,NULL),
	(24,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'AF1005FD3FBC4ED885BB3D04BEE2558F','genericDna.A260_230',0,NULL,'1.83',3,NULL),
	(25,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'CA7DE992E65F4807908555D1FAF36F63','chipseqDna.fragmentSize',0,NULL,'250',3,NULL),
	(26,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'336C3F629C374CE987AD493846DBF693','chipseqDna.fragmentSizeSD',0,NULL,'25',3,NULL),
	(27,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'A6269C3E474C44C48412C5FCDAF3A154','chipseqDna.antibody',0,NULL,'goat',3,NULL),
	(28,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'5AB6053D2082422DA07457724D70A10A','genericBiomolecule.species',0,NULL,'Human',4,NULL),
	(29,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'7C04E269FA8C478EAF373F2B0BDFFFA0','chipseqDna.fragmentSize',0,NULL,'200',4,NULL),
	(30,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'E8D6E4B6C3E5496BB576ECB0718E6DFC','chipseqDna.fragmentSizeSD',0,NULL,'10',4,NULL),
	(31,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'70F7BDF0D7C44AE2ADE91B798E1391DC','chipseqDna.antibody',0,NULL,'sheep',4,NULL),
	(32,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'A15EE34A38C045C69A83D0F9E4C10465','genericLibrary.concentration',0,NULL,'34',4,NULL),
	(33,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'55676D281F054D2885720EDD93658BB3','genericLibrary.volume',0,NULL,'10',4,NULL),
	(34,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'231065FE2C5F4563880F343C168D369B','genericLibrary.buffer',0,NULL,'Water',4,NULL),
	(35,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'73E51D6971914640BD07D1C14E8A2322','genericLibrary.adaptorset',0,NULL,'2',4,NULL),
	(36,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'59A5E758274C42DABD478DF3B89B1A17','genericLibrary.adaptor',0,NULL,'3',4,NULL),
	(37,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'1E6F0E540D204D5E95B23F103745D01D','genericLibrary.size',0,NULL,'500',4,NULL),
	(38,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'760F4055293B424A80C6FB74748D3B0F','genericLibrary.sizeSd',0,NULL,'55',4,NULL),
	(39,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'4B7D6B6C8B5447048EA9134866C9EC4D','genericBiomolecule.species',0,NULL,'Human',5,NULL),
	(40,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'69DDAA47A0204197B327EE787348222D','chipseqDna.fragmentSize',0,NULL,'200',5,NULL),
	(41,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'C33FC87CACC644B5AAD8799B05048594','chipseqDna.fragmentSizeSD',0,NULL,'10',5,NULL),
	(42,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'B9446BA532BD48C397B3B62E5723A6D4','chipseqDna.antibody',0,NULL,'sheep',5,NULL),
	(43,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'60B16134B10B4B7B8FBC91A9CF316B2F','genericLibrary.concentration',0,NULL,'34',5,NULL),
	(44,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'DF9BFB08AE594D6AAFF181C7493D6446','genericLibrary.volume',0,NULL,'10',5,NULL),
	(45,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'985581D9515843B5BD030002F80CF368','genericLibrary.buffer',0,NULL,'Water',5,NULL),
	(46,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'B4184D552E6B46F0A24FCF9201FAAE6F','genericLibrary.adaptorset',0,NULL,'2',5,NULL),
	(47,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'BB6EFDC90F864AA6A9C456E599D626C6','genericLibrary.adaptor',0,NULL,'3',5,NULL),
	(48,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'9612A60291CD41AF871C8220A7C079F5','genericLibrary.size',0,NULL,'500',5,NULL),
	(49,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'A901AC497F9848B99765E1A0A208594B','genericLibrary.sizeSd',0,NULL,'55',5,NULL);

/*!40000 ALTER TABLE `sampledraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplefile
# ------------------------------------------------------------



# Dump of table samplejobcellselection
# ------------------------------------------------------------

LOCK TABLES `samplejobcellselection` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection` DISABLE KEYS */;

INSERT INTO `samplejobcellselection` (`id`, `created`, `updated`, `uuid`, `jobcellselectionid`, `libraryindex`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'499201CEC4E54D37B46132C021A31E06',1,1,21,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'301F44587CBB4CFB8339E2DB0FD2655B',1,2,22,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1E9BD2AE1DC845CF87489721D2F615A6',1,3,23,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'156BA4ABBA654B58A16782826E1C50A7',2,1,25,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8879CBD64A374DBAB1928D8F2FE4E0A5',3,1,26,NULL);

/*!40000 ALTER TABLE `samplejobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplelab
# ------------------------------------------------------------



# Dump of table samplemeta
# ------------------------------------------------------------

LOCK TABLES `samplemeta` WRITE;
/*!40000 ALTER TABLE `samplemeta` DISABLE KEYS */;

INSERT INTO `samplemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'D48681B0F47E4650AFCDA411F9EFD4C2','platformunitInstance.readType',0,NULL,'paired',1,NULL),
	(2,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'895CC8BB622B48FA96A40105A9D82489','platformunitInstance.readlength',0,NULL,'100',1,NULL),
	(3,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'6385532C82854DBA91E707FF9BA1BD69','platformunitInstance.comment',0,NULL,'',1,NULL),
	(4,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'589A65A37046468B9E0DDB5CD306ED93','platformunitInstance.readType',0,NULL,'single',10,NULL),
	(5,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'A3A2C157F0D74B6CA99A824D1DF57F72','platformunitInstance.readlength',0,NULL,'75',10,NULL),
	(6,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'3950DAEECF90452F8C7736FB8D71C300','platformunitInstance.comment',0,NULL,'',10,NULL),
	(7,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'1EB9B597489F4380BEA30986B7E14841','platformunitInstance.readType',0,NULL,'paired',19,NULL),
	(8,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'CAF94594ECEE48D3A57AF68E17F5F586','platformunitInstance.readlength',0,NULL,'150',19,NULL),
	(9,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'11322D546C1D4C8DA90EB8056784E2A9','platformunitInstance.comment',0,NULL,'',19,NULL),
	(10,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'DCEABE600700447FBED17355E9A361CA','genericBiomolecule.species',0,NULL,'Human',21,10),
	(11,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'2970A579B84A426CB6CD9CB24647B5C0','genericDna.concentration',0,NULL,'26',21,10),
	(12,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'F533AAE2A56D48E49CF3BF24205AA534','genericDna.volume',0,NULL,'10',21,10),
	(13,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B217D62A1FFA4B97B96713310F4CC280','genericDna.buffer',0,NULL,'TE',21,10),
	(14,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'ADC1194CA2354D10B18C6F6356B1BC59','genericDna.A260_280',0,NULL,'1.76',21,10),
	(15,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5086C61DBCBA48B49C38AFB297EBE11E','genericDna.A260_230',0,NULL,'1.9',21,10),
	(16,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'644F1BE19F354E1AA94CADD17D272605','chipseqDna.fragmentSize',0,NULL,'250',21,10),
	(17,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CB556B119F0949FCB904E136349E6E1D','chipseqDna.fragmentSizeSD',0,NULL,'25',21,10),
	(18,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'24C25481E73446C481C8873D3D3B5EE3','chipseqDna.antibody',0,NULL,'goat',21,10),
	(19,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'957154B2F3AC4810952D9F11581DB813','genericBiomolecule.species',0,NULL,'Human',22,10),
	(20,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'20EFDF4CCC264B78A479B157782A210E','genericDna.concentration',0,NULL,'23',22,10),
	(21,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'138C7519B1F044798B8E4CB083998758','genericDna.volume',0,NULL,'10',22,10),
	(22,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'35D077547FEF4615BB07A867C072AED0','genericDna.buffer',0,NULL,'TE',22,10),
	(23,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'39DFE209EB2948A38D3CDE0ECCF2AF00','genericDna.A260_280',0,NULL,'1.74',22,10),
	(24,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D6E54290C60B49AEB6A12BA22181FFAC','genericDna.A260_230',0,NULL,'1.86',22,10),
	(25,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'A053EE489F42445589DE1C73D6A643BD','chipseqDna.fragmentSize',0,NULL,'250',22,10),
	(26,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'8FC320CB7E554CCF83D3D62BD70D6ED7','chipseqDna.fragmentSizeSD',0,NULL,'25',22,10),
	(27,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'EDFA96F00E3849DB88C56825812DA2B9','chipseqDna.antibody',0,NULL,'goat',22,10),
	(28,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1EB0D68F45994DE099C4CAF5FEADE331','genericBiomolecule.species',0,NULL,'Human',23,10),
	(29,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'130514D3EDD84E4A9ACFD881FE274139','genericDna.concentration',0,NULL,'21',23,10),
	(30,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0D7488F6281643CD85524F8231737F5D','genericDna.volume',0,NULL,'10',23,10),
	(31,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'91B754294BE84581B169A87587ED53DB','genericDna.buffer',0,NULL,'TE',23,10),
	(32,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B90D4BBF9AFF4052A3724CB69E8500FD','genericDna.A260_280',0,NULL,'1.65',23,10),
	(33,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0921EC59FDAB4B8FB0DD84011D14F9D0','genericDna.A260_230',0,NULL,'1.83',23,10),
	(34,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'44DE2012A4EA43628A9D7AC411E4AC1F','chipseqDna.fragmentSize',0,NULL,'250',23,10),
	(35,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CA0F49DC2B8B4E9AB26F7F38FD02DA3F','chipseqDna.fragmentSizeSD',0,NULL,'25',23,10),
	(36,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1E871A229C044DB3B043CE4746312FD4','chipseqDna.antibody',0,NULL,'goat',23,10),
	(37,'2012-12-20 11:23:35','2013-03-12 12:39:41',X'CA6F2F129B89434AA0F5E77646534B61','statusMessage.sampleQCComment::4e42daec-91c3-484f-8cf8-1dad722fc4f8',0,NULL,'Sample QC Comment::slightly concerned about size range but going to proceed anyway.',23,1),
	(38,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'0815D70A8E404E65BAD8FD613AF47E5F','genericLibrary.concentration',0,NULL,'25',24,NULL),
	(39,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'91418A618D654926A500D5F2CD60CB7A','genericLibrary.adaptor',0,NULL,'2',24,NULL),
	(40,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'1BD036957E1240C6A864FA2E9010F604','genericLibrary.volume',0,NULL,'50',24,NULL),
	(41,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'D9ECB8D3C9144DDD8261996DC3A360BF','genericLibrary.adaptorset',0,NULL,'2',24,NULL),
	(42,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'8A5AFAE0363D40458CDD543B12D736CF','genericLibrary.sizeSd',0,NULL,'20',24,NULL),
	(43,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'2FA9DCE3A4B14B5FACBC9F412E651BD0','genericLibrary.size',0,NULL,'400',24,NULL),
	(44,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'37F36B58736941B5ABE39626D5AB6D60','genericLibrary.buffer',0,NULL,'TE',24,NULL),
	(45,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'7CF1EEC937104359A77D6254FB36351E','genericBiomolecule.species',0,NULL,'Human',25,10),
	(46,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'2CDAAD2B7A4B491E8C7F960F56F76B85','chipseqDna.fragmentSize',0,NULL,'200',25,10),
	(47,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'3653ACF9DBCE499FAD9FEDABCD05CD83','chipseqDna.fragmentSizeSD',0,NULL,'10',25,10),
	(48,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'882EEB7141F245CEB87268A22AD04488','chipseqDna.antibody',0,NULL,'sheep',25,10),
	(49,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E543816081FC497B823199E95CBBC78F','genericLibrary.concentration',0,NULL,'34',25,10),
	(50,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E74EF0EE73B04F6B9597A09357FF6AC7','genericLibrary.volume',0,NULL,'10',25,10),
	(51,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E805864595104758BF39248E472D7632','genericLibrary.buffer',0,NULL,'Water',25,10),
	(52,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FC55C33AF2B04990AE0952C4EAB56928','genericLibrary.adaptorset',0,NULL,'2',25,10),
	(53,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'220F98AC482C488A97E1B828C321088D','genericLibrary.adaptor',0,NULL,'3',25,10),
	(54,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'CEEAE13B02CF4A70AA86B7783E374C19','genericLibrary.size',0,NULL,'500',25,10),
	(55,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A74354B0957B4BAF89C4E370B9A54E86','genericLibrary.sizeSd',0,NULL,'55',25,10),
	(56,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'680447EF49224E3E8D6B9917441DEFA6','genericBiomolecule.species',0,NULL,'Human',26,10),
	(57,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FE9F88CEC8214A298DBA77E087E996AC','chipseqDna.fragmentSize',0,NULL,'200',26,10),
	(58,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FF8084325137416493B949D26902134B','chipseqDna.fragmentSizeSD',0,NULL,'10',26,10),
	(59,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'489BA118BB094A639898E074B7BFCF15','chipseqDna.antibody',0,NULL,'sheep',26,10),
	(60,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A3A0A88D5C474DA19DE7CF788457BEDB','genericLibrary.concentration',0,NULL,'34',26,10),
	(61,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E87727E7BC624A76891775E9186CAD99','genericLibrary.volume',0,NULL,'10',26,10),
	(62,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'B6865F8BB60245D3A7752D373324CCD5','genericLibrary.buffer',0,NULL,'Water',26,10),
	(63,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'70E89306F60140619FA51097992DB6E5','genericLibrary.adaptorset',0,NULL,'2',26,10),
	(64,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'AF51CE7CA0A747359AC1C3AA0381167A','genericLibrary.adaptor',0,NULL,'3',26,10),
	(65,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'C3A4317141EE42A69B8CC802CCD26B89','genericLibrary.size',0,NULL,'500',26,10),
	(66,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'366D72673A3A40D9887CF1C630701A6F','genericLibrary.sizeSd',0,NULL,'55',26,10);

/*!40000 ALTER TABLE `samplemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesource
# ------------------------------------------------------------

LOCK TABLES `samplesource` WRITE;
/*!40000 ALTER TABLE `samplesource` DISABLE KEYS */;

INSERT INTO `samplesource` (`id`, `created`, `updated`, `uuid`, `indexvalue`, `sampleid`, `source_sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A8EBCDB91BE3457E8C10998660562D79',1,1,2,1),
	(2,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'DD5E09225DB54B62BD81F5E7A645D693',2,1,3,1),
	(3,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'351163F860204F75A15BF8153CC4B78C',3,1,4,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'8E3E1384189D478E8558EE56DFF8A51C',4,1,5,1),
	(5,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'4FB7939784D34EC78C9EE967E92C4EE4',5,1,6,1),
	(6,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'3E6183D1073F46AE9E53ED4834ECC613',6,1,7,1),
	(7,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A0D91D35C99749FD9CCD1F1F2D0D6693',7,1,8,1),
	(8,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'EF20AD0180CF47619A92D0BB6DC539B0',8,1,9,1),
	(9,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'809171F5086C459C9E891BEA0E0A33B0',1,10,11,1),
	(10,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7F698F0DCE434893B9EBEE096E6CF04A',2,10,12,1),
	(11,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'60912FD02BCD4E749B8AD5FB6B318F66',3,10,13,1),
	(12,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'4B74F74C29BB426DB67F51A2A340F2C6',4,10,14,1),
	(13,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'25C6487C968042C9BFCC0C4C9B2A4204',5,10,15,1),
	(14,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'49993614BD004053B5FA29F175403B32',6,10,16,1),
	(15,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'775DD7A8A23C4EF4AFABB3493125E9CF',7,10,17,1),
	(16,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'6BC1B27A7D734D10B0DB61923D0D6A96',8,10,18,1),
	(17,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'36E53F3C741B47F39B125467B63DB191',1,19,20,1);

/*!40000 ALTER TABLE `samplesource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesourcefile
# ------------------------------------------------------------



# Dump of table samplesourcemeta
# ------------------------------------------------------------



# Dump of table samplesubtype
# ------------------------------------------------------------

LOCK TABLES `samplesubtype` WRITE;
/*!40000 ALTER TABLE `samplesubtype` DISABLE KEYS */;

INSERT INTO `samplesubtype` (`id`, `created`, `updated`, `uuid`, `arealist`, `iname`, `isactive`, `name`, `sampletypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'428F1072219342EC81C93789CB278210','genericBiomolecule,genericLibrary','controlLibrarySample',1,'Control Library',1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'501480C681C542B4A3243E16AB34C7B6','genericBiomolecule,genericDna,bisulseqDna','bisulseqDnaSample',1,'BISUL-seq DNA',2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'8BD45094E59243F2962D94544985368C','genericBiomolecule,bisulseqDna,genericLibrary','bisulseqLibrarySample',1,'BISUL-seq Library',1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'1414667765554DB8936CA012AFC4AECB','genericLibrary','bisulseqFacilityLibrarySample',1,'BISUL-seq Facility Library',1,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'389183F56BC6413DB26FD4AFA63E212E','genericBiomolecule,genericDna,chipseqDna','chipseqDnaSample',1,'ChIP-seq DNA',2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'31F05F7849F44DA5B3FF9A70B9AB6E38','genericBiomolecule,chipseqDna,genericLibrary','chipseqLibrarySample',1,'ChIP-seq Library',1,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:24',X'ACE32C32B9AF42BFBE68468CE1642F30','genericLibrary','chipseqFacilityLibrarySample',1,'ChIP-seq Facility Library',1,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:24',X'D3CA3015796241F4BC9FE14E3D92F981','genericBiomolecule,genericLibrary,helptagLibrary','helptagLibrarySample',1,'HELP-tag Library',1,NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:20:24',X'7C616F066B2F4A6E9C7BCA92043B20C2','illuminaFlowcellMiSeqV1','illuminaFlowcellMiSeqV1',1,'Illumina Flow Cell MiSeq V1',5,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:24',X'D3241DDE0D354428BDCCF80ED50D90E0','illuminaFlowcellV3','illuminaFlowcellV3',1,'Illumina Flow Cell Version 3',5,NULL),
	(11,'2013-03-12 12:39:45','2013-03-12 18:20:24',X'C05159DBAC2E4450B9CD2ABD6987F6FC','genericBiomolecule,genericDna','genericDnaDnaSample',1,'Generic DNA Seq DNA',2,NULL),
	(12,'2013-03-12 12:39:45','2013-03-12 18:20:24',X'BC8F78EEE13F477E8C9AA4A31D374C63','genericBiomolecule,genericLibrary','genericDnaLibrarySample',1,'Generic DNA Seq Library',1,NULL),
	(13,'2013-03-12 12:39:45','2013-03-12 18:20:24',X'D75BD89988A34E9C8C010387DBF4517E','genericLibrary','genericDnaFacilityLibrarySample',1,'Generic DNA Seq Facility Library',1,NULL);

/*!40000 ALTER TABLE `samplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtypemeta
# ------------------------------------------------------------

LOCK TABLES `samplesubtypemeta` WRITE;
/*!40000 ALTER TABLE `samplesubtypemeta` DISABLE KEYS */;

INSERT INTO `samplesubtypemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `samplesubtypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'70C51AEA76684907AD1FFA912CC5910A','controlLibrarySample.includeRoles',1,NULL,'ft,su',1,0),
	(2,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'C0F086A2F8D043AF8866CFAEEAE8B1B8','bisulseqDnaSample.includeRoles',1,NULL,'ft,lu',2,0),
	(3,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'EAF05D61CCD443768C8DED0A06450DC6','bisulseqLibrarySample.includeRoles',1,NULL,'lu',3,0),
	(4,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'90731F13B7314384B1286F8B5E05846D','bisulseqFacilityLibrarySample.includeRoles',1,NULL,'ft',4,0),
	(5,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'FE8EDE58EFC04B348825CC5B50FD01AE','chipseqDnaSample.includeRoles',1,NULL,'ft,lu',5,0),
	(6,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'593CDBB7D9FA456D8724C81FAF687F70','chipseqLibrarySample.includeRoles',1,NULL,'lu',6,0),
	(7,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'C7A3764FE59A47B6AD0D0906658FF706','chipseqFacilityLibrarySample.includeRoles',1,NULL,'ft',7,0),
	(8,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'918622B3991C45D194E2CD3AB66C9365','helptagLibrarySample.includeRoles',1,NULL,'ft,lu',8,0),
	(9,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'E8C6A0BB73764F30915E222CCDC125ED','illuminaFlowcellMiSeqV1.maxCellNumber',1,NULL,'1',9,0),
	(10,'2012-12-20 11:19:23','2013-03-12 12:39:41',X'47E20028C2764A769B070FD893F76FF7','illuminaFlowcellMiSeqV1.multiplicationFactor',2,NULL,'1',9,0),
	(11,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'CA6CF3BC387F420E84E919753C23FDB9','illuminaFlowcellV3.maxCellNumber',1,NULL,'8',10,0),
	(12,'2012-12-20 11:19:23','2013-03-12 12:39:41',X'E7E47544BBC74FDFB3E230600AE111F5','illuminaFlowcellV3.multiplicationFactor',2,NULL,'2',10,0),
	(13,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'36CFE8FFB69B4691B45C7D6ED2C3A2E3','genericDnaDnaSample.includeRoles',1,NULL,'ft,lu',11,NULL),
	(14,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'0EEB4B3DFC44476C9C292C21EC2E9778','genericDnaLibrarySample.includeRoles',1,NULL,'lu',12,NULL),
	(15,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'D076ED4458314B79907068DF2DD6D611','genericDnaFacilityLibrarySample.includeRoles',1,NULL,'ft',13,NULL);

/*!40000 ALTER TABLE `samplesubtypemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

LOCK TABLES `samplesubtyperesourcecategory` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `samplesubtypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B4FF6EBB9E5D46D9827FDFE9D083D7FE',2,9,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'6E3FFF625A3744D5A3B8F5033809DE4A',1,10,NULL);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype
# ------------------------------------------------------------

LOCK TABLES `sampletype` WRITE;
/*!40000 ALTER TABLE `sampletype` DISABLE KEYS */;

INSERT INTO `sampletype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `sampletypecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5A76F12B925E4C739FC4FB88C72FF824','library',1,'Library',1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'75D4337618814B228C4E3AE57784A275','dna',1,'DNA',1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'E0470BB5B471478BB80165FB193B8BD5','rna',1,'RNA',1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'019E9B91FD0044ACBF7667B318A83DBA','cell',1,'Cell',2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'06ACDFC22CC64AD1BE5EA457E6FA5243','platformunit',1,'Platform Unit',2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'384B075E967C44A6BCB92992EF2498F6','tissue',1,'Tissue',1,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'25F322AB0DF94853885C011884F69858','facilityLibrary',1,'Facilitylibrary',1,NULL);

/*!40000 ALTER TABLE `sampletype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletypecategory
# ------------------------------------------------------------

LOCK TABLES `sampletypecategory` WRITE;
/*!40000 ALTER TABLE `sampletypecategory` DISABLE KEYS */;

INSERT INTO `sampletypecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'37D5DFB2E54042BE97C1896BA323DAE0','biomaterial',1,'Biomaterial',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'658F41524B874BBB9C63F72873D1EA12','hardware',1,'Hardware',NULL);

/*!40000 ALTER TABLE `sampletypecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table software
# ------------------------------------------------------------

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;

INSERT INTO `software` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:29','2013-03-12 18:19:04',X'63A5B8381ACB4B3692E5A7C4EFA1D597','bowtieAligner',1,'Bowtie Aligner',8,0),
	(2,'2012-12-20 11:03:30','2013-03-12 18:19:04',X'20A9C2C6F306419F9AD1FC999C78EB95','macsPeakcaller',1,'MACS Peakcaller',4,0),
	(3,'2012-12-20 11:03:30','2013-03-12 18:19:04',X'2B2B1550FE3F406B86A33D4B0B61EB8C','bisulfiteSeqPipeline',1,'BISUL-seq Pipeline',6,0),
	(4,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'5E846435B5654EE7A410353FA98B2930','helptagPipeline',1,'HELP-tag Pipeline',7,0),
	(5,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'5C194015AFE94748B28D11E0101B2BE6','casava',1,'CASAVA',5,0);

/*!40000 ALTER TABLE `software` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table softwaremeta
# ------------------------------------------------------------

LOCK TABLES `softwaremeta` WRITE;
/*!40000 ALTER TABLE `softwaremeta` DISABLE KEYS */;

INSERT INTO `softwaremeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:29','2013-03-12 12:39:41',X'3C206E1F8DB441CCBECFBD2ED476E46B','bowtieAligner.currentVersion',1,NULL,'0.12.7',1,0),
	(2,'2012-12-20 11:03:29','2013-03-12 12:39:41',X'D9F7717276FA4FA4934278C52F442D7B','bowtieAligner.priorVersions',2,NULL,'',1,0),
	(3,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'6BB8CA249DC046E68D770D937A366E6A','macsPeakcaller.currentVersion',1,NULL,'4.1',2,0),
	(4,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'A8D9EC57C23149C5AD4CB4E012BE2BB3','macsPeakcaller.priorVersions',2,NULL,'',2,0),
	(5,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'3112D95D365E46F5B30BDCA129A53943','bisulfiteSeqPipeline.currentVersion',1,NULL,'1.0',3,0),
	(6,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'05CA3DBDEA9B43FFAB7F69774C982A19','bisulfiteSeqPipeline.priorVersions',2,NULL,'',3,0),
	(7,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'68E358EB5D1A4E1D9AAEB8D02E7E10FE','helptagPipeline.currentVersion',1,NULL,'1.0',4,0),
	(8,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'71B7F04AAE7449B695B45C9B2C597009','helptagPipeline.priorVersions',2,NULL,'',4,0),
	(9,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'5A278289DA454746921F668D6BFB9B12','casava.currentVersion',1,NULL,'1.8.2',5,0),
	(10,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'252029E8BE7241799818094E49D4B442','casava.priorVersions',2,NULL,'',5,0);

/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table usermeta
# ------------------------------------------------------------

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;

INSERT INTO `usermeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'293B13835E584F0C916BD728F42B9E39','user.title',0,NULL,'Dr',2,NULL),
	(2,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'23C3602E2C874CB08F69C443C51EF1CA','user.institution',0,NULL,'Einstein',2,NULL),
	(3,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'86CF9706268D4FDDBBD3DFB8279D9252','user.departmentId',0,NULL,'1',2,NULL),
	(4,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'55E43093D5414C62AA552CED1D416C0A','user.building_room',0,NULL,'Price 954',2,NULL),
	(5,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'24DC65BD577F4B449D38C367BF4454D3','user.address',0,NULL,'1301 Morris Park Ave',2,NULL),
	(6,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'A0E195468CE843719328B2A1F7F8B95E','user.city',0,NULL,'Bronx',2,NULL),
	(7,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'02655FD6DA2144E5ACD604E46D723627','user.state',0,NULL,'NY',2,NULL),
	(8,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'A0C88E4A70E045B1B0584AC97E57F1D0','user.country',0,NULL,'US',2,NULL),
	(9,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'EF9A9ED5679A4C849CF3E5404623F413','user.zip',0,NULL,'10461',2,NULL),
	(10,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'6FBA24318ED34804AA59CF001BAE7A63','user.phone',0,NULL,'718-600-1985',2,NULL),
	(11,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'F1833E3DB63742FB848D3384478E7174','user.fax',0,NULL,'',2,NULL),
	(12,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'89C93B89EAF44C3C8916F2ACEDB0698B','user.title',0,NULL,'Ms',3,NULL),
	(13,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'60BF5FF947594B148D64C0D2A463FFBF','user.institution',0,NULL,'Einstein',3,NULL),
	(14,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'0D03DF804E7546EFBB5AC643B6937E2E','user.departmentId',0,NULL,'1',3,NULL),
	(15,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'1890CD22952B411D9F0668F91B75FD00','user.building_room',0,NULL,'Price 1003',3,NULL),
	(16,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'2F148C3A95404B6EAE3C9853F7BC7B73','user.address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(17,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'C9A99ED7F86C47C1B80E67075152B7BF','user.city',0,NULL,'Bronx',3,NULL),
	(18,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'0EE0B6A9439E4BEBA1A873F1AA143AB9','user.state',0,NULL,'NY',3,NULL),
	(19,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'8184CE4356B54BA08831267E885A5D11','user.country',0,NULL,'US',3,NULL),
	(20,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'8A01B323F1D64AD8A044809348DC2CBE','user.zip',0,NULL,'10461',3,NULL),
	(21,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'F9446B35EDAA47FBB31BEFD4916A8D79','user.phone',0,NULL,'718-600-3465',3,NULL),
	(22,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'442A58ABB7904E9CBC9570C3DEDF5569','user.fax',0,NULL,'',3,NULL),
	(23,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'FC041CD2A7A3490D8C3CC92B175D66C9','user.title',0,NULL,'Ms',4,NULL),
	(24,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'78380D85F3B94B8D93FAB3DA05308E4A','user.institution',0,NULL,'Einstein',4,NULL),
	(25,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'9744B82F2B774FA78E70585145D3BDD3','user.departmentId',0,NULL,'1',4,NULL),
	(26,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'261AB6AC00044289909F3FA9A6E5F84A','user.building_room',0,NULL,'Price 201',4,NULL),
	(27,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'B2E9FF0162FF4F87B3A1A5A0F874471B','user.address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(28,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'1471EB324776465EA3735F003DA53681','user.city',0,NULL,'Bronx',4,NULL),
	(29,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'CC4DE4C13AC8494AA348D1EFE381F960','user.state',0,NULL,'NY',4,NULL),
	(30,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'689F7CF0A05C4A638330DD4CFC070916','user.country',0,NULL,'US',4,NULL),
	(31,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'E21B02D23CCC49718C345641B2CD16CD','user.zip',0,NULL,'10461',4,NULL),
	(32,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'ECE07AB65523479A9D912BA9D7436112','user.phone',0,NULL,'718-600-1100',4,NULL),
	(33,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'265904A3907F4D3C8D5BAFFBFA913CA4','user.fax',0,NULL,'',4,NULL),
	(34,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'3CA0860BEB634E279783BF5A773D927B','user.title',0,NULL,'Dr',5,NULL),
	(35,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'042315BDA30343538DB05403DC8E3474','user.institution',0,NULL,'Einstein',5,NULL),
	(36,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'0AA2EAD6AB064F4C870F44C7A2C8EC01','user.departmentId',0,NULL,'3',5,NULL),
	(37,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'C09AFCFABE6B41DEAE91D8AF5DCAF3A8','user.building_room',0,NULL,'Price 2200',5,NULL),
	(38,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'E01BB247E2EA4D1B832957618ACE6BF7','user.address',0,NULL,'1301 Morris Park Ave.',5,NULL),
	(39,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A5E2147EAEA746AFAC40D5F4111CA5B0','user.city',0,NULL,'Bronx',5,NULL),
	(40,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'BD6759F5CF844D3192DBC70FE5F2757F','user.state',0,NULL,'NY',5,NULL),
	(41,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'23725398AD944AB9A482497262258D7B','user.country',0,NULL,'US',5,NULL),
	(42,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'92BF62CAA17141F1BBB1EC6346FDA1F6','user.zip',0,NULL,'10461',5,NULL),
	(43,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'AF9377F05B02436A95DB23DAB19F2471','user.phone',0,NULL,'718-123-4567',5,NULL),
	(44,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'717F296DC16846FBA0CC9002E7624AFA','user.fax',0,NULL,'',5,NULL),
	(45,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'FA5F924581AD4231BBF7E00D7668D68B','user.labName',0,NULL,'Greally Lab',5,NULL),
	(46,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'A0B796D5810D47689087390DD5BC9CCB','user.title',0,NULL,'Ms',6,NULL),
	(47,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'C7E0CBDF0EF340428FD860424A7692BC','user.institution',0,NULL,'Einstein',6,NULL),
	(48,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'AF1932CC54114450B5722D00C6934327','user.departmentId',0,NULL,'3',6,NULL),
	(49,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'5D7132A021354F08B4386F076EE1769E','user.building_room',0,NULL,'Price 2220',6,NULL),
	(50,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F3062D90D83447ED86279038B9B495A8','user.address',0,NULL,'1301 Morris Park Ave.',6,NULL),
	(51,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F5715E9F8D1A44469071CC8FFA8AA41D','user.city',0,NULL,'Bronx',6,NULL),
	(52,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F71779C9A0654CF1BA11B7D826D51570','user.state',0,NULL,'NY',6,NULL),
	(53,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'2259CD1C170547308C672F61D373D442','user.country',0,NULL,'US',6,NULL),
	(54,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'4533EDEB8F9D4A6FABE2C84B8A324CC8','user.zip',0,NULL,'10461',6,NULL),
	(55,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'DDBBAD2F71C64DEC91EF141F2E1A6E4B','user.phone',0,NULL,'718-608-0000',6,NULL),
	(56,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'3E0A1D6D92B3448BA921EF2E630A1733','user.fax',0,NULL,'',6,NULL),
	(57,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'BC47F2B05741491883C56009D3823C6B','user.primaryuserid',0,NULL,'jgreally',6,NULL),
	(58,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'ABE40EBC37864F7FA8025B71D25413D8','user.title',0,NULL,'Prof',7,NULL),
	(59,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'FB34E5A15E9B46C9B10EA645B826E76A','user.institution',0,NULL,'Einstein',7,NULL),
	(60,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'1E3D9AA12EE7472CAD0E4733CEB97260','user.departmentId',0,NULL,'3',7,NULL),
	(61,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'332044EFBBC24A808A492398F65EBBAB','user.building_room',0,NULL,'Price 933',7,NULL),
	(62,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'01569FB4C3CA49839BBBA141265884C7','user.address',0,NULL,'1301 Morris Park Ave.',7,NULL),
	(63,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'3F492B09652748E2AA069E90EC817666','user.city',0,NULL,'Bronx',7,NULL),
	(64,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'C96C244B62CA420497618A360FEFCE81','user.state',0,NULL,'NY',7,NULL),
	(65,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'598AA2BA60B14141A42A054899F9D3C7','user.country',0,NULL,'US',7,NULL),
	(66,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'F3DE71B20686425A98247579BD4764C5','user.zip',0,NULL,'10461',7,NULL),
	(67,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'7FE18C9BC03D4F9E84B388AE243337EE','user.phone',0,NULL,'718-600-1192',7,NULL),
	(68,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'7EBAA127D11F4E1780545896BFBE0109','user.fax',0,NULL,'',7,NULL),
	(69,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'55737E230DDD4EC28581DC2033764237','user.labName',0,NULL,'Goldin Lab',7,NULL),
	(70,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'98D74D2912C748B190676428DF0FC2AD','user.title',0,NULL,'Dr',8,NULL),
	(71,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'1827CEF278D44BC9830A481936C4365B','user.institution',0,NULL,'Einstein',8,NULL),
	(72,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'D454DC61D36F48548D9DF7836A3E3E5D','user.departmentId',0,NULL,'3',8,NULL),
	(73,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'E01D8DDB28F449409CB3203E0293928A','user.building_room',0,NULL,'Price 654',8,NULL),
	(74,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'1AADE2C63B7D4E48B53D55CEC48CBFDC','user.address',0,NULL,'1301 Morris Park Ave.',8,NULL),
	(75,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'323FC6E68197455C9D8C9175C68BD9FE','user.city',0,NULL,'Bronx',8,NULL),
	(76,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'589846335D91417C9D038D7BA39DD19F','user.state',0,NULL,'NY',8,NULL),
	(77,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'AEE20CBF5E0F45658D6E388C26A81944','user.country',0,NULL,'US',8,NULL),
	(78,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'688B37F1990D4E21A5821AF3B05F350E','user.zip',0,NULL,'10461',8,NULL),
	(79,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'49A8E1497AD54AD7AD41ED458041B958','user.phone',0,NULL,'718-600-0019',8,NULL),
	(80,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'72FE8C8C069C487592E7A58B1A0DB28F','user.fax',0,NULL,'718-600-0020',8,NULL),
	(81,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'C5E2D5CC4FE147709F480129828CA157','user.labName',0,NULL,'Auton Lab',8,NULL),
	(82,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'93A2F9B8E181472C90476B5F39357C79','user.title',0,NULL,'Dr',9,NULL),
	(83,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'D1690FAFB3C04FF4AF4E39498A61AD1F','user.institution',0,NULL,'NYU Medical',9,NULL),
	(84,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'3D9B7268F7BB47A99494281857049138','user.departmentId',0,NULL,'2',9,NULL),
	(85,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'28C0F8A7FD704766BD53178FE84FA4B1','user.building_room',0,NULL,'Hammer 1406',9,NULL),
	(86,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'70D103FA8E7845DA8701E1843CE4BCB8','user.address',0,NULL,'16-50 54th Street',9,NULL),
	(87,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'5E241D29B5F74AFC9568CFE488A0B377','user.city',0,NULL,'New York',9,NULL),
	(88,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'C6D63704A73A4117A91C7E3B65E5FF59','user.state',0,NULL,'NY',9,NULL),
	(89,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'A9DDD745B68E41ECB872CA3BC471F59A','user.country',0,NULL,'US',9,NULL),
	(90,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'CBC3F9C0588743679161E2FF2FF82743','user.zip',0,NULL,'10002',9,NULL),
	(91,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'1239E07B105D49BEAE9C6E3C63BAE7C9','user.phone',0,NULL,'212-445-2345',9,NULL),
	(92,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'7946695B0D264A8A9F7B27233E48F48D','user.fax',0,NULL,'',9,NULL),
	(93,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'4597A3CD619F4BAFBCC150F480312E86','user.labName',0,NULL,'Trokie Lab',9,NULL),
	(94,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'7DF3B9B173AE4F47979308097021058F','user.title',0,NULL,'Dr',10,NULL),
	(95,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'C3AF49A247BB4EFDB29E290AB70FF43C','user.institution',0,NULL,'Einstein',10,NULL),
	(96,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'8A1BEFDC421A4016A5EDFA5AA518D9E8','user.departmentId',0,NULL,'3',10,NULL),
	(97,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'E4FEC4143A93480CAC8ED5CAA687D184','user.building_room',0,NULL,'Price 2222',10,NULL),
	(98,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'852110F51C3740149CC29D20DDEBF431','user.address',0,NULL,'1301 Morris Park Ave',10,NULL),
	(99,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'D60B6466D31E4944B536905FD8DD7C2B','user.city',0,NULL,'Bronx',10,NULL),
	(100,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'52264A7CE6A946C1BADCE4AFA38A385C','user.state',0,NULL,'NY',10,NULL),
	(101,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'2A349213347047D88D903D2F885EC477','user.country',0,NULL,'US',10,NULL),
	(102,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'27FA86DF25254927B0B82F976864F75B','user.zip',0,NULL,'10461',10,NULL),
	(103,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'5269E8B56E844811829D1F8F1A794EA6','user.phone',0,NULL,'718-500-6696',10,NULL),
	(104,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'110BEA0453AD4D09B616BED654ACEFFE','user.fax',0,NULL,'718-500-6697',10,NULL),
	(105,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'A9F2079A49A348618DC592CD876AE80E','user.primaryuserid',0,NULL,'jgreally',10,NULL),
	(106,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'C147F2A9982547F3B9A99F78DA451208','user.title',0,NULL,'Mr',11,NULL),
	(107,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'57B02B3E12654B40B846DE9A0C6FDF2A','user.institution',0,NULL,'Einstein',11,NULL),
	(108,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'59B68CBC30EB4BBB92A8E8380EEE3455','user.departmentId',0,NULL,'1',11,NULL),
	(109,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'17E669A9E4D14B4B899F743103957086','user.building_room',0,NULL,'Price 1357',11,NULL),
	(110,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'3475329003E6431AB758ABF7C2DFFDC0','user.address',0,NULL,'1301 Morris Park Ave.',11,NULL),
	(111,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'A6263711B0BF4AF38668C2F85A35443A','user.city',0,NULL,'Bronx',11,NULL),
	(112,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'AF176FA64D354A0DAF8A51FDA7E49ACF','user.state',0,NULL,'NY',11,NULL),
	(113,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'CFB7E6C4536842938C8FAA29510B58C1','user.country',0,NULL,'US',11,NULL),
	(114,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'8C49719668E8439987A72572B11D684F','user.zip',0,NULL,'10461',11,NULL),
	(115,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'16A26714B62142F498FFF244B81C6B2A','user.phone',0,NULL,'718-600-4533',11,NULL),
	(116,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'91D8DEE1D0C84180B5C244A151E23A5F','user.fax',0,NULL,'',11,NULL),
	(117,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'6D915A480583481DAECDD047D7EC87AD','user.title',0,NULL,'Prof',12,NULL),
	(118,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'2F1EB046B0C849D28D7ED10A0580238D','user.institution',0,NULL,'Einstein',12,NULL),
	(119,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'D0B878EF8BB34A6BA20238EC30AA6A8E','user.departmentId',0,NULL,'3',12,NULL),
	(120,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'47C20875F9EC4F46B47F84C692F7ECB5','user.building_room',0,NULL,'Price 222',12,NULL),
	(121,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'0D79D651D42C4DE8938DA90102E17144','user.address',0,NULL,'1301 Morris Park Ave',12,NULL),
	(122,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'51BC25C8CEDE4D339538D75BF2B6FD80','user.city',0,NULL,'Bronx',12,NULL),
	(123,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'223DB856477A4E38A7C0BBE0DCF608D7','user.state',0,NULL,'NY',12,NULL),
	(124,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'4D72F5A4E9B84F33BF398BC16C68EA73','user.country',0,NULL,'US',12,NULL),
	(125,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'D7C6F01BF60A44D9ACBB06A3475670B9','user.zip',0,NULL,'10461',12,NULL),
	(126,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'8F4106C7C4D24E4E966B2AB22F54067F','user.phone',0,NULL,'617-600-1313',12,NULL),
	(127,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'108FA0FB93E845E08B660EC6A8CDDFE1','user.fax',0,NULL,'',12,NULL),
	(128,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'48FE14A12C0145169FEF99362A885FB3','user.primaryuserid',0,NULL,'agoldin',12,NULL),
	(129,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'29088538C8284D35AC583BB611A9D034','user.title',0,NULL,'Dr',1,NULL),
	(130,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'F5A1D29086C3448296664290D6420E24','user.institution',0,NULL,'Einstein',1,NULL),
	(131,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'39991AAC5BD447B0A8B720028D9D2EE1','user.departmentId',0,NULL,'1',1,NULL),
	(132,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'EBAC39862B8A444EA7B4FC0CFC44B9F8','user.building_room',0,NULL,'N/A',1,NULL),
	(133,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'B3C86E3F22C64172B5235AA31EC35628','user.address',0,NULL,'N/A',1,NULL),
	(134,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'4C2E5B6279004771BA50150939ED230C','user.city',0,NULL,'N/A',1,NULL),
	(135,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'2B5EA1B4ACB84BEDB21665ABD1B66CA6','user.state',0,NULL,'NY',1,NULL),
	(136,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'2188B500E0624950BC6211522C9400F8','user.country',0,NULL,'US',1,NULL),
	(137,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'C1FFCC1699E242759049F0BBB5A7E259','user.zip',0,NULL,'N/A',1,NULL),
	(138,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'835BE5F788B640DAB840554A5ED63E80','user.phone',0,NULL,'N/A',1,NULL),
	(139,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'3B6E332170BD49078118ECAC0DBD9F56','user.fax',0,NULL,'N/A',1,NULL),
	(140,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'7E3BE51EBA6544FCBF7E81AD41BBE1B3','user.title',0,NULL,'Miss',13,NULL),
	(141,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'08DDF2512D6F4EC3B7BFD8F853D72631','user.institution',0,NULL,'Einstein',13,NULL),
	(142,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'4E8A62DA04474E5FBD32666BF27DB317','user.departmentId',0,NULL,'1',13,NULL),
	(143,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'F3E2E889D1D74BE88DFC2FFB4ED16C2A','user.building_room',0,NULL,'4333',13,NULL),
	(144,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'B65606661C224FAD905B6EAF04429B27','user.address',0,NULL,'Chanin',13,NULL),
	(145,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'8E874343CCCC4A13AD4AE25877CBF6D5','user.city',0,NULL,'Bronx',13,NULL),
	(146,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'CCB07D5545E04453BD7480C24E4FD4DB','user.state',0,NULL,'NY',13,NULL),
	(147,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'F27C0F3178C249C89AC5125FDD1927E8','user.country',0,NULL,'US',13,NULL),
	(148,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'B76DD097D55648E482A013D03825A389','user.zip',0,NULL,'10461',13,NULL),
	(149,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'FE162691EFD04C889F2DD6A7A0737B81','user.phone',0,NULL,'718-600-4455',13,NULL),
	(150,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'323DA29E1C29470792A5A258C1DB2929','user.fax',0,NULL,'',13,NULL);

/*!40000 ALTER TABLE `usermeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table userpasswordauth
# ------------------------------------------------------------



# Dump of table userpending
# ------------------------------------------------------------



# Dump of table userpendingmeta
# ------------------------------------------------------------



# Dump of table userrole
# ------------------------------------------------------------

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;

INSERT INTO `userrole` (`id`, `created`, `updated`, `uuid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:46','2013-03-12 12:39:41',X'6DCFCF48680B4536914F1CD38D407913',11,1,1),
	(2,'2012-05-23 19:25:50','2013-03-12 12:39:41',X'EC79AB0DF4434CE786E33AA6DCF2BDD4',1,2,1),
	(4,'2012-06-14 13:43:46','2013-03-12 12:39:41',X'3EC8D7F91C604388A42D29604A31CB05',3,11,1),
	(5,'2012-06-14 13:44:54','2013-03-12 12:39:41',X'5FB0FB8E7BEC4CE289826121533E3D86',5,4,1);

/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow
# ------------------------------------------------------------

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;

INSERT INTO `workflow` (`id`, `created`, `updated`, `uuid`, `createts`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 18:19:05',X'1D0AF0E8C19647AC97BF6881DED8EA79','2012-12-20 11:03:30','wasp-bisulfiteSeqPlugin',1,'BISUL Seq',0),
	(2,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'98F8736B1C634B54B29299A36AF480D5','2012-12-20 11:03:31','wasp-chipSeqPlugin',1,'ChIP Seq',0),
	(3,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'7609E24374CC4361A0FE6E8A077B391E','2012-12-20 11:03:31','wasp-genericDnaSeqPlugin',1,'Generic DNA Seq',0),
	(4,'2012-12-20 11:03:31','2013-03-12 18:19:05',X'6E79BFE149BB48C89C065B48E45405DC','2012-12-20 11:03:31','wasp-helpTagPlugin',1,'HELP Tagging',0);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta
# ------------------------------------------------------------

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;

INSERT INTO `workflowmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 18:20:20',X'5F26684A843940949BBC14967825569F','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',1,NULL),
	(2,'2012-12-20 11:03:30','2013-03-12 18:20:20',X'5005009F92EB43738FB90F1E9F383444','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/bisulfiteSeqPipeline/{n};/jobsubmit/submit/{n};/jobsubmit/ok',1,NULL),
	(3,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'88C026F02878429190F11EF19D9DBE1B','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',2,NULL),
	(4,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'49A5B7945AD3411785F99ADAECD9C779','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',2,NULL),
	(5,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'613B63752AAD4BC5BAD439AAD10DD480','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',3,NULL),
	(6,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'81B8DDE97221411C8EA46B2D91024D79','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',3,NULL),
	(7,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'8C16DA04A9D649EE83A2852049D6A996','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',4,NULL),
	(8,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'4A140F80678D4FE199E16A1D3E7E348C','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/helpTag/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/helptagPipeline/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',4,NULL);

/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CE26140F514D4D4CB49AE95BBA6D7ADF',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'36CD93AED2A043A997044E5D1720B1BA',2,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'7AD719767BE64514838A323FBD6582DF',1,2,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'62FD43596D2B4A6680641CE90F1568F5',2,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D4CE9F5C1F304C58B19C6BCD47BB44CB',1,3,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'2266C86CFBFE493196FC70D7AA0B44A6',2,3,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FCC88A9A61824052B2493627552A4D81',1,4,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1E6ED454CDF3457080ABC32832EA4C59',2,4,NULL);

/*!40000 ALTER TABLE `workflowresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta` DISABLE KEYS */;

INSERT INTO `workflowresourcecategorymeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `workflowresourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'407AEB75D78549198529BB934AA16AEC','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired;',1,1),
	(2,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'AC4671CA1B0A4526B1EE134113D32E21','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'100:100;150:150;',1,1),
	(3,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'CEFCB875217246138019AA3E7FA94FB4','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'100:100;150:150;',2,1),
	(4,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'87BB9877B4004BC1A25A8C8A66645C9F','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',2,1),
	(5,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'F43F1773DDA8476796C1426B8101F464','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired;',3,1),
	(6,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'47D8A5218DAB4DEF9BA870BCB8139E1E','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150;',3,1),
	(7,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'95FC99BCC05947108619252ED8435009','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'36:36;50:50;100:100;150:150;',4,1),
	(8,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'61764875E67A4684A2CB200F55CF2B7E','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',4,1),
	(9,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'BA34D8EF1C154BCD8904657F5EDCCAB8','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'paired:paired;',5,1),
	(10,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'F0B95147CB494DA49B047F50BC3BCE1B','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150;',5,1),
	(11,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'0A9A424593D44BF39D5B2BE610A65FA7','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'25:25;36:36;50:50;100:100;150:150;',6,1),
	(12,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'724C699022DB4D56952F656A9ABA4DD1','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',6,1),
	(13,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'FDD705E509C24CD7B7F2AF35E2F6F778','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;',7,1),
	(14,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'736965DB79614581AB9484553C51F00E','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;',7,1),
	(15,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'DA0496E89C70479A8E17901654891896','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'36:36;50:50;',8,1),
	(16,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'75B2F9247366413C917E9B7E13F05E35','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;',8,1);

/*!40000 ALTER TABLE `workflowresourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcetype
# ------------------------------------------------------------

LOCK TABLES `workflowresourcetype` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype` DISABLE KEYS */;

INSERT INTO `workflowresourcetype` (`id`, `created`, `updated`, `uuid`, `resourcetypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'81CD48DEB43943F5B5AC438099746CFA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BBE2EAD15E6C4F2C8DFAB3F60A81F806',6,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D74C4A3A2281432AA8C1C92EC4A1D4AF',2,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B461523CE228404DB61261E5936FEB12',4,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'4DFFC7D959244F969B3A3712645FF8D4',2,3,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'199CF52AB917407B8AD780BEA204641C',2,4,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'566048681FB44E3DB196F2DFD93DE44D',7,4,NULL),
	(11,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'98191B8C361142F693807203E9C1CC33',8,2,NULL),
	(12,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'7C81FE6C213742639F231291D1E67263',8,3,NULL),
	(13,'2013-03-12 12:39:46','2013-03-12 18:20:20',X'57ACAF6FD6A84B19846AA4C32D5C4F21',8,4,NULL);

/*!40000 ALTER TABLE `workflowresourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

LOCK TABLES `workflowsamplesubtype` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype` (`id`, `created`, `updated`, `uuid`, `samplesubtypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8494D0A06CF049B189E94F26F0DF9FCA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FDAD32DB21F480AAA28E3211F5AAE67',3,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BA33FFE5438D4E408AC7BE4C821A8D1F',4,1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FFF92CA4418A4091866CC260BA4005F4',5,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FF3B01B772DB42298E7C9ED5659B5113',6,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'ACC242019E65485AB41E0D18C5986954',7,2,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D45993423B4E4530ABFEA37D09830E93',8,4,NULL),
	(8,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'3E5BA3C3AEEA41C89E168C470DB49A0C',13,3,NULL),
	(9,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'4BAA48B3E5994E2E9D1F89AD4D13DB06',11,3,NULL),
	(10,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'9ABFD096C74143FFBE8B30FB76646B0B',12,3,NULL);

/*!40000 ALTER TABLE `workflowsamplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftware
# ------------------------------------------------------------

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;

INSERT INTO `workflowsoftware` (`id`, `created`, `updated`, `uuid`, `softwareid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3E42688D8ECB48F7BFF595AD8266AFC5',3,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'42F7FC857CE543F3ADC4655DDC31F4BD',1,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'18650F409B534C53A37119301537AA07',2,2,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D06782B0D3354979890A5DC5735D5AFD',1,3,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'9B56C7BD2F344D17A4C21954EFCAA120',1,4,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5F569BF1C13E4DCB88E58714B652300F',4,4,NULL);

/*!40000 ALTER TABLE `workflowsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftwaremeta
# ------------------------------------------------------------



# Dump of table wrole
# ------------------------------------------------------------

LOCK TABLES `wrole` WRITE;
/*!40000 ALTER TABLE `wrole` DISABLE KEYS */;

INSERT INTO `wrole` (`id`, `created`, `updated`, `uuid`, `domain`, `name`, `rolename`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'9C3F73294B114AFDA43A861E45F90323','system','Facilities Manager','fm',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'9A83A90F669646DF8C5893932B714CA6','system','System Administrator','sa',NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1246ED2D05A5474EB0547B091F035CEB','system','General Administrator','ga',NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5899784899B841D5B9181C1DFB2137D4','department','Department Administrator','da',NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'15AC1B7B67154CFD908640F7F4D45A61','system','Facilities Tech','ft',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CCACC1BD0F5440C69277E286AA51CB1C','lab','Primary Investigator','pi',NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'A95204D19D1A44C8A6AC63E282AD3DA3','lab','Lab Manager','lm',NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8BEA294813A740C197E27159AA6EC082','lab','Lab Member','lu',NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'0A892087C552488EA7AACCDB32668666','job','Job Submitter','js',NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'64188E72A36A4C75AAA4E9C22D8C6D31','job','Job Viewer','jv',NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'884D477070704C72B616C211113FF889','system','Super User','su',NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'91748F61E6E44FEDB91A1F1CF95C492E','lab','Lab Member Inactive','lx',NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'33640EC84E564160AD486E9D770EFB58','lab','Lab Member Pending','lp',NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'22FE7ABD37CF4F30A1D4D69EA30EBAA9','jobdraft','Job Drafter','jd',NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'50AF9690E5D340A0A048BFA09F3574F3','user','User','u',NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'32A92639A1A04339AB7A1C4A8F64E2A5','system','wasp','w',NULL);

/*!40000 ALTER TABLE `wrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table wuser
# ------------------------------------------------------------

LOCK TABLES `wuser` WRITE;
/*!40000 ALTER TABLE `wuser` DISABLE KEYS */;

INSERT INTO `wuser` (`id`, `created`, `updated`, `uuid`, `email`, `firstname`, `isactive`, `lastname`, `locale`, `login`, `password`, `lastupdatebyuser`)
VALUES
	(0,'2013-03-12 12:39:40','2013-03-12 12:39:41',X'A51CBFE14EE74AA7A1BB55CB121D186F','wasp@einstein.yu.edu','Wasp',0,'System','en_US','wasp','12dea96fec20593566ab75692c9949596833adc9',1),
	(1,'2012-06-14 13:49:39','2013-03-12 12:39:41',X'F80DAC4D82CE49F89725374EAF5F4C6E','super@super.com','Super',1,'User','en_US','super','12dea96fec20593566ab75692c9949596833adc9',1),
	(2,'2012-06-14 13:49:50','2013-03-12 12:39:41',X'EBAD7E56CB584708AC1AC29683DEE8F2','john.smithl@abc.edu','John',1,'Smith','en_US','jsmith','12dea96fec20593566ab75692c9949596833adc9',1),
	(3,'2012-06-14 13:49:57','2013-03-12 12:39:41',X'0D72336D70E742EEB2E36D1D99809B10','j.doe@abc.com','Joe',1,'Doe','en_US','jdoe','12dea96fec20593566ab75692c9949596833adc9',1),
	(4,'2012-06-14 13:50:12','2013-03-12 12:39:41',X'8D4F9F940F214731B2A9937D6B78C27B','peter@abc2.com','Peter',1,'Walters','en_US','pwalters','a609590597a1907002ddaa51054df6d6d7758005',1),
	(5,'2012-06-14 13:47:46','2013-03-12 12:39:41',X'3D1C8E014DB6484E97410863D083432C','ss@abc.com','Sally',1,'Smythe','en_US','ssmythe','12dea96fec20593566ab75692c9949596833adc9',1),
	(6,'2012-06-14 13:47:53','2013-03-12 12:39:41',X'370581596FEB4E1F9C9287E5A6D46AD9','npeters@abc.com','Nancy',1,'Peters','en_US','npeters','12dea96fec20593566ab75692c9949596833adc9',1),
	(7,'2012-06-14 13:50:32','2013-03-12 12:39:41',X'5C4A9363181043A096C552AE49927D05','andrew.godwin@abc.com','Andrew',1,'Godwin','en_US','agodwin','12dea96fec20593566ab75692c9949596833adc9',1),
	(8,'2012-06-14 13:48:05','2013-03-12 12:39:41',X'4B27CB1EA4534C6CA4D2D5FE055D6267','franny@abc.com','Fran',1,'Williams','en_US','fwilliams','12dea96fec20593566ab75692c9949596833adc9',1),
	(9,'2012-06-14 14:07:09','2013-03-12 12:39:41',X'341557F5DFDA402DB080D2C894151B52','fishyface@abc.com','Barrry',1,'Fish','en_US','bfish','12dea96fec20593566ab75692c9949596833adc9',1),
	(10,'2012-06-14 13:48:31','2013-03-12 12:39:41',X'045BF82ECE7942A6BF5840902F7F6151','Liu@abc.com','Percy',1,'Liu','en_US','pliu','12dea96fec20593566ab75692c9949596833adc9',1),
	(11,'2012-06-13 22:03:43','2013-03-12 12:39:41',X'D03FF0B3516746158E5B5FB8DF8B6DFD','robin@abc.com','Robin',1,'Lister','en_US','robin','12dea96fec20593566ab75692c9949596833adc9',1),
	(12,'2012-06-14 13:48:40','2013-03-12 12:39:41',X'96721D8E94084AA2BED8C839D99881F3','mac@abc.com','Simon',1,'McDonald','en_US','mac','12dea96fec20593566ab75692c9949596833adc9',1),
	(13,'2012-06-14 14:12:28','2013-03-12 12:39:41',X'7927EF8FA55E4E5AB48B61C0398827FD','gd@abc.com','Grainne',1,'O\'Donovan','en_US','gdon','12dea96fec20593566ab75692c9949596833adc9',1);

/*!40000 ALTER TABLE `wuser` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
