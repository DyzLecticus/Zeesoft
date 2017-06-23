;This file will be executed next to the application bundle image
;I.e. current directory will contain folder ZeeTracker with application files
[Setup]
AppId={{ZeeTracker}}
AppName=ZeeTracker
AppVersion=1.0A
AppVerName=ZeeTracker 1.0A
AppPublisher=Zeesoft
AppComments=ZeeTracker
AppCopyright=
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\ZeeTracker
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=Unknown
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=ZeeTracker-1.0A
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=ZeeTracker\ZeeTracker.ico
UninstallDisplayIcon={app}\ZeeTracker.ico
UninstallDisplayName=ZeeTracker
WizardImageStretch=No
WizardSmallImageFile=ZeeTracker-setup-icon.bmp   

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "ZeeTracker\ZeeTracker.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "ZeeTracker\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\ZeeTracker"; Filename: "{app}\ZeeTracker.exe"; IconFilename: "{app}\ZeeTracker.ico"; Check: returnTrue()
Name: "{commondesktop}\ZeeTracker"; Filename: "{app}\ZeeTracker.exe";  IconFilename: "{app}\ZeeTracker.ico"; Check: returnFalse()

[Run]
Filename: "{app}\ZeeTracker.exe"; Description: "{cm:LaunchProgram,ZeeTracker}"; Flags: nowait postinstall skipifsilent

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
