USE [master]
GO
/****** Object:  Database [yychat]    Script Date: 12/24/2021 15:13:35 ******/
CREATE DATABASE [yychat] ON  PRIMARY 
( NAME = N'yychat', FILENAME = N'D:\SQL2008R2\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\yychat.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'yychat_log', FILENAME = N'D:\SQL2008R2\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\yychat_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [yychat] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [yychat].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [yychat] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [yychat] SET ANSI_NULLS OFF
GO
ALTER DATABASE [yychat] SET ANSI_PADDING OFF
GO
ALTER DATABASE [yychat] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [yychat] SET ARITHABORT OFF
GO
ALTER DATABASE [yychat] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [yychat] SET AUTO_CREATE_STATISTICS ON
GO
ALTER DATABASE [yychat] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [yychat] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [yychat] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [yychat] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [yychat] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [yychat] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [yychat] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [yychat] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [yychat] SET  DISABLE_BROKER
GO
ALTER DATABASE [yychat] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [yychat] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [yychat] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [yychat] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [yychat] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [yychat] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [yychat] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [yychat] SET  READ_WRITE
GO
ALTER DATABASE [yychat] SET RECOVERY FULL
GO
ALTER DATABASE [yychat] SET  MULTI_USER
GO
ALTER DATABASE [yychat] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [yychat] SET DB_CHAINING OFF
GO
EXEC sys.sp_db_vardecimal_storage_format N'yychat', N'ON'
GO
USE [yychat]
GO
/****** Object:  Table [dbo].[tempmessage]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[tempmessage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[sender] [varchar](20) NULL,
	[receiver] [varchar](20) NULL,
	[content] [varchar](255) NULL,
	[sendtime] [varchar](50) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[chatmessage]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[chatmessage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[sender] [varchar](20) NULL,
	[receiver] [varchar](20) NULL,
	[content] [varchar](255) NULL,
	[sendtime] [varchar](50) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[userrelation]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[userrelation](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[masteruser] [varchar](20) NULL,
	[slaveuser] [varchar](20) NULL,
	[relationtype] [varchar](2) NULL,
 CONSTRAINT [PK_userrelation] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[user]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[user](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](20) NOT NULL,
	[password] [varchar](20) NULL,
	[email] [varchar](20) NULL,
	[image] [image] NULL,
	[webname] [varchar](20) NULL,
 CONSTRAINT [PK_user] PRIMARY KEY CLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  StoredProcedure [dbo].[tempTomessage]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[tempTomessage] @sender varchar(20),@receiver varchar(20)
as
begin
insert into chatmessage
select sender ,receiver,content,sendtime
from tempmessage
where sender=@sender and receiver=@receiver
order by id
end
GO
/****** Object:  StoredProcedure [dbo].[SendPasswordEmail]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[SendPasswordEmail] @owner varchar(20)
as
begin
DECLARE @conetent VARCHAR(8000);             
SELECT  @conetent = '你的密码为：' +[password]  from [user] where username=@owner
DECLARE @receivemail VARCHAR(30);
SELECT  @receivemail = email from [user] where username=@owner
EXEC msdb.dbo.sp_send_dbmail 
     @profile_name = 'db.mail',                  
     @recipients = @receivemail,       
     @subject = '找回密码',      
     @body = @conetent                          
end
GO
/****** Object:  StoredProcedure [dbo].[SeekOfflineMessage]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[SeekOfflineMessage] @user varchar(20),@friend varchar(20)
as
begin
	select sender ,receiver,content,sendtime
	from tempmessage 
	where (sender=@friend and receiver=@user)
	order by id 
end
GO
/****** Object:  StoredProcedure [dbo].[SeekMessage]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[SeekMessage] @user varchar(20),@friend varchar(20)
as
begin
	select sender ,receiver,content,sendtime
	from chatmessage 
	where (sender=@user and receiver=@friend) or (sender=@friend and receiver=@user)
	order by id 
end
GO
/****** Object:  StoredProcedure [dbo].[Register]    Script Date: 12/24/2021 15:13:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[Register] 
as
begin
	declare @seed int;
	declare @username varchar(20); 
	select @seed=(select  top 1 id+1 from [user] order by [id] desc)
	select @username=convert(varchar(20),ceiling(RAND(@seed)*1000000))
	insert into [user](username)
	values(@username)
	select @username
end
GO
