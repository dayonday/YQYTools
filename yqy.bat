@echo off



::===============================================================
:: Below logic is to ask for the directory to be handled, as we are
:: going to just handle the current directory so commment all these.
:: Keep it here in case we want to enable such capability later

::echo.
::echo Usage - Pass the directory either from command line or stardard input.
::echo.

::IF "%~1" == "" (
::    set /p src="Specify the directory to be handled: "
::)else (
::	set src=%1
::)
::===============================================================

::===============================================================
:: use current directory as the one to be processed

set src=%cd%
set bat=%~0

set "dst=%src%_TH\"
set "dzip=%src%_TH.zip"

echo.
echo Processing...
echo.

echo Directory : %src%
echo Target    : %dzip%
echo.

cd ..

xcopy /E /Y  "%src%" "%dst%" > nul

cd "%dst%"
del /S /Q "%bat%" *.c *.ktl *.kbi

::zip/rar the target

powershell Compress-Archive -LiteralPath '%dst%'-DestinationPath '%dzip%' -Force

::"C:\Program Files\7-Zip\7z.exe" a -tzip "%dst%" "%dzip%"
::"C:\Program Files\WinRAR\Rar.exe" a -ep1 -idq -r -y "%dst%" "%dzip%"


cd ..
rd /S /Q "%dst%"

echo.
echo Completed!!
echo.