[Setup]
AppName=SGFM - Sistema de Gestão Financeira Ministerial
AppVersion=1.0
AppPublisher=Ussene Matato
DefaultDirName={pf}\SGFM
DefaultGroupName=SGFM
OutputDir=.
OutputBaseFilename=SGFMInstaller
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
CreateAppDir=yes
DisableStartupPrompt=yes
DisableProgramGroupPage=no
DisableReadyPage=no
Uninstallable=yes
WizardStyle=modern
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

[Files]
Source: "dist\\SGEST.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "dist\\lib\\*"; DestDir: "{app}\\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "README.TXT"; DestDir: "{app}"; Flags: ignoreversion

; NOTE: The application creates and uses the SQLite database in %APPDATA%\SGFM\database.db.
; Do not assume the database file is stored in the application folder.

[Icons]
Name: "{autoprograms}\{#AppName}"; Filename: "{app}\{#AppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\scripts\setup_db.bat"; Description: "Configurando base de dados..."; Flags: waituntilterminated
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Code]
function IsJavaAvailable(): Boolean;
var
  ResultCode: Integer;
begin
  Result := Exec('javaw.exe', '-version', '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
end;

function InitializeSetup(): Boolean;
begin
  Result := True;
  if not IsJavaAvailable() then
  begin
    MsgBox('Java runtime não foi encontrado no PATH. Instale o JRE ou JDK e tente novamente.', mbError, MB_OK);
    Result := False;
  end;
end;
