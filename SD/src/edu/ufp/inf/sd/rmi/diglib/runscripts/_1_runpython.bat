@REM ************************************************************************************
@REM Description: run Pingclient
@REM Author: Rui Moreira
@REM Date: 20/02/2005
@REM ************************************************************************************
@REM Script usage: runclient <role> (where role should be: server / client)
@REM call setclientenv
call setenv client

@REM Run python on *build/classes* or *dist* directory
@REM cls
@cd %ABSPATH2CLASSES%
@REM cd %ABSPATH2DIST%
@REM python 3:
py -m http.server 8000

@cd %ABSPATH2SRC%\%JAVASCRIPTSPATH%