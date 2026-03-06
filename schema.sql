IF DB_ID('RealEstateDB') IS NULL
    CREATE DATABASE RealEstateDB;
GO

USE RealEstateDB;
GO

IF OBJECT_ID('MaintenanceRequests', 'U') IS NOT NULL DROP TABLE MaintenanceRequests;
IF OBJECT_ID('Expenses', 'U') IS NOT NULL DROP TABLE Expenses;
IF OBJECT_ID('Payments', 'U') IS NOT NULL DROP TABLE Payments;
IF OBJECT_ID('Leases', 'U') IS NOT NULL DROP TABLE Leases;
IF OBJECT_ID('Units', 'U') IS NOT NULL DROP TABLE Units;
IF OBJECT_ID('Tenants', 'U') IS NOT NULL DROP TABLE Tenants;
IF OBJECT_ID('Properties', 'U') IS NOT NULL DROP TABLE Properties;
IF OBJECT_ID('Owners', 'U') IS NOT NULL DROP TABLE Owners;
GO

CREATE TABLE Owners (
    ownerID     INT IDENTITY(1,1) PRIMARY KEY,
    name        NVARCHAR(200) NOT NULL,
    contactInfo NVARCHAR(300)
);

CREATE TABLE Properties (
    propertyID   INT IDENTITY(1,1) PRIMARY KEY,
    ownerID      INT            NOT NULL,
    address      NVARCHAR(400)  NOT NULL,
    propertyType NVARCHAR(100)  NOT NULL,
    CONSTRAINT FK_Properties_Owners FOREIGN KEY (ownerID) REFERENCES Owners(ownerID)
);

CREATE TABLE Tenants (
    tenantID    INT IDENTITY(1,1) PRIMARY KEY,
    fullName    NVARCHAR(200) NOT NULL,
    contactInfo NVARCHAR(300)
);

CREATE TABLE Units (
    unitID      INT IDENTITY(1,1) PRIMARY KEY,
    unitNumber  NVARCHAR(50)  NOT NULL,
    rentalPrice FLOAT         NOT NULL,
    area        FLOAT         NOT NULL,
    status      NVARCHAR(50)  NOT NULL
);

CREATE TABLE Leases (
    leaseID    INT IDENTITY(1,1) PRIMARY KEY,
    tenantID   INT            NOT NULL,
    unitID     INT            NOT NULL,
    startDate  DATETIME2      NOT NULL,
    endDate    DATETIME2      NOT NULL,
    rentAmount FLOAT          NOT NULL,
    status     NVARCHAR(50)   NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT FK_Leases_Tenants FOREIGN KEY (tenantID) REFERENCES Tenants(tenantID),
    CONSTRAINT FK_Leases_Units   FOREIGN KEY (unitID)   REFERENCES Units(unitID)
);

CREATE TABLE Payments (
    paymentID     INT IDENTITY(1,1) PRIMARY KEY,
    leaseID       INT            NOT NULL,
    amount        FLOAT          NOT NULL,
    paymentDate   DATETIME2      NOT NULL,
    paymentMethod NVARCHAR(100)  NOT NULL,
    CONSTRAINT FK_Payments_Leases FOREIGN KEY (leaseID) REFERENCES Leases(leaseID)
);

CREATE TABLE Expenses (
    expenseID   INT IDENTITY(1,1) PRIMARY KEY,
    propertyID  INT            NOT NULL,
    amount      FLOAT          NOT NULL,
    category    NVARCHAR(200)  NOT NULL,
    expenseDate DATETIME2      NOT NULL,
    CONSTRAINT FK_Expenses_Properties FOREIGN KEY (propertyID) REFERENCES Properties(propertyID)
);

CREATE TABLE MaintenanceRequests (
    requestID        INT IDENTITY(1,1) PRIMARY KEY,
    unitID           INT            NOT NULL,
    issueDescription NVARCHAR(1000) NOT NULL,
    requestDate      DATETIME2      NOT NULL,
    status           NVARCHAR(50)   NOT NULL DEFAULT 'OPEN',
    CONSTRAINT FK_Maintenance_Units FOREIGN KEY (unitID) REFERENCES Units(unitID)
);
GO
